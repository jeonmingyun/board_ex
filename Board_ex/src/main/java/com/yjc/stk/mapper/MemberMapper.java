package com.yjc.stk.mapper;

import java.util.ArrayList;
import com.yjc.stk.domain.MemberVO;

public interface MemberMapper {
	public ArrayList<MemberVO> getMemberList();
	public MemberVO login(MemberVO vo);
	public MemberVO idSearch(MemberVO vo);
	public int join(MemberVO vo);
}
