package com.microservice.gateway.jwt

import org.slf4j.Logger
import org.springframework.security.core.Authentication
import spock.lang.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import javax.crypto.SecretKey
import static org.mockito.Mockito.*

class JwtTokenProviderTest extends Specification {
    @Mock
    JwtProperties jwtProperties
    @Mock
    SecretKey secretKey
    @Mock
    Logger log
    @InjectMocks
    JwtTokenProvider jwtTokenProvider

    def setup() {
        MockitoAnnotations.openMocks(this)
    }

    def "test init"() {
        given:
        when(jwtProperties.getSecretKey()).thenReturn("getSecretKeyResponse")

        when:
        jwtTokenProvider.init()

        then:
        false//todo - validate something
    }

    def "test create Token"() {
        given:
        when(jwtProperties.getValidityInMs()).thenReturn(0l)

        when:
        String result = jwtTokenProvider.createToken(null)

        then:
        result == "replaceMeWithExpectedResult"
    }

    def "test get Authentication"() {
        when:
        Authentication result = jwtTokenProvider.getAuthentication("token")

        then:
        result == null
    }

    def "test validate Token"() {
        when:
        boolean result = jwtTokenProvider.validateToken("token")

        then:
        result == true
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme