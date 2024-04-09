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
import java.util.TreeSet;

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
            SearchVO searchVO = new SearchVO();
            searchVO.setNumber(1);
            model.addAttribute("search", searchVO);
            return "thymeleafEx/main";
        }
    }

    @GetMapping("/regist")
    public String regist(Model model) {
        model.addAttribute("regmembers", new MemberVO());
        return "thymeleafEx/regist";
    }

    @PostMapping("/regist")
    public String checkregist(@ModelAttribute("regmembers") MemberVO memberVO, Model model) {
        if (memberDAO.checkRegist(memberVO) != null) {
            model.addAttribute("fail", memberDAO.checkRegist(memberVO));
            return "thymeleafEx/registFail";
        }
        memberDAO.regist(memberVO);
        return "thymeleafEx/registRst";
    }
    @GetMapping("/findIdPw")
    public String findIdPw() {
        return "thymeleafEx/findIdPw";
    }

    @GetMapping("/findId")
    public String findId(Model model) {
        model.addAttribute("findId", new MemberVO());
        return "thymeleafEx/findId";
    }
    @PostMapping("/findId")
    public String checkFindId(@ModelAttribute("findId") MemberVO memberVO, Model model) {
            if (memberDAO.checkFindId(memberVO) == null) {
            model.addAttribute("fail", "회원정보가 존재하지 않습니다.");
            return "thymeleafEx/findFail";
        }
        model.addAttribute("idFind",memberDAO.checkFindId(memberVO));
        return "thymeleafEx/findIdRst";
    }
    @GetMapping("/findPw")
    public String findPw(Model model) {
        model.addAttribute("findPw", new MemberVO());
        return "thymeleafEx/findPw";
    }
    @PostMapping("/findPw")
    public String checkFindPw(@ModelAttribute("findPw") MemberVO memberVO, Model model) {
        if (memberDAO.checkFindPw(memberVO) == null) {
            model.addAttribute("fail", "회원정보가 존재하지 않습니다.");
            return "thymeleafEx/findFail";
        }
        model.addAttribute("pwFind",memberDAO.checkFindPw(memberVO));
        return "thymeleafEx/findPwRst";
    }

    @GetMapping("/main")
    public String main(Model model) {
        SearchVO searchVO = new SearchVO();
        searchVO.setNumber(1);
        model.addAttribute("search", searchVO);
        return "thymeleafEx/main";
    }

    @PostMapping("/main")
    public String checkmain(@ModelAttribute("search")SearchVO searchVO,Model model) {
        if (searchDAO.search(searchVO).isEmpty()) return "thymeleafEx/searchFail";
        model.addAttribute("ingredients", searchDAO.search(searchVO));
        return "thymeleafEx/nutrientsList";
    }

//    @GetMapping("/search")
//    public String Search(Model model) {
//        SearchVO searchVO = new SearchVO();
//        searchVO.setNumber(1);
//        model.addAttribute("search", searchVO);
//        return "thymeleafEx/search";
//    }
//
//    @PostMapping("/search")
//    public String CheckSearch(@ModelAttribute("search") SearchVO searchVO, Model model) {
//        if (searchDAO.search(searchVO).isEmpty()) return "thymeleafEx/searchFail";
//        model.addAttribute("ingredients", searchDAO.search(searchVO));
//        return "thymeleafEx/nutrientsList";
//    }

