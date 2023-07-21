package com.mia.chatting.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mia.chatting.R
import com.mia.chatting.data.ChatData
import com.mia.chatting.databinding.LayoutMyMsgItemBinding
import com.mia.chatting.databinding.LayoutOtherMsgItemBinding
import com.mia.chatting.util.DataConverter
import com.mia.chatting.util.DebugLog

class MsgAdapter(
    private val recyclerView: RecyclerView,
    private val roomId: String
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val logTag = MsgAdapter::class.simpleName

    var chatData: ArrayList<ChatData> = arrayListOf()     // 메시지 목록
    //var messageKeys: ArrayList<String> = arrayListOf()   // 메시지 키 목록
    private val myUid = FirebaseAuth.getInstance().currentUser?.uid

    init {
        setupMessages()
    }

    fun setupMessages() {
        getMessages()
    }

    fun getMessages() {
        DebugLog.i(logTag, "getMessages-()")
        FirebaseDatabase.getInstance().getReference("chats").child("message").child(roomId)   //전체 메시지 목록 가져오기
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    DebugLog.i(logTag, "onDataChange-()")
                    DebugLog.d(logTag, "snapshot: $snapshot")
                    chatData.clear()
                    if (snapshot.value != null) {
                        DebugLog.d(logTag, "snapshot.value: ${snapshot.value}")
                        val elements = snapshot.children.toMutableList()
                        for (element in elements) {
                            //your logic
                            val chat = DataConverter.jsonToChatData(element)
                            DebugLog.d(logTag, "chat: $chat")
                            chatData.add(chat)
                        }
                        notifyDataSetChanged() // 화면 업데이트
                        recyclerView.apply {
                            scrollToPosition(chatData.size - 1) // 스크롤 최하단으로 내리기
                            layoutChangeListener
                        }

                    } else {
                        DebugLog.d(logTag, "아직 대화 목록이 없음")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    DebugLog.i(logTag, "onCancelled-()")
                    DebugLog.e(error.toString())
                }
            })
    }

    override fun getItemViewType(position: Int): Int {               // 메시지의 id에 따라 내 메시지/상대 메시지 구분
        return if (chatData[position].senderUid.equals(myUid)) 1 else 0 // 1: mine 0: other
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> { // mine
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_my_msg_item, parent, false) // 내 메시지 레이아웃으로 초기화
                MyMessageViewHolder(LayoutMyMsgItemBinding.bind(view))
            }
            else -> { // other
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_other_msg_item, parent, false) // 상대 메시지 레이아웃으로 초기화
                OtherMessageViewHolder(LayoutOtherMsgItemBinding.bind(view))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (chatData[position].senderUid.equals(myUid)) {       // 레이아웃 항목 초기화
            (holder as MyMessageViewHolder).bind(position)
        } else {
            (holder as OtherMessageViewHolder).bind(position)
        }
    }

    override fun getItemCount(): Int {
        return chatData.size
    }

    val layoutChangeListener =
        recyclerView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom) {
                recyclerView.postDelayed({
                    if (chatData.size > 0) {
                        recyclerView.scrollToPosition(chatData.size - 1)
                    }
                }, 100)
            }
        }

    // 내 메시지용 ViewHolder
    inner class MyMessageViewHolder(itemView: LayoutMyMsgItemBinding): RecyclerView.ViewHolder(itemView.root) {
        var background = itemView.background
        var txtMessage = itemView.txtMessage
        var txtDate = itemView.txtDate

        fun bind(position: Int) {            //메시지 UI 레이아웃 초기화
            val chats = chatData[position]
            val sendDate = chats.sendDate
            txtMessage.text = chats.content
            txtDate.text = getDateText(sendDate)
        }

        private fun getDateText(sendDate: String): String {        //메시지 전송 시각 생성
            var dateText = ""
            var timeString = ""
            if (sendDate.isNotBlank()) {
                timeString = sendDate.substring(8, 12)
                val hour = timeString.substring(0, 2)
                val minute = timeString.substring(2, 4)

                val timeformat = "%02d:%02d"

                if (hour.toInt() > 11) {
                    dateText += "오후 "
                    dateText += timeformat.format(hour.toInt() - 12, minute.toInt())
                } else {
                    dateText += "오전 "
                    dateText += timeformat.format(hour.toInt(), minute.toInt())
                }
            }
            return dateText
        }
    }

    // 상대 메시지 뷰홀더
    inner class OtherMessageViewHolder(itemView: LayoutOtherMsgItemBinding): RecyclerView.ViewHolder(itemView.root) {
        var background = itemView.background
        var txtMessage = itemView.txtMessage
        var txtDate = itemView.txtDate

        fun bind(position: Int) {           //메시지 UI 항목 초기화
            val message = chatData[position]
            val sendDate = message.sendDate

            txtMessage.text = message.content

            txtDate.text = getDateText(sendDate)

            //setShown(position)             //해당 메시지 확인하여 서버로 전송
        }

        fun getDateText(sendDate: String): String {    //메시지 전송 시각 생성

            var dateText = ""
            var timeString = ""
            if (sendDate.isNotBlank()) {
                timeString = sendDate.substring(8, 12)
                val hour = timeString.substring(0, 2)
                val minute = timeString.substring(2, 4)

                val timeformat = "%02d:%02d"

                if (hour.toInt() > 11) {
                    dateText += "오후 "
                    dateText += timeformat.format(hour.toInt() - 12, minute.toInt())
                } else {
                    dateText += "오전 "
                    dateText += timeformat.format(hour.toInt(), minute.toInt())
                }
            }
            return dateText
        }

//        fun setShown(position: Int) {          //메시지 확인하여 서버로 전송
//            FirebaseDatabase.getInstance().getReference("ChatRoom")
//                .child("chatRooms").child(chatRoomKey!!).child("messages")
//                .child(messageKeys[position]).child("confirmed").setValue(true)
//                .addOnSuccessListener {
//                    Log.i("checkShown", "성공")
//                }
//        }
    }
}