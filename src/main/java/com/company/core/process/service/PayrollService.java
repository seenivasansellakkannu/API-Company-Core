package com.company.core.process.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.core.process.dto.SalaryCalculationSuccessResponse;
import com.company.core.process.dto.SalaryCalculationUpdateBasic;
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

		SalaryCalculationSuccessResponse response = new SalaryCalculationSuccessResponse();
		
		SalaryCalculationUpdateBasic salaryCalculation = requestBody.getPayload().getSalaryCalculation();
		
		Integer employeeId = salaryCalculation.getEmployeeId();
		String employeeName = salaryCalculation.getEmployeeName();
		Integer projectCode = salaryCalculation.getProjectCode();
		String projectName = salaryCalculation.getProjectName();
		Integer fromDate = salaryCalculation.getFromDate();
		Integer toDate = salaryCalculation.getToDate();
		
		
		
		return response;
	}
	

}