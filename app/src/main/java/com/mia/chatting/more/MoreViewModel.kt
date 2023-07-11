package com.mia.chatting.more

import androidx.lifecycle.ViewModel
import com.mia.chatting.util.DebugLog

class MoreViewModel: ViewModel() {

    private val logTag = MoreViewModel::class.simpleName

    init {
        DebugLog.i(logTag, "init-()")
    }

}