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

import com.company.core.process.dto.ExpenseSearchRequestBody;
import com.company.core.process.dto.ExpenseSearchResponse;
import com.company.core.process.dto.ExpenseUpdateRequestBody;
import com.company.core.process.dto.SuccessResponse;
import com.company.core.process.multitenantmanager.MultiTenantManager;
import com.company.core.process.service.ExpenseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Validated
@CrossOrigin
@RestController
@RequestMapping("/sr-construction")
public class ExpenseApiController {
	
	@Autowired
	private ExpenseService expenseService;
	
	@Autowired
	MultiTenantManager multiTenantManager;
	
	@Value("${tenantId}")
	private String tenantId;
	

	@Operation(summary = "Retrive the Expense details based on the search conditions", operationId = "expenseSearch", description = "Retrive the Expense list", responses = {
			@ApiResponse(responseCode = "200", description = "Expense list successfully retrieved.", content = @Content(schema = @Schema(implementation = ExpenseSearchResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Expense", })
	@PostMapping(value = "/expense/search",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<ExpenseSearchResponse> searchExpense(
			@RequestParam(defaultValue = "15") Integer limit, @RequestParam(defaultValue = "0") Integer offset,
			@RequestBody ExpenseSearchRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(expenseService.searchExpense(requestBody, limit, offset));
	}
	
	@Operation(summary = "Add the Expense details", operationId = "addExpense", description = "Expense details add", responses = {
			@ApiResponse(responseCode = "200", description = "Expense list added successfully.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Expense", })
	@PostMapping(value = "/expense",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SuccessResponse> addExpense(@RequestBody ExpenseUpdateRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(expenseService.addExpense(requestBody));
	}
	
	@Operation(summary = "Update the Expense details", operationId = "updateExpense", description = "Expense details UPDATE", responses = {
			@ApiResponse(responseCode = "200", description = "Expense Details updated successfully.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Expense", })
	@PatchMapping(value = "/expense",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SuccessResponse> updateExpense(@RequestBody ExpenseUpdateRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(expenseService.updateExpense(requestBody));
	}
	
	@Operation(summary = "Delete the Expense details", operationId = "deleteExpense", description = "Expense details DELETE", responses = {
			@ApiResponse(responseCode = "200", description = "Expense Details Deleted successfully.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Expense", })
	@DeleteMapping(value = "/expense",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SuccessResponse> deleteExpense(@RequestBody ExpenseUpdateRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(expenseService.deleteExpense(requestBody));
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
