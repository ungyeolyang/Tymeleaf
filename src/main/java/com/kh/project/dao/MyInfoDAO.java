package com.kh.project.dao;

import com.kh.project.common.Common;
import com.kh.project.vo.MemberVO;
import com.kh.project.vo.SearchVO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MyInfoDAO {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    public MemberVO myInfo(MemberVO memberVO) {
        MemberVO vo = null;
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            String query = "SELECT RPAD(SUBSTR(USER_PW,1,3),LENGTH (USER_PW),'*') AS \"USER_PW\",USER_NAME,USER_NICK,USER_ID FROM MEMBER WHERE USER_ID = '" + memberVO.getId() + "'";
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                String name = rs.getString("USER_NAME");
                String id = rs.getString("USER_ID");
                String pw = rs.getString("USER_PW");
                String nickName = rs.getString("USER_NICK");

                vo = new MemberVO();
                vo.setId(id);
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

    public void updateMyInfo(SearchVO searchVO, MemberVO memberVO) {
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
}