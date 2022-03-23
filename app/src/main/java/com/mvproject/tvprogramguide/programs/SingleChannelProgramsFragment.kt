package com.mvproject.tvprogramguide.programs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.mvproject.tvprogramguide.components.SingleChannelData
import com.mvproject.tvprogramguide.components.ToolbarWithBack
import com.mvproject.tvprogramguide.databinding.FragmentProgramsSingleComposeBinding
import com.mvproject.tvprogramguide.utils.Utils.parseChannelName
import com.mvproject.tvprogramguide.utils.routeToBack
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class SingleChannelProgramsFragment : Fragment() {
    private var _binding: FragmentProgramsSingleComposeBinding? = null
    private val binding get() = _binding!!

    private val singleChannelProgramsViewModel: SingleChannelProgramsViewModel by viewModels()
    private val arg: SingleChannelProgramsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgramsSingleComposeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.setContent {
            val title = arg.channelName?.parseChannelName() ?: "No Data"
            arg.channelId?.let { id ->
                singleChannelProgramsViewModel.loadPrograms(id)
            }

            val state = singleChannelProgramsViewModel.selectedPrograms.collectAsState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                ToolbarWithBack(title = title) {
                    routeToBack()
                }

                SingleChannelData(singleChannelPrograms = state.value)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
