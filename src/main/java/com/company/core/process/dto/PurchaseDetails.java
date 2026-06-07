package com.company.core.process.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PurchaseDetails {

	@JsonProperty("purchaseDetailsId")
	public Integer purchaseDetailsId;
	
	@JsonProperty("purchaseId")
	public Integer purchaseId;
	
	@JsonProperty("materialId")
	public Integer materialId;
	
	@JsonProperty("quantity")
	public BigDecimal quantity;
	
	@JsonProperty("rate")
	public BigDecimal rate;
	
	@JsonProperty("amount")
	public BigDecimal amount;
	
}
