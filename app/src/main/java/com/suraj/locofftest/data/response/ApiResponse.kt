package com.suraj.locofftest.data.response

import com.suraj.locofftest.model.Users

data class ApiResponse(
    val page: Int? = null,
    val per_page: Int? = null,
    val total: Int? = null,
    val total_pages: Int? = null,
    val data: List<Users>? = null
)
