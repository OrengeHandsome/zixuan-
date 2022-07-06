package com.zc.service;

import java.util.List;

import com.zc.entity.Major;


public interface IMajorService {
	List<Major> allMajor();
	
	String getNameById(int id);
	
	int getIdByName(String name);
}
