import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.NUnitStep
import jetbrains.buildServer.configs.kotlin.buildSteps.dotnetBuild
import jetbrains.buildServer.configs.kotlin.buildSteps.nunit
import jetbrains.buildServer.configs.kotlin.buildSteps.nunitConsole
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

version = "2024.03"

project {

    buildType(NUnitConsoleRunner1dll)
    buildType(NUnitConsoleRunnerDeprecatedNUnitRunner1dll)
}

object NUnitConsoleRunner1dll : BuildType({
    name = "✔️ NUnit Console Runner: 1 dll"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        dotnetBuild {
            name = "Build Project"
            id = "Build_Project"
            projects = "NUnitTests.sln"
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
        nunitConsole {
            name = "nUnit tests"
            id = "nUnit_tests"
            nunitPath = "%teamcity.tool.NUnit.Console.3.17.0%"
            includeTests = """NUnitTests\bin\Debug\net8.0\NUnitTests.dll"""
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
    }

    triggers {
        vcs {
        }
    }

    failureConditions {
        testFailure = false
        nonZeroExitCode = false
    }

    features {
        perfmon {
        }
    }
})

object NUnitConsoleRunnerDeprecatedNUnitRunner1dll : BuildType({
    name = "NUnit Console Runner + Deprecated NUnit Runner: 1 dll"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        dotnetBuild {
            name = "Build Project"
            id = "Build_Project"
            projects = "NUnitTests.sln"
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
        nunitConsole {
            name = "nUnit tests"
            id = "nUnit_tests"
            nunitPath = "%teamcity.tool.NUnit.Console.3.17.0%"
            includeTests = """NUnitTests\bin\Debug\net8.0\NUnitTests.dll"""
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
        nunit {
            name = "NUnit: deprecated, dll"
            id = "NUnit_deprecated_dll"
            nunitVersion = NUnitStep.NUnitVersion.NUnit_2_6_4
            runtimeVersion = NUnitStep.RuntimeVersion.v4_0
            includeTests = """NUnitTests\bin\Debug\net8.0\NUnitTests.dll"""
            runProcessPerAssembly = true
        }
    }

    triggers {
        vcs {
        }
    }

    failureConditions {
        testFailure = false
        nonZeroExitCode = false
    }

    features {
        perfmon {
        }
    }
})
