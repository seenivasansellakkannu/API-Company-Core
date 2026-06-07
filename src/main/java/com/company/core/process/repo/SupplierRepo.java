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

import com.company.core.process.dto.Supplier;
import com.company.core.process.dto.SupplierSearchRequestBody;
import com.company.core.process.dto.SupplierSearchResult;
import com.company.core.process.dto.SupplierUpdateBasic;
import com.company.core.process.dto.SupplierUpdateRequestBody;

import lombok.extern.slf4j.Slf4j;


@Repository
@Slf4j
public class SupplierRepo {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * searchSupplier is used to search supplier
	 * 
	 * @param requestBody, limit, offset
	 * @return list
	 */
	public List<SupplierSearchResult> searchSupplier(SupplierSearchRequestBody requestBody, Integer limit, Integer offset) {
		log.info("searchSupplier method starts");
		
		StringBuilder strSql = new StringBuilder();
		strSql.append("SELECT COUNT(*) OVER() AS totalRecords, ");
		//Supplier
		strSql.append("s.SUPPLIER_ID as supplierId, s.SUPPLIER_CODE as supplierCode, s.SUPPLIER_NAME as supplierName, ");
		strSql.append("s.CONTACT_PERSON as contactPerson, s.PHONE_NUMBER as phoneNumber, s.EMAIL as email, s.ADDRESS as address, s.GST_NUMBER as gstNumber ");
		strSql.append(" FROM SUPPLIER s ");
		strSql.append(searchCondition(requestBody));
		strSql.append(" ORDER BY s.SUPPLIER_CODE ");
		strSql.append(" LIMIT ");strSql.append(limit);
		strSql.append(" OFFSET ");strSql.append(offset);
//		strSql.append(" ROWS FETCH NEXT ");strSql.append(limit);strSql.append(" ROWS ONLY ");
		
		log.info("searchSupplier method ends");
	    return jdbcTemplate.query(strSql.toString(), (rs, rowNum) -> getSupplierMapper(rs, rowNum));
	}


	/**
	 * getSupplierMapper is used to map the employee table to result
	 * 
	 * @param rs, rowNum
	 * @return SupplierSearchResult
	 */
	private SupplierSearchResult getSupplierMapper(ResultSet rs, int rowNum) throws SQLException {
		
		SupplierSearchResult result = new SupplierSearchResult();
		
		result.setTotalRecords(rs.getInt("totalRecords"));
		
		Supplier supplier = new Supplier();
		supplier.setSupplierId(rs.getInt("supplierId"));
		supplier.setSupplierCode(rs.getString("supplierCode"));
		supplier.setSupplierName(rs.getString("supplierName"));
		supplier.setContactPerson(rs.getString("contactPerson"));
		supplier.setPhoneNumber(rs.getString("phoneNumber"));
		supplier.setEmail(rs.getString("email"));
		supplier.setAddress(rs.getString("address"));
		supplier.setGstNumber(rs.getString("gstNumber"));
		result.setSupplier(supplier);
		return result;
	}

	/**
	 * searchCondition is used to add the search condition
	 * 
	 * @param requestBody
	 * @return String
	 */
	private String searchCondition(SupplierSearchRequestBody requestBody) {
		StringBuilder condition = new StringBuilder();
		condition.append(" WHERE 1 = 1 ");
		
		if(Objects.nonNull(requestBody.getPayload())) {
			if(Objects.nonNull(requestBody.getPayload().getSupplierCode())) {
				condition.append(" AND UPPER(s.SUPPLIER_CODE) LIKE '%");condition.append(requestBody.getPayload().getSupplierCode());
				condition.append("%'");
			}
			if(StringUtils.isNotEmpty(requestBody.getPayload().getSupplierName())) {
				condition.append(" AND UPPER(s.SUPPLIER_NAME) LIKE '%");
				condition.append(requestBody.getPayload().getSupplierName().toUpperCase());
				condition.append("%'");
			}
		}
		return condition.toString();
	}

