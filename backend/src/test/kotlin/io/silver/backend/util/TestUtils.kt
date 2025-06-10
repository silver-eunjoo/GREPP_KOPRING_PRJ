package io.silver.backend.util

import io.silver.backend.domain.Member
import io.silver.backend.dto.MemberDescription
import io.silver.backend.dto.MemberUpdateDescription
import io.silver.backend.dto.Role

fun genMember(targetName: String, targetEmail: String, targetRole: Role = Role.BRONZE) =
    Member(null, targetName, targetEmail, targetRole)

fun genMemberDesc(
    actualName: String,
    actualEmail: String,
    actualRole: Role
) = MemberDescription(actualName, actualEmail, actualRole)

fun genMemberList(size: Int) : List<Member> {
    val result: MutableList<Member> = mutableListOf()

    for (i in 1..size) {
        result.add(genMember("member_$i", "member_${i}@email.com"))
    }

    return result
}

fun genMemberDescList(size: Int) : List<MemberDescription> {
    val result: MutableList<MemberDescription> = mutableListOf()

    for (i in 1..size) {
        result.add(genMemberDesc("member_$i", "member_${i}@email.com", Role.SILVER))
    }

    return result
}