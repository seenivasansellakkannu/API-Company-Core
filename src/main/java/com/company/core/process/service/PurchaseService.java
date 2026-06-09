package com.company.core.process.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.company.core.process.dto.PurchaseSearchRequestBody;
import com.company.core.process.dto.PurchaseSearchResponse;
import com.company.core.process.dto.PurchaseSearchResult;
import com.company.core.process.dto.PurchaseUpdateRequestBody;
import com.company.core.process.dto.SuccessResponse;
import com.company.core.process.repo.PurchaseRepo;

@Service
public class PurchaseService {

	@Autowired
	private PurchaseRepo purchaseRepo;
	
	/**
	 * searchPurchase is used to search the Purchase details
	 * 
	 * @param PurchaseSearchRequestBody, limit, offset
	 * @return PurchaseSearchResponse
	 */
	public PurchaseSearchResponse searchPurchase(PurchaseSearchRequestBody requestBody, Integer limit, Integer offset) {

		PurchaseSearchResponse response = new PurchaseSearchResponse();
		int count = 0;
		List<PurchaseSearchResult> PurchasesList = purchaseRepo.searchPurchase(requestBody, limit, offset);
		
		if(!PurchasesList.isEmpty()) {
			count = PurchasesList.stream().findFirst().get().getTotalRecords();
		}
		response.setPurchases(PurchasesList);
		response.setTotalRecords(count);
		return response;
	}
	/**
	 * addPurchase is used to add the Purchase and address table
	 * 
	 * @param PurchaseUpdateRequestBody
	 * @return SuccessResponse
	 */
	public SuccessResponse addPurchase(PurchaseUpdateRequestBody requestBody) {
		SuccessResponse successResponse = new SuccessResponse();
		
		purchaseRepo.addPurchase(requestBody);
		
		successResponse.setStatus(HttpStatus.CREATED.value());
		successResponse.setMessage(HttpStatus.CREATED.getReasonPhrase());
		successResponse.setTimestamp(LocalDateTime.now());
		return successResponse;
	}
	/**
	 * updatePurchase is used to update the Purchase and address table
	 * 
	 * @param PurchaseUpdateRequestBody
	 * @return SuccessResponse
	 */
	public SuccessResponse updatePurchase(PurchaseUpdateRequestBody requestBody) {
		SuccessResponse successResponse = new SuccessResponse();
		
		purchaseRepo.updatePurchase(requestBody);
		
		successResponse.setStatus(HttpStatus.OK.value());
		successResponse.setMessage(HttpStatus.OK.getReasonPhrase());
		successResponse.setTimestamp(LocalDateTime.now());
		return successResponse;
	}
	/**
	 * deletePurchase is used to delete the Purchase and address table
	 * 
	 * @param PurchaseUpdateRequestBody
	 * @return SuccessResponse
	 */
	public SuccessResponse deletePurchase(PurchaseUpdateRequestBody requestBody) {
		SuccessResponse successResponse = new SuccessResponse();
		
		purchaseRepo.deletePurchase(requestBody);
		
		successResponse.setStatus(HttpStatus.OK.value());
		successResponse.setMessage(HttpStatus.OK.getReasonPhrase());
		successResponse.setTimestamp(LocalDateTime.now());
		return successResponse;
	}
	

}