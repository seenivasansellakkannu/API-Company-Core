package com.company.core.process.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.company.core.process.dto.MaterialSearchRequestBody;
import com.company.core.process.dto.MaterialSearchResponse;
import com.company.core.process.dto.MaterialSearchResult;
import com.company.core.process.dto.MaterialUpdateRequestBody;
import com.company.core.process.dto.SuccessResponse;
import com.company.core.process.repo.MaterialRepo;

@Service
public class MaterialService {

	@Autowired
	private MaterialRepo materialRepo;
	
	/**
	 * searchMaterial is used to search the Material details
	 * 
	 * @param MaterialSearchRequestBody, limit, offset
	 * @return MaterialSearchResponse
	 */
	public MaterialSearchResponse searchMaterial(MaterialSearchRequestBody requestBody, Integer limit, Integer offset) {

		MaterialSearchResponse response = new MaterialSearchResponse();
		int count = 0;
		List<MaterialSearchResult> MaterialsList = materialRepo.searchMaterial(requestBody, limit, offset);
		
		if(!MaterialsList.isEmpty()) {
			count = MaterialsList.stream().findFirst().get().getTotalRecords();
		}
		response.setMaterials(MaterialsList);
		response.setTotalRecords(count);
		return response;
	}
	/**
	 * addMaterial is used to add the Material and address table
	 * 
	 * @param MaterialUpdateRequestBody
	 * @return SuccessResponse
	 */
	public SuccessResponse addMaterial(MaterialUpdateRequestBody requestBody) {
		SuccessResponse successResponse = new SuccessResponse();
		
		materialRepo.addMaterial(requestBody);
		
		successResponse.setStatus(HttpStatus.CREATED.value());
		successResponse.setMessage(HttpStatus.CREATED.getReasonPhrase());
		successResponse.setTimestamp(LocalDateTime.now());
		return successResponse;
	}
	/**
	 * updateMaterial is used to update the Material and address table
	 * 
	 * @param MaterialUpdateRequestBody
	 * @return SuccessResponse
	 */
	public SuccessResponse updateMaterial(MaterialUpdateRequestBody requestBody) {
		SuccessResponse successResponse = new SuccessResponse();
		
		materialRepo.updateMaterial(requestBody);
		
		successResponse.setStatus(HttpStatus.OK.value());
		successResponse.setMessage(HttpStatus.OK.getReasonPhrase());
		successResponse.setTimestamp(LocalDateTime.now());
		return successResponse;
	}
	/**
	 * deleteMaterial is used to delete the Material and address table
	 * 
	 * @param MaterialUpdateRequestBody
	 * @return SuccessResponse
	 */
	public SuccessResponse deleteMaterial(MaterialUpdateRequestBody requestBody) {
		SuccessResponse successResponse = new SuccessResponse();
		
		materialRepo.deleteMaterial(requestBody);
		
		successResponse.setStatus(HttpStatus.OK.value());
		successResponse.setMessage(HttpStatus.OK.getReasonPhrase());
		successResponse.setTimestamp(LocalDateTime.now());
		return successResponse;
	}
	

}