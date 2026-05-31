package com.company.core.process.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.core.process.dto.SalaryCalculationSuccessResponse;
import com.company.core.process.dto.SalaryCalculationUpdateRequestBody;
import com.company.core.process.repo.PayrollRepo;

@Service
public class PayrollService {

	@Autowired
	private PayrollRepo payrollRepo;
	
	/**
	 * payrollCalculation is used to calculate salary
	 * 
	 * @param SalaryCalculationUpdateRequestBody
	 * @return SalaryCalculationSuccessResponse
	 */
	public SalaryCalculationSuccessResponse payrollCalculation(SalaryCalculationUpdateRequestBody requestBody) {

		return payrollRepo.getPayrollData(requestBody);
	}
	

}