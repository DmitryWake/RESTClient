package com.ewake.restclient.presentation.model

data class ScriptModel(
    var id: Int? = null,
    var name: String = "",
    var itemsId: MutableList<Int> = mutableListOf()
)
