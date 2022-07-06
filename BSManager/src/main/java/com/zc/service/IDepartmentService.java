package com.zc.service;

import java.util.List;

import com.zc.entity.Department;



public interface IDepartmentService {
	List<Department> allDepartment();
	
	String getNameById(int id);
	
	int getIdByName(String departmentName);
}
