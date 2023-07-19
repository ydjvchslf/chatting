package com.mia.chatting.data

data class Chat (
    val senderUid: String,
    val sendDate: String,
    val content: String,
    val isChecked: Boolean
)
