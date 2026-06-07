package com.company.core.process.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.company.core.process.dto.SupplierSearchRequestBody;
import com.company.core.process.dto.SupplierSearchResponse;
import com.company.core.process.dto.SupplierSearchResult;
import com.company.core.process.dto.SupplierUpdateRequestBody;
import com.company.core.process.dto.SuccessResponse;
import com.company.core.process.repo.SupplierRepo;

@Service
public class SupplierService {

	@Autowired
	private SupplierRepo supplierRepo;
	
	/**
	 * searchSupplier is used to search the Supplier details
	 * 
	 * @param SupplierSearchRequestBody, limit, offset
	 * @return SupplierSearchResponse
	 */
	public SupplierSearchResponse searchSupplier(SupplierSearchRequestBody requestBody, Integer limit, Integer offset) {

		SupplierSearchResponse response = new SupplierSearchResponse();
		int count = 0;
		List<SupplierSearchResult> suppliersList = supplierRepo.searchSupplier(requestBody, limit, offset);
		
		if(!suppliersList.isEmpty()) {
			count = suppliersList.stream().findFirst().get().getTotalRecords();
		}
		response.setSuppliers(suppliersList);
		response.setTotalRecords(count);
		return response;
	}
	/**
	 * addSupplier is used to add the Supplier and address table
	 * 
	 * @param SupplierUpdateRequestBody
	 * @return SuccessResponse
	 */
	public SuccessResponse addSupplier(SupplierUpdateRequestBody requestBody) {
		SuccessResponse successResponse = new SuccessResponse();
		
		supplierRepo.addSupplier(requestBody);
		
		successResponse.setStatus(HttpStatus.CREATED.value());
		successResponse.setMessage(HttpStatus.CREATED.getReasonPhrase());
		successResponse.setTimestamp(LocalDateTime.now());
		return successResponse;
	}
	/**
	 * updateSupplier is used to update the Supplier and address table
	 * 
	 * @param SupplierUpdateRequestBody
	 * @return SuccessResponse
	 */
	public SuccessResponse updateSupplier(SupplierUpdateRequestBody requestBody) {
		SuccessResponse successResponse = new SuccessResponse();
		
		supplierRepo.updateSupplier(requestBody);
		
		successResponse.setStatus(HttpStatus.OK.value());
		successResponse.setMessage(HttpStatus.OK.getReasonPhrase());
		successResponse.setTimestamp(LocalDateTime.now());
		return successResponse;
	}
	/**
	 * deleteSupplier is used to delete the Supplier and address table
	 * 
	 * @param SupplierUpdateRequestBody
	 * @return SuccessResponse
	 */
	public SuccessResponse deleteSupplier(SupplierUpdateRequestBody requestBody) {
		SuccessResponse successResponse = new SuccessResponse();
		
		supplierRepo.deleteSupplier(requestBody);
		
		successResponse.setStatus(HttpStatus.OK.value());
		successResponse.setMessage(HttpStatus.OK.getReasonPhrase());
		successResponse.setTimestamp(LocalDateTime.now());
		return successResponse;
	}
	

}