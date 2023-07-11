package com.mia.chatting.contact

import androidx.lifecycle.ViewModel
import com.mia.chatting.util.DebugLog

class ContactViewModel: ViewModel() {

    private val logTag = ContactViewModel::class.simpleName

    init {
        DebugLog.i(logTag, "init-()")
    }

}