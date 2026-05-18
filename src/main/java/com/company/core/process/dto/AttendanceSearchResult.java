package com.company.core.process.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AttendanceSearchResult {
	
	@JsonProperty("projectId")
	public Integer projectId;
	
	@JsonProperty("projectName")
	public String projectName;

	@JsonProperty("employeeId")
	public Integer employeeId;
	
	@JsonProperty("employeeName")
	public String employeeName;
	
	@JsonProperty("workDate")
	public Integer workDate;
	
}
