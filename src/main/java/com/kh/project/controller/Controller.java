package com.kh.project.controller;
import com.kh.project.dao.BoardDAO;
import com.kh.project.dao.MemberDAO;
import com.kh.project.dao.MyInfoDAO;
import com.kh.project.dao.SearchDAO;
import com.kh.project.vo.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Controller
@RequestMapping("/emp")
public class Controller {
    MemberDAO memberDAO = new MemberDAO();
    MyInfoDAO myInfoDAO = new MyInfoDAO();
    SearchDAO searchDAO = new SearchDAO();
    BoardDAO boardDAO = new BoardDAO();

    @GetMapping("/start")
    public String login(Model model) {
        model.addAttribute("logmembers", new MemberVO());
        return "thymeleafEx/start";
    }

    @PostMapping("/start")
    public String checkLogin(@ModelAttribute("logmembers") MemberVO memberVO, Model model, HttpSession session) {
        if (memberDAO.logIn(memberVO) != null) {
            model.addAttribute("fail", memberDAO.logIn(memberVO));
            return "thymeleafEx/loginFail";
        } else {
            session.setAttribute("userInfo", myInfoDAO.myInfo(memberVO));
            return "thymeleafEx/main";
        }
    }

    @GetMapping("/regist")
    public String insertViewMember(Model model) {
        model.addAttribute("regmembers", new MemberVO());
        return "thymeleafEx/regist";
    }

    @PostMapping("/regist")
    public String insertDBMember(@ModelAttribute("regmembers") MemberVO memberVO, Model model) {
        if (memberDAO.checkRegist(memberVO) != null) {
            model.addAttribute("fail", memberDAO.checkRegist(memberVO));
            return "thymeleafEx/registFail";
        }
        memberDAO.regist(memberVO);
        return "thymeleafEx/registRst";
    }

    @GetMapping("/main")
    public String main() {
        return "thymeleafEx/main";
    }

    @GetMapping("/searchIn")
    public String SearchIn(Model model) {
        SearchVO searchVO = new SearchVO();
        searchVO.setNumber(1);
        model.addAttribute("searchIn", searchVO);
        return "thymeleafEx/searchIn";
    }

    @PostMapping("/searchIn")
    public String CheckSearchIn(@ModelAttribute("searchIn") SearchVO searchVO, Model model) {
        if (searchDAO.search(searchVO).isEmpty()) return "thymeleafEx/searchFail";
        model.addAttribute("ingredients", searchDAO.search(searchVO));
        return "thymeleafEx/nutrientsList";
    }

    @GetMapping("/searchEf")
    public String SearchEf(Model model) {
        SearchVO searchVO = new SearchVO();
        searchVO.setNumber(2);
        model.addAttribute("searchEf", searchVO);
        return "thymeleafEx/searchEf";
    }

    @PostMapping("/searchEf")
    public String CheckSearchEf(@ModelAttribute("searchEf") SearchVO searchVO, Model model) {
        if (searchDAO.search(searchVO).isEmpty()) return "thymeleafEx/searchFail";
        model.addAttribute("ingredients", searchDAO.search(searchVO));
        return "thymeleafEx/nutrientsList";
    }

    @GetMapping("/searchNu")
    public String SearchNu(Model model) {
        model.addAttribute("searchNu", new SearchVO());
        return "thymeleafEx/searchNu";
    }

    @PostMapping("/searchNu")
    public String CheckSearchNu(@ModelAttribute("searchNu") SearchVO searchVO, Model model, HttpSession session) {
        if (boardDAO.checkNut(searchVO.getData()) != null) {
            model.addAttribute("fail", boardDAO.checkNut(searchVO.getData()));
            return "thymeleafEx/searchNuFail";
        }
        List<NutrientsVO> list = new ArrayList<>();
        NutrientsVO voNut = boardDAO.boardNut(searchVO.getData());
        session.setAttribute("userNu", voNut);
        list.add(voNut);
        model.addAttribute("ingredients", list);
        model.addAttribute("efficacys", boardDAO.boardEFF(searchVO.getData()));
        model.addAttribute("comments", boardDAO.boardList(searchVO.getData()));
        return "thymeleafEx/nutrientsBoard";
    }

    @GetMapping("/comment")
    public String Comment(Model model) {
        model.addAttribute("comment", new SearchVO());
        return "thymeleafEx/comment";
    }

