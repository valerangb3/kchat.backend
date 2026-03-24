package com.kchat

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.kchat.tokens.model.JwtConfig
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity(jwtConfig: JwtConfig) {
    authentication {
        jwt("auth-jwt") {
            realm = jwtConfig.jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtConfig.jwtSecret))
                    .withAudience(jwtConfig.jwtAudience)
                    .withIssuer(jwtConfig.jwtDomain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtConfig.jwtAudience))
                    JWTPrincipal(credential.payload)
                else null
            }
        }
    }
}
