package com.jbones.project

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.kvision.remote.applyRoutes
import io.kvision.remote.kvisionInit

fun Application.main() {
    install(WebSockets)
    routing {
        applyRoutes(ChatServiceManager)
    }
    kvisionInit()
}
