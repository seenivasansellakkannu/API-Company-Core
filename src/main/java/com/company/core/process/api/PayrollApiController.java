package com.company.core.process.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.core.process.dto.ProjectSearchResponse;
import com.company.core.process.dto.SalaryCalculationSuccessResponse;
import com.company.core.process.dto.SalaryCalculationUpdateRequestBody;
import com.company.core.process.multitenantmanager.MultiTenantManager;
import com.company.core.process.service.PayrollService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Validated
@CrossOrigin
@RestController
@RequestMapping("/sr-construction")
public class PayrollApiController {
	
	@Autowired
	private PayrollService payrollService;
	
	@Autowired
	MultiTenantManager multiTenantManager;
	
	@Value("${tenantId}")
	private String tenantId;
	

	@Operation(summary = "Calculate salary based on the conditions", operationId = "payrollCalculation", description = "Salary Calculation based on the conditions", responses = {
			@ApiResponse(responseCode = "200", description = "Salary calculated successfully.", content = @Content(schema = @Schema(implementation = ProjectSearchResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "payroll", })
	@PostMapping(value = "/payroll/calculate-salary",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SalaryCalculationSuccessResponse> payrollCalculation(
			@RequestBody SalaryCalculationUpdateRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(payrollService.payrollCalculation(requestBody));
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
