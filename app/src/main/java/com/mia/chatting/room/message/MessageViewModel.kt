package com.mia.chatting.room.message

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mia.chatting.util.DebugLog

class MessageViewModel: ViewModel() {

    private val logTag = MessageViewModel::class.simpleName
    var friendName = MutableLiveData<String>()

    init {
        DebugLog.i(logTag, "init-()")
    }

}