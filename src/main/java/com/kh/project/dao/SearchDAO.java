package com.kh.project.dao;

import com.kh.project.common.Common;
import com.kh.project.vo.MemberVO;
import com.kh.project.vo.NutrientsVO;
import com.kh.project.vo.SearchVO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.TreeSet;

public class SearchDAO {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    public TreeSet<NutrientsVO> search(SearchVO searchVO){
        String query = null;
        TreeSet<NutrientsVO> set = new TreeSet<>();
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            switch (searchVO.getNumber()) {
                case 1:
                    query = "SELECT NUTRIENTS_NAME,INGREDIENT_A,INGREDIENT_B,COMPANY,HOW_TO_TAKE " +
                            "FROM NUTRIENTS n JOIN INGREDIENT i ON i.INGREDIENT_NAME = n.INGREDIENT_A " +
                            "OR i.INGREDIENT_NAME = n.INGREDIENT_B WHERE INGREDIENT_NAME LIKE" +  "'%"
                            + searchVO.getData() + "%'";
                    break;
                case 2:
                    query = "SELECT NUTRIENTS_NAME,INGREDIENT_A,INGREDIENT_B,COMPANY,HOW_TO_TAKE " +
                            "FROM NUTRIENTS n JOIN INGREDIENT i ON i.INGREDIENT_NAME = n.INGREDIENT_A" +
                            " OR i.INGREDIENT_NAME = n.INGREDIENT_B WHERE EFFICACY_A LIKE '%"
                            + searchVO.getData() + "%' OR EFFICACY_B LIKE '%"
                            + searchVO.getData() + "%' OR EFFICACY_C LIKE '%"
                            + searchVO.getData() + "%'";
                    break;
            }

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
            if(set.isEmpty()) return set;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Common.close(rs);
        Common.close(stmt);
        Common.close(conn);
        return set;
    }
    
}

