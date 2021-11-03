package com.ewake.restclient.presentation.ui.fragment.mainpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ewake.restclient.databinding.FragmentMainPageBinding
import com.ewake.restclient.presentation.model.RequestModel
import com.ewake.restclient.presentation.model.ResponseModel
import com.ewake.restclient.presentation.ui.view.requestresponse.RequestResponseView
import com.ewake.restclient.presentation.viewmodel.mainpage.MainPageViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author Nikolaevsky Dmitry (@d.nikolaevskiy)
 */
@AndroidEntryPoint
class MainPageFragment : Fragment(), RequestResponseView.OnSendRequestClickListener {

    private var _binding: FragmentMainPageBinding? = null
    private val binding: FragmentMainPageBinding
        get() = _binding!!

    private val viewModel by viewModels<MainPageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainPageBinding.inflate(inflater, container, false)

        viewModel.apply {
            messageLiveData.observe(viewLifecycleOwner, ::showMessage)
            responseLiveData.observe(viewLifecycleOwner, ::setResponse)
        }

        binding.requestResponse.onSendClickListener = this

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setResponse(response: ResponseModel) {
        binding.requestResponse.response = response
    }

    override fun onSend(requestModel: RequestModel) {
        viewModel.onSendClicked(requestModel)
    }
}