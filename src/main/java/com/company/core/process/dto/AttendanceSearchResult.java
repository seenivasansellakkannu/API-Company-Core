package com.company.core.process.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AttendanceSearchResult {
	
	@JsonProperty("totalRecords")
	@JsonIgnore
	public Integer totalRecords;
	
	@JsonProperty("employee")
	public Employee employee;
	
	@JsonProperty("attendance")
	public Attendance attendance;
	
	@JsonProperty("salary")
	public Salary salary;
	
	@JsonProperty("project")
	public Project project;
	
}
