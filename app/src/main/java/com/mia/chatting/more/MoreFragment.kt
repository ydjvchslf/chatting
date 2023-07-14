package com.mia.chatting.more

import android.content.Intent
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
import com.mia.chatting.contact.ContactFragmentDirections
import com.mia.chatting.databinding.FragmentMessageBinding
import com.mia.chatting.databinding.FragmentMoreBinding
import com.mia.chatting.util.DebugLog

class MoreFragment: Fragment() {

    private val logTag = MoreFragment::class.simpleName
    private lateinit var binding: FragmentMoreBinding
    private val moreViewModel: MoreViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false)
        with(binding) {
            viewModel = moreViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        DebugLog.i(logTag, "onCreateView-()")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DebugLog.i(logTag, "onViewCreated-()")

        binding.logoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            activity?.startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }
    }
}