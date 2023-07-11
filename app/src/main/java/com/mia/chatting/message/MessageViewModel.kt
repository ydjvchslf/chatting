package com.mia.chatting.message

import androidx.lifecycle.ViewModel
import com.mia.chatting.util.DebugLog

class MessageViewModel: ViewModel() {

    private val logTag = MessageViewModel::class.simpleName

    init {
        DebugLog.i(logTag, "init-()")
    }

}