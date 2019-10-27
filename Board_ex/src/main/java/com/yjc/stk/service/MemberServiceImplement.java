package com.yjc.stk.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.yjc.stk.domain.MemberVO;
import com.yjc.stk.mapper.MemberMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberServiceImplement implements MemberService {

	private MemberMapper mapper;
	@Override
	public ArrayList<MemberVO> getMemberList() {
		//System.out.println(mapper.getMemberList());
		return mapper.getMemberList();
	}
	@Override
	public MemberVO login(MemberVO vo) {
		return mapper.login(vo);
	}
	@Override
	public MemberVO idSearch(MemberVO vo) {
		return mapper.idSearch(vo);
	}
	@Override
	public int join(MemberVO vo) {
		return mapper.join(vo);
	}

}
