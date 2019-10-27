package com.yjc.stk.domain;

import lombok.Data;

@Data
public class PostPager {
	//페이지당 게시글 수
		public static final int PAGE_SCALE=5;
		//화면당 페이지 수
		public static final int BLOCK_SCALE=10;
		
		//현재 페이지수
		private int curPage;
		//이전 페이지
		private int prevPage;
		//다음 페이지
		private int nextPage;
		//전체 페이지 갯수
		private int tot_page;
		
		//현재페이지 블록
		private int curBlock;
		//전체 페이지 블록 갯수
		private int totBlock;
		//이전페이지 블록
		private int prevBlock;
		//다음페이지 블록
		private int nextBlock;
		
		//WHERE rn BETWEEN #{start} AND #{end}
		private int pageBegin;	//#{start}
		private int pageEnd;	//#{end}
		
		//	blockBegin=>41 42 43 44 45 46 47 48 49 50 
		private int blockBegin;	//현재페이지 블록의 시작번호
		//	41 42 43 44 45 46 47 48 49 50 <= blockEnd
		private int blockEnd;	//현재페이지 블록의 끝번호
		public PostPager() {}
		public PostPager(int count,int curPage) {
			curBlock=1;				//현재페이지 블록 번호
			this.curPage=curPage;	//현재 페이지 설정
			setTot_page(count);		//전체 페이지 갯수 계산
			setPageRange();			//
			setTotBlock();			//전체 페이지 블록 갯수 계산
			setBlockRange();		//페이지 블록의 시작, 끝번호 계산
		}
		
		public void setBlockRange() {
			//현재 페이지가 몇번째 페이지 블록에 속하는지 계산
			curBlock=(int)Math.ceil((curPage-1)/BLOCK_SCALE)+1;
			//현재 페이지 블록의 시작, 끝 번호 계산
			blockBegin=(curBlock-1)*BLOCK_SCALE+1;
			blockEnd=blockBegin+BLOCK_SCALE-1;
			
			//마지막 블록이 범위를 초과하지 않도록 계산
			if(blockEnd>tot_page) blockEnd=tot_page;
			//이전을 눌렀을때 이동할 페이지 번호
			if(curPage==1) {	prevPage=1;
			}else {				prevPage=(curBlock-1)*BLOCK_SCALE;	}
			//다음을 눌렀을때 이동할 페이지 번호
			nextPage= curBlock >totBlock ? (curBlock*BLOCK_SCALE):(curBlock*BLOCK_SCALE)+1;
			if(nextPage>=tot_page) nextPage=tot_page;
		}
		public void setPageRange() {
			//WHERE rn BETWEEN #{start} and #{end}
			//시작번호 =(현재페이지-1)*페이지당 게시글 수 +1
			pageBegin=(curPage-1)*PAGE_SCALE+1;
			//끝번호=시작번호+페이지당 게시글 수 -1
			pageEnd=pageBegin+PAGE_SCALE-1;
		}
		public void setTot_page(int count) {	tot_page=(int) Math.ceil(count*1.0 /PAGE_SCALE);	}
		public void setTotBlock() {
			totBlock=(int)Math.ceil(tot_page / BLOCK_SCALE);
			if(totBlock==0) {
				totBlock=1;
			}
		}
}
