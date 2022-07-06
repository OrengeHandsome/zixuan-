package com.zc.dao;

import java.util.List;

import com.zc.entity.Doubt;

/**
 * 
 * 添加学生提出的疑惑
 * 显示某一学生的所有疑惑
 *
 * 更新疑惑信息
 *
 */
public interface IDoubtDao {
	
	int addDoubt(Doubt doubt);
	List<Doubt> getAllDoubt(int studentId);
	
	int updateDoubt(Doubt doubt);
	
	
}
