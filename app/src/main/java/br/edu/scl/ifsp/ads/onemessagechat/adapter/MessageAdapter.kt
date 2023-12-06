package br.edu.scl.ifsp.ads.onemessagechat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.scl.ifsp.ads.onemessagechat.R
import br.edu.scl.ifsp.ads.onemessagechat.databinding.TileMessageBinding
import br.edu.scl.ifsp.ads.onemessagechat.model.Message

class MessageAdapter(context: Context, private val messageList: MutableList<Message>):
    ArrayAdapter<Message>(context, R.layout.tile_message, messageList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val message = messageList[position]
        var tmb: TileMessageBinding? = null

        var messageTileView = convertView
        if(messageTileView == null) {
            tmb = TileMessageBinding.inflate(
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false
            )
            messageTileView = tmb.root

            val tileMessageHolder = TileMessageHolder(tmb.identificadorTv, tmb.messageTv)
            messageTileView.tag = tileMessageHolder
        }
        val holder = messageTileView.tag as TileMessageHolder
        holder.identificadorTv.text = message.id
        holder.messageTv.text = message.message

        tmb?.identificadorTv?.text = message.id
        tmb?.messageTv?.text = message.message

        return messageTileView
    }

    private data class TileMessageHolder(val identificadorTv: TextView, val messageTv: TextView)
}