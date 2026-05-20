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

import com.company.core.process.dto.EmployeeSearchRequestBody;
import com.company.core.process.dto.EmployeeSearchResponse;
import com.company.core.process.dto.EmployeeUpdateRequestBody;
import com.company.core.process.dto.SuccessResponse;
import com.company.core.process.service.EmployeeService;
import com.company.core.process.multitenantmanager.MultiTenantManager;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Validated
@CrossOrigin
@RestController
@RequestMapping("/sr-construction")
public class CompanyApiController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	MultiTenantManager multiTenantManager;
	
	@Value("${tenantId}")
	private String tenantId;
	

	@Operation(summary = "Retrive the employee details based on the search conditions", operationId = "employeeSearch", description = "Retrive the employee list", responses = {
			@ApiResponse(responseCode = "200", description = "Employee list successfully retrieved.", content = @Content(schema = @Schema(implementation = EmployeeSearchResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Employee", })
	@PostMapping(value = "/employee/search",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<EmployeeSearchResponse> searchEmployee(
			@RequestParam(defaultValue = "15") Integer limit, @RequestParam(defaultValue = "0") Integer offset,
			@RequestBody EmployeeSearchRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(employeeService.searchEmployee(requestBody, limit, offset));
	}
	
	@Operation(summary = "Add the employee details", operationId = "employeeSearch", description = "Employee details add", responses = {
			@ApiResponse(responseCode = "200", description = "Employee list added successfully.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Employee", })
	@PostMapping(value = "/employee",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SuccessResponse> addEmployee(@RequestBody EmployeeUpdateRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(employeeService.addEmployee(requestBody));
	}
	
	@Operation(summary = "Update the employee details", operationId = "updateEmployee", description = "Employee details UPDATE", responses = {
			@ApiResponse(responseCode = "200", description = "Employee Details updated successfully.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Employee", })
	@PatchMapping(value = "/employee",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SuccessResponse> updateEmployee(@RequestBody EmployeeUpdateRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(employeeService.updateEmployee(requestBody));
	}
	
	@Operation(summary = "Delete the employee details", operationId = "deleteEmployee", description = "Employee details DELETE", responses = {
			@ApiResponse(responseCode = "200", description = "Employee Details Deleted successfully.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Employee", })
	@DeleteMapping(value = "/employee",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SuccessResponse> deleteEmployee(@RequestBody EmployeeUpdateRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(employeeService.deleteEmployee(requestBody));
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
