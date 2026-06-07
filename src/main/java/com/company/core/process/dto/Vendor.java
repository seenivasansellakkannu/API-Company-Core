package com.company.core.process.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString
public class Vendor {

	@JsonProperty("vendorId")
	public Integer vendorId;

	@JsonProperty("vendorCode")
	public String vendorCode;

	@JsonProperty("vendorName")
	public String vendorName;
	
	@JsonProperty("contactPerson")
	public String contactPerson;
	
	@JsonProperty("phoneNumber")
	public String phoneNumber;
	
	@JsonProperty("email")
	public String email;
	
	@JsonProperty("address")
	public String address;
	
	@JsonProperty("gstNumber")
	public String gstNumber;


}
