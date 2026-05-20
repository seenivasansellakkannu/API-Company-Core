package com.company.core.process.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Project {

	@JsonProperty("projectId")
	public Integer projectId;
	
	@JsonProperty("projectCode")
	public String projectCode;
	
	@JsonProperty("projectName")
	public String projectName;
	
	@JsonProperty("addressId")
	public String addressId;
	
}
