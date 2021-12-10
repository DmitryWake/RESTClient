package com.ewake.restclient.data.db.room.mapper

import com.ewake.restclient.data.db.room.entity.RequestResponseEntity
import com.ewake.restclient.presentation.model.RequestModel
import com.ewake.restclient.presentation.model.RequestResponseModel
import com.ewake.restclient.presentation.model.ResponseModel
import javax.inject.Inject

/**
 * @author Nikolaevsky Dmitry (@d.nikolaevskiy)
 */
class RequestResponseMapper @Inject constructor() {

    fun entityToModel(entity: RequestResponseEntity): RequestResponseModel {
        return RequestResponseModel(
            id = entity.id,
            request = RequestModel(
                method = entity.method,
                url = entity.url,
                headers = entity.headers,
                query = entity.query,
                body = entity.body
            ),
            response = ResponseModel(
                code = entity.code,
                message = entity.message,
                body = entity.responseBody
            ),
            scriptId = entity.scriptId
        )
    }

    fun modelToEntity(model: RequestResponseModel): RequestResponseEntity {
        return RequestResponseEntity(
            id = model.id,
            method = model.request.method,
            url = model.request.url,
            headers = model.request.headers,
            query = model.request.query,
            body = model.request.body,
            code = model.response.code,
            message = model.response.message,
            responseBody = model.response.body,
            scriptId = model.scriptId
        )
    }

    fun entityListToModelList(list: List<RequestResponseEntity>): List<RequestResponseModel> =
        list.map { entityToModel(it) }

    fun modelListToEntityList(list: List<RequestResponseModel>): List<RequestResponseEntity> =
        list.map { modelToEntity(it) }
}