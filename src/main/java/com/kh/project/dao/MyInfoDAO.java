package com.kh.project.dao;

import com.kh.project.common.Common;
import com.kh.project.vo.MemberVO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MyInfoDAO {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

//    public MemberVO myInfo() {
//        MemberVO vo = null;
//        try {
//            conn = Common.getConnection();
//            stmt = conn.createStatement();
//            String query = "SELECT RPAD(SUBSTR(USER_PW,1,3),LENGTH (USER_PW),'*') AS \"USER_PW\",USER_NAME,USER_NICK FROM MEMBER WHERE USER_ID = '" + Main.myId + "'";
//            rs = stmt.executeQuery(query);
//
//            while (rs.next()) {
//                String name = rs.getString("USER_NAME");
//                String pw = rs.getString("USER_PW");
//                String nickName = rs.getString("USER_NICK");
//
//                vo = new MemberVO();
//                vo.setName(name);
//                vo.setPw(pw);
//                vo.setNick(nickName);
//            }
//            Common.close(rs);
//            Common.close(stmt);
//            Common.close(conn);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return vo;
//    }

//    void updateMyInfo(int sel,String data) {
//        String query = null;
//        switch (sel) {
//            case 1:
//                query = "UPDATE MEMBER "
//                        + " SET USER_NAME = " + "'" + data + "'"
//                        + " WHERE USER_ID = " + "'" + Main.myId + "'";
//                break;
//            case 2:
//                query = "UPDATE MEMBER "
//                        + " SET USER_PW = " + "'" + data + "'"
//                        + " WHERE USER_ID = " + "'" + Main.myId + "'";
//                break;
//
//            case 3:
//                query = "UPDATE MEMBER "
//                        + " SET USER_NICK = " + "'" + data + "'"
//                        + " WHERE USER_ID = " + "'" + Main.myId + "'";
//                Main.myNickName = data;
//                break;
//        }
//        try {
//            conn = Common.getConnection();
//            stmt = conn.createStatement();
//            int ret = stmt.executeUpdate(query);
//            System.out.println("Return : " + ret);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        Common.close(stmt);
//        Common.close(conn);
//    }
//    void deleteMyInfo() {
//        try {
//            conn = Common.getConnection();
//            stmt = conn.createStatement();
//            String query = "DELETE FROM MEMBER WHERE USER_ID = '" + Main.myId + "'";
//            int ret = stmt.executeUpdate(query);
//            System.out.println("Return : " + ret);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Common.close(stmt);
//        Common.close(conn);
//    }
}