package com.example.demo.db;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.example.demo.vo.MemberVo;

public class MemberManager {
	private static SqlSessionFactory factory;
		
		static {
			try {
				Reader reader = Resources.getResourceAsReader("com/example/demo/db/sqlMapConfig.xml");
				factory = new SqlSessionFactoryBuilder().build(reader);
				reader.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				// TODO: handle exception
			}
		}
		
		public static int insertMember(MemberVo v) {
			SqlSession session = factory.openSession();
			int re = session.insert("member.insertMember", v);
			session.commit();
			session.close();
			return re;
		}
}
