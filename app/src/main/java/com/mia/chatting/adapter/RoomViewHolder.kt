package com.mia.chatting.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mia.chatting.data.AfterFirebaseData
import com.mia.chatting.data.FirebaseData
import com.mia.chatting.data.RoomData
import com.mia.chatting.data.UserData
import com.mia.chatting.databinding.LayoutContactItemBinding
import com.mia.chatting.databinding.LayoutRoomItemBinding
import com.mia.chatting.util.DebugLog

class RoomViewHolder(binding: LayoutRoomItemBinding): RecyclerView.ViewHolder(binding.root) {

    private val logTag = RoomViewHolder::class.simpleName ?: ""

    private val auth: FirebaseAuth

    private var nameTextView = binding.name
    private var contentTextView = binding.content
    private var timeTextView = binding.time
    private var countTextView = binding.count

    init {
        DebugLog.d(logTag, "init-()")
        auth = Firebase.auth
    }

    // 데이터와 뷰를 묶는다.
    @SuppressLint("SetTextI18n")
    fun bind(firebaseData: FirebaseData) {
        DebugLog.i(logTag, "bind-()")
        DebugLog.d(logTag, "firebaseData: $firebaseData")

        changeDataFormat(firebaseData)

        nameTextView.text = firebaseData.outerKey
        contentTextView.text = firebaseData.content
        timeTextView.text = firebaseData.sendDate
        //countTextView.text = firebaseData.content
    }

    fun changeDataFormat(origin: FirebaseData) {
        // uid 파싱해서 변환
        val beforeUid = origin.outerKey
        val beforeStr = beforeUid.split("-")
        var counterUid = ""
        beforeStr.forEach { whomUid ->
            auth.uid?.let { myUid ->
                if ( myUid != whomUid ) {
                    counterUid = whomUid
                }
            }
        }
        DebugLog.d(logTag, "counterUid: $counterUid")
    }
}