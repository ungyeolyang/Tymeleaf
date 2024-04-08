package com.kh.project.dao;
import com.kh.project.common.Common;
import com.kh.project.vo.BoardVO;
import com.kh.project.vo.MemberVO;
import com.kh.project.vo.NutrientsVO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class BoardDAO {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    public int checkWish(String nname,String id) {
        String query = "SELECT NUTRIENTS_NAME FROM WISHLIST WHERE NUTRIENTS_NAME LIKE '%" + nname + "%' AND USER_ID = '" + id + "'";
        List<String> list = new ArrayList<>();
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery(query);
            while (rs.next()) {
                list.add(rs.getString("NUTRIENTS_NAME"));
            }
            if(list.size()>1) return 2;
            else if(list.size() ==1 ) return 1;

        } catch (Exception e) {
            e.printStackTrace();
        }

        Common.close(rs);
        Common.close(stmt);
        Common.close(conn);
        return 0;
    }

    public void wishIn(String nname, String id) {
        String query = "INSERT INTO WISHLIST VALUES('" + nname + "', '" + id + "')";

        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            int ret = stmt.executeUpdate(query);
            System.out.println("Return : " + ret);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Common.close(stmt);
        Common.close(conn);
        System.out.println("찜이 완료되었습니다.");
    }

    public TreeSet<NutrientsVO> wishList(String id) {
        String query = null;
        TreeSet<NutrientsVO> set = new TreeSet<>();
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            query = "SELECT * FROM NUTRIENTS WHERE NUTRIENTS_NAME IN(SELECT NUTRIENTS_NAME FROM WISHLIST WHERE USER_ID = '" + id+"')";

            rs = stmt.executeQuery(query);

            while (rs.next()) {
                String nutrientsName = rs.getString("NUTRIENTS_NAME");
                String ingredientA = rs.getString("INGREDIENT_A");
                String ingredientB = rs.getString("INGREDIENT_B");
                String company = rs.getString("COMPANY");
                String howToTake = rs.getString("HOW_TO_TAKE");

                NutrientsVO vo = new NutrientsVO();
                vo.setNutrientsName(nutrientsName);
                vo.setIngredientA(ingredientA);
                vo.setIngredientB(ingredientB);
                vo.setCompany(company);
                vo.setHowToTake(howToTake);

                set.add(vo);
            }
            if (set.isEmpty()) {
                return set;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Common.close(rs);
        Common.close(stmt);
        Common.close(conn);
        return set;
    }
    public void deleteWish(String str,String id) {
        String query = "DELETE FROM WISHLIST WHERE NUTRIENTS_NAME LIKE '%" + str + "%' AND USER_ID = '"+id+"'";

        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            int ret = stmt.executeUpdate(query);
            System.out.println("Return : " + ret);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Common.close(stmt);
        Common.close(conn);
    }
    public void deleteWishALL(String id) {
        String query = "DELETE FROM WISHLIST WHERE USER_ID = '" + id +"'";

        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            int ret = stmt.executeUpdate(query);
            System.out.println("Return : " + ret);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Common.close(stmt);
        Common.close(conn);
    }
    public boolean checkMine(String str ,String id){
        String query = "SELECT USER_ID FROM BOARD WHERE NUTRIENTS_NAME = '" + str + "'";
        String mine = null;
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery(query);
            while (rs.next()){
                mine = rs.getString("USER_ID");
            }
            if(mine == null) return true;
            else if(mine.equals(id)){
                return false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Common.close(rs);
        Common.close(stmt);
        Common.close(conn);
        return true;
    }

    public boolean checkMine(int num ,String id){
        String query = "SELECT USER_ID FROM BOARD WHERE COMMENT_NO = '" + num + "'";
        String mine = null;
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery(query);
            while (rs.next()){
                mine = rs.getString("USER_ID");
            }
            if(mine == null) return true;
            else if(mine.equals(id)){
                return false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Common.close(rs);
        Common.close(stmt);
        Common.close(conn);
        return true;
    }
    public String  checkComment(String str){
        String query = "SELECT USER_ID FROM BOARD WHERE NUTRIENTS_NAME = '" + str + "'";
        String mine = null;
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery(query);
            while (rs.next()){
                mine = rs.getString("USER_ID");
            }
            if(mine == null){
                return "댓글이 존재하지 않습니다.";
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
    public boolean checkCommentGood(int num, String id){
        List<String> list = new ArrayList<>();
        String str = null;
        String query = "SELECT * FROM GOODBOARD WHERE USER_ID = '" + id + "' AND COMMENT_NO = '" + num + "'" ;

        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery(query);
            if (rs.next()){
                return false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Common.close(rs);
        Common.close(stmt);
        Common.close(conn);
        return true;
    }

    public String checkComment(int num, String str,String id){
        String query = "SELECT USER_ID FROM BOARD WHERE NUTRIENTS_NAME = '" + str + "' AND COMMENT_NO = '" + num + "'" ;
        String mine = null;
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery(query);
            while (rs.next()){
                mine = rs.getString("USER_ID");
            }
            if (mine == null) {
                return "존재하지 않는 댓글입니다.";
            }
            else if (mine.equals(id)){
                return "본인의 댓글입니다.";
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

    public void commentGood(int num,String id){
        String query = "INSERT INTO GOODBOARD VALUES(" + num + ",'" + id +"')";

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
        System.out.println("추천이 완료되었습니다.");
    }

    public boolean checkCommentBad(int num, String id){
        String query = "SELECT * FROM BADBOARD WHERE USER_ID = '" + id + "' AND COMMENT_NO = '" + num + "'" ;

        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery(query);
            if (rs.next()){
                return false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Common.close(rs);
        Common.close(stmt);
        Common.close(conn);
        return true;
    }
    public int checkGood(int num){
        int good = 0;
        String query = "SELECT COUNT(COMMENT_NO) AS COUNT FROM GOODBOARD " +
                " WHERE COMMENT_NO =" + num;
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery(query);
            while (rs.next()){
                good = rs.getInt("COUNT");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Common.close(rs);
        Common.close(stmt);
        Common.close(conn);
        return good;
    }

    public void updateGoodBoard(int num, int cnum){
        String query = "UPDATE BOARD SET GOOD =" + num + " WHERE COMMENT_NO = " + cnum;

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

    public void commentBad(int num,String id){
        String query = "INSERT INTO BADBOARD VALUES(" + num + ",'" + id + "')";

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
        System.out.println("신고가 완료되었습니다.");
    }
    public int checkBad(int num){
        int bad = 0;
        String query = "SELECT COUNT(COMMENT_NO) AS COUNT FROM BADBOARD " +
                " WHERE COMMENT_NO =" + num;

        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery(query);
            while (rs.next()){
                bad = rs.getInt("COUNT");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Common.close(rs);
        Common.close(stmt);
        Common.close(conn);
        return bad;
    }
    public void updateBadBoard(int num, int cnum){
        String query = "UPDATE BOARD SET BAD =" + num + " WHERE COMMENT_NO = " + cnum;

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

    public void deleteBad(int num){
        String query = "DELETE FROM BADBOARD WHERE COMMENT_NO =" + num;

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
    public void deleteGood(int num){
        String query = "DELETE FROM GOODBOARD WHERE COMMENT_NO =" + num;

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
    public void deleteBadAll(String id){
        String query = "DELETE FROM BADBOARD WHERE USER_ID = '" + id+"'";

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
    public void deleteGoodAll(String id){
        String query = "DELETE FROM GOODBOARD WHERE USER_ID = '" + id+"'";

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

    public List<BoardVO> boardList(String str) {
        List<BoardVO> list = new ArrayList<>();
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            String query = "SELECT COMMENT_NO,USER_NICK,RPAD(SUBSTR(USER_ID,1,3),LENGTH (USER_ID),'*') AS \"USER_ID\",CONTENT,GOOD,BAD " +
                    "FROM BOARD WHERE NUTRIENTS_NAME LIKE '%" + str + "%'";

            rs = stmt.executeQuery(query);
            while (rs.next()) {
                BoardVO voB = new BoardVO();
                String num = rs.getString("COMMENT_NO");
                String nick = rs.getString("USER_NICK");
                String id = rs.getString("USER_ID");
                String content = rs.getString("CONTENT");
                int good = rs.getInt("GOOD");
                int bad = rs.getInt("BAD");

                voB.setCommentNo(num);
                voB.setUserNick(nick);
                voB.setUserId(id);
                voB.setContent(content);
                voB.setGood(good);
                voB.setBad(bad);

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
    public List<BoardVO> goodBoardList(MemberVO memberVO) {
        List<BoardVO> list = new ArrayList<>();
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            String query = "SELECT NUTRIENTS_NAME,COMMENT_NO,USER_NICK,RPAD(SUBSTR(USER_ID,1,3),LENGTH (USER_ID),'*') AS \"USER_ID\",CONTENT,GOOD,BAD " +
                    "FROM BOARD WHERE COMMENT_NO IN(SELECT COMMENT_NO FROM GOODBOARD WHERE USER_ID = '" + memberVO.getId() + "')";

            rs = stmt.executeQuery(query);
            while (rs.next()) {
                BoardVO voB = new BoardVO();
                String name = rs. getString("NUTRIENTS_NAME");
                String num = rs.getString("COMMENT_NO");
                String nick = rs.getString("USER_NICK");
                String id = rs.getString("USER_ID");
                String content = rs.getString("CONTENT");
                int good = rs.getInt("GOOD");
                int bad = rs.getInt("BAD");

                voB.setNutrientsName(name);
                voB.setCommentNo(num);
                voB.setUserNick(nick);
                voB.setUserId(id);
                voB.setContent(content);
                voB.setGood(good);
                voB.setBad(bad);

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
    public List<BoardVO> badBoardList(MemberVO memberVO) {
        List<BoardVO> list = new ArrayList<>();
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            String query = "SELECT NUTRIENTS_NAME,COMMENT_NO,USER_NICK,RPAD(SUBSTR(USER_ID,1,3),LENGTH (USER_ID),'*') AS \"USER_ID\",CONTENT,GOOD,BAD " +
                    "FROM BOARD WHERE COMMENT_NO IN(SELECT COMMENT_NO FROM BADBOARD WHERE USER_ID = '" + memberVO.getId() + "')";

            rs = stmt.executeQuery(query);
            while (rs.next()) {
                BoardVO voB = new BoardVO();
                String name = rs. getString("NUTRIENTS_NAME");
                String num = rs.getString("COMMENT_NO");
                String nick = rs.getString("USER_NICK");
                String id = rs.getString("USER_ID");
                String content = rs.getString("CONTENT");
                int good = rs.getInt("GOOD");
                int bad = rs.getInt("BAD");

                voB.setNutrientsName(name);
                voB.setCommentNo(num);
                voB.setUserNick(nick);
                voB.setUserId(id);
                voB.setContent(content);
                voB.setGood(good);
                voB.setBad(bad);

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

    public List<BoardVO> searchBoard(MemberVO memberVO){
        List<BoardVO> list = new ArrayList<>();
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            String query1 = "SELECT * FROM BOARD WHERE USER_ID = '" +memberVO.getId()  + "'";
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

    public void comment(NutrientsVO voNu,MemberVO voMem, String content){
        String query = null;
        query = "INSERT INTO BOARD VALUES (SEQ_COMMENT.NEXTVAL, '"
                + voNu.getNutrientsName() + "', '" + voMem.getId() + "' , '" +
                voMem.getNick() + "' , '" + content + "', 0 , 0)";

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

    public boolean checkMyContent(int num,String id) {
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            String query = "SELECT * FROM BOARD WHERE USER_ID = '" + id  +"' AND COMMENT_NO = '" + num + "'";
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

    public void updateContent(int num,String content){
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

    public void deleteContent(int num) {
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

    public void deleteMyGood(int num,String id) {
        String query = "DELETE FROM GOODBOARD WHERE COMMENT_NO =" + num + "AND USER_ID = '"+ id+"'";
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            int ret = stmt.executeUpdate(query);
            System.out.println("Return : " + ret);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Common.close(stmt);
        Common.close(conn);
    }
    public void deleteMyBad(int num,String id) {
        String query = "DELETE FROM BABOARD WHERE COMMENT_NO =" + num + "AND USER_ID = '"+ id+"'";
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            int ret = stmt.executeUpdate(query);
            System.out.println("Return : " + ret);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Common.close(stmt);
        Common.close(conn);
    }

}
