package br.edu.scl.ifsp.ads.onemessagechat.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.scl.ifsp.ads.onemessagechat.databinding.ActivityMessageBinding
import br.edu.scl.ifsp.ads.onemessagechat.model.Constant.EXTRA_MESSAGE
import br.edu.scl.ifsp.ads.onemessagechat.model.Message

class MessageActivity: AppCompatActivity() {
    private val amb: ActivityMessageBinding by lazy {
        ActivityMessageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Message"

        val receivedMessage = intent.getParcelableExtra<Message>(EXTRA_MESSAGE)
        receivedMessage?.let{_receivedMessage ->
            with(amb) {
                identificadorEt.isEnabled = false
                identificadorEt.setText(_receivedMessage.id)
                messageEt.setText(_receivedMessage.message)
            }
        }

        with(amb) {
            okBt.setOnClickListener {
                val message = Message(
                    id = identificadorEt.text.toString(),
                    message = messageEt.text.toString()
                )

                if(message.id.isNotBlank() && message.id.isNotEmpty() && message.message.isNotBlank() && message.message.isNotEmpty()) {
                    val resultIntent = Intent()
                    resultIntent.putExtra(EXTRA_MESSAGE, message)
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
            }
        }
    }
}
