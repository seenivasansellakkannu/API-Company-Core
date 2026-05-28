package com.company.core.process.repo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.company.core.process.dto.Address;
import com.company.core.process.dto.AddressType;
import com.company.core.process.dto.Employee;
import com.company.core.process.dto.EmployeeSearchRequestBody;
import com.company.core.process.dto.EmployeeSearchResult;
import com.company.core.process.dto.EmployeeUpdateBasic;
import com.company.core.process.dto.EmployeeUpdateRequestBody;
import com.company.core.process.dto.Salary;

import lombok.extern.slf4j.Slf4j;


@Repository
@Slf4j
public class EmployeeRepo {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * searchEmployee is used to search employee
	 * 
	 * @param requestBody, limit, offset
	 * @return list
	 */
	public List<EmployeeSearchResult> searchEmployee(EmployeeSearchRequestBody requestBody, Integer limit, Integer offset) {
		log.info("searchEmployee method starts");
		
		StringBuilder strSql = new StringBuilder();
		strSql.append("SELECT COUNT(*) OVER() AS totalRecords, ");
		strSql.append("e.EMPLOYEE_ID as employeeId, e.EMPLOYEE_NAME as employeeName, e.EMPLOYEE_LEVEL as employeeLevel,");
		strSql.append(" e.PRIMARY_PHONE_NUMBER as primaryPhoneNumber, e.SECONDARY_PHONE_NUMBER as secondaryPhoneNumber,");
		strSql.append("e.DATE_OF_JOINING as dateOfJoining, e.DATE_OF_RESIGN as dateOfResign, e.EMPLOYEE_STATUS as employeeStatus, e.EMAIL as email,");
		strSql.append("a.ADDRESS_ID as addressId, a.ADDRESS_TYPE as addressType, a.ADDRESS_LINE1 as addressLine1, a.ADDRESS_LINE2 as addressLine2, a.CITY as city,");
		strSql.append("a.THALUK as thaluk, a.ADD_STATE as addState, a.COUNTRY as country, a.PINCODE as pincode, ");
		strSql.append("s.SALARY_PER_HOUR  as salaryPerHour ");
		strSql.append(" FROM EMPLOYEE e LEFT JOIN ADDRESS a ON e.ADDRESS_ID = a.ADDRESS_ID");
		strSql.append(" LEFT JOIN SALARY s ON e.EMPLOYEE_ID = s.EMPLOYEE_ID ");
		strSql.append(searchCondition(requestBody));
		strSql.append(" ORDER BY e.EMPLOYEE_ID ");
		strSql.append(" LIMIT ");strSql.append(limit);
		strSql.append(" OFFSET ");strSql.append(offset);
//		strSql.append(" ROWS FETCH NEXT ");strSql.append(limit);strSql.append(" ROWS ONLY ");
		
		log.info("searchEmployee method ends");
	    return jdbcTemplate.query(strSql.toString(), (rs, rowNum) -> getEmployeeMapper(rs, rowNum));
	}


	/**
	 * getEmployeeMapper is used to map the employee table to result
	 * 
	 * @param rs, rowNum
	 * @return EmployeeSearchResult
	 */
	private EmployeeSearchResult getEmployeeMapper(ResultSet rs, int rowNum) throws SQLException {
		
		EmployeeSearchResult result = new EmployeeSearchResult();
		
		result.setTotalRecords(rs.getInt("totalRecords"));
		
		Employee employee = new Employee();
		employee.setEmployeeId(rs.getInt("employeeId"));
		employee.setEmployeeName(rs.getString("employeeName"));
		employee.setEmployeeLevel(rs.getString("employeeLevel"));
		employee.setPrimaryPhoneNumber(rs.getString("primaryPhoneNumber"));
		employee.setSecondaryPhoneNumber(rs.getString("secondaryPhoneNumber"));
		employee.setDateOfJoining(Integer.parseInt(rs.getString("dateOfJoining")));
		String dateOfResign = StringUtils.isNotEmpty(rs.getString("dateOfResign")) ? rs.getString("dateOfResign") : "0";
		employee.setDateOfResign(Integer.parseInt(dateOfResign));
		employee.setEmployeeStatus(rs.getString("employeeStatus"));
		employee.setEmail(rs.getString("email"));
		
		Address address = new Address();
		address.setAddressId(rs.getInt("addressId"));
		address.setAddressType(AddressType.valueOf(rs.getString("addressType")));
		address.setAddressLine1(rs.getString("addressLine1"));
		address.setAddressLine2(rs.getString("addressLine2"));
		address.setCity(rs.getString("city"));
		address.setThaluk(rs.getString("thaluk"));
		address.setAddState(rs.getString("addState"));
		address.setCountry(rs.getString("country"));
		address.setPincode(rs.getString("pincode"));
		
		Salary salary = new Salary();
		salary.setEmployeeId(rs.getInt("employeeId"));
		salary.setEmployeeLevel(rs.getString("employeeLevel"));
		salary.setSalaryPerHour(rs.getBigDecimal("salaryPerHour"));
		
		result.setAddress(address);
		result.setEmployee(employee);
		result.setSalary(salary);
		return result;
	}

