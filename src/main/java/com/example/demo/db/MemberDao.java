package com.example.demo.db;

import org.springframework.stereotype.Repository;

import com.example.demo.vo.MemberVo;
@Repository
public class MemberDao {
	public int insertMember(MemberVo v) {
		return MemberManager.insertMember(v);
	}
}
