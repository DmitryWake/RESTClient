package com.ewake.restclient.presentation.ui.fragment.scriptdetail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ewake.restclient.R
import com.ewake.restclient.databinding.FragmentScriptDetailBinding
import com.ewake.restclient.presentation.model.RequestResponseModel
import com.ewake.restclient.presentation.ui.fragment.scriptdetail.adapter.ScriptViewPagerAdapter
import com.ewake.restclient.presentation.viewmodel.scriptdetail.ScriptDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScriptDetailFragment : Fragment() {

    private var _binding: FragmentScriptDetailBinding? = null
    private val binding: FragmentScriptDetailBinding
        get() = _binding!!

    private val viewModel by viewModels<ScriptDetailViewModel>()

    private val viewPagerAdapter = ScriptViewPagerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args by navArgs<ScriptDetailFragmentArgs>()
        viewModel.scriptId = args.scriptId
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScriptDetailBinding.inflate(inflater, container, false)

        binding.viewPager.adapter = viewPagerAdapter

        viewModel.apply {
            start()
            listLiveData.observe(viewLifecycleOwner, ::setData)
            addLiveData.observe(viewLifecycleOwner, ::addItem)
            deleteItemLiveData.observe(viewLifecycleOwner, ::deleteItem)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.script_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.title) {
            "Добавить" -> {
                viewModel.onAddClicked()
                true
            }
            "Удалить выбранный" -> {
                viewModel.onDeleteClicked(binding.viewPager.currentItem)
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setData(list: MutableList<RequestResponseModel>) {
        viewPagerAdapter.items = list
    }

    private fun addItem(item: RequestResponseModel) {
        viewPagerAdapter.addItem()
    }

    private fun deleteItem(position: Int) {
        viewPagerAdapter.deleteItem(position)
    }
}