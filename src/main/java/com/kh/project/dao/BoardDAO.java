package com.kh.project.dao;

import com.kh.project.common.Common;
import com.kh.project.vo.BoardVO;
import com.kh.project.vo.NutrientsVO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class BoardDAO {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    public String checkNut(String name){
        List<String> list = new ArrayList<>();

        String query = "SELECT NUTRIENTS_NAME FROM NUTRIENTS WHERE NUTRIENTS_NAME LIKE '%" + name + "%'";
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery(query);
            while (rs.next()) {
                list.add(rs.getString("NUTRIENTS_NAME"));
            }
            if (list.isEmpty()) {
                return "검색결과가 존재하지 않습니다.";
            }
            else if (list.size()>1){
                return  "검색결과가 2개 이상 존재합니다. 정확한 이름을 입력해 주세요";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Common.close(rs);
        Common.close(stmt);
        Common.close(conn);
        return null;
    }

    public NutrientsVO boardNut(String data) {
        NutrientsVO vo = new NutrientsVO();
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            String query = "SELECT * FROM NUTRIENTS WHERE NUTRIENTS_NAME LIKE '%" + data + "%'";
            rs = stmt.executeQuery(query);
            while (rs.next()){
                vo.setNutrientsName(rs.getString("NUTRIENTS_NAME"));
                vo.setIngredientA(rs.getString("INGREDIENT_A"));
                vo.setIngredientB(rs.getString("INGREDIENT_B"));
                vo.setCompany(rs.getString("COMPANY"));
                vo.setHowToTake(rs.getString("HOW_TO_TAKE"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Common.close(rs);
        Common.close(stmt);
        Common.close(conn);
        return vo;
    }

    public HashSet<String> boardEFF(String data) {
        HashSet<String> set = new HashSet<>();
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            String query1 = "SELECT EFFICACY_A ,EFFICACY_B ,EFFICACY_C " +
                    "FROM NUTRIENTS n JOIN INGREDIENT i ON i.INGREDIENT_NAME = n.INGREDIENT_A OR i.INGREDIENT_NAME = n.INGREDIENT_B " +
                    "WHERE NUTRIENTS_NAME Like '%" + data + "%'";
            rs = stmt.executeQuery(query1);
            while (rs.next()){
                if(rs.getString("EFFICACY_A") != null)set.add(rs.getString("EFFICACY_A"));
                if(rs.getString("EFFICACY_B") != null)set.add(rs.getString("EFFICACY_B"));
                if(rs.getString("EFFICACY_C") != null)set.add(rs.getString("EFFICACY_C"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Common.close(rs);
        Common.close(stmt);
        Common.close(conn);
        return set;
    }

    public List<BoardVO> boardList(String data) {
        BoardVO voB = new BoardVO();
        List<BoardVO> list = new ArrayList<>();
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            String query = "SELECT USER_NICK,RPAD(SUBSTR(USER_ID,1,3),LENGTH (USER_ID),'*') AS \"USER_ID\",CONTENT " +
                    "FROM BOARD WHERE NUTRIENTS_NAME = '%" + data +"%'";

            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String nick = rs.getString("USER_NICK");
                String id = rs.getString("USER_ID");
                String content = rs.getString("CONTENT");

                voB.setUserNick(nick);
                voB.setUserId(id);
                voB.setContent(content);

                list.add(voB);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Common.close(rs);
        Common.close(stmt);
        Common.close(conn);
        return list;
    }

    public List<BoardVO> searchBoard(){
        List<BoardVO> list = new ArrayList<>();
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            String query1 = "SELECT * FROM BOARD WHERE USER_ID = '" +   "'";
            rs = stmt.executeQuery(query1);

            while (rs.next()) {
                String commentNo = rs.getString("COMMENT_NO");
                String nutrientsName = rs.getString("NUTRIENTS_NAME");
                String content = rs.getString("CONTENT");

                BoardVO vo = new BoardVO();
                vo.setCommentNo(commentNo);
                vo.setNutrientsName(nutrientsName);
                vo.setContent(content);

                list.add(vo);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(rs);
        Common.close(stmt);
        Common.close(conn);
        return list;
    }

    public boolean searchBoard(String str){
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            String query1 = "SELECT * FROM BOARD WHERE USER_ID = '" +  "' AND NUTRIENTS_NAME = '" + str + "'";
            rs = stmt.executeQuery(query1);
            if (rs.next()) return false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(rs);
        Common.close(stmt);
        Common.close(conn);
        return true;
    }

    public void comment(NutrientsVO vo,String content){
        String query = null;
        query = "INSERT INTO BOARD VALUES (SEQ_COMMENT.NEXTVAL, '"
                + vo.getNutrientsName() + "', '" +    "' , '" +
                  "' , '" + content + "')";

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

    public boolean checkMyContent(int num) {
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            String query = "SELECT * FROM BOARD WHERE USER_ID = '" +   "' AND COMMENT_NO = '" + num + "'";
            rs = stmt.executeQuery(query);
            if (!rs.next()) {
                System.out.println("존재하지 않는 글입니다.");
                return false;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Common.close(rs);
        Common.close(stmt);
        Common.close(conn);
        return true;
    }

    void updateContent(int num,String content){
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            String query = "UPDATE BOARD SET CONTENT = " + "'" + content + "' "
                    +"WHERE COMMENT_NO = '" + num + "'";

            int ret = stmt.executeUpdate(query);
            System.out.println("Return : " + ret);
        }

        catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(stmt);
        Common.close(conn);
    }

    void deleteContent(int num) {
        Scanner sc = new Scanner(System.in);
        String query1;
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            query1 = "DELETE FROM BOARD WHERE COMMENT_NO = '"+ num + "'";

            int ret = stmt.executeUpdate(query1);
            System.out.println("Return : " + ret);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(stmt);
        Common.close(conn);
    }

    void printBoard(NutrientsVO vo, HashSet<String> set, List<BoardVO> list) {
        System.out.println(vo.getNutrientsName());
        System.out.println("=".repeat(10));
        if(vo.getIngredientB() != null) System.out.println("성분 : " + vo.getIngredientA() + ", " + vo.getIngredientB());
        else System.out.println("성분 : " + vo.getIngredientA());
        System.out.println("효능 : " + set.toString().replace("[","").replace("]",""));
        System.out.println("제조사 : " + vo.getCompany());
        System.out.println("복용법 : " + vo.getHowToTake() + " ");
        if(!list.isEmpty()) {
            for (BoardVO e : list) {
                System.out.println("=".repeat(30));
                System.out.println(e.getUserNick() + "(" + e.getUserId() +") : " + e.getContent());
            }
        }
    }

    void printMyContent(List<BoardVO> list) {
        for (BoardVO e : list) {
            System.out.println("=".repeat(30));
            System.out.println(e.getCommentNo()+" / " + e.getNutrientsName() +" : " + e.getContent());
        }
    }
}
