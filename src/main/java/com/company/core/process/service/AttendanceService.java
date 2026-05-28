package com.company.core.process.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.company.core.process.dto.AttendanceSearchRequestBody;
import com.company.core.process.dto.AttendanceSearchResponse;
import com.company.core.process.dto.AttendanceSearchResult;
import com.company.core.process.dto.AttendanceUpdateRequestBody;
import com.company.core.process.dto.SuccessResponse;
import com.company.core.process.repo.AttendanceRepo;

@Service
public class AttendanceService {

	@Autowired
	private AttendanceRepo attendanceRepo;
	
	/**
	 * searchAttendance is used to search the Attendance details
	 * 
	 * @param AttendanceSearchRequestBody, limit, offset
	 * @return AttendanceSearchResponse
	 */
	public AttendanceSearchResponse searchAttendance(AttendanceSearchRequestBody requestBody, Integer limit, Integer offset) {

		AttendanceSearchResponse response = new AttendanceSearchResponse();
		int count = 0;
		List<AttendanceSearchResult> attendancesList = attendanceRepo.searchAttendance(requestBody, limit, offset);
		
		if(!attendancesList.isEmpty()) {
			count = attendancesList.stream().findFirst().get().getTotalRecords();
		}
		response.setAttendance(attendancesList);
		response.setTotalRecords(count);
		return response;
	}
	/**
	 * addAttendance is used to add the Attendance and address table
	 * 
	 * @param AttendanceUpdateRequestBody
	 * @return SuccessResponse
	 */
	public SuccessResponse addAttendance(AttendanceUpdateRequestBody requestBody) {
		SuccessResponse successResponse = new SuccessResponse();
		
		attendanceRepo.addAttendance(requestBody);
		
		successResponse.setStatus(HttpStatus.CREATED.value());
		successResponse.setMessage(HttpStatus.CREATED.getReasonPhrase());
		successResponse.setTimestamp(LocalDateTime.now());
		return successResponse;
	}
	/**
	 * updateAttendance is used to update the Attendance and address table
	 * 
	 * @param AttendanceUpdateRequestBody
	 * @return SuccessResponse
	 */
	public SuccessResponse updateAttendance(AttendanceUpdateRequestBody requestBody) {
		SuccessResponse successResponse = new SuccessResponse();
		
		attendanceRepo.updateAttendance(requestBody);
		
		successResponse.setStatus(HttpStatus.OK.value());
		successResponse.setMessage(HttpStatus.OK.getReasonPhrase());
		successResponse.setTimestamp(LocalDateTime.now());
		return successResponse;
	}
	/**
	 * deleteAttendance is used to delete the Attendance and address table
	 * 
	 * @param AttendanceUpdateRequestBody
	 * @return SuccessResponse
	 */
	public SuccessResponse deleteAttendance(AttendanceUpdateRequestBody requestBody) {
		SuccessResponse successResponse = new SuccessResponse();
		
		attendanceRepo.deleteAttendance(requestBody);
		
		successResponse.setStatus(HttpStatus.OK.value());
		successResponse.setMessage(HttpStatus.OK.getReasonPhrase());
		successResponse.setTimestamp(LocalDateTime.now());
		return successResponse;
	}
	

}