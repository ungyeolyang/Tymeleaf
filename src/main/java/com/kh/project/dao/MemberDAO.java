package com.kh.project.dao;


import com.kh.project.common.Common;
import com.kh.project.vo.MemberVO;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MemberDAO {
    Connection conn = null;
    Statement stmt = null;
    PreparedStatement  pstmt = null;
    ResultSet rs = null;
    Scanner sc = new Scanner(System.in);

    public String logIn(MemberVO memberVo) {
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            String query = "SELECT USER_ID, USER_PW, USER_NICK FROM MEMBER WHERE USER_ID = '" + memberVo.getId() +"'";
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                if (!memberVo.getPw().equals(rs.getString("USER_PW"))) {
                    return "잘못된 비밀번호 입니다. ";
                }
//            nickName = rs.getString("USER_NICK");
            }
        else {
            return "없는 아이디입니다. ";
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String checkRegist(MemberVO memberVo) {
            if(memberVo.getName().isEmpty()){
                return "이름을 입력하세요.";
            }
            else if(checkJumin(memberVo) != null){
                return checkJumin(memberVo);
            }
            else if(checkId(memberVo) != null){
                return checkId(memberVo);
            }
            else if (memberVo.getPw().length() < 4) {
                return "4자리 이상 비밀번호를 입력하세요. ";
            }
            else if (memberVo.getNick().isEmpty()){
                return "닉네임을 입력하세요.";
            }
        return null;
    }
    public String checkFindId(MemberVO memberVo) {
        String id = null;
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            String query = "SELECT USER_ID FROM MEMBER WHERE USER_NAME = '" + memberVo.getName() +
                    "' AND USER_JUMIN = '" + memberVo.getJumin() +"'";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                id = rs.getString("USER_ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(rs);
        Common.close(stmt);
        Common.close(conn);
        return id;
    }

    public String checkFindPw(MemberVO memberVo) {
        String pw = null;
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            String query = "SELECT USER_PW FROM MEMBER WHERE USER_NAME = '" + memberVo.getName() +
                    "' AND USER_ID = '" + memberVo.getId() +"' AND USER_JUMIN = '" + memberVo.getJumin() +"'";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                pw = rs.getString("USER_PW");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(rs);
        Common.close(stmt);
        Common.close(conn);
        return pw;
    }
    public String checkId(MemberVO memberVo) {
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            String query = "SELECT * FROM MEMBER WHERE USER_ID = '" + memberVo.getId() + "'";
            rs = stmt.executeQuery(query);
            if(memberVo.getId().isEmpty()) {
                return "아이디를 입력해주세요. ";
            }
            else if (rs.next()) {
                return "이미 사용중인 아이디입니다. ";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(rs);
        Common.close(stmt);
        Common.close(conn);
        return null;
    }

    public String checkJumin(MemberVO memberVo) {
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            String query = "SELECT USER_JUMIN FROM MEMBER WHERE USER_JUMIN = '" + memberVo.getJumin() + "'";
            rs = stmt.executeQuery(query);
            if(memberVo.getJumin().isEmpty()) {
                return "주민번호를 입력해주세요. ";
            }
            else if (rs.next()) {
                return "이미 등록된 주민등록번호입니다.";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(rs);
        Common.close(stmt);
        Common.close(conn);
        return null;
    }

    public void regist(MemberVO memberVo) {
        try {
            conn = Common.getConnection();
            String sql = "INSERT INTO MEMBER(USER_ID, USER_PW, USER_NAME, USER_NICK, USER_JUMIN) VALUES (?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, memberVo.getId());
            pstmt.setString(2, memberVo.getPw());
            pstmt.setString(3, memberVo.getName());
            pstmt.setString(4, memberVo.getNick());
            pstmt.setString(5, memberVo.getJumin());
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(pstmt);
        Common.close(conn);
    }

    public void empDelete() {
        System.out.print("삭제할 이름을 입력 하세요 : ");
        String name = sc.next();
        String sql = "DELETE FROM EMP WHERE ENAME = ?";

        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(stmt);
        Common.close(conn);
    }
}
