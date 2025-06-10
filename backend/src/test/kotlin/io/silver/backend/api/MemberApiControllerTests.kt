package io.silver.backend.api

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.silver.backend.app.MemberService
import io.silver.backend.dto.Role
import io.silver.backend.exceptions.MemberNotFoundException
import io.silver.backend.util.genMemberDesc
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(MemberApiController::class)
class MemberApiControllerSliceTests {

    @Autowired
    lateinit var mockMvc : MockMvc

    @MockitoBean
    lateinit var service: MemberService

    @Autowired
    lateinit var om: ObjectMapper

    @Test
    fun `회원 저장 요청을 보내면 성공적으로 저장하고 회원 정보가 포함되어있는 응답을 200 OK로 내린다`() {

        val expectedName = "성찬"
        val expectedEmail = "성찬@email.com"
        val expectedRole = Role.SILVER
        val expectedMsg = "회원이 성공적으로 등록되었습니다!"

        val expectedDesc = genMemberDesc(expectedName, expectedEmail, expectedRole)

        `when`(service.save(expectedDesc)).thenReturn(expectedDesc)
//        every { service.save(expectedDesc) } returns expectedDesc

        mockMvc.post("/api/members") {
            contentType = MediaType.APPLICATION_JSON
            content = om.writeValueAsString(expectedDesc)
        }.andExpect {
            status { isOk() }
            jsonPath("$.message") { value(expectedMsg) }
            jsonPath("$.data.name") { value(expectedName) }
            jsonPath("$.data.email") { value(expectedEmail) }
            jsonPath("$.data.role") { value(expectedRole.name) }
        }
    }

    @Test
    fun `존재하는 email로 회원을 조회하면 규격에 맞는 응답을 반환한다`() {

        val expectedName = "성찬"
        val expectedEmail = "성찬@email.com"
        val expectedRole = Role.SILVER
        val expectedMsg = "회원을 성공적으로 조회했습니다."

        val expectedDesc = genMemberDesc(expectedName, expectedEmail, expectedRole)

        `when`(service.getDescByEmail(expectedEmail)).thenReturn(expectedDesc)

        mockMvc.get("/api/members/${expectedEmail}")
            .andExpect {
            status { isOk() }
            jsonPath("$.message") { value(expectedMsg) }
            jsonPath("$.data.name") { value(expectedName) }
            jsonPath("$.data.email") { value(expectedEmail) }
            jsonPath("$.data.role") { value(expectedRole.name) }
        }

    }

    @Test
    fun `존재하지 않는 email로 회원을 조회하면 규격에 맞는 오류 응답을 반환한다`() {

        val targetEmail = "UNAVAILABLE_EMAIL"
        val expectedMsg = "조건에 맞는 회원을 찾을 수 없습니다."

        `when`(service.getDescByEmail(targetEmail)).thenThrow(MemberNotFoundException())

        mockMvc.get("/api/members/${targetEmail}")
            .andExpect {
                status { isNotFound() }
                jsonPath("$.message") { value(expectedMsg) }
            }
            .andDo{ print() }

    }
}