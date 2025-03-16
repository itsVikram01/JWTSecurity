package com.apigateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallBackController {

	@RequestMapping("/orderFallBack")
	public Mono<String> orderServiceFallBack() {
		return Mono.just("Order-Service is talking too much time or down!!");		
	}

	@RequestMapping("/paymentFallBack")
	public Mono<String> paymentServiceFallBack() {
		return Mono.just("Payment-Service is talking too much time or down!!");		
	}
	
}
