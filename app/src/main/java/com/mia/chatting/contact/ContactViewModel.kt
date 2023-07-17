package com.mia.chatting.contact

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mia.chatting.adapter.ContactAdapter
import com.mia.chatting.data.UserData
import com.mia.chatting.util.DataConverter
import com.mia.chatting.util.DebugLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ContactViewModel: ViewModel() {

    private val logTag = ContactViewModel::class.simpleName
    private val auth: FirebaseAuth
    private val databaseRef = Firebase.database.reference

    var currentUser = UserData()
    var contactAdapter = ContactAdapter()
    var contactSize = MutableLiveData(0)

    init {
        DebugLog.i(logTag, "init-()")
        auth = Firebase.auth
    }

    fun getCurrentContact() {
        DebugLog.i(logTag, "getCurrentContact-()")
        getMyData {
            settingContactList()
        }
    }

    fun getMyData(isDone: (Boolean) -> Unit) {
        DebugLog.i(logTag, "getMyData-()")
        viewModelScope.launch(Dispatchers.IO) {
            auth.uid?.let { uid ->
                databaseRef.child("users").child(uid).get().addOnSuccessListener { dataSnapshot ->
                    val currentUserData = DataConverter.jsonToUserData(dataSnapshot)
                    DebugLog.d(logTag, "currentUserData => $currentUserData")
                    currentUser = currentUserData
                    isDone.invoke(true)
                }.addOnFailureListener{
                    DebugLog.e("$it")
                }
            }
        }
    }

    fun settingContactList() {
        DebugLog.i(logTag, "settingContactList-()")
        val emailList = currentUser.contacts
        val emailCount = emailList?.size ?: 0 // Get the count of emails
        var completedCount = 0 // Counter variable
        val friendList = ArrayList<UserData>()

        DebugLog.d(logTag, "emailList : $emailList")

        emailList?.forEach { email ->
            auth.uid?.let { uid ->
                val userRef = databaseRef.child("users")
                userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (data in snapshot.children) {
                            friendList.add(DataConverter.jsonToUserData(data))
                        }
                        completedCount++ // Increment the counter after processing data

                        if (completedCount == emailCount) {
                            DebugLog.d(logTag, "forEach 끝")
                            contactAdapter.submitList(friendList)
                            contactSize.value = friendList.size
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        // Handle cancelled event
                        completedCount++ // Increment the counter after processing data

                        if (completedCount == emailCount) {
                            DebugLog.d(logTag, "forEach 끝")
                        }
                    }
                })
            }
        }
    }

    fun checkHistory(frdUid: String?, isExist: (Boolean, String?) -> Unit) {
        DebugLog.i(logTag, "checkHistory-()")
        if ((frdUid?.isNotEmpty() == true) && auth.uid?.isNotEmpty() == true) {
            val roomIdList = arrayListOf(frdUid, auth.uid!!).sorted()
            val roomId = "${roomIdList[0]}-${roomIdList[1]}" // uid-uid
            DebugLog.d(logTag, "roomId: $roomId")
            // 기존 채팅방 있는지 체크
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.hasChild("roomId")) {
                        // run some code
                        val currentUserData = DataConverter.jsonToUserData(dataSnapshot)
                        DebugLog.d(logTag, "currentUserData => $currentUserData")
                        isExist.invoke(true, roomId)
                    } else {
                        DebugLog.d(logTag, "기존 채팅방을 못 찾음")
                        isExist.invoke(false, roomId)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    DebugLog.e("$error")
                }
            })
        }
    }
}