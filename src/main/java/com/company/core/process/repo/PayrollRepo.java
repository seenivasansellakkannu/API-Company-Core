package com.company.core.process.repo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
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

		strSql.append("SELECT ");
		strSql.append("p.PROJECT_ID as projectId, ");
		strSql.append("p.PROJECT_CODE as projectCode, ");
		strSql.append("p.PROJECT_NAME as projectName, ");
		strSql.append("p.ADDRESS_ID as addressId, ");
		strSql.append("e.EMPLOYEE_ID as employeeId, ");
		strSql.append("e.EMPLOYEE_NAME as employeeName, ");
		strSql.append("a.WORK_DATE as workDate, ");
		strSql.append("a.WORKDAYS as workdays, ");
		strSql.append("s.SALARY_PER_HOUR as salaryPerHour, ");
		strSql.append("(a.WORKDAYS * s.SALARY_PER_HOUR) as salary ");
		strSql.append("FROM ATTENDANCE a ");
		strSql.append("LEFT JOIN EMPLOYEE e ON a.EMPLOYEE_ID = e.EMPLOYEE_ID ");
		strSql.append("LEFT JOIN PROJECT p ON a.PROJECT_ID = p.PROJECT_ID ");
		strSql.append("LEFT JOIN SALARY s ON s.EMPLOYEE_ID = e.EMPLOYEE_ID ");
		strSql.append("WHERE 1 = 1 ");

		List<Object> queryParams = new ArrayList<>();

		if (requestBody.getPayload().getSalaryCalculation().getEmployeeId() != null) {

			strSql.append(" AND e.EMPLOYEE_ID = ? ");
			queryParams.add(requestBody.getPayload().getSalaryCalculation().getEmployeeId());
		}

		if (requestBody.getPayload().getSalaryCalculation().getProjectCode() != null) {

			strSql.append(" AND p.PROJECT_CODE = ? ");
			queryParams.add(requestBody.getPayload().getSalaryCalculation().getProjectCode().toString());
		}

		if (requestBody.getPayload().getSalaryCalculation().getFromDate() != null
				&& requestBody.getPayload().getSalaryCalculation().getToDate() != null) {

			strSql.append(" AND TO_DATE(a.WORK_DATE, 'DDMMYYYY') " + "BETWEEN TO_DATE(?, 'DDMMYYYY') "
					+ "AND TO_DATE(?, 'DDMMYYYY') ");

			queryParams.add(requestBody.getPayload().getSalaryCalculation().getFromDate().toString());

			queryParams.add(requestBody.getPayload().getSalaryCalculation().getToDate().toString());
		}

		strSql.append(" ORDER BY e.EMPLOYEE_ID, a.WORK_DATE ");

		SalaryCalculationSuccessResponse response = jdbcTemplate.query(strSql.toString(), queryParams.toArray(), rs -> {

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

				BigDecimal salary = rs.getBigDecimal("salary");

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
