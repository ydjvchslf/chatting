package com.mia.chatting.util

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mia.chatting.databinding.LayoutDialogBinding

class CustomDialog (
    confirmDialogInterface: ConfirmDialogInterface,
    private var title: String,
    private var cmd: String
): DialogFragment() {

    private val logTag = CustomDialog::class.simpleName
    private lateinit var binding: LayoutDialogBinding

    private var confirmDialogInterface: ConfirmDialogInterface? = null

    init {
        this.confirmDialogInterface = confirmDialogInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutDialogBinding.inflate(inflater, container, false)
        DebugLog.i(logTag, "onCreateView-()")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DebugLog.i(logTag, "onViewCreated-()")
        DebugLog.d(logTag, "전달받은 title: $title, cmd: $cmd")
        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // TRANSPARENT

        binding.title.text = title

        // 취소 버튼 클릭
        binding.cancelBtn.setOnClickListener {
            dismiss()
        }
        // 확인 버튼 클릭
        binding.yesBtn.setOnClickListener {
            this.confirmDialogInterface?.yesBtnClicked()
            dismiss()
        }

    }
}

interface ConfirmDialogInterface {
    fun yesBtnClicked()
}