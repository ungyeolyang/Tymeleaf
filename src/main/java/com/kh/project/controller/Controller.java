package com.kh.project.controller;
import com.kh.project.dao.BoardDAO;
import com.kh.project.dao.MemberDAO;
import com.kh.project.dao.SearchDAO;
import com.kh.project.vo.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Controller
@RequestMapping("/emp")
public class Controller {
    @GetMapping("/start")
    public String login(Model model){
        model.addAttribute("logmembers", new MemberVO());
        return "thymeleafEx/start";
    }
    @PostMapping("/start")
    public String checkLogin(@ModelAttribute("logmembers") MemberVO memberVO,Model model) {
        MemberDAO memberDAO = new MemberDAO();
        if(memberDAO.logIn(memberVO) != null) {
            model.addAttribute("fail", memberDAO.logIn(memberVO));
        return "thymeleafEx/loginFail";
        }
        else {return "thymeleafEx/main";}
    }
    @GetMapping("/regist")
    public String insertViewMember(Model model){
        model.addAttribute("regmembers", new MemberVO());
        return "thymeleafEx/regist";
    }
    @PostMapping("/regist")
    public String insertDBMember(@ModelAttribute("regmembers") MemberVO memberVO, Model model) {
        MemberDAO memberDAO = new MemberDAO();
        if(memberDAO.checkRegist(memberVO) != null) {
            model.addAttribute("fail", memberDAO.checkRegist(memberVO));
            return "thymeleafEx/registFail";
        }
        memberDAO.regist(memberVO);
        return "thymeleafEx/registRst";
    }
    @GetMapping("/main")
    public String main(){
        return "thymeleafEx/main";
    }

    @GetMapping("/searchIn")
    public String SearchIn(Model model){
        SearchVO searchVO = new SearchVO();
        searchVO.setNumber(1);
        model.addAttribute("searchIn",searchVO);
        return "thymeleafEx/searchIn";
    }

    @PostMapping("/searchIn")
    public String CheckSearchIn(@ModelAttribute("searchIn") SearchVO searchVO,Model model){
        SearchDAO searchDAO = new SearchDAO();
        if(searchDAO.search(searchVO).isEmpty())return "thymeleafEx/searchFail";
        model.addAttribute("ingredients",searchDAO.search(searchVO));
        return "thymeleafEx/nutrientsList";
    }
    @GetMapping("/searchEf")
    public String SearchEf(Model model){
        SearchVO searchVO = new SearchVO();
        searchVO.setNumber(2);
        model.addAttribute("searchEf",searchVO);
        return "thymeleafEx/searchEf";
    }

    @PostMapping("/searchEf")
    public String CheckSearchEf(@ModelAttribute("searchEf") SearchVO searchVO,Model model){
        SearchDAO searchDAO = new SearchDAO();
        if(searchDAO.search(searchVO).isEmpty())return "thymeleafEx/searchFail";
        model.addAttribute("ingredients", searchDAO.search(searchVO));
        return "thymeleafEx/nutrientsList";
    }
    @GetMapping("/searchNu")
    public String SearchNu(Model model){
        model.addAttribute("searchNu",new SearchVO());
        return "thymeleafEx/searchNu";
    }
    @PostMapping("/searchNu")
    public String CheckSearchNu(@ModelAttribute("searchNu") SearchVO searchVO, Model model){
        BoardDAO boardDAO = new BoardDAO();
        if(boardDAO.checkNut(searchVO.getData()) != null) {
            model.addAttribute("fail",boardDAO.checkNut(searchVO.getData()));
            return "thymeleafEx/searchNuFail";}
        List<NutrientsVO> list = new ArrayList<>();
        list.add(boardDAO.boardNut(searchVO.getData()));
        model.addAttribute("ingredients",list);
        model.addAttribute("efficacys", boardDAO.boardEFF(searchVO.getData()));
        model.addAttribute("comments", boardDAO.boardList(searchVO.getData()));
        return "thymeleafEx/nutrientsBoard";
    }
    @GetMapping("/comment")
    public String Comment(Model model){
        model.addAttribute("comment",new SearchVO());
        return "thymeleafEx/comment";
    }
    @PostMapping("/comment")
    public String CheckComment(@ModelAttribute("searchNu") SearchVO searchVO
            ,@ModelAttribute("comment") SearchVO commentVO){
        BoardDAO boardDAO = new BoardDAO();
        boardDAO.comment(boardDAO.boardNut(searchVO.getData()),commentVO.getData());
        return "thymeleafEx/searchNu";
    }

//    @GetMapping("/mypage")
//    public String myPage(){
//        return "thymeleafEx/myPage";
//    }
//    @GetMapping("/searchIn")
//    public String searchIn(Model model){
//        model.addAttribute("ingredients", new IngredientVO());
//        return "thymeleafEx/searchIn";
//    }
//    @PostMapping("/searchIn")
//    public String checkSearchIn(@ModelAttribute("ingredients") IngredientVO ingredientVO ,Model model){
//        SearchDAO searchDAO = new SearchDAO();
//        TreeSet<NutrientsVO> emps = searchDAO.search(ingredientVO);
//        model.addAttribute("employees", emps);
//        if(emps.isEmpty()) return "thymeleafEx/searchRst";
//        return "thymeleafEx/nutrientsList";
//    }
//
//    @GetMapping("/searchNu")
//    public String searchNu(Model model){
//        model.addAttribute("Nutrients", new NutrientsVO());
//        return "thymeleafEx/searchNu";
//    }
//    @PostMapping("/searchNu")
//    public String ChecksearchNu(@ModelAttribute("members") NutrientsVO nutrientsVO){
//        BoardDAO boardDAO = new BoardDAO();
//        NutrientsVO nuts = boardDAO.boardNut(nutrientsVO);
//        HashSet<String> set = boardDAO.boardEFF(nuts);
//    return "thymeleafEx/searchNu";
//    }
  }
