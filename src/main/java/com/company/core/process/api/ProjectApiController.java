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

import com.company.core.process.dto.ProjectSearchRequestBody;
import com.company.core.process.dto.ProjectSearchResponse;
import com.company.core.process.dto.ProjectUpdateRequestBody;
import com.company.core.process.dto.SuccessResponse;
import com.company.core.process.multitenantmanager.MultiTenantManager;
import com.company.core.process.service.ProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Validated
@CrossOrigin
@RestController
@RequestMapping("/sr-construction")
public class ProjectApiController {
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	MultiTenantManager multiTenantManager;
	
	@Value("${tenantId}")
	private String tenantId;
	

	@Operation(summary = "Retrive the project details based on the search conditions", operationId = "projectSearch", description = "Retrive the project list", responses = {
			@ApiResponse(responseCode = "200", description = "project list successfully retrieved.", content = @Content(schema = @Schema(implementation = ProjectSearchResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "project", })
	@PostMapping(value = "/project/search",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<ProjectSearchResponse> searchProject(
			@RequestParam(defaultValue = "15") Integer limit, @RequestParam(defaultValue = "0") Integer offset,
			@RequestBody ProjectSearchRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(projectService.searchProject(requestBody, limit, offset));
	}
	
	@Operation(summary = "Add the project details", operationId = "addproject", description = "project details add", responses = {
			@ApiResponse(responseCode = "200", description = "project list added successfully.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "project", })
	@PostMapping(value = "/project",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SuccessResponse> addProject(@RequestBody ProjectUpdateRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(projectService.addProject(requestBody));
	}
	
	@Operation(summary = "Update the Project details", operationId = "updateProject", description = "Project details UPDATE", responses = {
			@ApiResponse(responseCode = "200", description = "Project Details updated successfully.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "project", })
	@PatchMapping(value = "/project",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SuccessResponse> updateProject(@RequestBody ProjectUpdateRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(projectService.updateProject(requestBody));
	}
	
	@Operation(summary = "Delete the project details", operationId = "deleteProject", description = "project details DELETE", responses = {
			@ApiResponse(responseCode = "200", description = "Project Details Deleted successfully.", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class))) }

			, tags = { "project", })
	@DeleteMapping(value = "/project",consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<SuccessResponse> deleteProject(@RequestBody ProjectUpdateRequestBody requestBody) throws Exception {
		setTenant();
		return ResponseEntity.status(HttpStatus.OK).body(projectService.deleteProject(requestBody));
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
