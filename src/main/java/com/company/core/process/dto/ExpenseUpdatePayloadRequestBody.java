package com.company.core.process.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ExpenseUpdatePayloadRequestBody {
	
	@JsonProperty("expenses")
	public List<ExpenseUpdateBasic> expenses;
	
}
