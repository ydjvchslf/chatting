package com.mia.chatting.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mia.chatting.R
import com.mia.chatting.data.UserData
import com.mia.chatting.databinding.LayoutContactItemBinding
import com.mia.chatting.util.DebugLog

class ContactAdapter: RecyclerView.Adapter<ContactViewHolder>() {

    private val logTag = ContactAdapter::class.simpleName
    var contactList = ArrayList<UserData>()

    var clickListener : ((UserData, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        // 연결할 레이아웃 설정
        val item = LayoutInflater.from(parent.context).inflate(R.layout.layout_contact_item, parent, false)
        val contactViewHolder = ContactViewHolder(LayoutContactItemBinding.bind(item))

        // 리스너 콜백 등록
        item.setOnClickListener {
            val position = contactViewHolder.adapterPosition
            DebugLog.i(logTag, "position: $position, ${contactList[position]}")
            clickListener?.invoke(contactList[position], position)
        }

        return contactViewHolder
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        DebugLog.i(logTag, "onBindViewHolder-()")
        holder.bind(this.contactList[position])
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    // 외부데이터 넘기기 -> adapter에서 갖고 있는 deviceList 와 외부에서 들어온 deviceList 를 연결해주는 함수
    @SuppressLint("NotifyDataSetChanged")
    fun submitList(contactList: ArrayList<UserData>){
        this.contactList = contactList // 외부데이터를 adapter 데이터로 할당
        DebugLog.d(logTag, "this.contactList: ${this.contactList}")
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        contactList.clear()
        notifyDataSetChanged()
    }
}