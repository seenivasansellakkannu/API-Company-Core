package com.company.core.process.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ExpenseSearchPayloadRequestBody {
	
	@JsonProperty("expenseId")
	public Integer expenseId;

	@JsonProperty("expenseCode")
	public String expenseCode;

	@JsonProperty("expenseName")
	public String expenseName;
	
	@JsonProperty("expenseType")
	public String expenseType;
	
	@JsonProperty("description")
	public String description;
	
}
