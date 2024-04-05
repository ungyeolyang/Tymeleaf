package com.kh.project.dao;

import com.kh.project.common.Common;
import com.kh.project.vo.MemberVO;
import com.kh.project.vo.SearchVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class MyInfoDAO {
    Connection conn = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    public MemberVO myInfo(MemberVO memberVO) {
        MemberVO vo = null;
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            String query = "SELECT USER_PW, USER_JUMIN, USER_NAME,USER_NICK,USER_ID FROM MEMBER WHERE USER_ID = '" + memberVO.getId() + "'";
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                String name = rs.getString("USER_NAME");
                String jumin = rs.getString("USER_JUMIN");
                String id = rs.getString("USER_ID");
                String pw = rs.getString("USER_PW");
                String nickName = rs.getString("USER_NICK");

                vo = new MemberVO();
                vo.setId(id);
                vo.setJumin(jumin);
                vo.setName(name);
                vo.setPw(pw);
                vo.setNick(nickName);
            }
            Common.close(rs);
            Common.close(stmt);
            Common.close(conn);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return vo;
    }

    public void updateMyInfo1(SearchVO searchVO, MemberVO memberVO) {
        String query = null;
        switch (searchVO.getNumber()) {
            case 1:
                query = "UPDATE MEMBER "
                        + " SET USER_NAME = " + "'" + searchVO.getData() + "'"
                        + " WHERE USER_ID = " + "'" + memberVO.getId() + "'";
                break;
            case 2:
                query = "UPDATE MEMBER "
                        + " SET USER_PW = " + "'" + searchVO.getData() + "'"
                        + " WHERE USER_ID = " + "'" + memberVO.getId() + "'";
                break;

            case 3:
                query = "UPDATE MEMBER "
                        + " SET USER_NICK = " + "'" + searchVO.getData() + "'"
                        + " WHERE USER_ID = " + "'" + memberVO.getId() + "'";
                break;
        }
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            int ret = stmt.executeUpdate(query);
            System.out.println("Return : " + ret);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(stmt);
        Common.close(conn);
    }

    public void updateMyInfo(MemberVO memberVO) {
        String sql = "UPDATE MEMBER SET USER_ID = ?, USER_PW = ?," +
                "USER_NAME = ?, USER_NICK = ?, USER_JUMIN = ?  " +
                "WHERE USER_ID = ?";

        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memberVO.getId());
            pstmt.setString(2, memberVO.getPw());
            pstmt.setString(3, memberVO.getName());
            pstmt.setString(4, memberVO.getNick());
            pstmt.setString(5,memberVO.getJumin());
            pstmt.setString(6,memberVO.getId());
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(stmt);
        Common.close(conn);
    }
    public void deleteMyInfo(MemberVO memberVO) {
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            String query = "DELETE FROM MEMBER WHERE USER_ID = '" + memberVO.getId() + "'";
            int ret = stmt.executeUpdate(query);
            System.out.println("Return : " + ret);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(stmt);
        Common.close(conn);
    }
    public int getAmericanAge(String jumin_input) {
        // 오늘 날짜
        LocalDate today = LocalDate.now();
        int todayYear = today.getYear();
        int todayMonth = today.getMonthValue();
        int todayDay = today.getDayOfMonth();

        // 주민등록번호를 통해 입력 받은 날짜
        int year = Integer.parseInt(jumin_input.substring(0,2));
        int month = Integer.parseInt(jumin_input.substring(2,4));
        int day = Integer.parseInt(jumin_input.substring(4,6));

        // 주민등록번호 뒷자리로 몇년대인지
        String gender = jumin_input.substring(6,7);
        if(gender.equals("1") || gender.equals("2")) {
            year += 1900;
        } else if(gender.equals("3") || gender.equals("4")) {
            year += 2000;
        } else if(gender.equals("0") || gender.equals("9")) {
            year += 1800;
        }

        // 올해 - 태어난년도
        int americanAge = todayYear - year;

        // 생일이 안지났으면 - 1
        if(month > todayMonth) {
            americanAge--;
        } else if(month == todayMonth) {
            if(day > todayDay) {
                americanAge--;
            }
        }

        return americanAge;
    }

    public String gender_output (String juminBack) {
        // 주민등록번호 뒷자리로 성별출력
        String gender = juminBack.substring(6, 7);
        String gender_output;
        if (gender.equals("1") || gender.equals("3")) {
            return gender_output = "남자";
        } else if (gender.equals("2") || gender.equals("4")) {
            return gender_output = "여자";
        }
        return gender_output = "알 수 없음";
    }
}