package com.jortegat.alienrfidplayground

import com.alien.enterpriseRFID.notify.Message
import com.alien.enterpriseRFID.notify.MessageListener
import com.alien.enterpriseRFID.notify.MessageListenerService

class TagManager : MessageListener {

    private val messageService: MessageListenerService by lazy {
        MessageListenerService(MESSAGES_PORT)
    }

    lateinit var onMessageArrived: (message: String) -> Unit

    init {
        messageService.messageListener = this
    }

    fun startMessageService() {
        messageService.startService()
    }

    override fun messageReceived(message: Message?) {
        message?.let {
            onMessageArrived.invoke(message.tagList[0].tagID)
            return
        }
        onMessageArrived.invoke("no llegó nada")
    }

    companion object {
        private const val MESSAGES_PORT = 3988
    }

}
