package com.yjc.stk.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yjc.stk.domain.MemberVO;
import com.yjc.stk.service.MemberService;

import lombok.AllArgsConstructor;
import net.sf.json.JSONObject;

/**
 * Handles requests for the application home page.
 */
@Controller
@AllArgsConstructor
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private MemberService memservice;
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model, @CookieValue(value="loginCookie", required = false) Cookie loginCookie) {
		logger.info("Welcome home! The client locale is {}.", locale);
		MemberVO loginInfo = new MemberVO();
		if (loginCookie != null){
			loginInfo.setMember_id(loginCookie.getValue());
		}
		model.addAttribute("loginCookie",loginInfo);
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		//model.addAttribute("memlist",memservice.getMemberList());
		
		return "home";
	}
	
	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestBody MemberVO mvo,HttpServletResponse response) {
		//로그인 하기 위한 객체생성
		
		String member_pw = mvo.getMember_pw();
		MemberVO resultVO = new MemberVO();
		//결과 비교용 객체(아이디가 같은것만 찾는다==> 비밀번호 판독을 위해)
		resultVO =memservice.login(mvo);
		System.out.println(mvo.getMember_id() +" <-입력 || 결과 -> " + resultVO.getMember_id());
		System.out.println(mvo.getMember_pw() +" <-입력 || 결과 -> " + resultVO.getMember_pw());
		//결과 전송용 JSON객체 생성
		JSONObject resultobj = new JSONObject();
		
		//로그인 결과 판독
		if(resultVO==null) {
			//아이디가 없거나 아이디 오류
			resultobj.put("result","none");
			System.out.println(resultobj.toString());
			return resultobj.toString();
		} else if(resultVO.getMember_pw().equals(member_pw)) {
			//로그인 성공
			//쿠키에 로그인정보 저장
			Cookie loginCookie = new Cookie("curuser",mvo.getMember_id());
			loginCookie.setPath("/");
			loginCookie.setMaxAge(60*60*24*30);
			response.addCookie(loginCookie);
			resultobj.put("result","success");
			System.out.println(resultobj.toString());
			return resultobj.toString();
		} else {
			//비밀번호 불일치
			resultobj.put("result","wrong");
			System.out.println(resultobj.toString());
			return resultobj.toString();
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value = "/logined", method = RequestMethod.POST)
	public String logined(@RequestBody MemberVO mvo,HttpServletResponse response) {
		//로그인 하기 위한 객체생성
		
		String member_id = mvo.getMember_id();
		MemberVO resultVO = new MemberVO();
		//결과 비교용 객체(아이디가 같은것만 찾는다==> 비밀번호 판독을 위해)
		resultVO =memservice.login(mvo);
		System.out.println(mvo.getMember_id() +" <-입력 || 결과 -> " + resultVO.getMember_id());
		System.out.println(mvo.getMember_pw() +" <-입력 || 결과 -> " + resultVO.getMember_pw());
		//결과 전송용 JSON객체 생성
		JSONObject resultobj = new JSONObject();
		
		//로그인 결과 판독
		if(resultVO==null) {
			//아이디가 없거나 아이디 오류
			resultobj.put("result","none");
			System.out.println(resultobj.toString());
			return resultobj.toString();
		}else {
			//로그인성공
			resultobj.put("result","success");
			System.out.println(resultobj.toString());
			return resultobj.toString();
		}
		
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public String join(@RequestBody MemberVO mvo) {
		//회원가입 하기 위한 객체생성
		String member_id = mvo.getMember_id();
		MemberVO resultVO = new MemberVO();
		
		//결과 비교용 객체(아이디가 같은것만 찾는다==> 비밀번호 판독을 위해)
		resultVO =memservice.login(mvo);
		
		//결과 전송용 JSON객체 생성
		JSONObject resultobj = new JSONObject();
		
		//아이디 중복검사 결과 판독
		if(resultVO==null) {
			//중복없으므로 회원가입처리
			resultobj.put("result","success");
			System.out.println(resultobj.toString());
			return resultobj.toString();

		} else {
			//비밀번호 일치
			resultobj.put("result","fail");
			System.out.println(resultobj.toString());
			return resultobj.toString();
		}
		
	}
	
}
