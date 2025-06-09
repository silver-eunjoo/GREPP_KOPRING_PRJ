package io.silver.backend.domain

import io.silver.backend.dto.Role
import io.silver.backend.global.TimeStamp
import jakarta.persistence.*

@Entity
class Member (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var name: String,
    var email: String,

    @Enumerated(EnumType.STRING)
    var role: Role,

) : TimeStamp()

