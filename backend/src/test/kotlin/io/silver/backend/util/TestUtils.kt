package io.silver.backend.util

import io.silver.backend.domain.Member
import io.silver.backend.dto.MemberDescription
import io.silver.backend.dto.Role

fun genMember(targetName: String, targetEmail: String, targetRole: Role = Role.BRONZE) =
    Member(null, targetName, targetEmail, targetRole)

fun genMemberDesc(
    actualName: String,
    actualEmail: String,
    actualRole: Role
) = MemberDescription(actualName, actualEmail, actualRole)