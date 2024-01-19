package com.rodrigomv.certificacionmercadopagopreference.controller

import com.mercadopago.MercadoPagoConfig
import com.mercadopago.client.preference.PreferenceBackUrlsRequest
import com.mercadopago.client.preference.PreferenceClient
import com.mercadopago.client.preference.PreferenceItemRequest
import com.mercadopago.client.preference.PreferenceRequest
import com.rodrigomv.certificacionmercadopagopreference.config.MercadoPagoProperties
import com.rodrigomv.certificacionmercadopagopreference.model.Product
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/mercadopago")
@CrossOrigin(
    origins = ["*"],
    methods = [RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE]
)
class MercadoPagoController(
    private val mercadoPagoProperties: MercadoPagoProperties
) {

    @GetMapping
    fun index() = "Hello World!"

    @PostMapping("/createPreference")
    fun createAndRedirect(@RequestBody product: Product): ResponseEntity<String> {
        try {
            MercadoPagoConfig.setAccessToken(mercadoPagoProperties.accessToken)

            val itemRequest = PreferenceItemRequest.builder()
                .id("1234")
                .title(product.title)
                .pictureUrl("https://www.lacuracao.pe/media/catalog/product/c/f/cfi-1215a_1.jpg?quality=80&bg-color=255,255,255&fit=bounds&height=700&width=700&canvas=700:700")
                .quantity(product.quantity)
                .currencyId("PEN")
                .unitPrice(BigDecimal(product.price))
                .build()

            val items = ArrayList<PreferenceItemRequest>()

            items.add(itemRequest)

            val preferenceBackUrlsRequest = PreferenceBackUrlsRequest.builder()
                .success("http://localhost:5173")
                .failure("https://www.youtube.com/")
                .pending("https://www.youtube.com/")
                .build()
            val preferenceRequest = PreferenceRequest.builder()
                .backUrls(preferenceBackUrlsRequest)
                .items(items)
                .build()
            val client = PreferenceClient()
            val preference = client.create(preferenceRequest)

            println(preference.id)

            return ResponseEntity(preference.id, HttpStatus.OK)
        } catch (e: Exception) {
            return ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

}