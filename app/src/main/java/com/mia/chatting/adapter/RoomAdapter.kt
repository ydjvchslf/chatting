package com.mia.chatting.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mia.chatting.R
import com.mia.chatting.data.AfterFirebaseData
import com.mia.chatting.data.FirebaseData
import com.mia.chatting.data.RoomData
import com.mia.chatting.data.UserData
import com.mia.chatting.databinding.LayoutRoomItemBinding
import com.mia.chatting.util.DebugLog

class RoomAdapter: RecyclerView.Adapter<RoomViewHolder>() {

    private val logTag = RoomAdapter::class.simpleName
    var roomList = ArrayList<AfterFirebaseData>()

    var clickListener : ((AfterFirebaseData, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        // 연결할 레이아웃 설정
        val item = LayoutInflater.from(parent.context).inflate(R.layout.layout_room_item, parent, false)
        val roomViewHolder = RoomViewHolder(LayoutRoomItemBinding.bind(item))

        // 리스너 콜백 등록
        item.setOnClickListener {
            val position = roomViewHolder.adapterPosition
            DebugLog.i(logTag, "position: $position, ${roomList[position]}")
            clickListener?.invoke(roomList[position], position)
        }

        return roomViewHolder
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        DebugLog.i(logTag, "onBindViewHolder-()")
        holder.bind(this.roomList[position])
    }

    override fun getItemCount(): Int {
        return roomList.size
    }

    // 외부데이터 넘기기 -> adapter에서 갖고 있는 deviceList 와 외부에서 들어온 deviceList 를 연결해주는 함수
    @SuppressLint("NotifyDataSetChanged")
    fun submitList(roomList: ArrayList<AfterFirebaseData>){
        this.roomList = roomList // 외부데이터를 adapter 데이터로 할당
        DebugLog.d(logTag, "this.contactList: ${this.roomList}")
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        roomList.clear()
        notifyDataSetChanged()
    }
}