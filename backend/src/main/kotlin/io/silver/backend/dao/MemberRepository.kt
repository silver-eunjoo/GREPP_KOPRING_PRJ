package io.silver.backend.dao

import io.silver.backend.domain.Member
import io.silver.backend.dto.MemberView
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByEmail(email: String): Member?

//    @Query("""
//        select
//            new io.silver.backend.dto.MemberDescription(m.name, m.email, m.role)
//        from
//            Member m
//    """)
//    fun findAllDescription()


    @Query("""
        select
            m.name as name, m.email as email, m.role as role
        from Member m
    """)
    fun findAllMemberView() : List<MemberView>
}