package com.company.core.process.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.company.core.process.dto.EmployeeSearchRequestBody;
import com.company.core.process.dto.EmployeeSearchResponse;
import com.company.core.process.dto.EmployeeSearchResult;
import com.company.core.process.dto.EmployeeUpdateRequestBody;
import com.company.core.process.dto.SuccessResponse;
import com.company.core.process.repo.EmployeeRepo;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepo employeeRepo;
	
	/**
	 * searchEmployee is used to search the Employee and address details
	 * 
	 * @param EmployeeSearchRequestBody, limit, offset
	 * @return EmployeeSearchResponse
	 */
	public EmployeeSearchResponse searchEmployee(EmployeeSearchRequestBody requestBody, Integer limit, Integer offset) {

		EmployeeSearchResponse response = new EmployeeSearchResponse();
		int count = 0;
		List<EmployeeSearchResult> employeesList = employeeRepo.searchEmployee(requestBody, limit, offset);
		
		if(!employeesList.isEmpty()) {
			count = employeesList.stream().findFirst().get().getTotalRecords();
		}
		response.setEmployees(employeesList);
		response.setTotalRecords(count);
		return response;
	}
	/**
	 * addEmployee is used to add the Employee and address table
	 * 
	 * @param EmployeeUpdateRequestBody
	 * @return SuccessResponse
	 */
	public SuccessResponse addEmployee(EmployeeUpdateRequestBody requestBody) {
		SuccessResponse successResponse = new SuccessResponse();
		
		employeeRepo.addEmployee(requestBody);
		
		successResponse.setStatus(HttpStatus.CREATED.value());
		successResponse.setMessage(HttpStatus.CREATED.getReasonPhrase());
		successResponse.setTimestamp(LocalDateTime.now());
		return successResponse;
	}
	/**
	 * updateEmployee is used to update the Employee and address table
	 * 
	 * @param EmployeeUpdateRequestBody
	 * @return SuccessResponse
	 */
	public SuccessResponse updateEmployee(EmployeeUpdateRequestBody requestBody) {
		SuccessResponse successResponse = new SuccessResponse();
		
		employeeRepo.updateEmployee(requestBody);
		
		successResponse.setStatus(HttpStatus.OK.value());
		successResponse.setMessage(HttpStatus.OK.getReasonPhrase());
		successResponse.setTimestamp(LocalDateTime.now());
		return successResponse;
	}
	/**
	 * deleteEmployee is used to delete the Employee and address table
	 * 
	 * @param EmployeeUpdateRequestBody
	 * @return SuccessResponse
	 */
	public SuccessResponse deleteEmployee(EmployeeUpdateRequestBody requestBody) {
		SuccessResponse successResponse = new SuccessResponse();
		
		employeeRepo.deleteEmployee(requestBody);
		
		successResponse.setStatus(HttpStatus.OK.value());
		successResponse.setMessage(HttpStatus.OK.getReasonPhrase());
		successResponse.setTimestamp(LocalDateTime.now());
		return successResponse;
	}
	

}