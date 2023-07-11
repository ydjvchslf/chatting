package com.mia.chatting.login

import androidx.lifecycle.ViewModel
import com.mia.chatting.util.DebugLog

class LoginViewModel: ViewModel() {

    private val logTag = LoginViewModel::class.simpleName

    init {
        DebugLog.i(logTag, "init-()")
    }

}