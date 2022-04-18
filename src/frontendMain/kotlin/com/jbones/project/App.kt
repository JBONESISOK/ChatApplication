package com.jbones.project

import io.kvision.*
import io.kvision.core.*
import io.kvision.form.text.textInput
import io.kvision.html.label
import io.kvision.panel.SimplePanel
import io.kvision.panel.root
import io.kvision.panel.simplePanel
import io.kvision.utils.ENTER_KEY
import io.kvision.utils.px
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch

external fun prompt(msg: String): dynamic
val AppScope = CoroutineScope(window.asCoroutineDispatcher())

lateinit var chatBox: SimplePanel

class App : Application() {
    private fun submitMessage(message: Message) {
        AppScope.launch {
            Server.messageChannel.send(message)
        }
    }

    override fun start(state: Map<String, Any>) {
        val name: String = prompt("Whats your name?") as String
        val root = root("kvapp") {
            chatBox = simplePanel {
                id = "chat_box"
                background = Background(color = Color.name(Col.GRAY))
                lineHeight = 5.px
                width = 800.px
                height = 600.px
                overflow = Overflow.SCROLL
                overflowWrap = OverflowWrap.NORMAL
            }
            label {
                forId = "chat_input"
                +"Input your message here"
            }
            textInput {
                id = "chat_input"
                onEvent {
                    keydown = {
                        if (it.keyCode == ENTER_KEY) {
                            it.preventDefault()
                            submitMessage(Message(name, value!!))
                            value = ""
                        }
                    }
                }
            }
        }
        with(Server) {
            connect()
            AppScope.launch {
                messageChannel.send(Message("Server", "$name has joined!"))
            }
        }
    }
}

fun main() {
    startApplication(
        ::App,
        module.hot,
        BootstrapModule,
        BootstrapCssModule,
        CoreModule
    )
}
