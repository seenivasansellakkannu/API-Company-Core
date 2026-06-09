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

import com.company.core.process.dto.MaterialMaster;
import com.company.core.process.dto.Project;
import com.company.core.process.dto.Purchase;
import com.company.core.process.dto.PurchaseDetails;
import com.company.core.process.dto.PurchaseSearchRequestBody;
import com.company.core.process.dto.PurchaseSearchResult;
import com.company.core.process.dto.PurchaseUpdateBasic;
import com.company.core.process.dto.PurchaseUpdateRequestBody;
import com.company.core.process.dto.Supplier;

import lombok.extern.slf4j.Slf4j;


@Repository
@Slf4j
public class PurchaseRepo {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * searchPurchase is used to search Purchase
	 * 
	 * @param requestBody, limit, offset
	 * @return list
	 */
	public List<PurchaseSearchResult> searchPurchase(PurchaseSearchRequestBody requestBody, Integer limit, Integer offset) {
		log.info("searchPurchase method starts");
		
		StringBuilder strSql = new StringBuilder();
		strSql.append("SELECT COUNT(*) OVER() AS totalRecords, ");
		//Purchase
		strSql.append("p.PURCHASE_ID as purchaseId, p.PURCHASE_NUMBER as purchaseNumber, p.PROJECT_ID as projectId, ");
		strSql.append("p.SUPPLIER_ID as supplierId, p.PURCHASE_DATE as purchaseDate, p.INVOICE_NUMBER as invoiceNumber, p.TOTAL_AMOUNT as totalAmount, ");
		strSql.append("p.PAID_AMOUNT as paidAmount, p.BALANCE_AMOUNT as balanceAmount, p.PAYMENT_STATUS as paymentStatus, p.REMARKS as remarks, ");
		//purchase_details
		strSql.append("d.PURCHASE_DETAILS_ID as purchaseDetailsId, d.MATERIAL_ID as materialId, d.QUANTITY as quantity, d.RATE as rate, d.AMOUNT as amount, ");
		//material_master
		strSql.append("m.MATERIAL_CODE as materialCode, m.MATERIAL_NAME as materialName, m.MATERIAL_TYPE as materialType, m.UNIT as unit, ");
		//project
		strSql.append("pr.PROJECT_CODE as projectCode, pr.PROJECT_NAME as projectName, pr.ADDRESS_ID as addressId, ");
		strSql.append("pr.CLIENT_NAME as clientName, pr.TOTAL_SQUARE_FEET as totalSquareFeet, pr.SQUARE_FEET_RATE as squareFeetRate, pr.ESTIMATED_AMOUNT as estimatedAmount, ");
		//Supplier
		strSql.append("s.SUPPLIER_ID as supplierIds, s.SUPPLIER_CODE as supplierCode, s.SUPPLIER_NAME as supplierName, ");
		strSql.append("s.CONTACT_PERSON as contactPerson, s.PHONE_NUMBER as phoneNumber, s.EMAIL as email, s.ADDRESS as address, s.GST_NUMBER as gstNumber ");
		
		strSql.append(" FROM PURCHASE p LEFT JOIN PURCHASE_DETAILS d ON s.PURCHASE_ID = d.PURCHASE_ID ");
		strSql.append(" LEFT JOIN MATERIAL_MASTER m ON s.MATERIAL_ID = m.MATERIAL_ID ");
		strSql.append(" LEFT JOIN PROJECT pr ON pr.PROJECT_ID = p.PROJECT_ID ");
		strSql.append(" LEFT JOIN SUPPLIER s ON s.SUPPLIER_ID = p.SUPPLIER_ID ");
		strSql.append(searchCondition(requestBody));
		strSql.append(" ORDER BY s.PURCHASE_CODE ");
		strSql.append(" LIMIT ");strSql.append(limit);
		strSql.append(" OFFSET ");strSql.append(offset);
//		strSql.append(" ROWS FETCH NEXT ");strSql.append(limit);strSql.append(" ROWS ONLY ");
		
		log.info("searchPurchase method ends");
	    return jdbcTemplate.query(strSql.toString(), (rs, rowNum) -> getPurchaseMapper(rs, rowNum));
	}


