package com.mia.chatting.register

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.mia.chatting.MainActivity
import com.mia.chatting.R
import com.mia.chatting.databinding.FragmentRegisterBinding
import com.mia.chatting.util.DebugLog

class RegisterFragment : Fragment() {

    private val logTag = RegisterFragment::class.simpleName
    private lateinit var binding: FragmentRegisterBinding
    private val registerViewModel: RegisterViewModel by activityViewModels()

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DebugLog.i(logTag, "onViewCreated-()")
        binding.signInBtn.setOnClickListener {

        }
        binding.loginBtn.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
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