package com.kh.tripply.party.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.kh.tripply.party.domain.Party;
import com.kh.tripply.party.service.PartyService;

@Controller
public class PartyController {
	@Autowired
	private PartyService pService;

	// 동행자 게시판 등록view
	@RequestMapping(value = "/party/writeView.kh", method = RequestMethod.GET)
	public String showBoardWrite() {
		return "party/partyWriteForm";
	}

	// 동행자 게시판 게시글 등록
	@RequestMapping(value = "/party/register.kh", method = RequestMethod.POST)
	public ModelAndView registerBoard(ModelAndView mv, @ModelAttribute Party party,
			@RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile,
			HttpServletRequest request) { // resources 경로 가져오려고

		try {

			String boardFilename = uploadFile.getOriginalFilename();

			if (!boardFilename.equals("")) {
				String root = request.getSession().getServletContext().getRealPath("resources");
				String savePath = root + "\\partyuploadFiles"; // 저장경로 지정
				File file = new File(savePath);

				// 파일 이름이 같다고 안들어가는 경우 방지 --> 파일명을 날짜+시간으로 바꿈
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				String boardFileRename = sdf.format(new Date(System.currentTimeMillis())) + "."
						+ boardFilename.substring(boardFilename.lastIndexOf(".") + 1);// .다음부터 끝까지 잘라서 반환
				if (!file.exists()) {
					file.mkdir(); // 경로 폴더가 없으면 폴더 생성
				}
				uploadFile.transferTo(new File(savePath + "\\" + boardFileRename));// 파일을 buploadFile경로에 저장하는 메소드
				party.setPartyFileName(boardFilename);
				party.setPartyFileRename(boardFileRename); // 모든 파일이 고유한 값을 갖게 해야함

				String boardFilepath = savePath + "\\" + boardFileRename;// 절대경로

				party.setPartyFilePath(boardFilepath);

			}

			int result = pService.registerParty(party);
			mv.setViewName("redirect:/party/writeView.kh");

		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("msg", e.getMessage());
			mv.setViewName("common/errorPage");
		}
		return mv;
	}
	
//	// 동행자 게시판 리스트 수정중!!!
//	@RequestMapping(value="/party/list.kh", method=RequestMethod.GET)
//	public ModelAndView partyListView(
//			ModelAndView mv
//			,@RequestParam(value = "page", required=false) Integer page
//			,HttpServletRequest request) {
//		
//		try {
//			
//			int currentPage = (page != null) ? page : 1;
//			int totalCount = pService.getTotalCount("","");
//			int boardLimit = 9;
//			int naviLimit = 5;
//			int maxPage;
//			int startNavi;
//			int endNavi;
//			
//			maxPage = (int)((double)totalCount/boardLimit + 0.9);
//			startNavi = ((int)((double)currentPage/naviLimit + 0.9)-1)*naviLimit+1;
//			endNavi = startNavi + naviLimit - 1;
//			if(maxPage < endNavi) {
//				endNavi = maxPage;
//			}
//			
//			
//			List<Party> pList = pService.printAllBoard(currentPage, boardLimit);
//			
//			
//			if(!pList.isEmpty()) {
//				mv.addObject("currentPage", currentPage);
//				mv.addObject("maxPage", maxPage);  
//				mv.addObject("startNavi", startNavi);
//				mv.addObject("endNavi", endNavi);
//				mv.addObject("pList", pList);
//				mv.addObject("urlVal", "list");
//				mv.setViewName("party/partyListView");
//			}else {
//				mv.addObject("msg", "실패").setViewName("common/errorPage");
//			}
//			
//		}catch (Exception e) {
//			mv.addObject("msg", e.getMessage()).setViewName("common/errorPage");
//		}
//		return mv;
//		
//		
//	}
}
