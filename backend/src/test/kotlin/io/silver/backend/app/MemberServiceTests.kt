package io.silver.backend.app

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.silver.backend.dao.MemberRepository
import io.silver.backend.domain.Member
import io.silver.backend.dto.MemberDescription
import io.silver.backend.dto.Role
import io.silver.backend.util.genMember
import io.silver.backend.util.genMemberDesc
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MemberServiceUnitTest {

    val repository = mockk<MemberRepository>()
    val service = MemberService(repository)

    @Test
    fun `적절한 데이터가 들어온다면 저장된 후 MemberDescription을 반환한다`() {

        val actualName = "member1"
        val actualEmail = "member1@email.com"
        val actualRole = Role.BRONZE

        val memberDesc = genMemberDesc(actualName, actualEmail, actualRole)
        val member = genMember(actualName, actualEmail, actualRole)

        every { repository.save(member) } returns member

        val expectedMemberDesc = service.save(memberDesc)

        assertEquals(expectedMemberDesc.name, actualName)
        expectedMemberDesc.name shouldBe actualName

        assertEquals(expectedMemberDesc.email, actualEmail)
        expectedMemberDesc.email shouldBe actualEmail

        assertEquals(expectedMemberDesc.role, actualRole)
        expectedMemberDesc.role shouldBe actualRole
    }

    @Test
    fun `회원이 저장되어 있고 findByEmail을 호출하면 회원을 정상적으로 조회한다`() {

        val actualName = "성찬"
        val actualEmail = "성찬@email.com"
        val actualRole = Role.SILVER

        val expected = genMember(actualName, actualEmail, actualRole)

        every { repository.findByEmail(actualEmail) } returns expected

        val result = service.findByEmail(actualEmail)

        result.name shouldBe actualName
        result.email shouldBe actualEmail
        result.role shouldBe actualRole

    }

    @Test
    fun `없는 회원의 이메일로 findByEmail을 호출하면 NoSuchElementException이 발생할 것이다`() {

        val unavailableEmail = "UNAVAILABLE_EMAIL"

        every { repository.findByEmail(unavailableEmail) } returns null

        val actual = assertThrows<NoSuchElementException> {
            service.findByEmail(unavailableEmail)
        }

        verify(exactly = 1) { repository.findByEmail(unavailableEmail)}
        actual.message shouldBe "해당 회원은 존재하지 않습니다!"

    }

    @Test
    fun `회원이 저장되어 있고 getDescByEmail을 호출하면 MemberDescription을 정상적으로 반환한다`() {

        val targetEmail = "성찬@email.com"
        val expected = genMember("성찬", targetEmail)

        every { repository.findByEmail(targetEmail) } returns expected

        val actual = service.getDescByEmail(targetEmail)

        verify(exactly=1) { repository.findByEmail(targetEmail) }

        actual.name shouldBe expected.name
        actual.email shouldBe expected.email
        actual.role shouldBe expected.role

    }

    @Test
    fun `잘못된 이메일로 getDescByEmail을 호출하면 NoSuchElementException이 발생한다`() {

        val unavailableEmail = "성찬@email.com"

        every { repository.findByEmail(unavailableEmail) } returns null

        val actual = assertThrows<NoSuchElementException> {
            service.getDescByEmail(unavailableEmail)
        }

        verify(exactly=1) {repository.findByEmail(unavailableEmail)}

        actual.message shouldBe "해당 회원은 존재하지 않습니다!"


    }

}

// #1 BDD
class MemberServiceUnitTestWithKoTest : BehaviorSpec({
    val repository = mockk<MemberRepository>()
    val service = MemberService(repository)

    // Given When Then

    Given("적절한 데이터가 주어지고") {
        val actualName = "member1"
        val actualEmail = "member1@email.com"
        val actualRole = Role.BRONZE

        val memberDesc = genMemberDesc(actualName, actualEmail, actualRole)
        val member = genMember(actualName, actualEmail, actualRole)

        beforeTest {
            every { repository.save(member) } returns member
        }

        When("memberService의 save를 호출하면") {

            val expected = service.save(memberDesc)

            Then("repository의 save가 한 번 호출될 것이다.") {
                verify(exactly =1) {
                    repository.save(member)
                }
            }

            Then("반환된 memberDescription의 값은 주어진 값과 동일할 것이다.") {

                expected.name shouldBe actualName
                expected.email shouldBe actualEmail
                expected.role shouldBe actualRole

            }
        }
    }
})

// #2 함수 기반 스타일
class MemberServiceUnitTestWithFunSpec : FunSpec({

    val repository = mockk<MemberRepository>()
    val service = MemberService(repository)

    test("적절한 데이터가 들어온다면 저장된 후 MemberDescription을 반환한다.") {

        // Given
        val actualName = "member1"
        val actualEmail = "member1@email.com"
        val actualRole = Role.BRONZE

        val memberDesc = genMemberDesc(actualName, actualEmail, actualRole)
        val member = genMember(actualName, actualEmail, actualRole)

        every { repository.save(member) } returns member

        // When
        val expectedMemberDesc = service.save(memberDesc)

        // Then
        expectedMemberDesc.name shouldBe actualName
        expectedMemberDesc.email shouldBe actualEmail
        expectedMemberDesc.role shouldBe actualRole

    }
})

// #3 StringSpec
class MemberServiceUnitTestWithStringSpec : StringSpec({

    val repository = mockk<MemberRepository>()
    val service = MemberService(repository)

    "적절한 데이터가 들어온다면 저장된 후 MemberDescription을 반환한다." {

        // Given
        val actualName = "member1"
        val actualEmail = "member1@email.com"
        val actualRole = Role.BRONZE

        val memberDesc = genMemberDesc(actualName, actualEmail, actualRole)
        val member = genMember(actualName, actualEmail, actualRole)

        every { repository.save(member) } returns member

        // When
        val expected = service.save(memberDesc)

        // then
        expected.name shouldBe actualName
        expected.email shouldBe actualEmail
        expected.role shouldBe actualRole

    }
})

// #4 describe-it
class MemberServiceUnitTestWithDescribeSpec : DescribeSpec({
    val repository = mockk<MemberRepository>()
    val service = MemberService(repository)

    describe("MemberService의 save 메서드는") {
        context("적절한 데이터가 주어진다면") {
            val actualName = "member1"
            val actualEmail = "member1@email.com"
            val actualRole = Role.BRONZE

            val memberDesc = genMemberDesc(actualName, actualEmail, actualRole)
            val member = genMember(actualName, actualEmail, actualRole)

            every { repository.save(member) } returns member

            it("데이터를 저장하고, 입력한 정보와 동일한 MemberDescription을 반환해야 한다.") {
                val expected = service.save(memberDesc)

                verify(exactly=1) { repository.save(member) }

                expected.name shouldBe actualName
                expected.email shouldBe actualEmail
                expected.role shouldBe actualRole
            }
        }
    }
})