package com.ewake.restclient.presentation.ui.fragment.scriptdetail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ewake.restclient.R
import com.ewake.restclient.databinding.FragmentScriptDetailBinding
import com.ewake.restclient.presentation.model.RequestResponseModel
import com.ewake.restclient.presentation.ui.dialog.RenameDialog
import com.ewake.restclient.presentation.ui.fragment.scriptdetail.adapter.ScriptViewPagerAdapter
import com.ewake.restclient.presentation.viewmodel.scriptdetail.ScriptDetailViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScriptDetailFragment : Fragment(), RenameDialog.OnPositiveClickListener {

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

        viewPagerAdapter.onRequestSendClickListener = viewModel::onSendRequestClicked

        viewModel.apply {
            start()
            listLiveData.observe(viewLifecycleOwner, ::setData)
            addLiveData.observe(viewLifecycleOwner, ::addItem)
            deleteItemLiveData.observe(viewLifecycleOwner, ::deleteItem)
            messageLiveData.observe(viewLifecycleOwner, ::showMessage)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.script_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.title) {
            "Сохранить" -> {
                viewModel.onSaveClicked(viewPagerAdapter.items)
                true
            }
            "Добавить" -> {
                viewModel.onAddClicked()
                true
            }
            "Удалить выбранный" -> {
                viewModel.onDeleteClicked(binding.viewPager.currentItem)
                true
            }
            "Переименовать сценарий" -> {
                RenameDialog.Builder().setOnPositiveClickListener(this).build().show(childFragmentManager, "RenameDialog")
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
        binding.viewPager.setCurrentItem(viewPagerAdapter.items.size - 1, true)
    }

    private fun deleteItem(position: Int) {
        viewPagerAdapter.deleteItem(position)
    }

    private fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onClick(name: String) {
        viewModel.onScriptRenameClicked(name)
    }
}