package com.ewake.restclient.presentation.model

/**
 * @author Nikolaevsky Dmitry (@d.nikolaevskiy)
 */

data class RequestResponseModel(
    var id: Int? = null,
    var request: RequestModel = RequestModel(),
    var response: ResponseModel = ResponseModel(),
    var scriptId: Int? = null
)

data class RequestModel(
    var method: RequestMethod = RequestMethod.GET,
    var url: String = "",
    var headers: MutableList<Pair<String, String>> = mutableListOf(),
    var query: MutableList<Pair<String, String>> = mutableListOf(),
    var body: String = ""
)

data class ResponseModel(
    var code: Int = 0,
    var message: String = "",
    var body: String = ""
)

enum class RequestMethod {
    GET, POST
}