package com.yjc.stk.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yjc.stk.domain.PostPager;
import com.yjc.stk.domain.UploadHandler;
import com.yjc.stk.service.MemberService;
import com.yjc.stk.service.PostService;

import lombok.AllArgsConstructor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Handles requests for the application home page.
 */
@Controller
@AllArgsConstructor
public class PostController {
	
	private static final Logger logger = LoggerFactory.getLogger(PostController.class);
	private MemberService memservice;
	private PostService postservice;
	@Resource(name = "uploadPath")
	String uploadPath;
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@ResponseBody
	@RequestMapping(value = "/load", method = RequestMethod.POST, produces = "application/json; charset=utf8")
	public String loadPost(@RequestParam(defaultValue="1") int curPage, @RequestParam(defaultValue="") String keyword) {
		//190924 처음 불러와서 테스트용
		int totPage=0;
		PostPager postPager;
		if(keyword.isEmpty()) {
			totPage=postservice.totalPage();
			System.out.println("인자 1개"+totPage);
			postPager=new PostPager(totPage,curPage);
		}else {
			totPage=postservice.totalPage2(keyword);
			System.out.println("인자 2개"+totPage);
			postPager=new PostPager(totPage,curPage);
		}
		int start=postPager.getPageBegin();
		int end=postPager.getPageEnd();
		List<Map<String,Object>> areaList=postservice.allList(start,end,keyword);
		System.out.println(areaList.toString());
		System.out.println(postPager.toString());
		Map<String,Object> map=new HashMap<String,Object>();
		JSONArray postJsonArr = new JSONArray();
//		for(Map<String,Object> post : areaList) {
//			JSONObject obj = JSONObject.fromObject
//		}
		postJsonArr=JSONArray.fromObject(areaList);
		System.out.println("JSON 변환 잘 됐나? ==> "+postJsonArr.toString());
		JSONObject resultobj = new JSONObject();
		resultobj.put("totPage",totPage);
		resultobj.put("keyword",keyword);
		resultobj.put("postPager",postPager);
		resultobj.put("postlist",postJsonArr);
		resultobj.put("result","success");
		
		
		//글을 전부다 불러온다
		//댓글을 확인한다(ref가 있으면 다 댓글)
		//파일들을 불러온다 (post_id 별로 분류)
		//모집공고 컨트롤러
//		@RequestMapping(value = "/announce")
//		public String recruit(Model model,@RequestParam(defaultValue="1") int post_class,
//				@RequestParam(defaultValue="1") int curPage, @RequestParam(defaultValue="") String keyword) {
//			//========================================페이징 하기=======================================
//			int totPage=0;
//			PostPager postPager;
//			if(keyword.isEmpty()) {
//				totPage=postService.totalPage(post_class);
//				System.out.println("인자 1개"+totPage);
//				postPager=new PostPager(totPage,curPage);
//			}else {
//				totPage=postService.totalPage2(post_class,keyword);
//				System.out.println("인자 2개"+totPage);
//				postPager=new PostPager(totPage,curPage);
//			}
//			int start=postPager.getPageBegin();
//			int end=postPager.getPageEnd();
//			ArrayList<Map<String,Object>> areaList=postService.allList(start,end,keyword,post_class);
//			System.out.println(areaList.toString());
//			System.out.println(postPager.toString());
//			Map<String,Object> map=new HashMap<String,Object>();
//			map.put("totPage",totPage);
//			map.put("keyword",keyword);
//			map.put("postPager",postPager);
//			model.addAttribute("announcepostList",postService.getPostList(1));
//			model.addAttribute("announceList",areaList);
//			model.addAttribute("map",map);
//			return "announce/localrecruit";
//		}
		
		return resultobj.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public String post(@RequestBody Map<String,Object> map) {
		JSONObject resultobj = new JSONObject();
		try {
			postservice.post(map);
			resultobj.put("result","success");
		}catch(Exception e) {
			System.out.println("===========================에러============================");
			e.printStackTrace();
			resultobj.put("result","fail");
		}
		
		
		return resultobj.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/commentPost", method = RequestMethod.POST)
	public String commentPost(@RequestBody Map<String,Object> map) {
		JSONObject resultobj = new JSONObject();
		try {
			postservice.post(map);
			resultobj.put("result","success");
		}catch(Exception e) {
			System.out.println("===========================에러============================");
			e.printStackTrace();
			resultobj.put("result","fail");
		}
		
		
		return resultobj.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/postDetail", method = RequestMethod.POST, produces = "application/json; charset=utf8")
	public String postDetail(@RequestBody Map<String,Object> map) {
		JSONObject resultobj = new JSONObject();
		try {
			Map<String,Object> resultmap = new HashMap<>();
			resultmap=postservice.postDetail((String)map.get("post_id"));
			System.out.println((String)map.get("post_id"));
			resultobj.put("post_id",(String)resultmap.get("POST_ID"));
			resultobj.put("post_regdate",(String)resultmap.get("POST_REGDATE"));
			resultobj.put("post_title",(String)resultmap.get("POST_TITLE"));
			resultobj.put("post_visit",resultmap.get("POST_VISIT"));
			resultobj.put("post_moddate",(String)resultmap.get("POST_MODDATE"));
			resultobj.put("post_cont",(String)resultmap.get("POST_CONT"));
			resultobj.put("post_userid",(String)resultmap.get("POST_USERID"));
			JSONArray replyarr = new JSONArray();
			replyarr = JSONArray.fromObject(resultmap.get("reply"));
			resultobj.put("reply",replyarr);
			resultmap.clear();
			resultmap = postservice.fileFind((String)map.get("post_id"));
			JSONArray filearr = new JSONArray();
			filearr = JSONArray.fromObject(resultmap.get("files"));
			System.out.println(filearr.toString());
			resultobj.put("files",filearr);
			resultobj.put("result","success");
		}catch(Exception e) {
			System.out.println("===========================에러============================");
			e.printStackTrace();
			resultobj.put("result","fail");
		}
		
		
		return resultobj.toString();
	}
	
	@RequestMapping(value="/fileUpload.do",method=RequestMethod.POST,produces = "application/json; charset=utf8")
	@ResponseBody
	public String fileUpload(MultipartHttpServletRequest multipartRequest) {
		System.err.println(multipartRequest.getParameter("post_title"));
		System.err.println(multipartRequest.getParameter("post_cont"));
		System.err.println(multipartRequest.getParameter("post_userid"));
		JSONObject resultobj = new JSONObject();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("post_title",(String)multipartRequest.getParameter("post_title"));
		map.put("post_cont",(String)multipartRequest.getParameter("post_cont"));
		map.put("post_userid",(String)multipartRequest.getParameter("post_userid"));
		try {
			
			String file_path = uploadPath;
			String post_id = postservice.post(map);
			System.err.println(post_id);
			Map<String,List<String>> fileNames = new UploadHandler(multipartRequest,file_path).getUploadFileName();
			//=========최종 예정============
			//	post = {
			//		post_id : 'df',
			//		post_cont : 'dfdfdf',
			//		post_visit : 2323,
			//		post_userid: 'dfdf',
			//		reply : [댓글1{
			//					post_id :'댓글 아디',
			//					post_cont: '댓글 내용',
			//					post_userid : '댓글 작성자',
			//					post_regdate : '댓글 작성일자',
			//					post_moddate: '댓글 수정일자'
			//				},댓글2{}],
			//		post_regdate : '게시글 작성일자',
			//		post_moddate : '게시글 수정일자',
			//		post_file : [ {file_id : '파일 아이디', file_path:'파일 경로'},{}]
			//	}
			//
			Map<String,Object> fileMap = new HashMap<String,Object>();
//			for(String fileName:fileNames.get("file_origins")) {
//				fileMap.put("post_id",post_id);
//				fileMap.put("file_path",fileName);
//				for(String oldName:fileNames.get("file_paths")) {
//					fileMap.put("file_origin",oldName);
//				}
//				postservice.fileUpload(fileMap);
//			}
			for(int i =0; i<fileNames.get("file_origins").size();i++) {
				fileMap.put("post_id",post_id);
				fileMap.put("file_path",fileNames.get("file_origins").get(i));
				fileMap.put("file_origin",fileNames.get("file_paths").get(i));
				postservice.fileUpload(fileMap);
			}
			
			System.err.println(fileNames.toString());
			resultobj.put("result","success");
		}catch(Exception e) {
			System.out.println("===========================에러============================");
			e.printStackTrace();
			resultobj.put("result","fail");
		}
		return resultobj.toString();
	}
	@RequestMapping(value="/fileDownload.do")
	@ResponseBody
	public byte[] fileDownload(HttpServletResponse resp,@RequestParam("saveName") String saveName,@RequestParam("oldName") String oldName) {
		String filePath = uploadPath;
		System.out.println(saveName + "||||" + oldName);
		return new UploadHandler(resp, filePath, saveName, oldName).getDownloadFileByte();
	}
	
	
}
