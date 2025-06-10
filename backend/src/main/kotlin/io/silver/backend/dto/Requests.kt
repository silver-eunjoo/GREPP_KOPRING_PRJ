package io.silver.backend.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class MemberDescription(

    @field:NotBlank(message = "이름은 반드시 입력되어야 합니다.")
    val name: String,

    @Email(message = "올바른 이메일 형식으로 입력해야 합니다.")
    @field:NotBlank(message = "이메일은 반드시 입력되어야 합니다.")
    val email: String,

    @field:NotNull(message = "등급은 반드시 입력되어야 합니다.")
    val role: Role
)

data class MemberUpdateDescription(
    val name: String,
    val role: Role
)