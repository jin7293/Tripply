package com.kh.tripply.review.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonObject;
import com.kh.tripply.review.common.Paging;
import com.kh.tripply.review.domain.Review;
import com.kh.tripply.review.service.ReviewService;

@Controller
public class ReviewController {
	@Autowired
	ReviewService rService;
	
	/**
	 * 후기게시판 목록 페이지 이동
	 * @param mv,page
	 * @return mv.addObject : rList, paging / mv.setViewName("review/reviewList")
	 */
	@RequestMapping(value="/review/list.kh", method=RequestMethod.GET)
	public ModelAndView reviewListView(ModelAndView mv,
			@RequestParam(value="currentPage", required=false)Integer page) {
		int currentPage = (page!=null)? page : 1;
//		게시물의 총 개수, 현재페이지, 페이지 당 게시물 개수, 페이징네비 사이즈
		Paging paging = new Paging(rService.getTotalCount(), currentPage, 9, 5);
		try {
			List<Review> rList = rService.printAllReview(paging);
			if(!rList.isEmpty()) {
				mv.addObject("rList",rList).
				addObject("paging",paging).
				setViewName("review/reviewList");
			}else {
				
			}
		} catch (Exception e) {
		}
		return mv;
	}
	
	/**
	 * 후기 게시물 작성 페이지 이동
	 * @return String: "/review/reviewWrite"
	 */
	@RequestMapping(value="/review/writeView.kh",method=RequestMethod.GET)
	public String reviewWriteView() {
		return "/review/reviewWrite";
	}
	
	/**
	 * 후기 게시물 저장
	 * @param mv
	 * @return mv.seViewName("/review/reviewList")
	 */
	@RequestMapping(value="/review/write.kh",method=RequestMethod.POST)
	public ModelAndView reviewWrite(ModelAndView mv,
					@ModelAttribute Review review) {
		
		
		int result = rService.registerReview(review);
		try {
			if(result > 0) {
				mv.setViewName("redirect:/review/list.kh?currnentPage=1");
			}else {
				mv.addObject("msg","게시물 저장에 실패하였습니다.").setViewName("/common/errorPage");
			}
		} catch (Exception e) {
			mv.addObject("msg",e.getMessage()).setViewName("/common/errorPage");
		}
		return mv;
	}

	/**
	 * 후기 게시물 상세 페이지 이동
	 * @param mv,boardNo
	 * @return
	 */
	@RequestMapping(value="/review/detailView.kh",method=RequestMethod.GET)
	public ModelAndView reviewDetailView(ModelAndView mv,
			@RequestParam("boardNo") Integer boardNo) {
		try {
			Review review = rService.printDetailReviewByNo(boardNo);
			if(review != null) {
				mv.addObject("review",review).setViewName("/review/reviewDetail");
			}else {
			}
		} catch (Exception e) {
			
		}
		return mv;
	}
	
	/**
	 * 썸머노트 ajax 매핑 메소드
	 * 에디터 업로드 이미지 저장 및 파일 경로 json반환
	 * @param multipartFile
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/uploadSummernoteImageFile",method=RequestMethod.POST)
	public JsonObject uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile,
			HttpServletRequest request) {
		JsonObject jsonObject = new JsonObject();
//파일저장 외부 경로, 파일명, 저장할 파일명		
		try {
			String originalFileName = multipartFile.getOriginalFilename();
			String root = request.getSession().getServletContext().getRealPath("resources");
			String savePath = root + "\\image\\review\\summerImageFiles";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String extension = originalFileName.substring(originalFileName.lastIndexOf(".")+1);
			String boardFileRename = sdf.format(new Date(System.currentTimeMillis())) + "." + extension; 
			File targetFile = new File(savePath);
			if(!targetFile.exists()) {
				targetFile.mkdir();
			}
			multipartFile.transferTo(new File(savePath+"\\"+boardFileRename));
			
			System.out.println(savePath);
			jsonObject.addProperty("url","/resources/image/review/summerImageFiles/"+boardFileRename);
			jsonObject.addProperty("originName",originalFileName);
			jsonObject.addProperty("reponseCode","success");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return jsonObject;
	}
}