//    @GetMapping("/searchEf")
//    public String SearchEf(Model model) {
//        SearchVO searchVO = new SearchVO();
//        searchVO.setNumber(2);
//        model.addAttribute("searchEf", searchVO);
//        return "thymeleafEx/searchEf";
//    }
//
//    @PostMapping("/searchEf")
//    public String CheckSearchEf(@ModelAttribute("searchEf") SearchVO searchVO, Model model) {
//        if (searchDAO.search(searchVO).isEmpty()) return "thymeleafEx/searchFail";
//        model.addAttribute("ingredients", searchDAO.search(searchVO));
//        return "thymeleafEx/nutrientsList";
//    }

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
        list.add(voNut);
        session.setAttribute("userNu", voNut);
        model.addAttribute("ingredients", list);
        model.addAttribute("efficacys", boardDAO.boardEFF(searchVO.getData()));
        model.addAttribute("comments", boardDAO.boardList(searchVO.getData()));
        return "thymeleafEx/nutrientsBoard";
    }

    @GetMapping("/comment")
    public String Comment(Model model,HttpSession session) {
        MemberVO memberVO = (MemberVO)session.getAttribute("userInfo");
        NutrientsVO nutrientsVO = (NutrientsVO)session.getAttribute("userNu");
        if(!boardDAO.checkMine(nutrientsVO.getNutrientsName(),memberVO.getId())) {
            model.addAttribute("fail", "이미 작성한 댓글입니다.");
            return "thymeleafEx/commentFail";
        }
        model.addAttribute("comment", new SearchVO());
        return "thymeleafEx/comment";
    }

    @PostMapping("/comment")
    public String checkComment(@ModelAttribute("comment") SearchVO searchVO, HttpSession session,Model model) {
        NutrientsVO nutrientsVO = (NutrientsVO) session.getAttribute("userNu");
        MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
        if (searchVO.getData().isEmpty()) {
            model.addAttribute("fail", "내용을 입력하세요.");
            return "thymeleafEx/commentFail";
        }
        boardDAO.comment(nutrientsVO, memberVO, searchVO.getData());
//        SearchVO searchVO3 = new SearchVO();
//        searchVO3.setNumber(1);
//        model.addAttribute("search", searchVO);
//        return "thymeleafEx/main";
          model.addAttribute("fail", "댓글 작성이 완료되었습니다.");
          return "thymeleafEx/commentFail";
    }

    @GetMapping("/commentGood")
    public String CommentGood(Model model,HttpSession session) {
        MemberVO memberVO = (MemberVO)session.getAttribute("userInfo");
        NutrientsVO nutrientsVO = (NutrientsVO)session.getAttribute("userNu");
        if(boardDAO.checkComment(nutrientsVO.getNutrientsName())!= null) {
            model.addAttribute("fail", boardDAO.checkComment(nutrientsVO.getNutrientsName()) );
            return "thymeleafEx/commentFail";
        }
        model.addAttribute("commentGood", new SearchVO());
        return "thymeleafEx/commentGood";
    }

    @PostMapping("/commentGood")
    public String CheckCommentGood(@ModelAttribute("comment") SearchVO searchVO, HttpSession session,Model model) {
        NutrientsVO nutrientsVO = (NutrientsVO) session.getAttribute("userNu");
        MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
        if(!boardDAO.checkCommentGood(searchVO.getNumber(),memberVO.getId())){
            model.addAttribute("fail", "이미 추천한 댓글입니다.");
            return "thymeleafEx/commentFail";
        }
        else if(!boardDAO.checkMine(searchVO.getNumber(),memberVO.getId())) {
            model.addAttribute("fail", "본인이 작성한 댓글입니다.");
            return "thymeleafEx/commentFail";
        }
        else boardDAO.commentGood(searchVO.getNumber(),memberVO.getId());
        boardDAO.updateGoodBoard(boardDAO.checkGood(searchVO.getNumber()),searchVO.getNumber());
        model.addAttribute("fail", "추천 헀습니다.");
        return "thymeleafEx/commentFail";
    }
    @GetMapping("/commentBad")
    public String CommentBad(Model model,HttpSession session) {
        MemberVO memberVO = (MemberVO)session.getAttribute("userInfo");
        NutrientsVO nutrientsVO = (NutrientsVO)session.getAttribute("userNu");
        if(boardDAO.checkComment(nutrientsVO.getNutrientsName())!= null) {
            model.addAttribute("fail", boardDAO.checkComment(nutrientsVO.getNutrientsName()) );
            return "thymeleafEx/commentFail";
        }
        model.addAttribute("comment", new SearchVO());
        return "thymeleafEx/commentBad";
    }

    @PostMapping("/commentBad")
    public String CheckCommentBad(@ModelAttribute("comment") SearchVO searchVO, HttpSession session,Model model) {
        NutrientsVO nutrientsVO = (NutrientsVO) session.getAttribute("userNu");
        MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
        if(!boardDAO.checkCommentBad(searchVO.getNumber(),memberVO.getId())){
            model.addAttribute("fail", "이미 비추천한 댓글입니다.");
            return "thymeleafEx/commentFail";
        }
        else if(!boardDAO.checkMine(searchVO.getNumber(),memberVO.getId())) {
            model.addAttribute("fail", "본인이 작성한 댓글입니다.");
            return "thymeleafEx/commentFail";
        }
        else boardDAO.commentBad(searchVO.getNumber(),memberVO.getId());
        boardDAO.updateBadBoard(boardDAO.checkBad(searchVO.getNumber()),searchVO.getNumber());
        if(boardDAO.checkBad(searchVO.getNumber()) > 2) {
            boardDAO.deleteBad(searchVO.getNumber());
            boardDAO.deleteGood(searchVO.getNumber());
            boardDAO.deleteContent(searchVO.getNumber());
            model.addAttribute("fail", "비추천 누적으로 댓글이 삭제되었습니다.");
            return "thymeleafEx/commentFail";
        }
        model.addAttribute("fail", "비추천 헀습니다.");
        return "thymeleafEx/commentFail";
    }
    @GetMapping("/wishList")
    public String wishList(Model model, HttpSession session) {
        NutrientsVO nutrientsVO = (NutrientsVO) session.getAttribute("userNu");
        MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
        if(boardDAO.checkWish(nutrientsVO.getNutrientsName(),memberVO.getId()) != 0) {
            model.addAttribute("fail", "이미 찜한 상품입니다.");
            return "thymeleafEx/wishRst";
        }
        else boardDAO.wishIn(nutrientsVO.getNutrientsName(),memberVO.getId());
        model.addAttribute("fail", "찜이 완료되었습니다.");
        return "thymeleafEx/wishRst";
    }

    @GetMapping("/mypage")
    public String myPage(Model model, HttpSession session) {
        MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
        model.addAttribute("myNick", memberVO.getNick());
        return "thymeleafEx/myPage";
    }

    @GetMapping("/myWish")
    public String myWish(Model model, HttpSession session) {
        MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
        if (boardDAO.wishList(memberVO.getId()).isEmpty()) {
            model.addAttribute("fail", "찜목록이 존재하지 않습니다.");
            return "thymeleafEx/wishRst";
        }
        model.addAttribute("myWish",boardDAO.wishList(memberVO.getId()));
        return "thymeleafEx/myWish";
        }


    @GetMapping("/canWish")
    public String canWish(Model model, HttpSession session) {
        model.addAttribute("canWish",new SearchVO());
        return "thymeleafEx/canWish";
    }

