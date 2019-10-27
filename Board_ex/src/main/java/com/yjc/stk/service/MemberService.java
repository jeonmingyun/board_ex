package com.yjc.stk.service;

import java.util.ArrayList;
import com.yjc.stk.domain.MemberVO;

public interface MemberService {
	public ArrayList<MemberVO> getMemberList();
	public MemberVO login(MemberVO vo);
	public MemberVO idSearch(MemberVO vo);
	public int join(MemberVO vo);
}
