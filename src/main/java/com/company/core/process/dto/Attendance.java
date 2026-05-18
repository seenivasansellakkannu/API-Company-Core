package com.company.core.process.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Attendance {
	
	@JsonProperty("attendanceId")
	@JsonIgnore
	public Integer attendanceId;
	
	@JsonProperty("projectId")
	public Integer projectId;

	@JsonProperty("employeeId")
	public Integer employeeId;
	
	@JsonProperty("workDate")
	public Integer workDate;
	
	@JsonProperty("workdays")
	public Integer workdays;
	
	
}