@PostMapping("/canWish")
public String checkCanWish(@ModelAttribute("canWish") SearchVO searchVO, Model model, HttpSession session) {
    MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
    if (boardDAO.checkWish(searchVO.getData(),memberVO.getId()) == 2) {
        model.addAttribute("fail", "검색결과가 2개 이상 존재합니다. 정확한 이름을 입력해 주세요");
        return "thymeleafEx/canWishRst";
    } else if (boardDAO.checkWish(searchVO.getData(),memberVO.getId()) == 0) {
        model.addAttribute("fail", "검색결과가 존재하지 않습니다. 정확한 이름을 입력해 주세요");
        return "thymeleafEx/canWishRst";
    } else {
        boardDAO.deleteWish(searchVO.getData(),memberVO.getId());
        model.addAttribute("fail", "찜삭제가 완료되었습니다.");
        return "thymeleafEx/canWishRst";
    }
    }

    @GetMapping("/myInfo")
    public String myInfo(Model model, HttpSession session) {
        MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
        model.addAttribute("myInfoVo", memberVO);
        String jumin = memberVO.getJumin().substring(0,6) + "-" + memberVO.getJumin().charAt(6) + "*".repeat(6);
        model.addAttribute("juminS",jumin);
        String gender = myInfoDAO.gender_output(memberVO.getJumin());
        model.addAttribute("gender",gender);
        int age = myInfoDAO.getAmericanAge(memberVO.getJumin());
        model.addAttribute("age",age);
        return "thymeleafEx/myInfo";
    }
    @PostMapping("/myInfo")
    public String modMyInfo(@ModelAttribute("myInfoVo") MemberVO myInfoVO,
                            @ModelAttribute("juminS") String juminS,
                            @ModelAttribute("gender") String gender,
                            @ModelAttribute("age") String age,
                            HttpSession session,Model model) {
        myInfoDAO.updateMyInfo(myInfoVO);
        session.setAttribute("userInfo",myInfoVO);
//        SearchVO searchVO = new SearchVO();
//        searchVO.setNumber(1);
//        model.addAttribute("search", searchVO);
//        return "thymeleafEx/main";
        model.addAttribute("fail", "개인정보 수정이 완료되었습니다.");
        return "thymeleafEx/myInfoRst";
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
        List<BoardVO> myList= boardDAO.searchBoard(memberVO);
        List<BoardVO> myGoodList= boardDAO.goodBoardList(memberVO);
        List<BoardVO> myBadList= boardDAO.badBoardList(memberVO);
        if(myList.isEmpty() & myGoodList.isEmpty() & myBadList.isEmpty()) {
            model.addAttribute("fail","댓글이 존재하지 않습니다.");
            return "thymeleafEx/commentFail";
        }
        model.addAttribute("myComment", myList);
        model.addAttribute("myGoodComment", myGoodList);
        model.addAttribute("myBadComment", myBadList);
        return "thymeleafEx/myComment";
    }

    @GetMapping("/modComment")
    public String modComment(Model model,HttpSession session) {
        MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
        List<BoardVO> myList= boardDAO.searchBoard(memberVO);
        model.addAttribute("modComment", new SearchVO());
        if(myList.isEmpty()) {
            model.addAttribute("fail","작성한 댓글이 없습니다.");
            return "thymeleafEx/commentFail";
        }
        return "thymeleafEx/modComment";
    }

    @PostMapping("/modComment")
    public String checkModComment(@ModelAttribute("modComment") SearchVO searchVO,Model model,HttpSession session) {
        MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
        if (!boardDAO.checkMyContent(searchVO.getNumber(),memberVO.getId())) {
            model.addAttribute("fail","내가 작성한 댓글이 존재하지 않습니다.");
            return "thymeleafEx/commentFail";
        }
        else if(searchVO.getData().isEmpty()) {
            model.addAttribute("fail","내용을 입력하세요");
            return "thymeleafEx/commentFail";
        }
        boardDAO.updateContent(searchVO.getNumber(), searchVO.getData());
//        SearchVO searchVO1 = new SearchVO();
//        searchVO1.setNumber(1);
//        model.addAttribute("search", searchVO1);
//        return "thymeleafEx/main";
          model.addAttribute("fail", "댓글 수정이 완료되었습니다.");
          return "thymeleafEx/commentFail";
    }

    @GetMapping("/delComment")
    public String delComment(Model model,HttpSession session) {
        model.addAttribute("delComment", new SearchVO());
        MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
        List<BoardVO> myList= boardDAO.searchBoard(memberVO);
        if(myList.isEmpty()) {
            model.addAttribute("fail","작성한 댓글이 없습니다.");
            return "thymeleafEx/commentFail";
        }
        return "thymeleafEx/delComment";
    }
    @PostMapping("/delComment")
    public String checkDelComment(@ModelAttribute("delComment") SearchVO searchVO,Model model,HttpSession session) {
        MemberVO memberVO =(MemberVO) session.getAttribute("userInfo");
        if (!boardDAO.checkMyContent(searchVO.getNumber(),memberVO.getId())) {
            model.addAttribute("fail","내가 작성한 댓글이 존재하지 않습니다.");
            return "thymeleafEx/commentFail";
        }
        boardDAO.deleteContent(searchVO.getNumber());
        boardDAO.deleteBad(searchVO.getNumber());
        boardDAO.deleteGood(searchVO.getNumber());
//        SearchVO searchVO2 = new SearchVO();
//        searchVO2.setNumber(1);
//        model.addAttribute("search", searchVO2);
//        return "thymeleafEx/main";
        model.addAttribute("fail", "댓글 삭제가 완료되었습니다.");
        return "thymeleafEx/commentFail";
    }

    @GetMapping("/canComment")
    public String canComment(Model model,HttpSession session) {
        MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
        List<BoardVO> myGoodList= boardDAO.goodBoardList(memberVO);
        List<BoardVO> myBadList= boardDAO.badBoardList(memberVO);
        model.addAttribute("canComment", new SearchVO());
        if(myGoodList.isEmpty() & myBadList.isEmpty()) {
            model.addAttribute("fail","취소할 댓글이 없습니다.");
            return "thymeleafEx/commentFail";
        }
        return "thymeleafEx/canComment";
    }

    @PostMapping("/canComment")
    public String checkcanComment(@ModelAttribute("canComment") SearchVO searchVO,HttpSession session,Model model) {
        MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
        if (!boardDAO.checkCommentGood(searchVO.getNumber(),memberVO.getId())){
            boardDAO.deleteMyGood(searchVO.getNumber(),memberVO.getId());
            boardDAO.updateGoodBoard(boardDAO.checkGood(searchVO.getNumber()),searchVO.getNumber());
            model.addAttribute("fail","추천을 취소하였습니다. ");
            return "thymeleafEx/commentFail";
        }
        else if(!boardDAO.checkCommentBad(searchVO.getNumber(),memberVO.getId())) {
            boardDAO.deleteMyBad(searchVO.getNumber(),memberVO.getId());
            boardDAO.updateBadBoard(boardDAO.checkBad(searchVO.getNumber()),searchVO.getNumber());
            model.addAttribute("fail","비추천을 취소하였습니다. ");
            return "thymeleafEx/commentFail";
        }
        else {
            model.addAttribute("fail","취소할 댓글이 존재하지 않습니다.");
            return "thymeleafEx/commentFail";
        }
    }
}
