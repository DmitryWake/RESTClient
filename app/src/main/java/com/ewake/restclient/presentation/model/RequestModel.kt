package com.ewake.restclient.presentation.model

/**
 * @author Nikolaevsky Dmitry (@d.nikolaevskiy)
 */

data class RequestResponseModel(
    var id: Int? = null,
    var request: RequestModel,
    var response: ResponseModel
)

data class RequestModel(
    var method: RequestMethod,
    var url: String = "",
    var headers: MutableList<Pair<String, String>> = mutableListOf(),
    var query: MutableList<Pair<String, String>> = mutableListOf(),
    var body: String = ""
)

data class ResponseModel(
    var code: Int,
    var message: String,
    var body: String
)

enum class RequestMethod {
    GET, POST
}

data class ScriptModel(
    var id: Int? = null,
    var name: String = "",
    var itemsId: List<Int> = listOf()
)