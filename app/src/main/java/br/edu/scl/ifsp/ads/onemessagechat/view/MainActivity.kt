package br.edu.scl.ifsp.ads.onemessagechat.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.scl.ifsp.ads.onemessagechat.R
import br.edu.scl.ifsp.ads.onemessagechat.adapter.MessageAdapter
import br.edu.scl.ifsp.ads.onemessagechat.controller.MessageRtFbController
import br.edu.scl.ifsp.ads.onemessagechat.databinding.ActivityMainBinding
import br.edu.scl.ifsp.ads.onemessagechat.model.Constant.EXTRA_MESSAGE
import br.edu.scl.ifsp.ads.onemessagechat.model.Constant.MESSAGE_ARRAY
import br.edu.scl.ifsp.ads.onemessagechat.model.Message

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val messageList: MutableList<Message> = mutableListOf()
    private val messageController: MessageRtFbController by lazy {
        MessageRtFbController(this)
    }
    private val messageAdapter: MessageAdapter by lazy {
        MessageAdapter(this,messageList)
    }
    companion object {
        const val GET_MESSAGES_MSG = 1
        const val GET_MESSAGES_INTERVAL = 2000L
    }
    val updateMessageListHandler = object: Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: android.os.Message ) {
            super.handleMessage(msg)
            if(msg.what == GET_MESSAGES_MSG){
                messageController.getMessages()
                sendMessageDelayed(obtainMessage().apply { what = GET_MESSAGES_MSG }, GET_MESSAGES_INTERVAL)
            } else {
                msg.data.getParcelableArray(MESSAGE_ARRAY)?.also { messageArray ->
                    messageList.clear()
                    messageArray.forEach {
                        messageList.add(it as Message)
                    }
                    messageAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private lateinit var carl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        setSupportActionBar(amb.toolbarIn.toolbar)
        amb.messagesLv.adapter = messageAdapter
        carl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == RESULT_OK) {
                val message = result.data?.getParcelableExtra<Message>(EXTRA_MESSAGE)
                message?.let{_message ->
                    if (messageList.any { it.id == _message.id }) {
                        messageController.editMessage(_message)
                    } else {
                        messageController.insertMessage(_message)
                    }
                }
            }
        }

        amb.messagesLv.setOnItemClickListener { _, _, position, _ ->
            val message = messageList[position]
            val editMessageIntent = Intent(this, MessageActivity::class.java)
            editMessageIntent.putExtra(EXTRA_MESSAGE, message)
            carl.launch(editMessageIntent)
        }

        registerForContextMenu(amb.messagesLv)
        updateMessageListHandler.apply {
            sendMessageDelayed(obtainMessage().apply { what = GET_MESSAGES_MSG }, GET_MESSAGES_INTERVAL)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.addMessageMi -> {
                carl.launch(Intent(this, MessageActivity::class.java))
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterForContextMenu(amb.messagesLv)
    }
}