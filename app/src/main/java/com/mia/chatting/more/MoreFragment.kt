package com.mia.chatting.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mia.chatting.R
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
    }
}