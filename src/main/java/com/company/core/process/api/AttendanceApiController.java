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

import com.company.core.process.dto.AttendanceSearchRequestBody;
import com.company.core.process.dto.AttendanceSearchResponse;
import com.company.core.process.dto.AttendanceUpdateRequestBody;
import com.company.core.process.dto.EmployeeUpdateRequestBody;
import com.company.core.process.dto.SuccessResponse;
import com.company.core.process.multitenantmanager.MultiTenantManager;
import com.company.core.process.service.AttendanceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Validated
@CrossOrigin
@RestController
@RequestMapping("/sr-construction")
public class AttendanceApiController {
	
	@Autowired
	private AttendanceService attendanceService;
	
	@Autowired
	MultiTenantManager multiTenantManager;
	
	@Value("${tenantId}")
	private String tenantId;
	

	@Operation(summary = "Retrive the attendance details based on the search conditions", operationId = "attendanceSearch", description = "Retrive the attendance list", responses = {
			@ApiResponse(responseCode = "200", description = "Attendance list successfully retrieved.", content = @Content(schema = @Schema(implementation = AttendanceSearchResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Attendance", })
	@PostMapping(value = "/attendance/search",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<AttendanceSearchResponse> searchAttendance(
			@RequestParam(defaultValue = "15") Integer limit, @RequestParam(defaultValue = "0") Integer offset,
			@RequestBody AttendanceSearchRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(attendanceService.searchAttendance(requestBody, limit, offset));
	}
	
	@Operation(summary = "Add the attendance details", operationId = "addAttendance", description = "Attendance details add", responses = {
			@ApiResponse(responseCode = "200", description = "Attendance list added successfully.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Attendance", })
	@PostMapping(value = "/attendance",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SuccessResponse> addAttendance(@RequestBody AttendanceUpdateRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(attendanceService.addAttendance(requestBody));
	}
	
	@Operation(summary = "Update the attendance details", operationId = "updateAttendance", description = "Attendance details UPDATE", responses = {
			@ApiResponse(responseCode = "200", description = "Attendance Details updated successfully.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Attendance", })
	@PatchMapping(value = "/attendance",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SuccessResponse> updateAttendance(@RequestBody AttendanceUpdateRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(attendanceService.updateAttendance(requestBody));
	}
	
	@Operation(summary = "Delete the attendance details", operationId = "deleteAttendance", description = "Attendance details DELETE", responses = {
			@ApiResponse(responseCode = "200", description = "Attendance Details Deleted successfully.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "Attendance", })
	@DeleteMapping(value = "/attendance",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SuccessResponse> deleteAttendance(@RequestBody AttendanceUpdateRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(attendanceService.deleteAttendance(requestBody));
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
