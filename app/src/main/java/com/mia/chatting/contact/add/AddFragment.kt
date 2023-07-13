package com.mia.chatting.contact.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mia.chatting.R
import com.mia.chatting.databinding.FragmentAddBinding
import com.mia.chatting.databinding.FragmentContactBinding
import com.mia.chatting.util.ConfirmDialogInterface
import com.mia.chatting.util.CustomDialog
import com.mia.chatting.util.DebugLog

class AddFragment : Fragment(), ConfirmDialogInterface {

    private val logTag = AddFragment::class.simpleName
    private lateinit var binding: FragmentAddBinding
    private val addViewModel: AddViewModel by activityViewModels()

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
    }

    override fun yesBtnClicked() {
        DebugLog.i(logTag, "yesBtnClicked-()")
    }
}