	/**
	 * searchCondition is used to add the search condition
	 * 
	 * @param requestBody
	 * @return String
	 */
	private String searchCondition(EmployeeSearchRequestBody requestBody) {
		StringBuilder condition = new StringBuilder();
		condition.append(" WHERE 1 = 1 ");
		
		if(Objects.nonNull(requestBody.getPayload())) {
			if(Objects.nonNull(requestBody.getPayload().getEmployeeId())) {
				condition.append(" AND e.EMPLOYEE_ID = ");condition.append(requestBody.getPayload().getEmployeeId());
			}
			if(StringUtils.isNotEmpty(requestBody.getPayload().getEmployeeName())) {
				condition.append(" AND UPPER(e.EMPLOYEE_NAME) = '");
				condition.append(requestBody.getPayload().getEmployeeName().toUpperCase());
				condition.append("'");
			}
			if(StringUtils.isNotEmpty(requestBody.getPayload().getEmployeeLevel())) {
				condition.append(" AND UPPER(e.EMPLOYEE_level) = '");
				condition.append(requestBody.getPayload().getEmployeeLevel().toUpperCase());
				condition.append("'");
			}
			if(Objects.nonNull(requestBody.getPayload().getPrimaryPhoneNumber())) {
				condition.append(" AND e.primary_phone_number = '");condition.append(requestBody.getPayload().getPrimaryPhoneNumber());
				condition.append("'");
			}
			if(Objects.nonNull(requestBody.getPayload().getSecondaryPhoneNumber())) {
				condition.append(" AND e.secondary_phone_number = '");condition.append(requestBody.getPayload().getSecondaryPhoneNumber());
				condition.append("'");
			}
			if(Objects.nonNull(requestBody.getPayload().getDateOfJoining())) {
				condition.append(" AND e.date_of_joining = '");condition.append(requestBody.getPayload().getDateOfJoining());
				condition.append("'");
			}
			if(Objects.nonNull(requestBody.getPayload().getDateOfResign())) {
				condition.append(" AND e.date_of_resign '= ");condition.append(requestBody.getPayload().getDateOfResign());
				condition.append("'");
			}
			if(StringUtils.isNotEmpty(requestBody.getPayload().getEmployeeStatus())) {
				condition.append(" AND UPPER(e.EMPLOYEE_status) = '");
				condition.append(requestBody.getPayload().getEmployeeStatus().toUpperCase());
				condition.append("'");
			}
			if(StringUtils.isNotEmpty(requestBody.getPayload().getEmail())) {
				condition.append(" AND UPPER(e.email) = '");
				condition.append(requestBody.getPayload().getEmail().toUpperCase());
				condition.append("'");
			}
		}
		return condition.toString();
	}

