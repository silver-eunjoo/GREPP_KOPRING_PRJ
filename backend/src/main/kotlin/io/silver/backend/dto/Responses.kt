package io.silver.backend.dto

// 코드표
// 2001 -> Member 등록 성공
// 2002 -> Member 수정 성공

data class GeneralResponse<T>(
    val data: T?,
    val message: String,
)