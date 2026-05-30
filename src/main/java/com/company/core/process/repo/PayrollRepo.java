package com.company.core.process.repo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.company.core.process.dto.SalaryCalculationEmployee;
import com.company.core.process.dto.SalaryCalculationEmployeeDate;
import com.company.core.process.dto.SalaryCalculationProject;
import com.company.core.process.dto.SalaryCalculationSuccessResponse;
import com.company.core.process.dto.SalaryCalculationUpdateRequestBody;

import lombok.extern.slf4j.Slf4j;


@Repository
@Slf4j
public class PayrollRepo {
	
//	@Autowired
//	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/**
	 * getPayrollData is used to get Payroll data
	 * 
	 * @param requestBody, limit, offset
	 * @return list
	 */
	public SalaryCalculationSuccessResponse getPayrollData(SalaryCalculationUpdateRequestBody requestBody) {
		log.info("getPayrollData method starts");
		
		StringBuilder strSql = new StringBuilder();
		strSql.append("SELECT p.PROJECT_ID as projectId, p.PROJECT_CODE as projectCode, p.PROJECT_NAME as projectName, p.ADDRESS_ID as addressId, ");
		strSql.append("e.EMPLOYEE_ID as employeeId, E.EMPLOYEE_NAME as employeeName, a.WORK_DATE as workDate, a.WORKDAYS as workdays,");
		strSql.append("s.SALARY_PER_HOUR as salaryPerHour, (a.WORKDAYS * s.SALARY_PER_HOUR) as salary");
		strSql.append(" FROM ATTENDANCE a");
		strSql.append(" LEFT JOIN EMPLOYEE e ON a.EMPLOYEE_ID = e.EMPLOYEE_ID ");
		strSql.append(" LEFT JOIN PROJECT p ON a.PROJECT_ID = p.PROJECT_ID ");
		strSql.append(" LEFT JOIN SALARY s ON s.EMPLOYEE_ID  = e.EMPLOYEE_ID  ");
		strSql.append("WHERE 1 = 1 ");
		
		MapSqlParameterSource params = new MapSqlParameterSource();

		if(requestBody.getPayload().getSalaryCalculation().getEmployeeId() != null) {
			strSql.append(" AND e.EMPLOYEE_ID = :employeeId");
		    params.addValue("employeeId", requestBody.getPayload().getSalaryCalculation().getEmployeeId());
		}

		if(requestBody.getPayload().getSalaryCalculation().getProjectCode() != null) {
			strSql.append(" AND p.PROJECT_CODE = :projectCode");
		    params.addValue("projectCode", requestBody.getPayload().getSalaryCalculation().getProjectCode());
		}

		if(requestBody.getPayload().getSalaryCalculation().getFromDate() != null && requestBody.getPayload().getSalaryCalculation().getToDate() != null) {
			strSql.append(" AND a.WORK_DATE BETWEEN :fromDate AND :toDate");
		    params.addValue("fromDate", requestBody.getPayload().getSalaryCalculation().getFromDate());
		    params.addValue("toDate", requestBody.getPayload().getSalaryCalculation().getToDate());
		}
		
		strSql.append(" ORDER BY e.EMPLOYEE_ID, a.WORK_DATE ");
		
		
		SalaryCalculationSuccessResponse response =
				namedParameterJdbcTemplate.query(strSql.toString(), params, rs -> {
					SalaryCalculationProject project = new SalaryCalculationProject();
					Map<Integer, SalaryCalculationEmployee> employeeMap = new LinkedHashMap<>();
					BigDecimal projectTotalSalary = BigDecimal.ZERO;

					while (rs.next()) {
						if (project.getProjectCode() == null) {
							project.setProjectCode(rs.getInt("projectCode"));
							project.setProjectName(rs.getString("projectName"));
						}
						Integer employeeId = rs.getInt("employeeId");
						String employeeName = rs.getString("employeeName");

						SalaryCalculationEmployee employee = employeeMap.computeIfAbsent(employeeId, id -> {
							SalaryCalculationEmployee emp = new SalaryCalculationEmployee();
							emp.setEmployeeId(id);
							emp.setEmployeeName(employeeName);
							emp.setSalary(new ArrayList<>());
							emp.setTotalSalary(BigDecimal.ZERO);
							return emp;
						});

						BigDecimal salary = rs.getBigDecimal("workdays").multiply(rs.getBigDecimal("salaryPerHour"));
						SalaryCalculationEmployeeDate salaryDate = new SalaryCalculationEmployeeDate();
						salaryDate.setWorkDate(rs.getInt("workDate"));
						salaryDate.setWorkdays(rs.getBigDecimal("workdays"));
						salaryDate.setSalary(salary);
						employee.getSalary().add(salaryDate);
						employee.setTotalSalary(employee.getTotalSalary().add(salary));
						projectTotalSalary = projectTotalSalary.add(salary);
					}
					project.setEmployees(new ArrayList<>(employeeMap.values()));
					project.setTotalSalary(projectTotalSalary);
					SalaryCalculationSuccessResponse result = new SalaryCalculationSuccessResponse();
					result.setProject(project);
					result.setTotalRecords(employeeMap.size());
					return result;
				});
		log.info("getPayrollData method ends");
	    return response;
	}
}
