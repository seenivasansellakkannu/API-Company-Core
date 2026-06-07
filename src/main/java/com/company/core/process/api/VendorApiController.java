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

import com.company.core.process.dto.VendorSearchRequestBody;
import com.company.core.process.dto.VendorSearchResponse;
import com.company.core.process.dto.VendorUpdateRequestBody;
import com.company.core.process.dto.SuccessResponse;
import com.company.core.process.multitenantmanager.MultiTenantManager;
import com.company.core.process.service.VendorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Validated
@CrossOrigin
@RestController
@RequestMapping("/sr-construction")
public class VendorApiController {
	
	@Autowired
	private VendorService vendorService;
	
	@Autowired
	MultiTenantManager multiTenantManager;
	
	@Value("${tenantId}")
	private String tenantId;
	

	@Operation(summary = "Retrive the Vendor details based on the search conditions", operationId = "vendorSearch", description = "Retrive the Vendor list", responses = {
			@ApiResponse(responseCode = "200", description = "Vendor list successfully retrieved.", content = @Content(schema = @Schema(implementation = VendorSearchResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Vendor", })
	@PostMapping(value = "/vendor/search",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<VendorSearchResponse> searchVendor(
			@RequestParam(defaultValue = "15") Integer limit, @RequestParam(defaultValue = "0") Integer offset,
			@RequestBody VendorSearchRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(vendorService.searchVendor(requestBody, limit, offset));
	}
	
	@Operation(summary = "Add the Vendor details", operationId = "addVendor", description = "Vendor details add", responses = {
			@ApiResponse(responseCode = "200", description = "Vendor list added successfully.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Vendor", })
	@PostMapping(value = "/vendor",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SuccessResponse> addVendor(@RequestBody VendorUpdateRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(vendorService.addVendor(requestBody));
	}
	
	@Operation(summary = "Update the Vendor details", operationId = "updateVendor", description = "Vendor details UPDATE", responses = {
			@ApiResponse(responseCode = "200", description = "Vendor Details updated successfully.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Vendor", })
	@PatchMapping(value = "/vendor",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SuccessResponse> updateVendor(@RequestBody VendorUpdateRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(vendorService.updateVendor(requestBody));
	}
	
	@Operation(summary = "Delete the Vendor details", operationId = "deleteVendor", description = "Vendor details DELETE", responses = {
			@ApiResponse(responseCode = "200", description = "Vendor Details Deleted successfully.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Vendor", })
	@DeleteMapping(value = "/vendor",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SuccessResponse> deleteVendor(@RequestBody VendorUpdateRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(vendorService.deleteVendor(requestBody));
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
