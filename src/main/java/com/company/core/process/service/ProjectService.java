package com.company.core.process.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.company.core.process.dto.ProjectSearchRequestBody;
import com.company.core.process.dto.ProjectSearchResponse;
import com.company.core.process.dto.ProjectSearchResult;
import com.company.core.process.dto.ProjectUpdateRequestBody;
import com.company.core.process.dto.SuccessResponse;
import com.company.core.process.repo.ProjectRepo;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepo projectRepo;
	
	/**
	 * searchProject is used to search the Project details
	 * 
	 * @param ProjectSearchRequestBody, limit, offset
	 * @return ProjectSearchResponse
	 */
	public ProjectSearchResponse searchProject(ProjectSearchRequestBody requestBody, Integer limit, Integer offset) {

		ProjectSearchResponse response = new ProjectSearchResponse();
		int count = 0;
		List<ProjectSearchResult> projectsList = projectRepo.searchProject(requestBody, limit, offset);
		
		if(!projectsList.isEmpty()) {
			count = projectsList.stream().findFirst().get().getTotalRecords();
		}
		response.setProjects(projectsList);
		response.setTotalRecords(count);
		return response;
	}
	/**
	 * addProject is used to add the Project and address table
	 * 
	 * @param ProjectUpdateRequestBody
	 * @return SuccessResponse
	 */
	public SuccessResponse addProject(ProjectUpdateRequestBody requestBody) {
		SuccessResponse successResponse = new SuccessResponse();
		
		projectRepo.addProject(requestBody);
		
		successResponse.setStatus(HttpStatus.CREATED.value());
		successResponse.setMessage(HttpStatus.CREATED.getReasonPhrase());
		successResponse.setTimestamp(LocalDateTime.now());
		return successResponse;
	}
	/**
	 * updateProject is used to update the Project and address table
	 * 
	 * @param ProjectUpdateRequestBody
	 * @return SuccessResponse
	 */
	public SuccessResponse updateProject(ProjectUpdateRequestBody requestBody) {
		SuccessResponse successResponse = new SuccessResponse();
		
		projectRepo.updateProject(requestBody);
		
		successResponse.setStatus(HttpStatus.OK.value());
		successResponse.setMessage(HttpStatus.OK.getReasonPhrase());
		successResponse.setTimestamp(LocalDateTime.now());
		return successResponse;
	}
	/**
	 * deleteProject is used to delete the Project and address table
	 * 
	 * @param ProjectUpdateRequestBody
	 * @return SuccessResponse
	 */
	public SuccessResponse deleteProject(ProjectUpdateRequestBody requestBody) {
		SuccessResponse successResponse = new SuccessResponse();
		
		projectRepo.deleteProject(requestBody);
		
		successResponse.setStatus(HttpStatus.OK.value());
		successResponse.setMessage(HttpStatus.OK.getReasonPhrase());
		successResponse.setTimestamp(LocalDateTime.now());
		return successResponse;
	}
	

}