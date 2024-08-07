package com.in28minutes.microservices.currency_conversion_service;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CurrencyConversionController {
 
	@Autowired
	private CurrencyExchangeProxy currencyExchangeProxy;
	
	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversion(@PathVariable String from,
			@PathVariable String to,
			@PathVariable BigDecimal quantity) {
		
		HashMap<String, String> uriVarriables = new HashMap<>();
		uriVarriables.put("from", from);
		uriVarriables.put("to", to);
		ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity(
				"http://localhost:8000/currency-exchange/from/{from}/to/{to}", 
				CurrencyConversion.class,
				uriVarriables);
		CurrencyConversion currencyConversion = responseEntity.getBody();
		
		return new CurrencyConversion(currencyConversion.getId(), 
				from, to, quantity,
				currencyConversion.getConversionMultiple(),
				quantity.multiply(currencyConversion.getConversionMultiple()), 
				currencyConversion.getEnvironment()+" "+" rest tmplate");
	}
	
	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversionFeign(@PathVariable String from,
			@PathVariable String to,
			@PathVariable BigDecimal quantity) {	
		
		CurrencyConversion currencyConversion = currencyExchangeProxy.retrieveExcahngeValue(from, to);
		
		return new CurrencyConversion(currencyConversion.getId(), 
				from, to, quantity,
				currencyConversion.getConversionMultiple(),
				quantity.multiply(currencyConversion.getConversionMultiple()), 
				currencyConversion.getEnvironment()+" "+" Feign");
	}
}
