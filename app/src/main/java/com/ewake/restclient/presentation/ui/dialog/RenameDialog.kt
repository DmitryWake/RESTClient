package com.ewake.restclient.presentation.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ewake.restclient.databinding.DialogRenameBinding

class RenameDialog(private val builder: Builder? = null) : DialogFragment() {

    private var _binding: DialogRenameBinding? = null
    private val binding: DialogRenameBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogRenameBinding.inflate(inflater)

        binding.positiveButton.setOnClickListener {
            builder?.onPositiveClickListener?.onClick(binding.input.text.toString())
            dialog?.dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class Builder {

        var onPositiveClickListener: OnPositiveClickListener? = null

        fun setOnPositiveClickListener(listener: OnPositiveClickListener): Builder {
            onPositiveClickListener = listener
            return this
        }

        fun build(): RenameDialog {
            return RenameDialog(this)
        }

    }

    fun interface OnPositiveClickListener {
        fun onClick(string: String)
    }
}