package com.mvproject.tvprogramguide.customlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.customlists.OrderCategoryAdapter.Companion.SELECTED_CHANNELS
import com.mvproject.tvprogramguide.databinding.FragmentCustomListBinding
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@AndroidEntryPoint
class CustomListFragment : Fragment() {
    private var _binding: FragmentCustomListBinding? = null
    private val binding get() = _binding!!

    private val customListViewModel: CustomListViewModel by viewModels()

    private val params: CustomListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customListViewModel.setSelectedList(params.listFoEdit)

        with(binding) {
            OrderCategoryAdapter(requireActivity() as AppCompatActivity).also {
                categoryPager.adapter = it
            }

            // set proper title to tab
            TabLayoutMediator(
                categoryPagerHeader,
                categoryPager
            ) { tab: TabLayout.Tab, position: Int ->
                tab.text = getPageTitle(position)
            }.attach()
        }
    }

    private fun getPageTitle(position: Int) = when (position) {
        SELECTED_CHANNELS -> resources.getText(R.string.selected_channels_title)
        else -> resources.getText(R.string.all_channels_title)
    }

    override fun onDestroyView() {
        customListViewModel.clearSelectedList()
        super.onDestroyView()
        _binding = null
    }
}
