package com.ewake.restclient.model

/**
 * @author Nikolaevsky Dmitry (@d.nikolaevskiy)
 */

data class ResponseRequestModel(
    var id: Int? = null,
    var request: RequestModel,
    var response: ResponseModel
)

data class RequestModel(
    var method: RequestMethod,
    var url: String = "",
    var headers: MutableMap<String, String> = mutableMapOf(),
    var query: MutableMap<String, String> = mutableMapOf(),
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