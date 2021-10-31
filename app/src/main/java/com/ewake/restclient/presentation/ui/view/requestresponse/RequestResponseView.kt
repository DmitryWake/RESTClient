package com.ewake.restclient.presentation.ui.view.requestresponse

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.ewake.restclient.R
import com.ewake.restclient.databinding.RequestResponseViewBinding
import com.ewake.restclient.presentation.model.RequestMethod
import com.ewake.restclient.presentation.model.RequestResponseModel

/**
 * @author Nikolaevsky Dmitry (@d.nikolaevskiy)
 */
class RequestResponseView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : NestedScrollView(context, attributeSet) {

    private val binding =
        RequestResponseViewBinding.inflate(LayoutInflater.from(context), this, true)

    var model: RequestResponseModel? = null
        set(value) {
            field = value
            value?.let { notifyDataChanged() }
        }

    private val headersAdapter = KeyValueAdapter()
    private val queryAdapter = KeyValueAdapter()

    private val methodAdapter =
        ArrayAdapter(
            context,
            R.layout.dropdown_item,
            RequestMethod.values().map { it.name }
        )

    private val onHeadersDataChangedListener =
        KeyValueAdapter.OnKeyValueDataChangedListener { position, data ->
            model?.request?.headers?.set(
                position,
                data
            )
        }

    private val onQueryDataChangedListener =
        KeyValueAdapter.OnKeyValueDataChangedListener { position, data ->
            model?.request?.query?.set(
                position,
                data
            )
        }

    init {
        setAdapters()
        addListeners()
    }

    private fun addListeners() {
        binding.apply {
            headersAdapter.onItemDataChangedListener = onHeadersDataChangedListener
            queryAdapter.onItemDataChangedListener = onQueryDataChangedListener
        }
    }

    private fun notifyDataChanged() {
        binding.apply {

        }
    }

    private fun setAdapters() {
        binding.apply {
            headers.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = headersAdapter
            }

            query.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = queryAdapter
            }

            method.setAdapter(methodAdapter)
        }
    }
}