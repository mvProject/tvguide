package com.mvproject.tvprogramguide.customlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.databinding.FragmentAllChannelsBinding
import com.mvproject.tvprogramguide.utils.decoration.ItemSpacingDecorator
import com.mvproject.tvprogramguide.utils.collectFlow
import com.mvproject.tvprogramguide.utils.onTextChanged
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllChannelsFragment : Fragment() {
    private var _binding: FragmentAllChannelsBinding? = null
    private val binding get() = _binding!!

    private val allChannelViewModel: AllChannelViewModel by viewModels()

    private lateinit var allChannelsAdapter: AllChannelsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllChannelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allChannelsAdapter = AllChannelsAdapter { item ->
            allChannelViewModel.applyChannelAction(item)
        }

        with(binding) {
            collectFlow(allChannelViewModel.allChannels) { lists ->
                allChannelsAdapter.items = lists
            }

            collectFlow(allChannelViewModel.selectedChannels) {
                allChannelViewModel.reloadAllChannels()
            }

            allChannelList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = allChannelsAdapter
                addItemDecoration(
                    ItemSpacingDecorator(
                        spacing = requireContext().resources.getDimensionPixelSize(R.dimen.size_10)
                    )
                )
            }

            searchContainer.searchEditText.onTextChanged {
                allChannelViewModel.filterByQuery(it)
            }
        }
    }
}
