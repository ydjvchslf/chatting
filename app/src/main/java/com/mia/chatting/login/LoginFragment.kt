package com.mia.chatting.login

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
import com.google.firebase.ktx.Firebase
import com.mia.chatting.MainActivity
import com.mia.chatting.R
import com.mia.chatting.databinding.FragmentLoginBinding
import com.mia.chatting.register.RegisterFragmentDirections
import com.mia.chatting.util.DebugLog

class LoginFragment : Fragment() {

    private val logTag = LoginFragment::class.simpleName
    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        with(binding) {
            viewModel = loginViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        DebugLog.i(logTag, "onCreateView-()")
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DebugLog.i(logTag, "onViewCreated-()")
        // 자동로그인
        if (auth.currentUser != null) {
            Navigation.findNavController(binding.root).navigate(LoginFragmentDirections.actionLoginFragmentToRoomFragment())
            return
        }

        binding.signInBtn.setOnClickListener {
            signIn(binding.inputEmail.text.toString(), binding.inputPw.text.toString())
        }
        binding.registerBtn.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
        binding.googleBtn.setOnClickListener {

        }
    }

    private fun signIn(email: String, password: String) {
        DebugLog.i(logTag, "signIn-()")
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        DebugLog.d(logTag, "로그인 Success")
                        Navigation.findNavController(binding.root).navigate(LoginFragmentDirections.actionLoginFragmentToRoomFragment())
                    } else {
                        DebugLog.d(logTag, "로그인 Fail")
                        DebugLog.d(logTag, "e => ${task.exception.toString()}")
                    }
                }
        }
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