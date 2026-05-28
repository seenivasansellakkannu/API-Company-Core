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
import com.company.core.process.dto.Attendance;
import com.company.core.process.dto.AttendanceSearchRequestBody;
import com.company.core.process.dto.AttendanceSearchResult;
import com.company.core.process.dto.AttendanceUpdateRequestBody;
import com.company.core.process.dto.Employee;
import com.company.core.process.dto.EmployeeSearchRequestBody;
import com.company.core.process.dto.EmployeeSearchResult;
import com.company.core.process.dto.EmployeeUpdateBasic;
import com.company.core.process.dto.EmployeeUpdateRequestBody;
import com.company.core.process.dto.Project;
import com.company.core.process.dto.Salary;

import lombok.extern.slf4j.Slf4j;


@Repository
@Slf4j
public class AttendanceRepo {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * searchEmployee is used to search employee
	 * 
	 * @param requestBody, limit, offset
	 * @return list
	 */
	public List<AttendanceSearchResult> searchAttendance(AttendanceSearchRequestBody requestBody, Integer limit, Integer offset) {
		log.info("searchAttendance method starts");
		
		StringBuilder strSql = new StringBuilder();
		strSql.append("SELECT COUNT(*) OVER() AS totalRecords, ");
		//ATTENDANCE
		strSql.append("a.ATTENDANCE_ID as attendanceId, a.EMPLOYEE_ID as employeeId, a.PROJECT_ID as projectId, ");
		strSql.append("a.WORK_DATE as workDate, a.WORKDAYS as workdays,");
		//PROJECT
		strSql.append("p.PROJECT_CODE as projectCode, p.PROJECT_NAME as projectName,");
		//EMPLOYEE
		strSql.append("e.EMPLOYEE_ID as employeeId, e.EMPLOYEE_NAME as employeeName, e.EMPLOYEE_LEVEL as employeeLevel, e.EMPLOYEE_STATUS as employeeStatus, ");
		//SALARY
		strSql.append("s.SALARY_PER_HOUR  as salaryPerHour ");
		strSql.append(" FROM ATTENDANCE a LEFT JOIN PROJECT p ON a.PROJECT_ID = p.PROJECT_ID");
		strSql.append(" LEFT JOIN EMPLOYEE e ON e.EMPLOYEE_ID = a.EMPLOYEE_ID ");
		strSql.append(" LEFT JOIN SALARY s ON s.EMPLOYEE_ID = e.EMPLOYEE_ID ");
		strSql.append(searchCondition(requestBody));
		strSql.append(" ORDER BY a.EMPLOYEE_ID ");
		strSql.append(" LIMIT ");strSql.append(limit);
		strSql.append(" OFFSET ");strSql.append(offset);
//		strSql.append(" ROWS FETCH NEXT ");strSql.append(limit);strSql.append(" ROWS ONLY ");
		
		log.info("searchAttendance method ends");
	    return jdbcTemplate.query(strSql.toString(), (rs, rowNum) -> getAttendanceMapper(rs, rowNum));
	}


