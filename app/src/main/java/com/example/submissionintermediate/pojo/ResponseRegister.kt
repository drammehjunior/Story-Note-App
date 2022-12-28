package com.example.submissionintermediate.pojo

import com.google.gson.annotations.SerializedName

data class ResponseRegister(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
