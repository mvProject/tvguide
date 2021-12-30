package com.mvproject.tvprogramguide.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mvproject.tvprogramguide.R
import com.mvproject.tvprogramguide.databinding.FragmentSettingsChannelsListBinding
import com.mvproject.tvprogramguide.utils.decoration.ItemSpacingDecorator
import com.mvproject.tvprogramguide.database.entity.CustomListEntity
import com.mvproject.tvprogramguide.utils.OnItemClickListener
import com.mvproject.tvprogramguide.utils.collectFlow
import com.mvproject.tvprogramguide.utils.createAddDialog
import com.mvproject.tvprogramguide.utils.routeTo
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SettingsChannelsListFragment : Fragment() {
    private var _binding: FragmentSettingsChannelsListBinding? = null
    private val binding get() = _binding!!

    private var alertDialog: AlertDialog? = null

    private val settingsChannelsListViewModel: SettingsChannelsListViewModel by viewModels()

    private lateinit var customListAdapter: CustomListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsChannelsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customListAdapter = CustomListAdapter(object : OnItemClickListener<CustomListEntity> {
            override fun onItemClick(item: CustomListEntity) {
                routeTo(
                    destination = SettingsChannelsListFragmentDirections.toCustomListFragment(item.name),
                )
            }

            override fun onDeleteClick(item: CustomListEntity) {
                settingsChannelsListViewModel.deleteList(item)
            }

        })

        collectFlow(settingsChannelsListViewModel.customs) { lists ->
            customListAdapter.items = lists
        }

        with(binding) {
            addCustomList.setOnClickListener {
                alertDialog?.cancel()
                alertDialog = createAddDialog(
                    activity = requireActivity(),
                    title = getString(R.string.add_new_custom_list),
                ) { result ->
                    settingsChannelsListViewModel.addCustomList(result)
                    alertDialog?.cancel()
                    // todo update list
                }.apply {
                    show()
                }
            }

            channelList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = customListAdapter
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
        Timber.d("testing onResume")
    }
}
