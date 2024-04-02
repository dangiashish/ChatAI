package com.codebyashish.chatai.adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.codebyashish.chatai.R
import com.codebyashish.chatai.utility.KeyConstants.CHAT_TYPE.AI
import com.codebyashish.chatai.utility.KeyConstants.CHAT_TYPE.CLOSED
import com.codebyashish.chatai.utility.KeyConstants.CHAT_TYPE.USER
import org.json.JSONArray
import org.json.JSONObject

class ChatAdapter(private val context: Context, private val itemList: JSONArray) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTextView: TextView = itemView.findViewById(R.id.itemTextView)
        val itemTextView2: TextView = itemView.findViewById(R.id.itemTextView2)
        val layout: LinearLayout = itemView.findViewById(R.id.layout)
        val layoutClosed: LinearLayout = itemView.findViewById(R.id.layoutClosed)
        val layout2: RelativeLayout = itemView.findViewById(R.id.layout2)
        val slt: RelativeLayout = itemView.findViewById(R.id.sLt)
        val rlt: RelativeLayout = itemView.findViewById(R.id.rLt)
        val lottie: LottieAnimationView = itemView.findViewById(R.id.lottie)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_chat_msg, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position] as JSONObject

        Log.d("MyAdapter", "onBindViewHolder: $item")

        val msg = item.optString("msg")
        val type = item.optString("type")

        if (type == USER) {
            holder.itemTextView.text = msg
            holder.layout.visibility = VISIBLE
            holder.layout2.visibility = GONE
            holder.layoutClosed.visibility = GONE

        } else if (type == AI) {

            if (msg.isEmpty()){
                holder.lottie.visibility = VISIBLE
                holder.rlt.visibility = GONE
            } else {
                holder.lottie.visibility  = GONE
                holder.rlt.visibility = VISIBLE
                holder.itemTextView2.text = msg
            }

            holder.layout.visibility = GONE
            holder.layout2.visibility = VISIBLE
            holder.layoutClosed.visibility = GONE

        } else  if (type == CLOSED){
            holder.layout.visibility = GONE
            holder.layout2.visibility = GONE
            holder.layoutClosed.visibility = VISIBLE
        }

        holder.slt.setOnLongClickListener {
            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("copied", msg)
            clipboardManager.setPrimaryClip(clipData)
            true
        }
        holder.rlt.setOnLongClickListener {
            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("copied", msg)
            clipboardManager.setPrimaryClip(clipData)
            true
        }


    }

    override fun getItemCount(): Int {
        return itemList.length()
    }
}
