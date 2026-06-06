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

import com.company.core.process.dto.MaterialSearchRequestBody;
import com.company.core.process.dto.MaterialSearchResponse;
import com.company.core.process.dto.MaterialUpdateRequestBody;
import com.company.core.process.dto.SuccessResponse;
import com.company.core.process.multitenantmanager.MultiTenantManager;
import com.company.core.process.service.MaterialService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Validated
@CrossOrigin
@RestController
@RequestMapping("/sr-construction")
public class MaterialApiController {
	
	@Autowired
	private MaterialService materialService;
	
	@Autowired
	MultiTenantManager multiTenantManager;
	
	@Value("${tenantId}")
	private String tenantId;
	

	@Operation(summary = "Retrive the Material details based on the search conditions", operationId = "materialSearch", description = "Retrive the Material list", responses = {
			@ApiResponse(responseCode = "200", description = "Material list successfully retrieved.", content = @Content(schema = @Schema(implementation = MaterialSearchResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Material", })
	@PostMapping(value = "/material/search",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<MaterialSearchResponse> searchMaterial(
			@RequestParam(defaultValue = "15") Integer limit, @RequestParam(defaultValue = "0") Integer offset,
			@RequestBody MaterialSearchRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(materialService.searchMaterial(requestBody, limit, offset));
	}
	
	@Operation(summary = "Add the Material details", operationId = "addMaterial", description = "Material details add", responses = {
			@ApiResponse(responseCode = "200", description = "Material list added successfully.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Material", })
	@PostMapping(value = "/material",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SuccessResponse> addMaterial(@RequestBody MaterialUpdateRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(materialService.addMaterial(requestBody));
	}
	
	@Operation(summary = "Update the Material details", operationId = "updateMaterial", description = "Material details UPDATE", responses = {
			@ApiResponse(responseCode = "200", description = "Material Details updated successfully.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Material", })
	@PatchMapping(value = "/material",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SuccessResponse> updateMaterial(@RequestBody MaterialUpdateRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(materialService.updateMaterial(requestBody));
	}
	
	@Operation(summary = "Delete the Material details", operationId = "deleteMaterial", description = "Material details DELETE", responses = {
			@ApiResponse(responseCode = "200", description = "Material Details Deleted successfully.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Material", })
	@DeleteMapping(value = "/material",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SuccessResponse> deleteMaterial(@RequestBody MaterialUpdateRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(materialService.deleteMaterial(requestBody));
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
