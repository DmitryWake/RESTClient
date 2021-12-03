package com.ewake.restclient.presentation.ui.fragment.scriptlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ewake.restclient.data.db.room.entity.ScriptEntity
import com.ewake.restclient.databinding.FragmentScriptListBinding
import com.ewake.restclient.presentation.ui.fragment.scriptlist.adapter.ScriptAdapter
import com.ewake.restclient.presentation.viewmodel.scriptlist.ScriptListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScriptListFragment : Fragment() {

    private var _binding: FragmentScriptListBinding? = null
    private val binding: FragmentScriptListBinding
        get() = _binding!!

    private val viewModel by viewModels<ScriptListViewModel>()

    private val adapter = ScriptAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter.apply {
            onDeleteItemClickListener = viewModel::onDeleteItemClicked
            onItemClickListener = viewModel::onItemClicked
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScriptListBinding.inflate(inflater, container, false)

        binding.apply {
            scriptList.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = this@ScriptListFragment.adapter
            }

            add.setOnClickListener { viewModel.onAddButtonClicked() }
        }

        viewModel.apply {
            navigationLiveData.observe(viewLifecycleOwner, ::navigate)
            messageLiveData.observe(viewLifecycleOwner, ::showMessage)
            onItemAddLiveData.observe(viewLifecycleOwner, ::addItem)
            deleteItemLiveData.observe(viewLifecycleOwner, ::deleteItem)
            scriptListLiveData.observe(viewLifecycleOwner, ::setItems)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigate(action: NavDirections) {
        findNavController().navigate(action)
    }

    private fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setItems(mutableList: MutableList<ScriptEntity>) {
        adapter.items = mutableList
    }

    private fun addItem(item: ScriptEntity) {
        adapter.addItem(item)
    }

    private fun deleteItem(item: ScriptEntity) {
        adapter.deleteItem(item)
    }
}