	/**
	 * addSupplier is used to add the Supplier details
	 * 
	 * @param SupplierUpdateRequestBody
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addSupplier(SupplierUpdateRequestBody requestBody) {
		log.info("addSupplier method starts");

		if (Objects.nonNull(requestBody.getPayload())
				&& Objects.nonNull(requestBody.getPayload().getSuppliers())) {

			List<SupplierUpdateBasic> supplierList = requestBody.getPayload().getSuppliers();

			StringBuilder supplierSql = new StringBuilder();
			supplierSql.append("INSERT INTO SUPPLIER (SUPPLIER_CODE, SUPPLIER_NAME, CONTACT_PERSON, PHONE_NUMBER, EMAIL, ADDRESS, GST_NUMBER) ");
			supplierSql.append("VALUES(?, ?, ?, ?, ?, ?, ?)");

			for (SupplierUpdateBasic suppliers : supplierList) {

				jdbcTemplate.update(supplierSql.toString(),suppliers.getSupplier().getSupplierCode(),suppliers.getSupplier().getSupplierName(),
						suppliers.getSupplier().getContactPerson(), suppliers.getSupplier().getPhoneNumber(), suppliers.getSupplier().getEmail(),
						suppliers.getSupplier().getAddress(), suppliers.getSupplier().getGstNumber());
			}
		}
		log.info("addSupplier method ends");
	}
	
	/**
	 * updateSupplier is used to update the Supplier details
	 * 
	 * @param SupplierUpdateRequestBody
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateSupplier(SupplierUpdateRequestBody requestBody) {
		log.info("updateSupplier method starts");

		if (Objects.nonNull(requestBody.getPayload())
				&& Objects.nonNull(requestBody.getPayload().getSuppliers())) {

			List<SupplierUpdateBasic> SuppliersList = requestBody.getPayload().getSuppliers();

			StringBuilder supplierUpdateSql = new StringBuilder();
			supplierUpdateSql.append(" UPDATE SUPPLIER SET ");
			supplierUpdateSql.append(" SUPPLIER_NAME = ?, CONTACT_PERSON = ?, PHONE_NUMBER = ?, EMAIL = ?, ADDRESS = ?, GST_NUMBER = ? ");
			supplierUpdateSql.append(" WHERE SUPPLIER_ID = ? AND SUPPLIER_CODE = ? ");

			for (SupplierUpdateBasic suppliers : SuppliersList) {
				jdbcTemplate.update(supplierUpdateSql.toString(),suppliers.getSupplier().getSupplierName(),
						suppliers.getSupplier().getContactPerson(), suppliers.getSupplier().getPhoneNumber(), suppliers.getSupplier().getEmail(),
						suppliers.getSupplier().getAddress(), suppliers.getSupplier().getGstNumber(), 
						suppliers.getSupplier().getSupplierId(), suppliers.getSupplier().getSupplierCode());
			}
		}
		log.info("updateSupplier method ends");
	}
	
	/**
	 * deleteSupplier is used to delete the Suppliers table
	 * 
	 * @param SupplierUpdateRequestBody
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteSupplier(SupplierUpdateRequestBody requestBody) {
		log.info("deleteSupplier method starts");

		if (Objects.nonNull(requestBody.getPayload()) && Objects.nonNull(requestBody.getPayload().getSuppliers())) {

			List<SupplierUpdateBasic> suppliersList = requestBody.getPayload().getSuppliers();

			StringBuilder supplierDeleteSql = new StringBuilder();
			supplierDeleteSql.append(" DELETE FROM SUPPLIER ");
			supplierDeleteSql.append(" WHERE SUPPLIER_ID = ? AND SUPPLIER_CODE = ? ");

			for (SupplierUpdateBasic Supplier : suppliersList) {
				jdbcTemplate.update(supplierDeleteSql.toString(), Supplier.getSupplier().getSupplierId(), Supplier.getSupplier().getSupplierCode());
			}
		}
		log.info("deleteSupplier method ends");
	}
}
