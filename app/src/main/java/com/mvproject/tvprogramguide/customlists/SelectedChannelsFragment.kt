package com.mvproject.tvprogramguide.customlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.databinding.FragmentSelectedChannelsBinding
import com.mvproject.tvprogramguide.decoration.ItemSpacingDecorator
import com.mvproject.tvprogramguide.utils.collectFlow
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SelectedChannelsFragment : Fragment() {
    private var _binding: FragmentSelectedChannelsBinding? = null
    private val binding get() = _binding!!

    private val selectedChannelsViewModel: SelectedChannelsViewModel by viewModels()

    private lateinit var selectedChannelsAdapter: SelectedChannelsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectedChannelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedChannelsAdapter = SelectedChannelsAdapter { item ->
            selectedChannelsViewModel.deleteList(item)
        }


        collectFlow(selectedChannelsViewModel.selectedChannels) { lists ->
            selectedChannelsAdapter.items = lists
        }

        with(binding) {
            selectedChannelList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = selectedChannelsAdapter
                addItemDecoration(
                    ItemSpacingDecorator(
                        spacing = requireContext().resources.getDimensionPixelSize(R.dimen.size_10)
                    )
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
       // selectedChannelsViewModel.loadChannels()
    }
}
