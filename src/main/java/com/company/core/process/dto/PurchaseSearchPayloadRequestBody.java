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
	
	@JsonProperty("projectCode")
	public String projectCode;
	
	@JsonProperty("projectName")
	public String projectName;
	
	@JsonProperty("supplierId")
	public String supplierId;
	
	@JsonProperty("purchaseDate")
	public String purchaseDate;
	
	@JsonProperty("materialId")
	public Integer materialId;

	@JsonProperty("materialCode")
	public String materialCode;

	@JsonProperty("materialName")
	public String materialName;
	
	@JsonProperty("materialType")
	public String materialType;
	
	
}
