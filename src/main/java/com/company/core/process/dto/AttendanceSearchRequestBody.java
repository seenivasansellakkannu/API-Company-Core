package com.company.core.process.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AttendanceSearchRequestBody {
	
	@JsonProperty("payload")
	public AttendanceSearchPayloadRequestBody payload;
	
}
