package br.edu.scl.ifsp.ads.onemessagechat.model

interface MessageDao {
    fun createMessage(message: Message): Int
    fun retrieveMessage(id: String): Message?
    fun retrieveMessages(): MutableList<Message>
    fun updateMessage(message: Message): Int
}