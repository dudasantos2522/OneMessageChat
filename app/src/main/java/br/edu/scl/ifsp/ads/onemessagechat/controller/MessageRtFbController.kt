package br.edu.scl.ifsp.ads.onemessagechat.controller

import br.edu.scl.ifsp.ads.onemessagechat.model.Constant.MESSAGE_ARRAY
import br.edu.scl.ifsp.ads.onemessagechat.model.Message
import br.edu.scl.ifsp.ads.onemessagechat.model.MessageDaoRtDbFb
import br.edu.scl.ifsp.ads.onemessagechat.view.MainActivity

class MessageRtFbController(private val mainActivity: MainActivity) {
    private val messageDaoImpl: MessageDaoRtDbFb = MessageDaoRtDbFb()

    fun insertMessage(message: Message) {
        Thread {
            messageDaoImpl.createMessage(message)
        }.start()
    }

    fun getMessage(id: String) = messageDaoImpl.retrieveMessage(id)

    fun getMessages() {
        Thread {
            val returnList = messageDaoImpl.retrieveMessages()
            val message = android.os.Message()
            message.data.putParcelableArray(
                MESSAGE_ARRAY,
                returnList.toTypedArray()
            )
            mainActivity.updateMessageListHandler.sendMessage(message)
        }.start()
    }

    fun editMessage(message: Message) {
        Thread {
            messageDaoImpl.updateMessage(message)
        }.start()
    }
}