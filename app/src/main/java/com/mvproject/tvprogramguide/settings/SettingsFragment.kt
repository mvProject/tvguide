package com.mvproject.tvprogramguide.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.databinding.FragmentSettingsBinding
import com.mvproject.tvprogramguide.utils.routeTo
import com.mvproject.tvprogramguide.utils.routeToBack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            settingsToolbar.apply {
                toolbarTitle.text = getString(R.string.settings_title)
                btnBack.setOnClickListener {
                    routeToBack()
                }
            }

            optionChannels.setOnClickListener {
                routeTo(destination = SettingsFragmentDirections.toSettingChannelsListFragment())
            }

            optionSettings.setOnClickListener {
                routeTo(destination = SettingsFragmentDirections.toAppSettingsFragment())
            }
        }
    }
}
