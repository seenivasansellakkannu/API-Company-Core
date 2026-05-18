package com.company.core.process.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.core.process.service.HealthCheckApiService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Validated
@CrossOrigin
@RestController
@RequestMapping("/sr-construction")
public class HealthCheckApiController {
	
	@Autowired
	private HealthCheckApiService healthCheckApiService;

	@Operation(summary = "Check the health of the application", operationId = "health", description = "Check the health of the application", responses = {
			@ApiResponse(responseCode = "200", description = "Health status successfully retrieved.", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Health", })
	@GetMapping(value = "/health", produces = { "application/json" })
	public ResponseEntity<String> getHealth() throws Exception {
		return ResponseEntity.status(HttpStatus.OK).body(healthCheckApiService.getHealth());
	}

}