package com.company.core.process.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EmployeeUpdateRequestBody {
	
	@JsonProperty("payload")
	public EmployeeUpdatePayloadRequestBody payload;
	
}
