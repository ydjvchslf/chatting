package com.mia.chatting.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.mia.chatting.data.UserData
import com.mia.chatting.databinding.LayoutContactItemBinding
import com.mia.chatting.util.DebugLog

class ContactViewHolder(binding: LayoutContactItemBinding): RecyclerView.ViewHolder(binding.root) {

    private val logTag = ContactViewHolder::class.simpleName ?: ""

    private var nameTextView = binding.name
    private var emailTextView = binding.email

    init {
        DebugLog.d(logTag, "init-()")
    }

    // 데이터와 뷰를 묶는다.
    @SuppressLint("SetTextI18n")
    fun bind(userData: UserData) {
        DebugLog.i(logTag, "bind-()")
        DebugLog.d(logTag, "userData: $userData")
        nameTextView.text = userData.name
        emailTextView.text = userData.email
    }
}