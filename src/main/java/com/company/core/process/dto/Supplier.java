package com.company.core.process.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString
public class Supplier {

	@JsonProperty("supplierId")
	public Integer supplierId;

	@JsonProperty("supplierCode")
	public String supplierCode;

	@JsonProperty("supplierName")
	public String supplierName;
	
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