	/**
	 * getAttendanceMapper is used to map the employee table to result
	 * 
	 * @param rs, rowNum
	 * @return EmployeeSearchResult
	 */
	private AttendanceSearchResult getAttendanceMapper(ResultSet rs, int rowNum) throws SQLException {
		
		AttendanceSearchResult result = new AttendanceSearchResult();
		
		result.setTotalRecords(rs.getInt("totalRecords"));
		
		Attendance attendance = new Attendance();
		attendance.setAttendanceId(rs.getInt("attendanceId"));
		attendance.setEmployeeId(rs.getInt("employeeId"));
		attendance.setProjectId(rs.getInt("projectId"));
		attendance.setWorkDate(rs.getInt("workDate"));
		attendance.setWorkdays(rs.getInt("workdays"));
		
		Project project = new Project();
		project.setProjectCode(rs.getString("projectCode"));
		project.setProjectName(rs.getString("projectName"));
		
		Employee employee = new Employee();
		employee.setEmployeeId(rs.getInt("employeeId"));
		employee.setEmployeeName(rs.getString("employeeName"));
		employee.setEmployeeLevel(rs.getString("employeeLevel"));
		employee.setEmployeeStatus(rs.getString("employeeStatus"));
		
		Salary salary = new Salary();
		salary.setSalaryPerHour(rs.getBigDecimal("salaryPerHour"));
		salary.setEmployeeId(rs.getInt("employeeId"));
		salary.setEmployeeLevel(rs.getString("employeeLevel"));
		
		result.setAttendance(attendance);
		result.setProject(project);
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
	private String searchCondition(AttendanceSearchRequestBody requestBody) {
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
			if(Objects.nonNull(requestBody.getPayload().getProjectCode())) {
				condition.append(" AND UPPER(p.PROJECT_CODE) = '");
				condition.append(requestBody.getPayload().getProjectCode());
				condition.append("'");
			}
			if(StringUtils.isNotEmpty(requestBody.getPayload().getProjectName())) {
				condition.append(" AND p.PROJECT_NAME = '");condition.append(requestBody.getPayload().getProjectName());
				condition.append("'");
			}
			if(Objects.nonNull(requestBody.getPayload().getFromDate()) && Objects.nonNull(requestBody.getPayload().getToDate())) {
				condition.append(" AND a.WORK_DATE BETWEEN ");condition.append(requestBody.getPayload().getFromDate());
				condition.append(" AND ");condition.append(requestBody.getPayload().getToDate());
			}
		}
		return condition.toString();
	}

	/**
	 * addAttendance is used to add the Attendance details
	 * 
	 * @param EmployeeUpdateRequestBody
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addAttendance(AttendanceUpdateRequestBody requestBody) {
		log.info("addAttendance method starts");

		if (Objects.nonNull(requestBody.getPayload())
				&& Objects.nonNull(requestBody.getPayload().getAttendances())) {

			List<Attendance> attendanceList = requestBody.getPayload().getAttendances();

			StringBuilder attendanceSql = new StringBuilder();
			attendanceSql.append("INSERT INTO ATTENDANCE (EMPLOYEE_ID, PROJECT_ID, WORK_DATE, WORKDAYS) ");
			attendanceSql.append("VALUES(?, ?, ?, ?)");

			for (Attendance attendance : attendanceList) {

				jdbcTemplate.update(attendanceSql.toString(),attendance.getEmployeeId(),attendance.getProjectId(),
						attendance.getWorkDate(),attendance.getWorkdays());

			}
		}
		log.info("addAttendance method ends");
	}
	
	/**
	 * updateAttendance is used to update the Attendance details
	 * 
	 * @param AttendanceUpdateRequestBody
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateAttendance(AttendanceUpdateRequestBody requestBody) {
		log.info("updateAttendance method starts");

		if (Objects.nonNull(requestBody.getPayload())
				&& Objects.nonNull(requestBody.getPayload().getAttendances())) {

			List<Attendance> attendancesList = requestBody.getPayload().getAttendances();

			StringBuilder attendanceUpdateSql = new StringBuilder();
			attendanceUpdateSql.append(" UPDATE ATTENDANCE SET ");
			attendanceUpdateSql.append(" WORKDAYS = ? ");
			attendanceUpdateSql.append(" WHERE ATTENDANCE_ID = ? ");

			for (Attendance attendance : attendancesList) {
				jdbcTemplate.update(attendanceUpdateSql.toString(), attendance.getWorkdays(),
						attendance.getAttendanceId());
			}
		}
		log.info("updateAttendance method ends");
	}
	
	/**
	 * deleteAttendance is used to delete the Attendances table
	 * 
	 * @param AttendanceUpdateRequestBody
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteAttendance(AttendanceUpdateRequestBody requestBody) {
		log.info("deleteAttendance method starts");

		if (Objects.nonNull(requestBody.getPayload()) && Objects.nonNull(requestBody.getPayload().getAttendances())) {

			List<Attendance> attendancesList = requestBody.getPayload().getAttendances();

			StringBuilder attendanceDeleteSql = new StringBuilder();
			attendanceDeleteSql.append(" DELETE FROM ATTENDANCE ");
			attendanceDeleteSql.append(" WHERE ATTENDANCE_ID = ? ");

			for (Attendance attendance : attendancesList) {
				jdbcTemplate.update(attendanceDeleteSql.toString(),attendance.getAttendanceId());
			}
		}
		log.info("deleteAttendance method ends");
	}
}
