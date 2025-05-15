package org.example.expert.config

import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.example.expert.domain.common.dto.AuthUser
import org.example.expert.domain.user.enums.UserRole
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

class JwtFilter (
    private val jwtUtil: JwtUtil
): OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val bearerJwt = request.getHeader("Authorization")

        if (bearerJwt == null || !bearerJwt.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val jwt = jwtUtil.substringToken(bearerJwt)

        try {
            // JWT 유효성 검사와 claims 추출
            val claims = jwtUtil.extractClaims(jwt)
            if (claims == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 JWT 토큰입니다.")
                return
            }

            val userId = claims.subject.toLong()
            val email = claims.get("email", String::class.java)
            val nickname = claims.get("nickname", String::class.java)
            val userRole = UserRole.valueOf(claims.get("userRole", String::class.java))

            val authUser = AuthUser(userId, email, nickname, userRole)
            val authenticationToken = UsernamePasswordAuthenticationToken(
                authUser,
                null, authUser.authorities
            )

            SecurityContextHolder.getContext().authentication = authenticationToken
        } catch (e: ExpiredJwtException) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.", e)
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다.")
        } catch (e: Exception) {
            log.error("Internal server error", e)
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
        }

        filterChain.doFilter(request, response)
    }
}
