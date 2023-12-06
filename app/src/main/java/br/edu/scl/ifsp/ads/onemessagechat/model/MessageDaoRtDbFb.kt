package br.edu.scl.ifsp.ads.onemessagechat.model

import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class MessageDaoRtDbFb: MessageDao {
    companion object{
        private const val MESSAGE_LIST_ROOT_NODE = "messageList"
    }
    private val messageRtDbFbReference = Firebase.database.getReference(MESSAGE_LIST_ROOT_NODE)
    private val messageList: MutableList <Message> = mutableListOf()

    init{
        messageRtDbFbReference.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message: Message? = snapshot.getValue<Message>()

                message?.also{newMessage ->
                    if(!messageList.any{ it.id == newMessage.id }) {
                        messageList.add(newMessage)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val message: Message? = snapshot.getValue<Message>()

                message?.also{ editMessage ->
                    messageList.apply {
                        this[indexOfFirst { editMessage.id == it.id }] = editMessage
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val message: Message? = snapshot.getValue<Message>()

                message?.also{
                    messageList.remove(it)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        messageRtDbFbReference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messageMap = snapshot.getValue<Map<String, Message>>()

                messageList.clear()
                messageMap?.values?.also {
                    messageList.addAll(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun createMessage(message: Message): Int {
        createOrUpdateMessage(message)
        return 1
    }

    override fun retrieveMessage(id: String): Message {
        return messageList[messageList.indexOfFirst { it.id == id }]
    }

    override fun retrieveMessages(): MutableList<Message> = messageList

    override fun updateMessage(message: Message): Int {
        createOrUpdateMessage(message)
        return 1
    }

    private fun createOrUpdateMessage(message: Message) = messageRtDbFbReference.child(message.id).setValue(message)
}