    @PostMapping("/comment")
    public String CheckComment(@ModelAttribute("comment") SearchVO searchVO, HttpSession session) {
        NutrientsVO nutrientsVO = (NutrientsVO) session.getAttribute("userNu");
        MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
        boardDAO.comment(nutrientsVO, memberVO, searchVO.getData());
        return "thymeleafEx/main";
    }

    @GetMapping("/mypage")
    public String myPage(Model model, HttpSession session) {
        MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
        model.addAttribute("myNick", memberVO.getNick());
        return "thymeleafEx/myPage";
    }

    @GetMapping("/myInfo")
    public String myInfo(Model model, HttpSession session) {
        List<MemberVO> list = new ArrayList<>();
        MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
        list.add(memberVO);
        model.addAttribute("myInfolist", list);
        return "thymeleafEx/myInfo";
    }

    @GetMapping("/myInfoModName")
    public String myInfoModName(Model model) {
        SearchVO searchVO = new SearchVO();
        searchVO.setNumber(1);
        model.addAttribute("nameMod", searchVO);
        return "thymeleafEx/myInfoModName";
    }

    @PostMapping("/myInfoModName")
    public String myInfoModName1(@ModelAttribute("nameMod") SearchVO searchVO, HttpSession session) {
        MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
        System.out.println(memberVO.getId() + memberVO.getPw() + memberVO.getNick());
        myInfoDAO.updateMyInfo(searchVO, memberVO);
        memberVO.setName(searchVO.getData());
        session.setAttribute("userInfo", memberVO);
        return "thymeleafEx/main";
    }

    @GetMapping("/myInfoModPw")
    public String myInfoModPw(Model model) {
        SearchVO searchVO = new SearchVO();
        searchVO.setNumber(2);
        model.addAttribute("pwMod", searchVO);
        return "thymeleafEx/myInfoModPw";
    }

    @PostMapping("/myInfoModPw")
    public String myInfoModPw1(@ModelAttribute("pwMod") SearchVO searchVO, HttpSession session) {
        MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
        myInfoDAO.updateMyInfo(searchVO, memberVO);
        memberVO.setPw(searchVO.getData());
        session.setAttribute("userInfo", memberVO);
        return "thymeleafEx/main";
    }

    @GetMapping("/myInfoModNick")
    public String myInfoModNick(Model model) {
        SearchVO searchVO = new SearchVO();
        searchVO.setNumber(3);
        model.addAttribute("nickMod", searchVO);
        return "thymeleafEx/myInfoModNick";
    }

    @PostMapping("/myInfoModNick")
    public String myInfoModNick1(@ModelAttribute("nickMod") SearchVO searchVO, HttpSession session) {
        MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
        myInfoDAO.updateMyInfo(searchVO, memberVO);
        memberVO.setNick(searchVO.getData());
        session.setAttribute("userInfo", memberVO);
        return "thymeleafEx/main";
    }

    @GetMapping("/delCheck")
    public String delCheck() {
        return "thymeleafEx/delCheck";
    }

    @GetMapping("/delMyInfo")
    public String delMyInfo(HttpSession session) {
        MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
        myInfoDAO.deleteMyInfo(memberVO);
        return "thymeleafEx/delMyInfo";
    }

    @GetMapping("/myComment")
    public String myComment(HttpSession session, Model model) {
        MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
        model.addAttribute("myComment", boardDAO.searchBoard(memberVO));
        return "thymeleafEx/myComment";
    }

    @GetMapping("/modComment")
    public String modComment(Model model) {
        model.addAttribute("modComment", new SearchVO());
        return "thymeleafEx/modComment";
    }

    @PostMapping("/modComment")
    public String checkModComment(@ModelAttribute("modComment") SearchVO searchVO) {
        boardDAO.updateContent(searchVO.getNumber(), searchVO.getData());
        return "thymeleafEx/main";
    }

    @GetMapping("/delComment")
    public String delComment(Model model) {
        model.addAttribute("delComment", new SearchVO());
        return "thymeleafEx/delComment";
    }
    @PostMapping("/delComment")
    public String checkDelComment(@ModelAttribute("modComment") SearchVO searchVO) {
        boardDAO.deleteContent(searchVO.getNumber());
        return "thymeleafEx/main";
    }
}
