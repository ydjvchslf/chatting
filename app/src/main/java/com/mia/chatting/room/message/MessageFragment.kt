package com.mia.chatting.room.message

import android.os.Build
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mia.chatting.R
import com.mia.chatting.adapter.MsgAdapter
import com.mia.chatting.data.Chat
import com.mia.chatting.databinding.FragmentMessageBinding
import com.mia.chatting.util.DebugLog
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MessageFragment: Fragment() {

    private val logTag = MessageFragment::class.simpleName
    private lateinit var binding: FragmentMessageBinding
    private val messageViewModel: MessageViewModel by activityViewModels()
    private val args: MessageFragmentArgs by navArgs()
    private lateinit var auth: FirebaseAuth
    private val databaseRef = Firebase.database.reference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false)
        with(binding) {
            viewModel = messageViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        DebugLog.i(logTag, "onCreateView-()")
        auth = Firebase.auth
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DebugLog.i(logTag, "onViewCreated-()")
        DebugLog.d(logTag, "전달 받은 args.uid : ${args.roomId}, args.friendName: ${args.friendName}")

        messageViewModel.friendName.value = args.friendName

        args.roomId?.let {
            val msgAdapter = MsgAdapter(binding.recyclerMessages, it)
            binding.recyclerMessages.apply {
                adapter = msgAdapter
            }
        }

        binding.submitBtn.setOnClickListener {
            sendMsg()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMsg() { // 메시지 전송
        DebugLog.i(logTag, "sendMsg-()")
        try {
            if (binding.edtMessage.text.isNotEmpty()) {
                val chat = Chat(auth.uid!!, getDateTimeString(), binding.edtMessage.text.toString(), false)    //메시지 정보 초기화
                databaseRef.child("chats").child("message").child(args.roomId ?: "none") //현재 채팅방에 메시지 추가
                    .push().setValue(chat).addOnSuccessListener {
                        DebugLog.i(logTag, "메시지 전송 Success!!!")
                        binding.edtMessage.text.clear()
                    }.addOnCanceledListener {
                        DebugLog.i(logTag, "메시지 전송에 Fail")
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            DebugLog.i(logTag, "메시지 전송 중 오류가 발생하였습니다.")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDateTimeString(): String { // 메시지 보낸 시각 정보 반환
        DebugLog.i(logTag, "getDateTimeString-()")
        try {
            val localDateTime = LocalDateTime.now()
            localDateTime.atZone(TimeZone.getDefault().toZoneId())
            val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
            return localDateTime.format(dateTimeFormatter).toString()
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("getTimeError")
        }
    }
}