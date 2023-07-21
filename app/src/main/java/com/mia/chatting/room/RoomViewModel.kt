package com.mia.chatting.room

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.mia.chatting.adapter.RoomAdapter
import com.mia.chatting.data.FirebaseData
import com.mia.chatting.data.MessageData
import com.mia.chatting.data.RoomData
import com.mia.chatting.data.UserData
import com.mia.chatting.util.DataConverter
import com.mia.chatting.util.DebugLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.SimpleTimeZone

class RoomViewModel: ViewModel() {

    private val logTag = RoomViewModel::class.simpleName
    private val auth: FirebaseAuth
    private val databaseRef = Firebase.database.reference

    var roomAdapter = RoomAdapter()
    var roomSize = MutableLiveData(-1)

    init {
        DebugLog.i(logTag, "init-()")
        auth = Firebase.auth
    }

    fun getCurrentRoom() {
        DebugLog.i(logTag, "getChattingRoom-()")
        checkRoomExist { _, dataSnapshot ->
            dataSnapshot?.let {
                findMyChatId(it)
            }
        }
    }

    fun checkRoomExist(isDone: (Boolean, DataSnapshot?) -> Unit) {
        DebugLog.i(logTag, "checkRoomExist-()")
        viewModelScope.launch(Dispatchers.IO) {
            auth.uid?.let { uid ->
                databaseRef.child("chats").child("message").get().addOnSuccessListener { dataSnapshot ->
                    DebugLog.d(logTag, "dataSnapshot => $dataSnapshot")
                    val isExist = dataSnapshot.value.toString().contains(uid)
                    if (isExist) { // 채팅방 존재
                        isDone.invoke(true, dataSnapshot)
                    } else { // 채팅방 미존재
                        roomSize.value = 0
                        isDone.invoke(true, null)
                    }
                }.addOnFailureListener{
                    DebugLog.e("$it")
                }
            }
        }
    }

    fun findMyChatId(dataSnapshot: DataSnapshot) {
        DebugLog.i(logTag, "findMyChatId-()")
        // 내 uid-uid 들어있는 데이터만 찾아서 파싱
        // Get the value from the DataSnapshot
        val data = dataSnapshot.value

        // Check if the data is a map (key-value pairs)
        if (data is Map<*, *>) {
            val keyValueList = data.toList()

            // Now, keyValueList will contain the List of key-value pairs from the DataSnapshot value
            // You can use this list as per your requirements
            //DebugLog.d(logTag, "keyValueList: $keyValueList")
            //DebugLog.d(logTag, "keyValueList.size: ${keyValueList.size}")

            val chatIdList = arrayListOf<String>()
            keyValueList.forEach {
                val chatId = it.first.toString()
                chatIdList.add(chatId)
            }

            auth.uid?.let { myUid ->
                val myChatList = chatIdList.filter { chatId ->
                    chatId.contains(myUid)
                }
                DebugLog.d(logTag, "myChatList: $myChatList")
                roomSize.value = myChatList.size
                settingMyChattingList(myChatList, dataSnapshot)
            }
        }
    }

    fun settingMyChattingList(myChatList: List<String>, dataSnapshot: DataSnapshot) {
        DebugLog.i(logTag, "settingMyChattingList-()")
        DebugLog.i(logTag, "originData====> $dataSnapshot")

        val data = dataSnapshot.value

        // Check if the data is a map (key-value pairs)
        if (data is Map<*, *>) {
            // Create a list to store Data objects
            val dataList = mutableListOf<FirebaseData>()

            // Iterate over the outer map's entries
            for ((outerKey, outerValue) in data) {
                if (outerValue is Map<*, *>) {
                    // Iterate over the inner map's entries
                    for ((innerKey, innerValue) in outerValue) {
                        if (innerValue is Map<*, *>) {
                            // Extract the required fields from the inner map
                            val senderUid = innerValue["senderUid"] as? String
                            val sendDate = innerValue["sendDate"] as? String
                            val checked = innerValue["checked"] as? Boolean
                            val content = innerValue["content"] as? String

                            // Create a Data object and add it to the dataList
                            val dataObject = FirebaseData(outerKey.toString(), innerKey.toString(), senderUid, sendDate, checked, content)
                            dataList.add(dataObject)
                        }
                    }
                }
            }
            DebugLog.d(logTag, "dataList====> $dataList") // 존재하는 FirebaseData 리스트
            DebugLog.d(logTag, "dataList.size====> ${dataList.size}")

            val groupedDataMap: Map<String, List<FirebaseData>> = dataList.groupBy { it.outerKey }

            val adapterList = arrayListOf<FirebaseData>()
            myChatList.forEach { myChatId ->
                val whatList = groupedDataMap[myChatId]?.sortedByDescending { it.sendDate }?.first()
                DebugLog.d(logTag, "whatList=> $whatList")
                whatList?.let {
                    adapterList.add(whatList)
                }
            }
            roomAdapter.submitList(adapterList)
        }
    }
}