package com.zc.dao;

import java.util.List;

import com.zc.entity.Major;



public interface IMajorDao {
	
	List<Major> getAllMajor();
	
	String getNameByID(int id);

	Integer getIdByName(String name);
	
	
}
