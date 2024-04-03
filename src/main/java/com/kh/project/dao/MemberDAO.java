package com.kh.project.dao;


import com.kh.project.common.Common;
import com.kh.project.vo.MemberVO;

import java.sql.*;
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
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            String query = "SELECT * FROM MEMBER WHERE USER_ID = '" + memberVo.getId() + "'";
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                return "이미 사용중인 아이디입니다. ";
            }
            else if (memberVo.getPw().length() < 4) {
                return "4자리 이상 비밀번호를 입력하세요. ";
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
            String sql = "INSERT INTO MEMBER(USER_ID, USER_PW, USER_NAME, USER_NICK) VALUES (?,?,?,?)";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, memberVo.getId());
            pstmt.setString(2, memberVo.getPw());
            pstmt.setString(3, memberVo.getName());
            pstmt.setString(4, memberVo.getNick());
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
    public void empUpdate() {
        System.out.print("변경할 사원의 이름을 입력 하세요 : ");
        String name = sc.next();
        System.out.print("직책 : ");
        String job = sc.next();
        System.out.print("급여 : " );
        int sal = sc.nextInt();
        System.out.print("성과급 : " );
        int comm = sc.nextInt();

        String sql = "UPDATE EMP SET JOB = ?, SAL = ?, COMM = ? WHERE ENAME = ?";

        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, job);
            pstmt.setInt(2, sal);
            pstmt.setInt(3,  comm);
            pstmt.setString(4, name);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(stmt);
        Common.close(conn);
    }
}
