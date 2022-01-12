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
import com.mvproject.tvprogramguide.utils.Utils.toThemeMode
import com.mvproject.tvprogramguide.utils.routeToBack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppSettingsFragment : Fragment() {
    private var _binding: FragmentAppSettingsBinding? = null
    private val binding get() = _binding!!

    private val appSettingsViewModel: AppSettingsViewModel by viewModels()

    private var langSelected = ""
    private var themeSelected = ""

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

        val langOptions = listOf("English", "Russian", "Ukrainian")
        val supportedLocales = listOf("en", "ru", "uk")
        val themeOptions = listOf("Light", "Dark", "System")
        with(binding) {
            val params = RadioGroup.LayoutParams(binding.root.context, null).apply {
                setMargins(
                    DEFAULT_HORIZONTAL_MARGIN,
                    DEFAULT_VERTICAL_MARGIN,
                    DEFAULT_HORIZONTAL_MARGIN,
                    DEFAULT_VERTICAL_MARGIN
                )
            }

            appSettingsToolbar.apply {
                toolbarTitle.text = getString(R.string.settings_title)
                btnBack.setOnClickListener {
                    routeToBack()
                }

                for (item in langOptions.indices) {
                    MaterialRadioButton(binding.root.context, null, R.attr.radioButtonStyle).apply {
                        id = View.generateViewId()
                        text = langOptions[item]
                        tag = supportedLocales[item]
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

                for (item in themeOptions.indices) {
                    MaterialRadioButton(binding.root.context, null, R.attr.radioButtonStyle).apply {
                        id = View.generateViewId()
                        text = themeOptions[item]
                        tag = themeOptions[item].toThemeMode()
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
                        radioGroup.findViewById<RadioButton>(clickedButton).text.toString()
                    appSettingsViewModel.setSelectedTheme(themeSelected)
                }
            }
        }
    }

    companion object {
        private const val DEFAULT_VERTICAL_MARGIN = 4
        private const val DEFAULT_HORIZONTAL_MARGIN = 22
    }
}
