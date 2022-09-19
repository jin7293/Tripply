package com.kh.tripply.notice.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kh.tripply.notice.domain.Notice;
import com.kh.tripply.notice.service.NoticeService;

@Controller
public class NoticeController {

	@Autowired
	private NoticeService nService;

	// 공지사항 등록 화면
	@RequestMapping(value = "/notice/writeView.kh", method = RequestMethod.GET)
	public String showBoardWrite() {
		return "notice/noticeWriteForm";
	}

	// 공지사항 등록
	@RequestMapping(value = "/notice/register.kh", method = RequestMethod.POST)
	public ModelAndView registerBoard(ModelAndView mv, @ModelAttribute Notice notice) {

		try {
			int result = nService.registerNotice(notice);
			mv.setViewName("redirect:/notice/list.kh");
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("msg", e.getMessage());
			mv.setViewName("common/errorPage");
		}
		return mv;
	}

	// 공지사항 리스트
	@RequestMapping(value = "/notice/list.kh", method = RequestMethod.GET)
	public ModelAndView noticeListView(ModelAndView mv
			, @RequestParam(value = "page", required = false) Integer page,
			HttpServletRequest request) {

		try {
			// 페이징
			int currentPage = (page != null) ? page : 1;
			int totalCount = nService.getTotalCount("", "");
			int boardLimit = 10;
			int naviLimit = 5;
			int maxPage;
			int startNavi;
			int endNavi;
			maxPage = (int) ((double) totalCount / boardLimit + 0.9);
			startNavi = ((int) ((double) currentPage / naviLimit + 0.9) - 1) * naviLimit + 1;
			endNavi = startNavi + naviLimit - 1;
			if (maxPage < endNavi) {
				endNavi = maxPage;
			}
			mv.addObject("currentPage", currentPage);
			mv.addObject("maxPage", maxPage);
			mv.addObject("startNavi", startNavi);
			mv.addObject("endNavi", endNavi);
			mv.addObject("urlVal", "list");
			// 페이징

			List<Notice> nList = nService.printAllNotice(currentPage, boardLimit);
			mv.addObject("nList", nList);
			mv.setViewName("notice/noticeListView");
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("msg", e.getMessage());
			mv.setViewName("common/errorPage");
		}
		return mv;
	}

	// 공지사항 상세 뷰
	@RequestMapping(value = "/notice/detail.kh", method = RequestMethod.GET)
	public ModelAndView noticeDetailView(ModelAndView mv, @RequestParam("noticeNo") int noticeNo,
			@RequestParam("page") Integer page, HttpSession session) {

		try {
			Notice notice = nService.printOneNotice(noticeNo);
			mv.addObject("notice", notice);
			mv.addObject("page", page);

			mv.setViewName("notice/noticeDetailView");
		} catch (Exception e) {
			mv.addObject("msg", e.getMessage());

			mv.setViewName("common/errorPage");
		}

		return mv;

	}

//	// 공지사항 삭제 수정중
//	@RequestMapping(value="notice/remove.kh", method = RequestMethod.GET)
//	public String noticeRemove(HttpSession session
//			, Model model
//			, @RequestParam("page") Integer page) {
//		
//		 try {
//			 int noticeNo = (int) session.getAttribute("noticeNo");
//			 System.out.println(noticeNo);
//			 int result = nService.removeOneByNo(noticeNo);
//			 if(result > 0) {
//				session.removeAttribute("noticeNo");
//				
//			 }else {
//				 System.out.println("실패");
//			 }
//			 
//			 return "redirect:/notice/list.kh?page=" + page;
//		} catch (Exception e) {
//			model.addAttribute("msg", e.getMessage());
//			return "common/errorPage";
//		}
//	}
	
	// 공지사항 수정 뷰
	@RequestMapping(value="notice/modifyView.kh", method = RequestMethod.GET)
	public ModelAndView noticeModifyView(
							ModelAndView mv
							, @RequestParam("page") Integer page
							, @RequestParam("noticeNo") Integer noticeNo) {
		
		try {
			Notice notice = nService.printOneNotice(noticeNo);
			mv.addObject("notice", notice);
			mv.addObject("page", page);
			mv.setViewName("notice/noticeModifyForm");
		} catch (Exception e) {
			mv.addObject("msg", e.getMessage());
			mv.setViewName("common/errorPage");
		}
		return mv;
		
	}
	
	// 공지사항 수정
	@RequestMapping(value="notice/modify.kh", method = RequestMethod.POST)
	public ModelAndView noticeModify(
			ModelAndView mv
			, @ModelAttribute Notice notice
			, @RequestParam("page") Integer page
			, HttpServletRequest request) { // 파일경로는 request로 알아옴
			
		try {
			int result = nService.modifyNotice(notice);
			if(result > 0) {
				mv.setViewName("redirect:/notice/list.kh?page=" + page);
			}
		} catch (Exception e) {
			mv.addObject("msg", e.getMessage());
			mv.setViewName("common/errorPage");
		}
		
		return mv;
		
	}
}
