package com.jbones.project

import io.kvision.html.P
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object Server {
    private val chatService = ChatService()
    val messages = mutableListOf<Message>()

    val messageChannel = Channel<Message>()

    fun connect() {
        AppScope.launch {
            while(true) {
                chatService.socketConnection { output, input ->
                    coroutineScope {
                        launch {
                            for(message in messageChannel) {
                                output.send(message)
                            }
                        }
                        launch {
                            for(message in input) {
                                chatBox.add(P {
                                    +"${message.username}: ${message.msg}"
                                })
                                messages.add(message)
                            }
                        }
                    }
                }
                delay(5000)
            }
        }
    }
}