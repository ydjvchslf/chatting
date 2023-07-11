package com.mia.chatting.register

import androidx.lifecycle.ViewModel
import com.mia.chatting.util.DebugLog

class RegisterViewModel: ViewModel() {

    private val logTag = RegisterViewModel::class.simpleName

    init {
        DebugLog.i(logTag, "init-()")
    }

}