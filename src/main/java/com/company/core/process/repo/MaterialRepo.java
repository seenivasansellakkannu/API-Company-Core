package com.company.core.process.repo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.company.core.process.dto.MaterialMaster;
import com.company.core.process.dto.MaterialSearchRequestBody;
import com.company.core.process.dto.MaterialSearchResult;
import com.company.core.process.dto.MaterialUpdateBasic;
import com.company.core.process.dto.MaterialUpdateRequestBody;
import com.company.core.process.dto.Employee;
import com.company.core.process.dto.Project;
import com.company.core.process.dto.Salary;

import lombok.extern.slf4j.Slf4j;


@Repository
@Slf4j
public class MaterialRepo {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * searchEmployee is used to search employee
	 * 
	 * @param requestBody, limit, offset
	 * @return list
	 */
	public List<MaterialSearchResult> searchMaterial(MaterialSearchRequestBody requestBody, Integer limit, Integer offset) {
		log.info("searchMaterial method starts");
		
		StringBuilder strSql = new StringBuilder();
		strSql.append("SELECT COUNT(*) OVER() AS totalRecords, ");
		//Material
		strSql.append("m.MATERIAL_ID as materialId, m.MATERIAL_CODE as materialCode, m.MATERIAL_NAME as materialName, ");
		strSql.append("m.MATERIAL_TYPE as materialType, m.UNIT as unit");
		strSql.append(" FROM MATERIAL m ");
		strSql.append(searchCondition(requestBody));
		strSql.append(" ORDER BY m.MATERIAL_CODE ");
		strSql.append(" LIMIT ");strSql.append(limit);
		strSql.append(" OFFSET ");strSql.append(offset);
//		strSql.append(" ROWS FETCH NEXT ");strSql.append(limit);strSql.append(" ROWS ONLY ");
		
		log.info("searchMaterial method ends");
	    return jdbcTemplate.query(strSql.toString(), (rs, rowNum) -> getMaterialMapper(rs, rowNum));
	}


	/**
	 * getMaterialMapper is used to map the employee table to result
	 * 
	 * @param rs, rowNum
	 * @return MaterialSearchResult
	 */
	private MaterialSearchResult getMaterialMapper(ResultSet rs, int rowNum) throws SQLException {
		
		MaterialSearchResult result = new MaterialSearchResult();
		
		result.setTotalRecords(rs.getInt("totalRecords"));
		
		MaterialMaster material = new MaterialMaster();
		material.setMaterialId(rs.getInt("materialId"));
		material.setMaterialCode(rs.getString("materialCode"));
		material.setMaterialType(rs.getString("materialType"));
		material.setUnit(rs.getString("unit"));
		
		result.setMaterial(material);
		return result;
	}

	/**
	 * searchCondition is used to add the search condition
	 * 
	 * @param requestBody
	 * @return String
	 */
	private String searchCondition(MaterialSearchRequestBody requestBody) {
		StringBuilder condition = new StringBuilder();
		condition.append(" WHERE 1 = 1 ");
		
		if(Objects.nonNull(requestBody.getPayload())) {
			if(Objects.nonNull(requestBody.getPayload().getMaterialCode())) {
				condition.append(" AND UPPER(m.MATERIAL_CODE) LIKE '%");condition.append(requestBody.getPayload().getMaterialCode());
				condition.append("%'");
			}
			if(StringUtils.isNotEmpty(requestBody.getPayload().getMaterialName())) {
				condition.append(" AND UPPER(m.MATERIAL_NAME) LIKE '%");
				condition.append(requestBody.getPayload().getMaterialName().toUpperCase());
				condition.append("%'");
			}
			if(Objects.nonNull(requestBody.getPayload().getMaterialType())) {
				condition.append(" AND m.MATERIAL_TYPE = '");
				condition.append(requestBody.getPayload().getMaterialType());
				condition.append("'");
			}
		}
		return condition.toString();
	}

	/**
	 * addMaterial is used to add the Material details
	 * 
	 * @param EmployeeUpdateRequestBody
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addMaterial(MaterialUpdateRequestBody requestBody) {
		log.info("addMaterial method starts");

		if (Objects.nonNull(requestBody.getPayload())
				&& Objects.nonNull(requestBody.getPayload().getMaterials())) {

			List<MaterialUpdateBasic> materialList = requestBody.getPayload().getMaterials();

			StringBuilder materialSql = new StringBuilder();
			materialSql.append("INSERT INTO MATERIAL (MATERIAL_CODE, MATERIAL_NAME, MATERIAL_TYPE, UNIT) ");
			materialSql.append("VALUES(?, ?, ?, ?)");

			for (MaterialUpdateBasic materials : materialList) {

				jdbcTemplate.update(materialSql.toString(),materials.getMaterial().getMaterialCode(),materials.getMaterial().getMaterialName(),
						materials.getMaterial().getMaterialType(),materials.getMaterial().getUnit());

			}
		}
		log.info("addMaterial method ends");
	}
	
	/**
	 * updateMaterial is used to update the Material details
	 * 
	 * @param MaterialUpdateRequestBody
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateMaterial(MaterialUpdateRequestBody requestBody) {
		log.info("updateMaterial method starts");

		if (Objects.nonNull(requestBody.getPayload())
				&& Objects.nonNull(requestBody.getPayload().getMaterials())) {

			List<MaterialUpdateBasic> materialsList = requestBody.getPayload().getMaterials();

			StringBuilder materialUpdateSql = new StringBuilder();
			materialUpdateSql.append(" UPDATE MATERIAL_MASTER SET ");
			materialUpdateSql.append(" MATERIAL_NAME = ?, MATERIAL_TYPE = ?, UNIT = ? ");
			materialUpdateSql.append(" WHERE MATERIAL_ID = ? AND MATERIAL_CODE = ? ");

			for (MaterialUpdateBasic material : materialsList) {
				jdbcTemplate.update(materialUpdateSql.toString(), material.getMaterial().getMaterialName(), material.getMaterial().getMaterialType(), 
						material.getMaterial().getUnit(), material.getMaterial().getMaterialId(), material.getMaterial().getMaterialCode());
			}
		}
		log.info("updateMaterial method ends");
	}
	
	/**
	 * deleteMaterial is used to delete the Materials table
	 * 
	 * @param MaterialUpdateRequestBody
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteMaterial(MaterialUpdateRequestBody requestBody) {
		log.info("deleteMaterial method starts");

		if (Objects.nonNull(requestBody.getPayload()) && Objects.nonNull(requestBody.getPayload().getMaterials())) {

			List<MaterialUpdateBasic> materialsList = requestBody.getPayload().getMaterials();

			StringBuilder materialDeleteSql = new StringBuilder();
			materialDeleteSql.append(" DELETE FROM MATERIAL_MASTER ");
			materialDeleteSql.append(" WHERE MATERIAL_ID = ? AND MATERIAL_CODE = ? ");

			for (MaterialUpdateBasic material : materialsList) {
				jdbcTemplate.update(materialDeleteSql.toString(), material.getMaterial().getMaterialId(), material.getMaterial().getMaterialCode());
			}
		}
		log.info("deleteMaterial method ends");
	}
}
