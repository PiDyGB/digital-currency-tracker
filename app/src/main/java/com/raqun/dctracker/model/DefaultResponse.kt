package com.raqun.dctracker.model

import com.google.gson.annotations.SerializedName

/**
 * Created by tyln on 12/09/2017.
 */
data class DefaultResponse<T>(
        @SerializedName("response_code") val code: Int,
        @SerializedName("response_text") val message: String,
        @SerializedName("response_data") val data: T
)