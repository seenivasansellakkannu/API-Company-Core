package com.company.core.process.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Attendance {
	
	@JsonProperty("attendanceId")
	public Integer attendanceId;
	
	@JsonProperty("projectId")
	public Integer projectId;

	@JsonProperty("employeeId")
	public Integer employeeId;
	
	@JsonProperty("workDate")
	public Integer workDate;
	
	@JsonProperty("workdays")
	public BigDecimal workdays;
	
	
}
