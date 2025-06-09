package io.silver.backend.util

import io.silver.backend.domain.Member
import io.silver.backend.dto.Role

fun genMember(targetName: String, targetEmail: String) =
    Member(null, targetName, targetEmail, Role.BRONZE)