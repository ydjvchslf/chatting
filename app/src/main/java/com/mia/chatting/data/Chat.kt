package com.mia.chatting.data

data class Chat (
    val chatId: String?,
    val senderUid: String,
    val sendDate: String,
    val content: String,
    val isChecked: Boolean
)
