package com.ewake.restclient.presentation.ui.view.requestresponse

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ewake.restclient.R
import com.ewake.restclient.databinding.RequestResponseViewBinding
import com.ewake.restclient.presentation.model.RequestMethod
import com.ewake.restclient.presentation.model.RequestModel
import com.ewake.restclient.presentation.model.RequestResponseModel
import com.ewake.restclient.presentation.model.ResponseModel

/**
 * @author Nikolaevsky Dmitry (@d.nikolaevskiy)
 */
class RequestResponseView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : NestedScrollView(context, attributeSet) {

    private val binding =
        RequestResponseViewBinding.inflate(LayoutInflater.from(context), this, true)

    var model: RequestResponseModel = RequestResponseModel()
        set(value) {
            field = value
            notifyDataChanged()
        }

    var response: ResponseModel
        set(value) {
            model.response = value
            notifyResponseChanged()
        }
        get() = model.response

    var request: RequestModel
        set(value) {
            model.request = value
            notifyRequestChanged()
        }
        get() = model.request

    var onSendClickListener: OnSendRequestClickListener? = null

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
            model.request.headers[position] = data
        }

    private val onQueryDataChangedListener =
        KeyValueAdapter.OnKeyValueDataChangedListener { position, data ->
            model.request.query[position] = data
        }

    private val onHeaderItemTouchHelper =
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                with(viewHolder.adapterPosition) {
                    binding.root.requestFocus()
                    headersAdapter.removeAt(this)
                    model.request.headers.removeAt(this)
                }
            }

        })

    private val onQueryItemTouchHelper =
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                with(viewHolder.adapterPosition) {
                    binding.root.requestFocus()
                    queryAdapter.removeAt(this)
                    model.request.query.removeAt(this)
                }
            }

        })

    init {
        setAdapters()
        addListeners()
        addTouchHelpers()
    }

    private fun addTouchHelpers() {
        binding.apply {
            onHeaderItemTouchHelper.attachToRecyclerView(headers)
            onQueryItemTouchHelper.attachToRecyclerView(query)
        }
    }

    private fun notifyResponseChanged() {
        binding.apply {
            code.text = "${model.response.code.toString()} ${model.response.message}"
            responseBody.setText(model.response.body)
        }
    }

    private fun notifyRequestChanged() {
        binding.apply {
            method.setText(model.request.method.name, false)
            url.setText(model.request.url)

            headersAdapter.items = model.request.headers
            queryAdapter.items = model.request.query

            body.setText(model.request.body)
        }
    }


    private fun addListeners() {
        binding.apply {
            headersAdapter.onItemDataChangedListener = onHeadersDataChangedListener
            queryAdapter.onItemDataChangedListener = onQueryDataChangedListener

            addQuery.setOnClickListener {
                with("" to "") {
                    model.request.query.add(this)
                    queryAdapter.addItem(this)
                }
            }
            addHeader.setOnClickListener {
                with("" to "") {
                    model.request.headers.add(this)
                    headersAdapter.addItem(this)
                }
            }

            send.setOnClickListener { onSendClickListener?.onSend(model.request) }

            url.doAfterTextChanged { model.request.url = it.toString() }
            body.doAfterTextChanged { model.request.body = it.toString() }

            method.setOnItemClickListener { _, _, i, _ ->
                request.method = RequestMethod.values()[i]
            }
        }
    }

    private fun notifyDataChanged() {
        notifyRequestChanged()
        notifyResponseChanged()
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

    fun interface OnSendRequestClickListener {
        fun onSend(requestModel: RequestModel)
    }
}