package com.company.core.process.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Data
public class ProjectSearchResponse {
	
	@JsonProperty("projects")
	public List<ProjectSearchResult> projects;
	
	@JsonProperty("totalRecords")
	public Integer totalRecords;
	
}
