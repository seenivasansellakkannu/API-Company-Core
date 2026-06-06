package com.company.core.process.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.company.core.process.dto.ExpenseSearchRequestBody;
import com.company.core.process.dto.ExpenseSearchResponse;
import com.company.core.process.dto.ExpenseSearchResult;
import com.company.core.process.dto.ExpenseUpdateRequestBody;
import com.company.core.process.dto.SuccessResponse;
import com.company.core.process.repo.ExpenseRepo;

@Service
public class ExpenseService {

	@Autowired
	private ExpenseRepo expenseRepo;
	
	/**
	 * searchExpense is used to search the Expense details
	 * 
	 * @param ExpenseSearchRequestBody, limit, offset
	 * @return ExpenseSearchResponse
	 */
	public ExpenseSearchResponse searchExpense(ExpenseSearchRequestBody requestBody, Integer limit, Integer offset) {

		ExpenseSearchResponse response = new ExpenseSearchResponse();
		int count = 0;
		List<ExpenseSearchResult> expensesList = expenseRepo.searchExpense(requestBody, limit, offset);
		
		if(!expensesList.isEmpty()) {
			count = expensesList.stream().findFirst().get().getTotalRecords();
		}
		response.setExpenses(expensesList);
		response.setTotalRecords(count);
		return response;
	}
	/**
	 * addExpense is used to add the Expense and address table
	 * 
	 * @param ExpenseUpdateRequestBody
	 * @return SuccessResponse
	 */
	public SuccessResponse addExpense(ExpenseUpdateRequestBody requestBody) {
		SuccessResponse successResponse = new SuccessResponse();
		
		expenseRepo.addExpense(requestBody);
		
		successResponse.setStatus(HttpStatus.CREATED.value());
		successResponse.setMessage(HttpStatus.CREATED.getReasonPhrase());
		successResponse.setTimestamp(LocalDateTime.now());
		return successResponse;
	}
	/**
	 * updateExpense is used to update the Expense and address table
	 * 
	 * @param ExpenseUpdateRequestBody
	 * @return SuccessResponse
	 */
	public SuccessResponse updateExpense(ExpenseUpdateRequestBody requestBody) {
		SuccessResponse successResponse = new SuccessResponse();
		
		expenseRepo.updateExpense(requestBody);
		
		successResponse.setStatus(HttpStatus.OK.value());
		successResponse.setMessage(HttpStatus.OK.getReasonPhrase());
		successResponse.setTimestamp(LocalDateTime.now());
		return successResponse;
	}
	/**
	 * deleteExpense is used to delete the Expense and address table
	 * 
	 * @param ExpenseUpdateRequestBody
	 * @return SuccessResponse
	 */
	public SuccessResponse deleteExpense(ExpenseUpdateRequestBody requestBody) {
		SuccessResponse successResponse = new SuccessResponse();
		
		expenseRepo.deleteExpense(requestBody);
		
		successResponse.setStatus(HttpStatus.OK.value());
		successResponse.setMessage(HttpStatus.OK.getReasonPhrase());
		successResponse.setTimestamp(LocalDateTime.now());
		return successResponse;
	}
	

}