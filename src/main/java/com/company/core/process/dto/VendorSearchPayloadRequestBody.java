package com.company.core.process.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class VendorSearchPayloadRequestBody {
	
	@JsonProperty("vendorCode")
	public Integer vendorCode;
	
	@JsonProperty("vendorName")
	public String vendorName;
	
}
