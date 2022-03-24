package com.mvproject.tvprogramguide.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.components.SettingsContent
import com.mvproject.tvprogramguide.components.ToolbarWithBack
import com.mvproject.tvprogramguide.databinding.FragmentAppSettingsComposeBinding
import com.mvproject.tvprogramguide.utils.routeToBack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppSettingsFragment : Fragment() {
    private var _binding: FragmentAppSettingsComposeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppSettingsComposeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.setContent {

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                ToolbarWithBack(title = stringResource(id = R.string.settings_title)) {
                    routeToBack()
                }

                SettingsContent()
            }
        }
    }
}
