package com.mia.chatting.contact.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.mia.chatting.R
import com.mia.chatting.data.UserData
import com.mia.chatting.databinding.FragmentAddBinding
import com.mia.chatting.register.RegisterFragmentDirections
import com.mia.chatting.util.ConfirmDialogInterface
import com.mia.chatting.util.CustomDialog
import com.mia.chatting.util.DebugLog


class AddFragment : Fragment(), ConfirmDialogInterface {

    private val logTag = AddFragment::class.simpleName
    private lateinit var binding: FragmentAddBinding
    private val addViewModel: AddViewModel by activityViewModels()

    private val databaseRef = Firebase.database.reference
    private val usersRef = databaseRef.child("users")
    private val auth = Firebase.auth

    var cntList: ArrayList<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false)
        with(binding) {
            viewModel = addViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        DebugLog.i(logTag, "onCreateView-()")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DebugLog.i(logTag, "onViewCreated-()")
        binding.addBtn.setOnClickListener {
            val dialog = CustomDialog(this, "연락처를 추가하시겠습니까?", "Add Contact")
            dialog.apply {
                isCancelable = false // 배경 클릭 막기
            }.show(activity?.supportFragmentManager!!, "ConfirmDialog")
        }
        // 기존 연락처 가져오기
        cntList = currentContact()

        binding.backBtn.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(AddFragmentDirections.actionAddFragmentToContactFragment())
        }
    }

    override fun yesBtnClicked() {
        DebugLog.i(logTag, "yesBtnClicked-()")

        val friendEmail = binding.friendEmail.text.toString().trim()
        if (friendEmail.isNotEmpty()) {
            // 입력한 Email 유효한지 체크
            checkEmailDB(friendEmail) { isExist ->
                if (isExist) {
                    // 연락처 추가 (파베 db 업데이트)
                    updateContact(cntList, friendEmail)
                } else {
                    // 유효하지 않은 이메일 입력
                    Toast.makeText(context,"이메일을 다시 확인해주세요!", Toast.LENGTH_LONG).show()
                    binding.friendEmail.text.clear()
                }
            }

        }
    }

    private fun checkEmailDB(inputEmail: String, result: (Boolean) -> Unit) {
        DebugLog.i(logTag, "checkEmailDB-()")
        auth.uid?.let {
            usersRef.orderByChild("email").equalTo(inputEmail).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        DebugLog.d(logTag, "data.value => ${snapshot.value}")
                        result.invoke(true)
                    } else {
                        DebugLog.d(logTag, "data.value == null, ${snapshot.value}")
                        result.invoke(false)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Handle cancelled event
                }
            })
        }
    }

    private fun currentContact(): ArrayList<String>? {
        DebugLog.i(logTag, "currentContact-()")

        auth.uid?.let { uid ->
            databaseRef.child("users").child(uid).get().addOnSuccessListener {
                DebugLog.d(logTag, "Got value => ${it.key}")
                DebugLog.d(logTag, "Got value => ${it.value}")
                val gson = Gson()
                val obj = it.getValue(Object::class.java)
                val json = gson.toJson(obj)
                val currentUserData = gson.fromJson(json, UserData::class.java) //Json 파일을 데이터 객체로 변환
                DebugLog.d(logTag, "currentUserData => $currentUserData")
                cntList = currentUserData?.contacts
            }.addOnFailureListener{
                DebugLog.e("$it")
            }
        }
        return cntList
    }

    private fun updateContact(cntList: ArrayList<String>?, friendEmail: String) {
        DebugLog.i(logTag, "updateContact-()")

        if (cntList == null) { // 연락처 첫등록
            val contactList = arrayListOf<String>()
            contactList.add(friendEmail)
            val userUpdates: MutableMap<String, Any> = HashMap()
            userUpdates["${Firebase.auth.currentUser?.uid}/contacts"] = contactList
            usersRef.updateChildren(userUpdates)
        } else { // 기존 연락처 존재
            cntList.add(friendEmail)
            val userUpdates: MutableMap<String, Any> = HashMap()
            userUpdates["${Firebase.auth.currentUser?.uid}/contacts"] = cntList
            usersRef.updateChildren(userUpdates)
        }

        Navigation.findNavController(binding.root).navigate(AddFragmentDirections.actionAddFragmentToContactFragment())
    }

}