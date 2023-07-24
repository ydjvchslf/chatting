package com.mia.chatting.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mia.chatting.R
import com.mia.chatting.data.UserData
import com.mia.chatting.databinding.FragmentRegisterBinding
import com.mia.chatting.util.DebugLog


class RegisterFragment: Fragment() {

    private val logTag = RegisterFragment::class.simpleName
    private lateinit var binding: FragmentRegisterBinding
    private val registerViewModel: RegisterViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    private val databaseRef = Firebase.database.reference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        with(binding) {
            viewModel = registerViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DebugLog.i(logTag, "onViewCreated-()")
        binding.signUpBtn.setOnClickListener {
            createAccount(binding.inputName.text.toString(), binding.inputEmail.text.toString().trim(), binding.inputPw.text.toString())
        }
        binding.loginBtn.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }
    }

    private fun createAccount(name:String, email: String, password: String) {
        DebugLog.i(logTag, "createAccount-()")
        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            DebugLog.d(logTag, "name: $name, email: $email, pw: $password")
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        DebugLog.d(logTag, "계정 생성 Success")
                        val user = auth.currentUser
                        DebugLog.d(logTag, "user: $user")
                        // Firebase User db 추가
                        val userData = UserData(name, user?.uid, email)
                        addUser(userData)
                        // 홈 화면 전환
                        Navigation.findNavController(binding.root).navigate(RegisterFragmentDirections.actionRegisterFragmentToRoomFragment())
                    } else {
                        DebugLog.d(logTag, "계정 생성 Fail, e => ${task.exception.toString()}")
                    }
                }
        }
    }

    private fun addUser(user: UserData) {
        DebugLog.i(logTag, "addUser-()")
        databaseRef.child("users").child(user.uid ?: "none").setValue(user)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        DebugLog.i(logTag, "onDestroyView-()")
    }

    override fun onDestroy() {
        super.onDestroy()
        DebugLog.i(logTag, "onDestroy-()")
    }
}