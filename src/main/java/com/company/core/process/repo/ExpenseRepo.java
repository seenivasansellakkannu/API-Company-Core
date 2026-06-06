package com.company.core.process.repo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.company.core.process.dto.ExpenseMaster;
import com.company.core.process.dto.ExpenseSearchRequestBody;
import com.company.core.process.dto.ExpenseSearchResult;
import com.company.core.process.dto.ExpenseUpdateBasic;
import com.company.core.process.dto.ExpenseUpdateRequestBody;
import com.company.core.process.dto.Employee;
import com.company.core.process.dto.Project;
import com.company.core.process.dto.Salary;

import lombok.extern.slf4j.Slf4j;


@Repository
@Slf4j
public class ExpenseRepo {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * searchEmployee is used to search employee
	 * 
	 * @param requestBody, limit, offset
	 * @return list
	 */
	public List<ExpenseSearchResult> searchExpense(ExpenseSearchRequestBody requestBody, Integer limit, Integer offset) {
		log.info("searchExpense method starts");
		
		StringBuilder strSql = new StringBuilder();
		strSql.append("SELECT COUNT(*) OVER() AS totalRecords, ");
		//Expense
		strSql.append("e.EXPENSE_ID as expenseId, e.EXPENSE_CODE as expenseCode, e.EXPENSE_NAME as expenseName, ");
		strSql.append("e.EXPENSE_TYPE as expenseType, e.DESCRIPTION as description");
		strSql.append(" FROM EXPENSE_MASTER e ");
		strSql.append(searchCondition(requestBody));
		strSql.append(" ORDER BY e.EXPENSE_CODE ");
		strSql.append(" LIMIT ");strSql.append(limit);
		strSql.append(" OFFSET ");strSql.append(offset);
//		strSql.append(" ROWS FETCH NEXT ");strSql.append(limit);strSql.append(" ROWS ONLY ");
		
		log.info("searchExpense method ends");
	    return jdbcTemplate.query(strSql.toString(), (rs, rowNum) -> getExpenseMapper(rs, rowNum));
	}


	/**
	 * getExpenseMapper is used to map the employee table to result
	 * 
	 * @param rs, rowNum
	 * @return ExpenseSearchResult
	 */
	private ExpenseSearchResult getExpenseMapper(ResultSet rs, int rowNum) throws SQLException {
		
		ExpenseSearchResult result = new ExpenseSearchResult();
		
		result.setTotalRecords(rs.getInt("totalRecords"));
		
		ExpenseMaster Expense = new ExpenseMaster();
		Expense.setExpenseId(rs.getInt("expenseId"));
		Expense.setExpenseCode(rs.getString("expenseCode"));
		Expense.setExpenseType(rs.getString("expenseType"));
		Expense.setDescription(rs.getString("description"));
		
		result.setExpense(Expense);
		return result;
	}

	/**
	 * searchCondition is used to add the search condition
	 * 
	 * @param requestBody
	 * @return String
	 */
	private String searchCondition(ExpenseSearchRequestBody requestBody) {
		StringBuilder condition = new StringBuilder();
		condition.append(" WHERE 1 = 1 ");
		
		if(Objects.nonNull(requestBody.getPayload())) {
			if(Objects.nonNull(requestBody.getPayload().getExpenseCode())) {
				condition.append(" AND UPPER(e.EXPENSE_CODE) LIKE '%");condition.append(requestBody.getPayload().getExpenseCode());
				condition.append("%'");
			}
			if(StringUtils.isNotEmpty(requestBody.getPayload().getExpenseName())) {
				condition.append(" AND UPPER(e.EXPENSE_NAME) LIKE '%");
				condition.append(requestBody.getPayload().getExpenseName().toUpperCase());
				condition.append("%'");
			}
			if(Objects.nonNull(requestBody.getPayload().getExpenseType())) {
				condition.append(" AND e.EXPENSE_TYPE = '");
				condition.append(requestBody.getPayload().getExpenseType());
				condition.append("'");
			}
		}
		return condition.toString();
	}

	/**
	 * addExpense is used to add the Expense details
	 * 
	 * @param EmployeeUpdateRequestBody
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addExpense(ExpenseUpdateRequestBody requestBody) {
		log.info("addExpense method starts");

		if (Objects.nonNull(requestBody.getPayload())
				&& Objects.nonNull(requestBody.getPayload().getExpenses())) {

			List<ExpenseUpdateBasic> expenseList = requestBody.getPayload().getExpenses();

			StringBuilder ExpenseSql = new StringBuilder();
			ExpenseSql.append("INSERT INTO EXPENSE_MASTER (EXPENSE_CODE, EXPENSE_NAME, EXPENSE_TYPE, DESCRIPTION) ");
			ExpenseSql.append("VALUES(?, ?, ?, ?)");

			for (ExpenseUpdateBasic expenses : expenseList) {

				jdbcTemplate.update(ExpenseSql.toString(),expenses.getExpense().getExpenseCode(),expenses.getExpense().getExpenseName(),
						expenses.getExpense().getExpenseType(),expenses.getExpense().getDescription());

			}
		}
		log.info("addExpense method ends");
	}
	
	/**
	 * updateExpense is used to update the Expense details
	 * 
	 * @param ExpenseUpdateRequestBody
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateExpense(ExpenseUpdateRequestBody requestBody) {
		log.info("updateExpense method starts");

		if (Objects.nonNull(requestBody.getPayload())
				&& Objects.nonNull(requestBody.getPayload().getExpenses())) {

			List<ExpenseUpdateBasic> ExpensesList = requestBody.getPayload().getExpenses();

			StringBuilder ExpenseUpdateSql = new StringBuilder();
			ExpenseUpdateSql.append(" UPDATE EXPENSE_MASTER SET ");
			ExpenseUpdateSql.append(" EXPENSE_NAME = ?, EXPENSE_TYPE = ?, DESCRIPTION = ? ");
			ExpenseUpdateSql.append(" WHERE EXPENSE_ID = ? AND EXPENSE_CODE = ? ");

			for (ExpenseUpdateBasic Expense : ExpensesList) {
				jdbcTemplate.update(ExpenseUpdateSql.toString(), Expense.getExpense().getExpenseName(), Expense.getExpense().getExpenseType(), 
						Expense.getExpense().getDescription(), Expense.getExpense().getExpenseId(), Expense.getExpense().getExpenseCode());
			}
		}
		log.info("updateExpense method ends");
	}
	
	/**
	 * deleteExpense is used to delete the Expenses table
	 * 
	 * @param ExpenseUpdateRequestBody
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteExpense(ExpenseUpdateRequestBody requestBody) {
		log.info("deleteExpense method starts");

		if (Objects.nonNull(requestBody.getPayload()) && Objects.nonNull(requestBody.getPayload().getExpenses())) {

			List<ExpenseUpdateBasic> expensesList = requestBody.getPayload().getExpenses();

			StringBuilder expenseDeleteSql = new StringBuilder();
			expenseDeleteSql.append(" DELETE FROM EXPENSE_MASTER ");
			expenseDeleteSql.append(" WHERE EXPENSE_ID = ? AND EXPENSE_CODE = ? ");

			for (ExpenseUpdateBasic expense : expensesList) {
				jdbcTemplate.update(expenseDeleteSql.toString(), expense.getExpense().getExpenseId(), expense.getExpense().getExpenseCode());
			}
		}
		log.info("deleteExpense method ends");
	}
}