	/**
	 * getPurchaseMapper is used to map the employee table to result
	 * 
	 * @param rs, rowNum
	 * @return PurchaseSearchResult
	 */
	private PurchaseSearchResult getPurchaseMapper(ResultSet rs, int rowNum) throws SQLException {
		
		PurchaseSearchResult result = new PurchaseSearchResult();
		
		result.setTotalRecords(rs.getInt("totalRecords"));
		
		Purchase purchase = new Purchase();
		purchase.setPurchaseId(rs.getInt("purchaseId"));
		purchase.setPurchaseNumber(rs.getString("purchaseNumber"));
		purchase.setProjectId(rs.getInt("projectId"));
		purchase.setSupplierId(rs.getInt("supplierId"));
		purchase.setPurchaseDate(rs.getInt("purchaseDate"));
		purchase.setInvoiceNumber(rs.getInt("invoiceNumber"));
		purchase.setTotalAmount(rs.getBigDecimal("totalAmount"));
		purchase.setPaidAmount(rs.getBigDecimal("paidAmount"));
		purchase.setBalanceAmount(rs.getBigDecimal("balanceAmount"));
		purchase.setPaymentStatus(rs.getString("paymentStatus"));
		purchase.setRemarks(rs.getString("remarks"));
		
		PurchaseDetails purchaseDetails = new PurchaseDetails();
		purchaseDetails.setPurchaseId(rs.getInt("purchaseId"));
		purchaseDetails.setMaterialId(rs.getInt("materialId"));
		purchaseDetails.setPurchaseDetailsId(rs.getInt("purchaseDetailsId"));
		purchaseDetails.setAmount(rs.getBigDecimal("amount"));
		purchaseDetails.setQuantity(rs.getBigDecimal("quantity"));
		purchaseDetails.setRate(rs.getBigDecimal("rate"));
		
		MaterialMaster material = new MaterialMaster();
		material.setMaterialId(rs.getInt("materialId"));
		material.setMaterialCode(rs.getString("materialCode"));
		material.setMaterialType(rs.getString("materialType"));
		material.setUnit(rs.getString("unit"));
		
		Project project = new Project();
		project.setProjectId(rs.getInt("projectId"));
		project.setProjectCode(rs.getString("projectCode"));
		project.setProjectName(rs.getString("projectName"));
		project.setAddressId(rs.getInt("addressId"));
		project.setClientName(rs.getString("clientName"));
		project.setTotalSquareFeet(rs.getBigDecimal("totalSquareFeet"));
		project.setSquareFeetRate(rs.getBigDecimal("squareFeetRate"));
		project.setEstimatedAmount(rs.getBigDecimal("estimatedAmount"));
		
		Supplier supplier = new Supplier();
		supplier.setSupplierId(rs.getInt("supplierId"));
		supplier.setSupplierCode(rs.getString("supplierCode"));
		supplier.setSupplierName(rs.getString("supplierName"));
		supplier.setContactPerson(rs.getString("contactPerson"));
		supplier.setPhoneNumber(rs.getString("phoneNumber"));
		supplier.setEmail(rs.getString("email"));
		supplier.setAddress(rs.getString("address"));
		supplier.setGstNumber(rs.getString("gstNumber"));
		
		result.setPurchase(purchase);
		result.setDetails(purchaseDetails);
		result.setMaterial(material);
		result.setProject(project);
		result.setSupplier(supplier);
		return result;
	}

