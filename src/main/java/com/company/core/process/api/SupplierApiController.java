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

import com.company.core.process.dto.SupplierSearchRequestBody;
import com.company.core.process.dto.SupplierSearchResponse;
import com.company.core.process.dto.SupplierUpdateRequestBody;
import com.company.core.process.dto.SuccessResponse;
import com.company.core.process.multitenantmanager.MultiTenantManager;
import com.company.core.process.service.SupplierService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Validated
@CrossOrigin
@RestController
@RequestMapping("/sr-construction")
public class SupplierApiController {
	
	@Autowired
	private SupplierService supplierService;
	
	@Autowired
	MultiTenantManager multiTenantManager;
	
	@Value("${tenantId}")
	private String tenantId;
	

	@Operation(summary = "Retrive the Supplier details based on the search conditions", operationId = "supplierSearch", description = "Retrive the Supplier list", responses = {
			@ApiResponse(responseCode = "200", description = "Supplier list successfully retrieved.", content = @Content(schema = @Schema(implementation = SupplierSearchResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Supplier", })
	@PostMapping(value = "/supplier/search",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SupplierSearchResponse> searchSupplier(
			@RequestParam(defaultValue = "15") Integer limit, @RequestParam(defaultValue = "0") Integer offset,
			@RequestBody SupplierSearchRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(supplierService.searchSupplier(requestBody, limit, offset));
	}
	
	@Operation(summary = "Add the Supplier details", operationId = "addSupplier", description = "Supplier details add", responses = {
			@ApiResponse(responseCode = "200", description = "Supplier list added successfully.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Supplier", })
	@PostMapping(value = "/supplier",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SuccessResponse> addSupplier(@RequestBody SupplierUpdateRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(supplierService.addSupplier(requestBody));
	}
	
	@Operation(summary = "Update the Supplier details", operationId = "updateSupplier", description = "Supplier details UPDATE", responses = {
			@ApiResponse(responseCode = "200", description = "Supplier Details updated successfully.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Supplier", })
	@PatchMapping(value = "/supplier",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SuccessResponse> updateSupplier(@RequestBody SupplierUpdateRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(supplierService.updateSupplier(requestBody));
	}
	
	@Operation(summary = "Delete the Supplier details", operationId = "deleteSupplier", description = "Supplier details DELETE", responses = {
			@ApiResponse(responseCode = "200", description = "Supplier Details Deleted successfully.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Supplier", })
	@DeleteMapping(value = "/supplier",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SuccessResponse> deleteSupplier(@RequestBody SupplierUpdateRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(supplierService.deleteSupplier(requestBody));
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
