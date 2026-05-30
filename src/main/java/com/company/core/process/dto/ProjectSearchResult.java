package com.company.core.process.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ProjectSearchResult {
	
	@JsonProperty("totalRecords")
	@JsonIgnore
	public Integer totalRecords;
	
	@JsonProperty("project")
	public Project project;
	
	@JsonProperty("address")
	public Address address;
	
}
