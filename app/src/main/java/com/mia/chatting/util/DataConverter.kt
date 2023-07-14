package com.mia.chatting.util

import com.google.firebase.database.DataSnapshot
import com.google.gson.Gson
import com.mia.chatting.data.UserData
import kotlin.math.log

object DataConverter {

    fun jsonToUserData(dataSnapshot: DataSnapshot): UserData {
        val gson = Gson()
        val obj = dataSnapshot.getValue(Object::class.java)
        val json = gson.toJson(obj)
        val data = gson.fromJson(json, UserData::class.java) //Json 파일을 데이터 객체로 변환
        DebugLog.d("DataConverter", "data: $data")
        return data
    }
}