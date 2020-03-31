package com.example.demo.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.db.MemberDao;

import kr.co.youiwe.webservice.ServiceSMSSoapProxy;

@RestController
public class SmsController {
	@Autowired
	private MemberDao dao;
	public void setDao(MemberDao dao) {
		this.dao = dao;
	}
	Random rand = new Random();
	int randNum = rand.nextInt(8999)+1000;
	
	@RequestMapping("/cNumOk")
	public String certiNum(int telOk) {
		String str ="인증 실패";
		if(randNum == telOk) {
			str = "인증 완료";
		}
		System.out.println(str);
		return str;
	}
	
	@RequestMapping("/cNum")
	public String numRequest(String tel) {
		String id="";	//너나우리 아이디 비번 
		String pwd="";
		
		String str ="인증번호 전송";
		ServiceSMSSoapProxy sendsms = new ServiceSMSSoapProxy();
		try {
			String sender="";	//너나우리 문자 보낼 수 있는 발신번호
			String receiver=tel;
			String content ="[인증번호] "+randNum;
			String test1 = (id + pwd + receiver);
			CEncrypt encrypt = new CEncrypt("MD5", test1);
			java.lang.String send = sendsms.sendSMS(id, encrypt.getEncryptData(), sender, receiver, content);
			System.out.println("결과코드 : " + send);
		} catch (Exception e) {
			System.out.println(e.getMessage());// TODO: handle exception
		}
		return str;
	}
	
	
	class CEncrypt {
		MessageDigest md;
		String strSRCData = "";
		String strENCData = "";

		public CEncrypt() {
		}

		// 인스턴스 만들 때 한방에 처리할 수 있도록 생성자 중복시켰습니다.
		public CEncrypt(String EncMthd, String strData) {
			this.encrypt(EncMthd, strData);
		}

		// 암호화 절차를 수행하는 메소드입니다.
		public void encrypt(String EncMthd, String strData) {
			try {
				MessageDigest md = MessageDigest.getInstance(EncMthd); // "MD5" or "SHA1"
				byte[] bytData = strData.getBytes();
				md.update(bytData);

				byte[] digest = md.digest();
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < digest.length; i++) {
					strENCData = sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1)).toString();
				}
			} catch (NoSuchAlgorithmException e) {
				System.out.print("암호화 알고리즘이 없습니다.");
			}
			;

			// 나중에 원본 데이터가 필요할지 몰라서 저장해 둡니다.
			strSRCData = strData;
		}

		// 접근자 인라인 함수(아니, 메소드)들입니다.
		public String getEncryptData() {
			return strENCData;
		}

		public String getSourceData() {
			return strSRCData;
		}

		// 데이터가 같은지 비교해주는 메소드입니다.
		public boolean equal(String strData) {
			// 암호화 데이터랑 비교를 하던, 원본이랑 비교를 하던 맘대로....
			if (strData == strENCData)
				return true;
			return false;
		}
	}
}
