import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.DotnetMsBuildStep
import jetbrains.buildServer.configs.kotlin.buildSteps.DotnetVsTestStep
import jetbrains.buildServer.configs.kotlin.buildSteps.dotnetBuild
import jetbrains.buildServer.configs.kotlin.buildSteps.dotnetMsBuild
import jetbrains.buildServer.configs.kotlin.buildSteps.dotnetRestore
import jetbrains.buildServer.configs.kotlin.buildSteps.dotnetTest
import jetbrains.buildServer.configs.kotlin.buildSteps.dotnetVsTest
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.projectFeatures.kubernetesExecutor
import jetbrains.buildServer.configs.kotlin.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2024.12"

project {

    buildType(TestLinuxAgent)
    buildType(TestLinuxAgentDocker)
    buildType(VSTestLinuxAgent)
    buildType(VSTestLinuxAgentDocker)

    features {
        kubernetesExecutor {
            id = "PROJECT_EXT_104"
            connectionId = "PROJECT_EXT_2"
            profileName = "K8s Executor"
            buildsLimit = "1"
            containerParameters = "teamcity.agent.jvm.os.name=Linux"
            templateName = "ubuntu-agent"
        }
    }
}

object TestLinuxAgent : BuildType({
    name = "test, Linux agent"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        dotnetRestore {
            id = "dotnet_1"
            projects = "NUnitTests.sln"
        }
        dotnetMsBuild {
            id = "dotnet"
            projects = "NUnitTests.sln"
            version = DotnetMsBuildStep.MSBuildVersion.CrossPlatform
        }
        dotnetTest {
            id = "dotnet_2"
            projects = "NUnitTests/NUnitTests.csproj"
            coverage = dotcover {
            }
        }
    }

    triggers {
        vcs {
        }
    }

    failureConditions {
        testFailure = false
    }

    features {
        perfmon {
        }
    }

    requirements {
        contains("teamcity.agent.jvm.os.name", "Linux")
    }
})

object TestLinuxAgentDocker : BuildType({
    name = "test, Linux agent + Docker"

    artifactRules = "**/* => sources.zip"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        dotnetRestore {
            id = "dotnet_1"
            projects = "NUnitTests.sln"
            param("teamcity.kubernetes.executor.container.image", "mcr.microsoft.com/dotnet/sdk:8.0")
            param("teamcity.kubernetes.executor.pull.policy", "IfNotPresent")
        }
        dotnetBuild {
            id = "dotnet"
            projects = "NUnitTests.sln"
            param("teamcity.kubernetes.executor.container.image", "mcr.microsoft.com/dotnet/sdk:8.0")
            param("teamcity.kubernetes.executor.pull.policy", "IfNotPresent")
        }
        dotnetTest {
            id = "dotnet_2"
            projects = "NUnitTests/NUnitTests.csproj"
            dockerImage = "mcr.microsoft.com/dotnet/sdk:8.0"
            param("teamcity.kubernetes.executor.pull.policy", "IfNotPresent")
        }
        script {
            id = "simpleRunner"
            scriptContent = """echo "Love cats!!""""
            param("teamcity.kubernetes.executor.pull.policy", "IfNotPresent")
        }
    }

    triggers {
        vcs {
        }
    }

    failureConditions {
        testFailure = false
    }

    features {
        perfmon {
        }
    }

    requirements {
        contains("teamcity.agent.jvm.os.name", "Linux")
    }
})

object VSTestLinuxAgent : BuildType({
    name = "VSTest, Linux agent"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        dotnetRestore {
            id = "dotnet_1"
            projects = "NUnitTests.sln"
        }
        dotnetMsBuild {
            id = "dotnet"
            projects = "NUnitTests.sln"
            version = DotnetMsBuildStep.MSBuildVersion.CrossPlatform
        }
        dotnetVsTest {
            id = "dotnet_2"
            assemblies = "%system.teamcity.build.checkoutDir%/NUnitTests/bin/Debug/net8.0/NUnitTests.dll"
            version = DotnetVsTestStep.VSTestVersion.CrossPlatform
            platform = DotnetVsTestStep.Platform.Auto
            coverage = dotcover {
            }
        }
    }

    triggers {
        vcs {
        }
    }

    failureConditions {
        testFailure = false
    }

    features {
        perfmon {
        }
    }

    requirements {
        contains("teamcity.agent.jvm.os.name", "Linux")
    }
})

object VSTestLinuxAgentDocker : BuildType({
    name = "VSTest, Linux agent + Docker"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        dotnetRestore {
            id = "dotnet_1"
            projects = "NUnitTests.sln"
            param("teamcity.kubernetes.executor.container.image", "mcr.microsoft.com/dotnet/sdk:8.0")
            param("teamcity.kubernetes.executor.pull.policy", "IfNotPresent")
        }
        dotnetBuild {
            id = "dotnet"
            projects = "NUnitTests.sln"
            param("teamcity.kubernetes.executor.container.image", "mcr.microsoft.com/dotnet/sdk:8.0")
            param("teamcity.kubernetes.executor.pull.policy", "IfNotPresent")
        }
        dotnetVsTest {
            id = "dotnet_2"
            assemblies = "%system.teamcity.build.checkoutDir%/NUnitTests/bin/Debug/net8.0/NUnitTests.dll"
            version = DotnetVsTestStep.VSTestVersion.CrossPlatform
            platform = DotnetVsTestStep.Platform.Auto
            dockerImage = "mcr.microsoft.com/dotnet/sdk:8.0"
            dockerImagePlatform = DotnetVsTestStep.ImagePlatform.Linux
        }
    }

    triggers {
        vcs {
        }
    }

    failureConditions {
        testFailure = false
    }

    features {
        perfmon {
        }
    }

    requirements {
        contains("teamcity.agent.jvm.os.name", "Linux")
    }
})