	/**
	 * searchCondition is used to add the search condition
	 * 
	 * @param requestBody
	 * @return String
	 */
	private String searchCondition(PurchaseSearchRequestBody requestBody) {
		StringBuilder condition = new StringBuilder();
		condition.append(" WHERE 1 = 1 ");
		
		if(Objects.nonNull(requestBody.getPayload())) {
			if(Objects.nonNull(requestBody.getPayload().getPurchaseNumber())) {
				condition.append(" AND p.PURCHASE_NUMBER =");
				condition.append(requestBody.getPayload().getPurchaseNumber());
				condition.append("");
			}
			if(StringUtils.isNotEmpty(requestBody.getPayload().getProjectId())) {
				condition.append(" AND p.PROJECT_ID = ");
				condition.append(requestBody.getPayload().getProjectId());
				condition.append("");
			}
			if(StringUtils.isNotEmpty(requestBody.getPayload().getProjectCode())) {
				condition.append(" AND pr.PROJECT_CODE = ");
				condition.append(requestBody.getPayload().getProjectCode());
				condition.append("");
			}
			if(StringUtils.isNotEmpty(requestBody.getPayload().getProjectName())) {
				condition.append(" AND pr.PROJECT_NAME = ");
				condition.append(requestBody.getPayload().getProjectName());
				condition.append("");
			}
			if(StringUtils.isNotEmpty(requestBody.getPayload().getSupplierId())) {
				condition.append(" AND p.SUPPLIER_ID = ");
				condition.append(requestBody.getPayload().getSupplierId());
				condition.append("");
			}
			if(StringUtils.isNotEmpty(requestBody.getPayload().getPurchaseDate())) {
				condition.append(" AND p.PURCHASE_DATE = ");
				condition.append(requestBody.getPayload().getPurchaseDate());
				condition.append("");
			}
			if(Objects.nonNull(requestBody.getPayload().getMaterialId())) {
				condition.append(" AND m.MATERIAL_ID = ");
				condition.append(requestBody.getPayload().getMaterialId());
				condition.append("");
			}
			if(StringUtils.isNotEmpty(requestBody.getPayload().getMaterialCode())) {
				condition.append(" AND m.MATERIAL_CODE = ");
				condition.append(requestBody.getPayload().getMaterialCode());
				condition.append("");
			}
			if(StringUtils.isNotEmpty(requestBody.getPayload().getMaterialName())) {
				condition.append(" AND m.MATERIAL_NAME = ");
				condition.append(requestBody.getPayload().getMaterialName());
				condition.append("");
			}
		}
		return condition.toString();
	}

