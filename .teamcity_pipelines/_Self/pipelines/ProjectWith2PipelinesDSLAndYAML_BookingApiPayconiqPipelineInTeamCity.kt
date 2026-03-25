package _Self.pipelines

import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.pipelines.*
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.buildSteps.maven

object ProjectWith2PipelinesDSLAndYAML_BookingApiPayconiqPipelineInTeamCity : Pipeline({
    id("BookingApiPayconiqPipelineInTeamCity")
    name = "BookingApiPayconiq: pipeline (in TeamCity)"

    repositories {
        repository(_Self.vcsRoots.HttpsGithubComDariaKrupBookingApiPayconiqRefsHeadsMaster)
    }

    triggers {
        vcs {
            branchFilter = """
                +:*
                +pr:*
            """.trimIndent()
        }
    }

    job(ProjectWith2PipelinesDSLAndYAML_BookingApiPayconiqPipelineInTeamCity_Job1)
})

object ProjectWith2PipelinesDSLAndYAML_BookingApiPayconiqPipelineInTeamCity_Job1 : Job({
    id("Job1")
    name = "Job: Maven"
    allowReuse = true
    enableDependencyCacheOptimization = false

    steps {
        maven {
            name = "clean test"
            goals = "clean test"
        }
    }
})
