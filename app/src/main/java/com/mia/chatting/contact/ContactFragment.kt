package com.mia.chatting.contact

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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mia.chatting.R
import com.mia.chatting.databinding.FragmentContactBinding
import com.mia.chatting.register.RegisterFragmentDirections
import com.mia.chatting.util.DebugLog

class ContactFragment : Fragment() {

    private val logTag = ContactFragment::class.simpleName
    private lateinit var binding: FragmentContactBinding
    private val contactViewModel: ContactViewModel by activityViewModels()

    private lateinit var auth: FirebaseAuth
    private val databaseRef = Firebase.database.reference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact, container, false)
        with(binding) {
            viewModel = contactViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        DebugLog.i(logTag, "onCreateView-()")
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DebugLog.i(logTag, "onViewCreated-()")
        binding.checkBtn.setOnClickListener {
            // 파베 DB 친구 목록 불러오기
            readContact()
        }
        binding.plusBtn.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(ContactFragmentDirections.actionContactFragmentToAddFragment())
        }
    }

    private fun readContact() {
        DebugLog.i(logTag, "readContact-()")
        auth.uid?.let { uid ->
            databaseRef.child("users").child(uid).get().addOnSuccessListener {
                DebugLog.d(logTag, "Got value => ${it.key}")
                DebugLog.d(logTag, "Got value => ${it.value}")
            }.addOnFailureListener{
                DebugLog.e("$it")
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