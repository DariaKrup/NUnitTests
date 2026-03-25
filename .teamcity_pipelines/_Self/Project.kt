package _Self

import _Self.vcsRoots.*
import _Self.pipelines.*
import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.Project

object Project : Project({

    vcsRoot(HttpsGithubComDariaKrupBookingApiPayconiqRefsHeadsMaster)

    params {
        param("teamcity.internal.pipelines.creation.enabled", "true")
    }

    pipeline(ProjectWith2PipelinesDSLAndYAML_BookingApiPayconiqPipelineInTeamCity)
})
