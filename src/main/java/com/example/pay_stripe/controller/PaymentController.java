package com.example.pay_stripe.controller;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestParam int amount) {
        Stripe.apiKey = stripeApiKey;

        try {
            PaymentIntent intent = PaymentIntent.create(
                    new PaymentIntentCreateParams.Builder()
                            .setCurrency("usd")
                            .setAmount((long) amount * 100)  // Convert amount to cents
                            .build()
            );

            return ResponseEntity.ok("Client Secret: " + intent.getClientSecret());
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating PaymentIntent: " + e.getMessage());
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload) {
        // Handle Stripe webhook events here
        // Implement your logic for successful and failed payments
        // Return appropriate responses

        return ResponseEntity.ok("Webhook received");
    }
}
