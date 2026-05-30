package com.company.core.process.repo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
import com.company.core.process.dto.EmployeeUpdateBasic;
import com.company.core.process.dto.Project;
import com.company.core.process.dto.ProjectSearchRequestBody;
import com.company.core.process.dto.ProjectSearchResult;
import com.company.core.process.dto.ProjectUpdateBasic;
import com.company.core.process.dto.ProjectUpdateRequestBody;
import com.company.core.process.dto.Salary;

import lombok.extern.slf4j.Slf4j;


@Repository
@Slf4j
public class ProjectRepo {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * searchProject is used to search Project
	 * 
	 * @param requestBody, limit, offset
	 * @return list
	 */
	public List<ProjectSearchResult> searchProject(ProjectSearchRequestBody requestBody, Integer limit, Integer offset) {
		log.info("searchProject method starts");
		
		StringBuilder strSql = new StringBuilder();
		strSql.append("SELECT COUNT(*) OVER() AS totalRecords, ");
		//PROJECT
		strSql.append("p.PROJECT_ID as projectId, p.PROJECT_CODE as projectCode, p.PROJECT_NAME as projectName, p.ADDRESS_ID as addressId, ");
		//ADDRESS
		strSql.append("a.ADDRESS_ID as addressId, a.ADDRESS_TYPE as addressType, a.ADDRESS_LINE1 as addressLine1, a.ADDRESS_LINE2 as addressLine2, a.CITY as city,");
		strSql.append("a.THALUK as thaluk, a.ADD_STATE as addState, a.COUNTRY as country, a.PINCODE as pincode ");
		strSql.append(" FROM PROJECT p");
		strSql.append(" LEFT JOIN ADDRESS a ON p.ADDRESS_ID = a.ADDRESS_ID ");
		strSql.append(searchCondition(requestBody));
		strSql.append(" ORDER BY p.PROJECT_ID ");
		strSql.append(" LIMIT ");strSql.append(limit);
		strSql.append(" OFFSET ");strSql.append(offset);
//		strSql.append(" ROWS FETCH NEXT ");strSql.append(limit);strSql.append(" ROWS ONLY ");
		
		log.info("searchProject method ends");
	    return jdbcTemplate.query(strSql.toString(), (rs, rowNum) -> getProjectMapper(rs, rowNum));
	}


	/**
	 * getProjectMapper is used to map the Project table to result
	 * 
	 * @param rs, rowNum
	 * @return EmployeeSearchResult
	 */
	private ProjectSearchResult getProjectMapper(ResultSet rs, int rowNum) throws SQLException {
		
		ProjectSearchResult result = new ProjectSearchResult();
		
		result.setTotalRecords(rs.getInt("totalRecords"));
		
		Project project = new Project();
		project.setProjectId(rs.getInt("projectId"));
		project.setProjectCode(rs.getString("projectCode"));
		project.setProjectName(rs.getString("projectName"));
		project.setAddressId(rs.getInt("addressId"));
		
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
		
		result.setProject(project);
		result.setAddress(address);
		return result;
	}

	/**
	 * searchCondition is used to add the search condition
	 * 
	 * @param requestBody
	 * @return String
	 */
	private String searchCondition(ProjectSearchRequestBody requestBody) {
		StringBuilder condition = new StringBuilder();
		condition.append(" WHERE 1 = 1 ");
		
		if(Objects.nonNull(requestBody.getPayload())) {
			if(Objects.nonNull(requestBody.getPayload().getProjectCode())) {
				condition.append(" AND UPPER(p.PROJECT_CODE) = '");
				condition.append(requestBody.getPayload().getProjectCode());
				condition.append("'");
			}
			if(StringUtils.isNotEmpty(requestBody.getPayload().getProjectName())) {
				condition.append(" AND p.PROJECT_NAME = '");condition.append(requestBody.getPayload().getProjectName());
				condition.append("'");
			}
		}
		return condition.toString();
	}

	/**
	 * addProject is used to add the Project details
	 * 
	 * @param ProjectUpdateRequestBody
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addProject(ProjectUpdateRequestBody requestBody) {
		log.info("addProject method starts");

		if (Objects.nonNull(requestBody.getPayload())
				&& Objects.nonNull(requestBody.getPayload().getProjectDetails())) {

			List<ProjectUpdateBasic> projectDetailsList = requestBody.getPayload().getProjectDetails();

			StringBuilder addressSql = new StringBuilder();
			addressSql.append(" INSERT INTO ADDRESS (ADDRESS_TYPE, ADDRESS_LINE1, ADDRESS_LINE2, CITY, THALUK, ");
			addressSql.append(" ADD_STATE, COUNTRY, PINCODE) VALUES(?,?,?,?,?,?,?,?)");
			
			StringBuilder projectSql = new StringBuilder();
			projectSql.append(" INSERT INTO PROJECT (PROJECT_CODE, PROJECT_NAME, ADDRESS_ID");
			projectSql.append(" ) VALUES(?,?,?)");

			for (ProjectUpdateBasic employeesDetails : projectDetailsList) {
				Project project = employeesDetails.getProject();
				Address address = employeesDetails.getAddress();
				
				GeneratedKeyHolder addressKeyHolder = new GeneratedKeyHolder();

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
				
				jdbcTemplate.update(projectSql.toString(), Integer.parseInt(String.valueOf(project.getProjectCode())), project.getProjectName(), addressId);
			}
		}
		log.info("addProject method ends");
	}
	
	/**
	 * updateProject is used to update the Project details
	 * 
	 * @param ProjectUpdateRequestBody
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateProject(ProjectUpdateRequestBody requestBody) {
		log.info("updateProject method starts");

		if (Objects.nonNull(requestBody.getPayload())
				&& Objects.nonNull(requestBody.getPayload().getProjectDetails())) {

			List<ProjectUpdateBasic> projectList = requestBody.getPayload().getProjectDetails();

			StringBuilder projectUpdateSql = new StringBuilder();
			projectUpdateSql.append(" UPDATE PROJECT SET ");
			projectUpdateSql.append(" PROJECT_NAME = ?, PROJECT_CODE=? ");
			projectUpdateSql.append(" WHERE PROJECT_ID = ? ");
			
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

			for (ProjectUpdateBasic project : projectList) {
				Project projects = project.getProject();
				Address address = project.getAddress();
				
				jdbcTemplate.update(projectUpdateSql.toString(), projects.getProjectName(),projects.getProjectCode(),
						projects.getProjectId());
				
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
			}
		}
		log.info("updateProject method ends");
	}
	
	/**
	 * deleteProject is used to delete the Project table
	 * 
	 * @param AttendanceUpdateRequestBody
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteProject(ProjectUpdateRequestBody requestBody) {
		log.info("deleteProject method starts");

		if (Objects.nonNull(requestBody.getPayload()) && Objects.nonNull(requestBody.getPayload().getProjectDetails())) {

			List<ProjectUpdateBasic> projectList = requestBody.getPayload().getProjectDetails();

			StringBuilder projectDeleteSql = new StringBuilder();
			projectDeleteSql.append(" DELETE FROM PROJECT ");
			projectDeleteSql.append(" WHERE PROJECT_ID = ? ");

			for (ProjectUpdateBasic project : projectList) {
				jdbcTemplate.update(projectDeleteSql.toString(),project.getProject().getProjectId());
			}
		}
		log.info("deleteProject method ends");
	}
}
