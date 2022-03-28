package com.mvproject.tvprogramguide.customlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.fragment.app.Fragment
import com.mvproject.tvprogramguide.components.SelectedChannelsScreen
import com.mvproject.tvprogramguide.databinding.FragmentSelectedChannelsComposeBinding
import com.mvproject.tvprogramguide.theme.TvGuideTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@AndroidEntryPoint
class SelectedChannelsFragment : Fragment() {
    private var _binding: FragmentSelectedChannelsComposeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectedChannelsComposeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.setContent {
            TvGuideTheme {
                SelectedChannelsScreen()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