	/**
	 * addPurchase is used to add the Purchase details
	 * 
	 * @param PurchaseUpdateRequestBody
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addPurchase(PurchaseUpdateRequestBody requestBody) {
		log.info("addPurchase method starts");

		if (Objects.nonNull(requestBody.getPayload())
				&& Objects.nonNull(requestBody.getPayload().getPurchases())) {

			List<PurchaseUpdateBasic> purchaseList = requestBody.getPayload().getPurchases();

			StringBuilder purchaseSql = new StringBuilder();
			purchaseSql.append("INSERT INTO PURCHASE (PURCHASE_NUMBER, PROJECT_ID, SUPPLIER_ID, PURCHASE_DATE, INVOICE_NUMBER, TOTAL_AMOUNT, PAID_AMOUNT, BALANCE_AMOUNT, PAYMENT_STATUS, REMARKS) ");
			purchaseSql.append("VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			StringBuilder purchaseDetailsSql = new StringBuilder();
			purchaseDetailsSql.append("INSERT INTO PURCHASE_DETAILS (PURCHASE_ID, MATERIAL_ID, QUANTITY, RATE, AMOUNT) ");
			purchaseDetailsSql.append("VALUES(?, ?, ?, ?, ?)");

			for (PurchaseUpdateBasic purchaseUpdateBasic : purchaseList) {
				Purchase purchase = purchaseUpdateBasic.getPurchase();
				PurchaseDetails details = purchaseUpdateBasic.getDetails();
				
				GeneratedKeyHolder purchaseKeyHolder = new GeneratedKeyHolder();

				jdbcTemplate.update(connection -> {
					PreparedStatement ps = connection.prepareStatement(purchaseSql.toString(),
							new String[] { "purchase_id" });
					ps.setString(1, purchase.getPurchaseNumber());
					ps.setInt(2, purchase.getProjectId());
					ps.setInt(3, purchase.getSupplierId());
					ps.setInt(4, purchase.getPurchaseDate());
					ps.setInt(5, purchase.getInvoiceNumber());
					ps.setBigDecimal(6, purchase.getTotalAmount());
					ps.setBigDecimal(7, purchase.getPaidAmount());
					ps.setBigDecimal(8, purchase.getBalanceAmount());
					ps.setString(9, purchase.getPaymentStatus());
					ps.setString(10, purchase.getRemarks());
					return ps;
				}, purchaseKeyHolder);

				int purchaseId = purchaseKeyHolder.getKey().intValue();

				jdbcTemplate.update(purchaseDetailsSql.toString(), purchaseId, details.getMaterialId(), details.getQuantity(), details.getRate(), details.getAmount());
			}
		}
		log.info("addPurchase method ends");
	}
	
	/**
	 * updatePurchase is used to update the Purchase details
	 * 
	 * @param PurchaseUpdateRequestBody
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updatePurchase(PurchaseUpdateRequestBody requestBody) {
		log.info("updatePurchase method starts");

		if (Objects.nonNull(requestBody.getPayload())
				&& Objects.nonNull(requestBody.getPayload().getPurchases())) {

			List<PurchaseUpdateBasic> purchasesList = requestBody.getPayload().getPurchases();

			StringBuilder purchaseUpdateSql = new StringBuilder();
			purchaseUpdateSql.append(" UPDATE PURCHASE SET ");
			purchaseUpdateSql.append(" PURCHASE_DATE = ?, INVOICE_NUMBER = ?, TOTAL_AMOUNT = ?, PAID_AMOUNT = ?, BALANCE_AMOUNT = ?, PAYMENT_STATUS = ?, REMARKS = ?, UPDATED_AT = CURRENT_TIMESTAMP ");
			purchaseUpdateSql.append(" WHERE PURCHASE_ID = ? ");
			
			StringBuilder purchaseDetailsUpdateSql = new StringBuilder();
			purchaseDetailsUpdateSql.append(" UPDATE PURCHASE_DETAILS SET ");
			purchaseDetailsUpdateSql.append(" QUANTITY = ?, RATE = ?, AMOUNT = ?");
			purchaseDetailsUpdateSql.append(" WHERE PURCHASE_DETAILS_ID = ? ");

			for (PurchaseUpdateBasic purchaseUpdateBasic : purchasesList) {
				Purchase purchase = purchaseUpdateBasic.getPurchase();
				PurchaseDetails details = purchaseUpdateBasic.getDetails();
				
				jdbcTemplate.update(purchaseUpdateSql.toString(),purchase.getPurchaseDate(),
						purchase.getInvoiceNumber(), purchase.getTotalAmount(), purchase.getPaidAmount(), purchase.getBalanceAmount(), purchase.getPaymentStatus(), purchase.getRemarks());
				
				jdbcTemplate.update(purchaseDetailsUpdateSql.toString(),details.getQuantity(),details.getRate(),details.getAmount(),details.getPurchaseDetailsId());
			}
		}
		log.info("updatePurchase method ends");
	}
	
	/**
	 * deletePurchase is used to delete the Purchases table
	 * 
	 * @param PurchaseUpdateRequestBody
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deletePurchase(PurchaseUpdateRequestBody requestBody) {
		log.info("deletePurchase method starts");

		if (Objects.nonNull(requestBody.getPayload()) && Objects.nonNull(requestBody.getPayload().getPurchases())) {

			List<PurchaseUpdateBasic> purchasesList = requestBody.getPayload().getPurchases();

			StringBuilder purchaseDeleteSql = new StringBuilder();
			StringBuilder purchaseDetailsDeleteSql = new StringBuilder();
			purchaseDeleteSql.append(" DELETE FROM PURCHASE WHERE PURCHASE_ID = ? ");
			purchaseDetailsDeleteSql.append(" DELETE FROM PURCHASE_DETAILS WHERE PURCHASE_DETAILS_ID = ? ");

			for (PurchaseUpdateBasic purchaseUpdateBasic : purchasesList) {
				Purchase purchase = purchaseUpdateBasic.getPurchase();
				PurchaseDetails details = purchaseUpdateBasic.getDetails();
				
				jdbcTemplate.update(purchaseDeleteSql.toString(), purchase.getPurchaseId());
				jdbcTemplate.update(purchaseDetailsDeleteSql.toString(), details.getPurchaseDetailsId());
			}
		}
		log.info("deletePurchase method ends");
	}
}
