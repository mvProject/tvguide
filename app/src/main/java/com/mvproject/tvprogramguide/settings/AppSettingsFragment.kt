package com.mvproject.tvprogramguide.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.radiobutton.MaterialRadioButton
import com.mvproject.tvprogramguide.MainActivity
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.databinding.FragmentAppSettingsBinding
import com.mvproject.tvprogramguide.utils.NO_VALUE_INT
import com.mvproject.tvprogramguide.utils.NO_VALUE_STRING
import com.mvproject.tvprogramguide.utils.routeToBack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppSettingsFragment : Fragment() {
    private var _binding: FragmentAppSettingsBinding? = null
    private val binding get() = _binding!!

    private val appSettingsViewModel: AppSettingsViewModel by viewModels()

    private var langSelected = NO_VALUE_STRING
    private var themeSelected = NO_VALUE_INT

    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            appSettingsToolbar.apply {
                toolbarTitle.text = getString(R.string.settings_title)
                btnBack.setOnClickListener {
                    routeToBack()
                }
            }

            programUpdateSelector.apply {
                setValue(appSettingsViewModel.updateProgramsPeriod)
                onValueChange { value ->
                    appSettingsViewModel.setProgramsUpdatePeriod(value)
                }
            }
            channelUpdateSelector.apply {
                setValue(appSettingsViewModel.updateChannelsPeriod)
                onValueChange { value ->
                    appSettingsViewModel.setChannelsUpdatePeriod(value)
                }
            }
            programViewSelector.apply {
                setValue(appSettingsViewModel.programsViewCount)
                onValueChange { value ->
                    appSettingsViewModel.setProgramsCount(value)
                }
            }
        }

        initLanguagesSelector()

        initThemesSelector()
    }

    private fun initLanguagesSelector() {
        val langOptions = listOf(AppLang.English, AppLang.Russian, AppLang.Ukrainian)
        with(binding) {
            for (item in langOptions.indices) {
                MaterialRadioButton(binding.root.context, null, R.attr.radioButtonStyle).apply {
                    id = View.generateViewId()
                    text = langOptions[item].name
                    tag = langOptions[item].locale
                    layoutParams = params
                    setTextColor(
                        ContextCompat.getColorStateList(
                            binding.root.context,
                            R.color.selector_dialog_text_color
                        )
                    )
                }.also { rb ->
                    langGroup.addView(rb)
                }
            }

            val lang = appSettingsViewModel.selectedLanguage
            langGroup.forEach { v ->
                if (v is RadioButton) {
                    if (v.tag.toString() == lang) {
                        v.isChecked = true
                    }
                }
            }
            langGroup.setOnCheckedChangeListener { radioGroup, clickedButton ->
                langSelected =
                    radioGroup.findViewById<RadioButton>(clickedButton).tag.toString()
                appSettingsViewModel.setSelectedLanguage(langSelected)
                (activity as? MainActivity)?.recreate()
            }
        }
    }

    private fun initThemesSelector() {
        val themeOptions = listOf(AppTheme.Light(), AppTheme.Dark(), AppTheme.System())
        with(binding) {
            for (item in themeOptions.indices) {
                MaterialRadioButton(binding.root.context, null, R.attr.radioButtonStyle).apply {
                    id = View.generateViewId()
                    text = getString(themeOptions[item].getProperTitle())
                    tag = themeOptions[item].value
                    layoutParams = params
                    setTextColor(
                        ContextCompat.getColorStateList(
                            binding.root.context,
                            R.color.selector_dialog_text_color
                        )
                    )
                }.also { rb ->
                    themeGroup.addView(rb)
                }
            }

            val mode = appSettingsViewModel.selectedThemeMode
            themeGroup.forEach { v ->
                if (v is RadioButton) {
                    if (Integer.parseInt(v.tag.toString()) == mode) {
                        v.isChecked = true
                    }
                }
            }

            themeGroup.setOnCheckedChangeListener { radioGroup, clickedButton ->
                themeSelected =
                    radioGroup.findViewById<RadioButton>(clickedButton).tag.toString().toInt()
                appSettingsViewModel.setSelectedTheme(themeSelected)
            }
        }
    }

    private val params
        get() = RadioGroup.LayoutParams(binding.root.context, null).apply {
            setMargins(
                DEFAULT_HORIZONTAL_MARGIN,
                DEFAULT_VERTICAL_MARGIN,
                DEFAULT_HORIZONTAL_MARGIN,
                DEFAULT_VERTICAL_MARGIN
            )
        }

    companion object {
        private const val DEFAULT_VERTICAL_MARGIN = 0
        private const val DEFAULT_HORIZONTAL_MARGIN = 22
    }
}
