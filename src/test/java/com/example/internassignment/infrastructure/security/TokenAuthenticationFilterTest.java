package com.example.internassignment.infrastructure.security;

import com.example.internassignment.infrastructure.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationFilterTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private TokenAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new TokenAuthenticationFilter(jwtTokenProvider, userDetailsService);
    }

    @AfterEach
    void clear(){
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("AuthenticationFilter: 유효한 토큰을 받으면 인증에 성공한다.")
    void test1 () throws Exception{
        // given
        String token = "valid.jwt.token";
        String username = "testUser";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        given(jwtTokenProvider.validToken(token)).willReturn(true);
        Claims claims = Jwts.claims().setSubject(username);
        given(jwtTokenProvider.extractClaims(token)).willReturn(claims);
        given(userDetails.getUsername()).willReturn(username);
        given(userDetailsService.loadUserByUsername(username)).willReturn(userDetails);
        given(userDetails.getAuthorities()).willReturn(List.of());

        // when
        filter.doFilterInternal(request, response, filterChain);

        // then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo(username);
    }

    @Test
    @DisplayName("AuthenticationFilter: 유효하지 않은 토큰을 받으면 인증에 실패한다.")
    void test2 () throws Exception{
        // given
        String token = "invalid.token";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        given(jwtTokenProvider.validToken(token)).willReturn(false);

        // when
        filter.doFilterInternal(request, response, filterChain);

        // then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNull();
    }
}