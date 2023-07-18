package com.mia.chatting.room

import androidx.lifecycle.ViewModel
import com.mia.chatting.util.DebugLog

class RoomViewModel: ViewModel() {

    private val logTag = RoomViewModel::class.simpleName

    init {
        DebugLog.i(logTag, "init-()")
    }

}