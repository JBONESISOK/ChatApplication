package com.jbones.project

import io.kvision.annotations.KVService
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel


@kotlinx.serialization.Serializable
data class Message(
    val username: String,
    val msg: String
)
@KVService
interface IChatService {
    suspend fun socketConnection(input: ReceiveChannel<Message>, output: SendChannel<Message>)
}