	/**
	 * addEmployee is used to add the Employee and address details
	 * 
	 * @param EmployeeUpdateRequestBody
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addEmployee(EmployeeUpdateRequestBody requestBody) {
		log.info("addEmployee method starts");

		if (Objects.nonNull(requestBody.getPayload())
				&& Objects.nonNull(requestBody.getPayload().getEmployeesDetails())) {

			List<EmployeeUpdateBasic> employeesDetailsList = requestBody.getPayload().getEmployeesDetails();

			StringBuilder employeeSql = new StringBuilder();
			employeeSql.append(
					" INSERT INTO EMPLOYEE (EMPLOYEE_NAME, EMPLOYEE_LEVEL, ADDRESS_ID, PRIMARY_PHONE_NUMBER, SECONDARY_PHONE_NUMBER, ");
			employeeSql.append(" DATE_OF_JOINING, EMPLOYEE_STATUS, EMAIL) VALUES(?,?,?,?,?,?,?,?)");

			StringBuilder addressSql = new StringBuilder();
			addressSql.append(" INSERT INTO ADDRESS (ADDRESS_TYPE, ADDRESS_LINE1, ADDRESS_LINE2, CITY, THALUK, ");
			addressSql.append(" ADD_STATE, COUNTRY, PINCODE) VALUES(?,?,?,?,?,?,?,?)");
			
			StringBuilder salarySql = new StringBuilder();
			salarySql.append(" INSERT INTO SALARY (EMPLOYEE_ID, EMPLOYEE_LEVEL, SALARY_PER_HOUR ");
			salarySql.append(" ) VALUES(?,?,?)");

			for (EmployeeUpdateBasic employeesDetails : employeesDetailsList) {
				Employee employee = employeesDetails.getEmployee();
				Address address = employeesDetails.getAddress();
				Salary salary = employeesDetails.getSalary();
				GeneratedKeyHolder addressKeyHolder = new GeneratedKeyHolder();
				GeneratedKeyHolder employeeKeyHolder = new GeneratedKeyHolder();

				jdbcTemplate.update(connection -> {
					PreparedStatement ps = connection.prepareStatement(addressSql.toString(),
							new String[] { "address_id" });
					ps.setString(1, address.getAddressType().name());
					ps.setString(2, address.getAddressLine1());
					ps.setString(3, address.getAddressLine2());
					ps.setString(4, address.getCity());
					ps.setString(5, address.getThaluk());
					ps.setString(6, address.getAddState());
					ps.setString(7, address.getCountry());
					ps.setString(8, address.getPincode());
					return ps;
				}, addressKeyHolder);

				int addressId = addressKeyHolder.getKey().intValue();
				
				jdbcTemplate.update(connection -> {
					PreparedStatement ps = connection.prepareStatement(employeeSql.toString(),
							new String[] { "employee_id" });
					ps.setString(1, employee.getEmployeeName());
					ps.setString(2, employee.getEmployeeLevel());
					ps.setInt(3, Integer.parseInt(String.valueOf(addressId)));
					ps.setString(4, String.valueOf(employee.getPrimaryPhoneNumber()));
					ps.setString(5, String.valueOf(employee.getSecondaryPhoneNumber()));
					ps.setString(6, String.valueOf(employee.getDateOfJoining()));
					ps.setString(7, employee.getEmployeeStatus());
					ps.setString(8, employee.getEmail());
					return ps;
				}, employeeKeyHolder);

				int employeeId = employeeKeyHolder.getKey().intValue();
				
				jdbcTemplate.update(salarySql.toString(), Integer.parseInt(String.valueOf(employeeId)), employee.getEmployeeLevel(), salary.getSalaryPerHour());
			}
		}
		log.info("addEmployee method ends");
	}
	
	/**
	 * updateEmployee is used to update the Employee and address details
	 * 
	 * @param EmployeeUpdateRequestBody
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateEmployee(EmployeeUpdateRequestBody requestBody) {
		log.info("updateEmployee method starts");

		if (Objects.nonNull(requestBody.getPayload())
				&& Objects.nonNull(requestBody.getPayload().getEmployeesDetails())) {

			List<EmployeeUpdateBasic> employeesDetailsList = requestBody.getPayload().getEmployeesDetails();

			StringBuilder employeeUpdateSql = new StringBuilder();
			employeeUpdateSql.append(" UPDATE EMPLOYEE SET ");
			employeeUpdateSql.append(" EMPLOYEE_NAME = ?, ");
			employeeUpdateSql.append(" EMPLOYEE_LEVEL = ?, ");
			employeeUpdateSql.append(" PRIMARY_PHONE_NUMBER = ?, ");
			employeeUpdateSql.append(" SECONDARY_PHONE_NUMBER = ?, ");
			employeeUpdateSql.append(" DATE_OF_JOINING = ?, ");
			employeeUpdateSql.append(" DATE_OF_RESIGN = ?, ");
			employeeUpdateSql.append(" EMPLOYEE_STATUS = ?, ");
			employeeUpdateSql.append(" EMAIL = ? ");
			employeeUpdateSql.append(" WHERE EMPLOYEE_ID = ? ");

			StringBuilder addressUpdateSql = new StringBuilder();
			addressUpdateSql.append(" UPDATE ADDRESS SET ");
			addressUpdateSql.append(" ADDRESS_TYPE = ?, ");
			addressUpdateSql.append(" ADDRESS_LINE1 = ?, ");
			addressUpdateSql.append(" ADDRESS_LINE2 = ?, ");
			addressUpdateSql.append(" CITY = ?, ");
			addressUpdateSql.append(" THALUK = ?, ");
			addressUpdateSql.append(" ADD_STATE = ?, ");
			addressUpdateSql.append(" COUNTRY = ?, ");
			addressUpdateSql.append(" PINCODE = ? ");
			addressUpdateSql.append(" WHERE ADDRESS_ID = ? ");
			
			StringBuilder salaryUpdateSql = new StringBuilder();
			salaryUpdateSql.append(" UPDATE SALARY SET ");
			salaryUpdateSql.append(" EMPLOYEE_LEVEL = ?, ");
			salaryUpdateSql.append(" SALARY_PER_HOUR = ? ");
			salaryUpdateSql.append(" WHERE EMPLOYEE_ID = ? ");

			for (EmployeeUpdateBasic employeesDetails : employeesDetailsList) {
				Employee employee = employeesDetails.getEmployee();
				Address address = employeesDetails.getAddress();
				Salary salary = employeesDetails.getSalary();

				jdbcTemplate.update(employeeUpdateSql.toString(),
					    employee.getEmployeeName(),
					    employee.getEmployeeLevel(),
					    employee.getPrimaryPhoneNumber(),
					    employee.getSecondaryPhoneNumber(),
					    employee.getDateOfJoining(),
					    employee.getDateOfResign(),
					    employee.getEmployeeStatus(),
					    employee.getEmail(),
					    employee.getEmployeeId()
				);
				
				jdbcTemplate.update(addressUpdateSql.toString(),
						address.getAddressType().name(),
						address.getAddressLine1(),
						address.getAddressLine2(),
						address.getCity(),
						address.getThaluk(),
						address.getAddState(),
						address.getCountry(),
						address.getPincode(),
						address.getAddressId()
				);
				
				jdbcTemplate.update(salaryUpdateSql.toString(),
						salary.getEmployeeLevel(),
						salary.getSalaryPerHour(),
						salary.getEmployeeId()
				);
			}
		}
		log.info("updateEmployee method ends");
	}
	
	/**
	 * deleteEmployee is used to delete the Employee and address table
	 * 
	 * @param EmployeeUpdateRequestBody
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteEmployee(EmployeeUpdateRequestBody requestBody) {
		log.info("updateEmployee method starts");

		if (Objects.nonNull(requestBody.getPayload())
				&& Objects.nonNull(requestBody.getPayload().getEmployeesDetails())) {

			List<EmployeeUpdateBasic> employeesDetailsList = requestBody.getPayload().getEmployeesDetails();

			StringBuilder salaryDeleteSql = new StringBuilder();
			salaryDeleteSql.append(" DELETE FROM SALARY ");
			salaryDeleteSql.append(" WHERE EMPLOYEE_ID = ? ");

			StringBuilder employeeDeleteSql = new StringBuilder();
			employeeDeleteSql.append(" DELETE FROM EMPLOYEE ");
			employeeDeleteSql.append(" WHERE EMPLOYEE_ID = ? ");
			
			StringBuilder addressDeleteSql = new StringBuilder();
			addressDeleteSql.append(" DELETE FROM ADDRESS ");
			addressDeleteSql.append(" WHERE ADDRESS_ID = ? ");

			for (EmployeeUpdateBasic employeesDetails : employeesDetailsList) {
				Employee employee = employeesDetails.getEmployee();
				Address address = employeesDetails.getAddress();
				Salary salary = employeesDetails.getSalary();

				// Delete salary first (child table)
				jdbcTemplate.update(salaryDeleteSql.toString(), employee.getEmployeeId());

				// Delete employee
				jdbcTemplate.update(employeeDeleteSql.toString(), employee.getEmployeeId());

				// Delete address
				jdbcTemplate.update(addressDeleteSql.toString(), employee.getAddressId());
			}
		}
		log.info("updateEmployee method ends");
	}
}
