package com.mia.chatting.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mia.chatting.data.AfterFirebaseData
import com.mia.chatting.data.FirebaseData
import com.mia.chatting.data.RoomData
import com.mia.chatting.data.UserData
import com.mia.chatting.databinding.LayoutContactItemBinding
import com.mia.chatting.databinding.LayoutRoomItemBinding
import com.mia.chatting.util.DataConverter
import com.mia.chatting.util.DebugLog
import java.text.SimpleDateFormat
import java.util.*

class RoomViewHolder(binding: LayoutRoomItemBinding): RecyclerView.ViewHolder(binding.root) {

    private val logTag = RoomViewHolder::class.simpleName ?: ""

    private val auth: FirebaseAuth
    private val databaseRef = Firebase.database.reference

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
    fun bind(afterFirebaseData: AfterFirebaseData) {
        DebugLog.i(logTag, "bind-()")
        DebugLog.d(logTag, "afterFirebaseData: $afterFirebaseData")

        changeDataFormat(afterFirebaseData) { yourName, after ->
            nameTextView.text = yourName
            contentTextView.text = afterFirebaseData.content
            timeTextView.text = after

            val unreadCount = afterFirebaseData.unreadNum.toString()
            if (unreadCount > 0.toString()) {
                countTextView.text = afterFirebaseData.unreadNum.toString()
            } else {
                countTextView.visibility = View.INVISIBLE
            }
        }
    }

    fun changeDataFormat(origin: AfterFirebaseData, after: (String, String) -> Unit) {
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


        var yourName = ""

        val userRef = databaseRef.child("users")
        userRef.child(counterUid).addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SimpleDateFormat")
            override fun onDataChange(snapshot: DataSnapshot) {
                DebugLog.d(logTag, "홀더에서 snapshot => $snapshot")
                val yourData = DataConverter.jsonToUserData(snapshot)
                yourName = yourData.name.toString()
                DebugLog.d(logTag, "홀더에서 yourName => $yourName")

                // 시간 변환
                val beforeTime = origin.sendDate?.substring(0, 8)
                val currentDate = SimpleDateFormat("yyyyMMdd").format(Date())

                DebugLog.d(logTag, "beforeTime => $beforeTime")
                DebugLog.d(logTag, "currentDate => $currentDate")

                beforeTime.let {
                    if (it == currentDate) {
                        after.invoke(yourName, "오늘")
                    } else {
                        val month = it?.substring(4, 6)
                        val day = it?.substring(6, 8)
                        val monthAndDay = "$month/$day"
                        after.invoke(yourName, monthAndDay)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}