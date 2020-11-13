package com.andrei.kit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrei.dataLayer.engineUtils.Status
import com.andrei.kit.viewModels.ViewModelChat
import com.andrei.dataLayer.models.deserialization.FriendRequest
import com.andrei.kit.Adapters.CustomDivider
import com.andrei.kit.databinding.LayoutFragmentFriendRequestBinding
import com.andrei.kit.utils.observeRequest
import com.andrei.kit.utils.reObserve


class ReceivedFriendRequestsFragment private constructor() : Fragment() {

    private val viewModelChat: ViewModelChat by activityViewModels()

    private val requestAdapter = FriendRequestsAdapter(::acceptRequest)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = LayoutFragmentFriendRequestBinding.inflate(inflater, container, false)

       binding.friendRequestsList.apply {
            adapter = requestAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(CustomDivider(20))
        }

        viewModelChat.receivedFriendRequests.reObserve(viewLifecycleOwner, {
            if(!it.isNullOrEmpty()) {
                requestAdapter.setData(it)
            }
        })
        return binding.root
    }

    private fun acceptRequest(request: FriendRequest) {
        viewModelChat.acceptFriendRequest(request).observeRequest(viewLifecycleOwner,{
            when(it.status){
                Status.SUCCESS ->{

                }
                Status.ERROR->{

                }
                Status.LOADING ->{

                }

            }
        })
    }
    companion object{
        @JvmStatic
        fun getInstance() = ReceivedFriendRequestsFragment()
    }
}