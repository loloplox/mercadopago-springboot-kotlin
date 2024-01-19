package com.rodrigomv.certificacionmercadopagopreference.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "mercadopago")
data class MercadoPagoProperties(
    var accessToken: String = ""
)

