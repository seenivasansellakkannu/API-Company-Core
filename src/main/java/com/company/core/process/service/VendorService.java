package com.company.core.process.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.company.core.process.dto.VendorSearchRequestBody;
import com.company.core.process.dto.VendorSearchResponse;
import com.company.core.process.dto.VendorSearchResult;
import com.company.core.process.dto.VendorUpdateRequestBody;
import com.company.core.process.dto.SuccessResponse;
import com.company.core.process.repo.VendorRepo;

@Service
public class VendorService {

	@Autowired
	private VendorRepo vendorRepo;
	
	/**
	 * searchVendor is used to search the Vendor details
	 * 
	 * @param VendorSearchRequestBody, limit, offset
	 * @return VendorSearchResponse
	 */
	public VendorSearchResponse searchVendor(VendorSearchRequestBody requestBody, Integer limit, Integer offset) {

		VendorSearchResponse response = new VendorSearchResponse();
		int count = 0;
		List<VendorSearchResult> vendorsList = vendorRepo.searchVendor(requestBody, limit, offset);
		
		if(!vendorsList.isEmpty()) {
			count = vendorsList.stream().findFirst().get().getTotalRecords();
		}
		response.setVendors(vendorsList);
		response.setTotalRecords(count);
		return response;
	}
	/**
	 * addVendor is used to add the Vendor and address table
	 * 
	 * @param VendorUpdateRequestBody
	 * @return SuccessResponse
	 */
	public SuccessResponse addVendor(VendorUpdateRequestBody requestBody) {
		SuccessResponse successResponse = new SuccessResponse();
		
		vendorRepo.addVendor(requestBody);
		
		successResponse.setStatus(HttpStatus.CREATED.value());
		successResponse.setMessage(HttpStatus.CREATED.getReasonPhrase());
		successResponse.setTimestamp(LocalDateTime.now());
		return successResponse;
	}
	/**
	 * updateVendor is used to update the Vendor and address table
	 * 
	 * @param VendorUpdateRequestBody
	 * @return SuccessResponse
	 */
	public SuccessResponse updateVendor(VendorUpdateRequestBody requestBody) {
		SuccessResponse successResponse = new SuccessResponse();
		
		vendorRepo.updateVendor(requestBody);
		
		successResponse.setStatus(HttpStatus.OK.value());
		successResponse.setMessage(HttpStatus.OK.getReasonPhrase());
		successResponse.setTimestamp(LocalDateTime.now());
		return successResponse;
	}
	/**
	 * deleteVendor is used to delete the Vendor and address table
	 * 
	 * @param VendorUpdateRequestBody
	 * @return SuccessResponse
	 */
	public SuccessResponse deleteVendor(VendorUpdateRequestBody requestBody) {
		SuccessResponse successResponse = new SuccessResponse();
		
		vendorRepo.deleteVendor(requestBody);
		
		successResponse.setStatus(HttpStatus.OK.value());
		successResponse.setMessage(HttpStatus.OK.getReasonPhrase());
		successResponse.setTimestamp(LocalDateTime.now());
		return successResponse;
	}
	

}