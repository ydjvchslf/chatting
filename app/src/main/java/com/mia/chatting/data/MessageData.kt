package com.mia.chatting.data

data class MessageData (
    val chatId: String,
    val chatVal: String
)

data class FirebaseData(
    val outerKey: String,
    val innerKey: String,
    val senderUid: String?,
    val sendDate: String?,
    val checked: Boolean?,
    val content: String?
)
