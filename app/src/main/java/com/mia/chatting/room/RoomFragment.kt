package com.mia.chatting.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.mia.chatting.R
import com.mia.chatting.contact.ContactFragmentDirections
import com.mia.chatting.databinding.FragmentRoomBinding
import com.mia.chatting.util.DebugLog

class RoomFragment: Fragment() {

    private val logTag = RoomFragment::class.simpleName
    private lateinit var binding: FragmentRoomBinding
    private val roomViewModel: RoomViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_room, container, false)
        with(binding) {
            viewModel = roomViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        DebugLog.i(logTag, "onCreateView-()")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DebugLog.i(logTag, "onViewCreated-()")

        binding.recyclerView.apply {
            adapter = roomViewModel.roomAdapter
        }

        roomViewModel.roomAdapter.clickListener = { afterFirebaseData, i ->
            DebugLog.d(logTag, "clicked afterFirebaseData uid: ${afterFirebaseData.outerKey}") // roomId
            val frdUid = roomViewModel.extractFromRoomId(afterFirebaseData.outerKey)
            roomViewModel.extractYourName(frdUid) {
                Navigation.findNavController(binding.root)
                    .navigate(RoomFragmentDirections.actionRoomFragmentToMessageFragment(afterFirebaseData.outerKey, it))
            }
        }

        // 파베 채팅방 목록 불러오기
        roomViewModel.getCurrentRoom()
    }


}