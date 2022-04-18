package com.jbones.project

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import java.util.concurrent.ConcurrentHashMap

object Model {
    val clients: ConcurrentHashMap.KeySetView<SendChannel<Message>, Boolean> = ConcurrentHashMap.newKeySet()
}


actual class ChatService : IChatService {
    override suspend fun socketConnection(input: ReceiveChannel<Message>, output: SendChannel<Message>) {
        with(Model) {
            clients.add(output)
            for(receivedMessage in input) {
                val message = receivedMessage.copy()
                clients.forEach {
                    it.send(message)
                }
            }
            clients.remove(output)
        }
    }

}