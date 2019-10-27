package com.yjc.stk.domain;

import java.io.File;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import lombok.Data;

@Data
public class UploadHandler {
	@Resource(name = "uploadPath")
	private String file_path; // common
    private MultipartHttpServletRequest multipartRequest; // upload
    private Map<String, List<String>> file_names; // upload
    private String oldName; // down
    private HttpServletResponse response; // down
    private byte[] fileByte; // down
    public UploadHandler() {}
    
    //생성자
    public UploadHandler(MultipartHttpServletRequest multipartRequest, String file_path) {
    	this.multipartRequest = multipartRequest;
    	this.file_path = file_path;
    	file_names = new HashMap<String,List<String>>();
    }
    
    //임시저장 생성자
    public UploadHandler(MultipartHttpServletRequest multipartRequest) {
    	this.multipartRequest=multipartRequest;
    	this.file_path = multipartRequest.getSession().getServletContext().getRealPath("/resources/temp");
    	file_names = new HashMap<String, List<String>>();
    }
    
    //생성자 세터
    public UploadHandler setUpload(MultipartHttpServletRequest multipartRequest,String file_path) {
    	this.multipartRequest = multipartRequest;
    	this.file_path=file_path;
    	file_names = new HashMap<String,List<String>>();
    	return this;
    }
    
    //임시 생성자 세터
    public UploadHandler setUpload(MultipartHttpServletRequest multipartRequest) {
    	this.multipartRequest = multipartRequest;
    	this.file_path=multipartRequest.getSession().getServletContext().getRealPath("/resources/temp");
    	file_names = new HashMap<String,List<String>>();
    	return this;
    }
    
    public Map<String,List<String>> getUploadFileName() {
    	upload(true);
    	return file_names;
    }
    
    public Map<String,List<String>> getUploadFileName(boolean saveNameCreate) {
    	upload(saveNameCreate);
    	return file_names;
    }
    
    private void upload(boolean saveNameCreate) {
    	Iterator<String> itr = multipartRequest.getFileNames();
    	List<String> oldNames = new ArrayList<String>();
    	List<String> saveNames = new ArrayList<String>();
    	StringBuffer sb= null;
    	while(itr.hasNext()) {
    		MultipartFile mpf = multipartRequest.getFile(itr.next());
    		sb = new StringBuffer();
    		String oldFileName = mpf.getOriginalFilename();
    		String saveFileName = sb.append(new SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis()))
    				.append("_")
    				.append(UUID.randomUUID().toString())
    				.append(oldFileName.substring(oldFileName.lastIndexOf('.'))).toString();
    		String fileFullPath =null;
    		if(saveNameCreate) {
    			fileFullPath = file_path + "/" + saveFileName;
    		}else {
    			fileFullPath = file_path + "/" + oldFileName;
    		}try {
    			File targetDir = new File(fileFullPath);
    			if(!targetDir.exists()) {
    				targetDir.mkdirs();
    			}
    			mpf.transferTo(new File(fileFullPath));
    			oldNames.add(oldFileName);
    			if(saveNameCreate) {
    				saveNames.add(saveFileName);
    			}else {
    				saveNames.add(oldFileName);
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	file_names.put("file_paths", oldNames);
    	file_names.put("file_origins", saveNames);
    }
    
    //다운 생성자
    public UploadHandler(HttpServletResponse response,String file_path,String saveName,String oldName) {
    	this.response = response;
    	this.file_path = file_path + "/"+saveName;
    	this.oldName = oldName;
    }
    
    //다운 세터
    public UploadHandler setDown(HttpServletResponse response,String file_path, String saveName, String oldName) {
    	this.response = response;
    	this.file_path = file_path + "/" + saveName;
    	this.oldName = oldName;
    	return this;
    }
    public byte[] getDownloadFileByte() {
    	download();
    	return fileByte;
    }
    
    private void download() {
    	try {
    		fileByte = FileUtils.readFileToByteArray(new File(file_path));
    		response.setContentType("application/octet-stream");
    		response.setContentLength(fileByte.length);
    		
    		response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(oldName,"UTF-8")+"\";");
    		response.setHeader("Content-Transfer-Encoding","binary");
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    //삭제 생성자
    public UploadHandler(String file_path,String saveName) {
    	this.file_path = file_path+"/" + saveName;
    }
    //삭제 세터
    public UploadHandler setDelete(String file_path,String saveName) {
    	this.file_path = file_path +"/" + saveName;
    	return this;
    }
    
    public boolean deleteFileExecute() {
    	boolean check = false;
    	File file = new File(file_path);
    	if(file.exists()) {
    		check = file.delete();
    	}
    	return check;
    }
    
    
}
