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

import com.company.core.process.dto.Vendor;
import com.company.core.process.dto.VendorSearchRequestBody;
import com.company.core.process.dto.VendorSearchResult;
import com.company.core.process.dto.VendorUpdateBasic;
import com.company.core.process.dto.VendorUpdateRequestBody;

import lombok.extern.slf4j.Slf4j;


@Repository
@Slf4j
public class VendorRepo {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * searchVendor is used to search Vendor
	 * 
	 * @param requestBody, limit, offset
	 * @return list
	 */
	public List<VendorSearchResult> searchVendor(VendorSearchRequestBody requestBody, Integer limit, Integer offset) {
		log.info("searchVendor method starts");
		
		StringBuilder strSql = new StringBuilder();
		strSql.append("SELECT COUNT(*) OVER() AS totalRecords, ");
		//Vendor
		strSql.append("v.VENDOR_ID as vendorId, v.VENDOR_CODE as vendorCode, v.VENDOR_NAME as vendorName, ");
		strSql.append("v.CONTACT_PERSON as contactPerson, v.PHONE_NUMBER as phoneNumber, v.EMAIL as email, v.ADDRESS as address, v.GST_NUMBER as gstNumber ");
		strSql.append(" FROM VENDOR v ");
		strSql.append(searchCondition(requestBody));
		strSql.append(" ORDER BY v.Vendor_CODE ");
		strSql.append(" LIMIT ");strSql.append(limit);
		strSql.append(" OFFSET ");strSql.append(offset);
//		strSql.append(" ROWS FETCH NEXT ");strSql.append(limit);strSql.append(" ROWS ONLY ");
		
		log.info("searchVendor method ends");
	    return jdbcTemplate.query(strSql.toString(), (rs, rowNum) -> getVendorMapper(rs, rowNum));
	}


	/**
	 * getVendorMapper is used to map the employee table to result
	 * 
	 * @param rs, rowNum
	 * @return VendorSearchResult
	 */
	private VendorSearchResult getVendorMapper(ResultSet rs, int rowNum) throws SQLException {
		
		VendorSearchResult result = new VendorSearchResult();
		
		result.setTotalRecords(rs.getInt("totalRecords"));
		
		Vendor Vendor = new Vendor();
		Vendor.setVendorId(rs.getInt("VendorId"));
		Vendor.setVendorCode(rs.getString("VendorCode"));
		Vendor.setVendorName(rs.getString("VendorName"));
		Vendor.setContactPerson(rs.getString("contactPerson"));
		Vendor.setPhoneNumber(rs.getString("phoneNumber"));
		Vendor.setEmail(rs.getString("email"));
		Vendor.setAddress(rs.getString("address"));
		Vendor.setGstNumber(rs.getString("gstNumber"));
		result.setVendor(Vendor);
		return result;
	}

	/**
	 * searchCondition is used to add the search condition
	 * 
	 * @param requestBody
	 * @return String
	 */
	private String searchCondition(VendorSearchRequestBody requestBody) {
		StringBuilder condition = new StringBuilder();
		condition.append(" WHERE 1 = 1 ");
		
		if(Objects.nonNull(requestBody.getPayload())) {
			if(Objects.nonNull(requestBody.getPayload().getVendorCode())) {
				condition.append(" AND UPPER(v.Vendor_CODE) LIKE '%");condition.append(requestBody.getPayload().getVendorCode());
				condition.append("%'");
			}
			if(StringUtils.isNotEmpty(requestBody.getPayload().getVendorName())) {
				condition.append(" AND UPPER(v.Vendor_NAME) LIKE '%");
				condition.append(requestBody.getPayload().getVendorName().toUpperCase());
				condition.append("%'");
			}
		}
		return condition.toString();
	}

	/**
	 * addVendor is used to add the Vendor details
	 * 
	 * @param VendorUpdateRequestBody
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addVendor(VendorUpdateRequestBody requestBody) {
		log.info("addVendor method starts");

		if (Objects.nonNull(requestBody.getPayload())
				&& Objects.nonNull(requestBody.getPayload().getVendors())) {

			List<VendorUpdateBasic> vendorList = requestBody.getPayload().getVendors();

			StringBuilder VendorSql = new StringBuilder();
			VendorSql.append("INSERT INTO VENDOR (VENDOR_CODE, VENDOR_NAME, CONTACT_PERSON, PHONE_NUMBER, EMAIL, ADDRESS, GST_NUMBER) ");
			VendorSql.append("VALUES(?, ?, ?, ?, ?, ?, ?)");

			for (VendorUpdateBasic vendors : vendorList) {

				jdbcTemplate.update(VendorSql.toString(),vendors.getVendor().getVendorCode(),vendors.getVendor().getVendorName(),
						vendors.getVendor().getContactPerson(), vendors.getVendor().getPhoneNumber(), vendors.getVendor().getEmail(),
						vendors.getVendor().getAddress(), vendors.getVendor().getGstNumber());
			}
		}
		log.info("addVendor method ends");
	}
	
	/**
	 * updateVendor is used to update the Vendor details
	 * 
	 * @param VendorUpdateRequestBody
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateVendor(VendorUpdateRequestBody requestBody) {
		log.info("updateVendor method starts");

		if (Objects.nonNull(requestBody.getPayload())
				&& Objects.nonNull(requestBody.getPayload().getVendors())) {

			List<VendorUpdateBasic> vendorsList = requestBody.getPayload().getVendors();

			StringBuilder vendorUpdateSql = new StringBuilder();
			vendorUpdateSql.append(" UPDATE VENDOR SET ");
			vendorUpdateSql.append(" VENDOR_NAME = ?, CONTACT_PERSON = ?, PHONE_NUMBER = ?, EMAIL = ?, ADDRESS = ?, GST_NUMBER = ? ");
			vendorUpdateSql.append(" WHERE VENDOR_ID = ? AND VENDOR_CODE = ? ");

			for (VendorUpdateBasic vendors : vendorsList) {
				jdbcTemplate.update(vendorUpdateSql.toString(),vendors.getVendor().getVendorName(),
						vendors.getVendor().getContactPerson(), vendors.getVendor().getPhoneNumber(), vendors.getVendor().getEmail(),
						vendors.getVendor().getAddress(), vendors.getVendor().getGstNumber(), 
						vendors.getVendor().getVendorId(), vendors.getVendor().getVendorCode());
			}
		}
		log.info("updateVendor method ends");
	}
	
	/**
	 * deleteVendor is used to delete the Vendors table
	 * 
	 * @param VendorUpdateRequestBody
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteVendor(VendorUpdateRequestBody requestBody) {
		log.info("deleteVendor method starts");

		if (Objects.nonNull(requestBody.getPayload()) && Objects.nonNull(requestBody.getPayload().getVendors())) {

			List<VendorUpdateBasic> vendorsList = requestBody.getPayload().getVendors();

			StringBuilder vendorDeleteSql = new StringBuilder();
			vendorDeleteSql.append(" DELETE FROM VENDOR ");
			vendorDeleteSql.append(" WHERE VENDOR_ID = ? AND VENDOR_CODE = ? ");

			for (VendorUpdateBasic vendor : vendorsList) {
				jdbcTemplate.update(vendorDeleteSql.toString(), vendor.getVendor().getVendorId(), vendor.getVendor().getVendorCode());
			}
		}
		log.info("deleteVendor method ends");
	}
}
