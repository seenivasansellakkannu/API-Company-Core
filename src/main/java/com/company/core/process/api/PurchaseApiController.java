package com.company.core.process.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.core.process.dto.PurchaseSearchRequestBody;
import com.company.core.process.dto.PurchaseSearchResponse;
import com.company.core.process.dto.PurchaseUpdateRequestBody;
import com.company.core.process.dto.SuccessResponse;
import com.company.core.process.multitenantmanager.MultiTenantManager;
import com.company.core.process.service.PurchaseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Validated
@CrossOrigin
@RestController
@RequestMapping("/sr-construction")
public class PurchaseApiController {
	
	@Autowired
	private PurchaseService purchaseService;
	
	@Autowired
	MultiTenantManager multiTenantManager;
	
	@Value("${tenantId}")
	private String tenantId;
	

	@Operation(summary = "Retrive the Purchase details based on the search conditions", operationId = "purchaseSearch", description = "Retrive the Purchase list", responses = {
			@ApiResponse(responseCode = "200", description = "Purchase list successfully retrieved.", content = @Content(schema = @Schema(implementation = PurchaseSearchResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Purchase", })
	@PostMapping(value = "/purchase/search",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<PurchaseSearchResponse> searchPurchase(
			@RequestParam(defaultValue = "15") Integer limit, @RequestParam(defaultValue = "0") Integer offset,
			@RequestBody PurchaseSearchRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(purchaseService.searchPurchase(requestBody, limit, offset));
	}
	
	@Operation(summary = "Add the Purchase details", operationId = "addPurchase", description = "Purchase details add", responses = {
			@ApiResponse(responseCode = "200", description = "Purchase list added successfully.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Purchase", })
	@PostMapping(value = "/purchase",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SuccessResponse> addPurchase(@RequestBody PurchaseUpdateRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(purchaseService.addPurchase(requestBody));
	}
	
	@Operation(summary = "Update the Purchase details", operationId = "updatePurchase", description = "Purchase details UPDATE", responses = {
			@ApiResponse(responseCode = "200", description = "Purchase Details updated successfully.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Purchase", })
	@PatchMapping(value = "/purchase",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SuccessResponse> updatePurchase(@RequestBody PurchaseUpdateRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(purchaseService.updatePurchase(requestBody));
	}
	
	@Operation(summary = "Delete the Purchase details", operationId = "deletePurchase", description = "Purchase details DELETE", responses = {
			@ApiResponse(responseCode = "200", description = "Purchase Details Deleted successfully.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Purchase", })
	@DeleteMapping(value = "/purchase",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SuccessResponse> deletePurchase(@RequestBody PurchaseUpdateRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(purchaseService.deletePurchase(requestBody));
	}
	
	
	
	public void setTenant() {
		try {
			multiTenantManager.setCurrentTenant(tenantId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
