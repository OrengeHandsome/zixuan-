package com.zc.dao;

import java.util.List;

import com.zc.entity.Department;

/**
 * 查询全部的院系
 * id ==> name
 * name ==> id
 * 
 */
public interface IDepartmentDao {
	
	List<Department> allDepartment();
	String getNameById(int id);
	Integer getIdByName(String departmentNameOld);
}
