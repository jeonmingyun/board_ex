package com.yjc.stk.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yjc.stk.mapper.PostMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostServiceImplement implements PostService {

	private PostMapper mapper;
	@Override
	public String post(Map<String, Object> map) {
		mapper.post(map);
		return (String)map.get("post_id");
	}
	
	@Override
	public ArrayList<Map<String, Object>> allList(int start, int end, String keyword) {
		Map<String,Object> map=new HashMap<>();
		map.put("start", start);
		map.put("end", end);
		map.put("keyword", keyword);
		return mapper.allList(map);
	}
	@Override
	public int totalPage() {
		Map<String,Object> map=mapper.totalPage();
		System.out.println(map.toString());
		int tot_page=Integer.parseInt(String.valueOf(map.get("TOT_PAGE")));
		return tot_page;
	}
	@Override
	public int totalPage2(String keyword) {
		Map<String,Object> map=new HashMap<>();
		map.put("keyword", keyword);
		System.out.println(map.toString());
		Map<String,Object> result=mapper.totalPage2(map);
		System.out.println(result.toString());
		int tot_page=Integer.parseInt(String.valueOf(result.get("TOT_PAGE")));
		return tot_page;
	}

	@Override
	public Map<String, Object> postDetail(String post_id) {
		Map<String, Object> map = new HashMap<>();
		map=mapper.postDetail(post_id);
		map.put("reply",mapper.post_reply(post_id));
		return map;
	}

	@Override
	public void fileUpload(Map<String, Object> map) {
		mapper.fileUpload(map);
	}

	@Override
	public Map<String, Object> fileFind(String post_id) {
		Map<String, Object> map = new HashMap<>();
		map.put("files",mapper.fileFind(post_id));
		return map;
	}
	

}
