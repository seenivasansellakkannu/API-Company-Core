package com.company.core.process.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString
public class MaterialMaster {

	@JsonProperty("materialId")
	public Integer materialId;

	@JsonProperty("materialCode")
	public String materialCode;

	@JsonProperty("materialName")
	public String materialName;
	
	@JsonProperty("materialType")
	public String materialType;
	
	@JsonProperty("unit")
	public String unit;


}
