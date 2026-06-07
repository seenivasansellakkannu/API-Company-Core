package com.company.core.process.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PurchaseSearchPayloadRequestBody {
	
	@JsonProperty("purchaseNumber")
	public Integer purchaseNumber;
	
	@JsonProperty("projectId")
	public String projectId;
	
	@JsonProperty("supplierId")
	public String supplierId;
	
	@JsonProperty("purchaseDate")
	public String purchaseDate;
	
}
