package com.company.core.process.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Purchase {

	@JsonProperty("purchaseId")
	public Integer purchaseId;
	
	@JsonProperty("purchaseNumber")
	public String purchaseNumber;
	
	@JsonProperty("projectId")
	public Integer projectId;
	
	@JsonProperty("supplierId")
	public Integer supplierId;
	
	@JsonProperty("purchaseDate")
	public Integer purchaseDate;
	
	@JsonProperty("invoiceNumber")
	public Integer invoiceNumber;
	
	@JsonProperty("totalAmount")
	public BigDecimal totalAmount  = BigDecimal.ZERO;
	
	@JsonProperty("paidAmount")
	public BigDecimal paidAmount  = BigDecimal.ZERO;
	
	@JsonProperty("balanceAmount")
	public BigDecimal balanceAmount  = BigDecimal.ZERO;
	
	@JsonProperty("paymentStatus")
	public String paymentStatus;
	
	@JsonProperty("remarks")
	public String remarks;
	
}
