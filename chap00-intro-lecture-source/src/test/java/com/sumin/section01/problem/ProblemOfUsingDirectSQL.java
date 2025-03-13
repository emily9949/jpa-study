package com.sumin.section01.problem;

import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProblemOfUsingDirectSQL {

    private Connection con;

    @BeforeEach
    void setConnection() throws SQLException, ClassNotFoundException {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/menudb";
        String user = "root";
        String password = "449966";

        Class.forName(driver);

        con = DriverManager.getConnection(url, user, password);
        con.setAutoCommit(false);
    }

    @AfterEach
    void closeConnection() throws SQLException {
        con.rollback();
        con.close();
    }

    /* 설명. JDBC API 를 이용해 직접 SQL 을 다룰 때 발생할 수 있는 문제점
    *   1. 데이터 변환, SQL 작성, JDBC API 코드 등의 중복 작성 (개발 시간 증가, 유지보수성 저하)
    *   2. SQL 에 의존하여 개발
    *   3. 패러다임 불일치 (상속, 연관관계, 객체 그래프 탐색, 방향성)
    *   4. 동일성 보장 문제
    * */

    @DisplayName("직접 SQL 을 작성하여 메뉴를 조회할 때 발생하는 문제 확인")
    @Test
    void testDirectSelectSql() throws SQLException {

        // given
        String query = "SELECT MENU_CODE, MENU_NAME, MENU_PRICE, CATEGORY_CODE, "
                + "ORDERABLE_STATUS FROM TBL_MENU";

        // when
        Statement stmt = con.createStatement();
        ResultSet rset = stmt.executeQuery(query);

        /* 설명. JDBC 사용의 단점. 쿼리문을 일일히 매핑해주어야 하고
        *    쿼리문이 수정될때마다 클래스의 필드 값, 생성자명 등을
        *   다 수정해주어야 한다. 쿼리에 의존적인 게 문제임
        */
        List<Menu> menuList = new ArrayList<>();
        while (rset.next()) {
            Menu menu = new Menu();
            menu.setMenuCode(rset.getInt("MENU_CODE"));
            menu.setMenuName(rset.getString("MENU_NAME"));
            menu.setMenuPrice(rset.getInt("MENU_PRICE"));
            menu.setCategoryCode(rset.getInt("CATEGORY_CODE"));
            menu.setOrderableStatus(rset.getString("ORDERABLE_STATUS"));

            menuList.add(menu);
        }

        // then
        Assertions.assertTrue(!menuList.isEmpty());
        if(!menuList.isEmpty()) menuList.forEach(System.out::println);

        rset.close();
        stmt.close();
    }

    /* 목차. 2 */
    @DisplayName("연관된 객체 문제 확인")
    @Test
    void testAssociationObject() throws SQLException {

        // given
        String query = "SELECT A.MENU_CODE, A.MENU_NAME, A.MENU_PRICE, B.CATEGORY_CODE, "
                + "B.CATEGORY_NAME, A.ORDERABLE_STATUS "
                + "FROM TBL_MENU A "
                + "JOIN TBL_CATEGORY B ON (A.CATEGORY_CODE = B.CATEGORY_CODE)";

        // when
        Statement stmt = con.createStatement();
        ResultSet rset = stmt.executeQuery(query);

        List<MenuAndCategory> menuAndCategories = new ArrayList<>();
        while (rset.next()) {
            MenuAndCategory menuAndCategory = new MenuAndCategory();
            menuAndCategory.setMenuCode(rset.getInt("MENU_CODE"));
            menuAndCategory.setMenuName(rset.getString("MENU_NAME"));
            menuAndCategory.setMenuPrice(rset.getInt("MENU_PRICE"));
            menuAndCategory.setCategory(new Category(rset.getInt("CATEGORY_CODE"),
                    rset.getString("CATEGORY_NAME")));
            menuAndCategory.setOrderableStatus(rset.getString("ORDERABLE_STATUS"));

            menuAndCategories.add(menuAndCategory);

        }

        // then

        Assertions.assertTrue(!menuAndCategories.isEmpty());
        if(!menuAndCategories.isEmpty()) menuAndCategories.forEach(System.out::println);

        rset.close();
        stmt.close();
    }

    /* 설명. 3. 패러다임 불일치(상속, 연관관계, 객체 그래프 탐색, 방향성) */
    /* 설명. 3-1. 상속 문제
     *  객체 지향 언어의 상속 개념과 유사한 것이 데이터베이스의 서브타입엔티티이다.(서브타입을 별도의 클래스로 나뉘었을 때)
     *  슈퍼타입의 모든 속성을 서브타입이 공유하지 못하여 물리적으로 다른 테이블로 분리가 된 형태이다.
     *  (설계에 따라서는 하나의 테이블로 속성이 추가되기도 한다.)
     *  하지만 객체지향의 상속은 슈퍼타입의 속성을 공유해서 사용하므로 여기에서 패러다임의 불일치가 발생한다.
     * */

    /* 설명. 3-2. 연관관계 문제, 객체 그래프 탐색 문제, 방향성 문제
     *  객체지향에서 말하는 가지고 있는(ASSOCIATION 연관관계 혹은 COLLECTION 연관관계) 경우 데이터베이스 저장 구조와
     *  다른 형태이다.
     *
     * 설명.
     *  - 데이터베이스 테이블에 맞춘 객체 모델
     *  public class Menu {
     *    private int menuCode;
     *    private String menuName;
     *    private int menuPrice;
     *    private int categoryCode;
     *    private String orderableStatus;
     *  }
     *  - 객체 지향 언어에 맞춘 객체 모델
     *  public class Menu {
     *    private int menuCode;
     *    private String menuName;
     *    private int menuPrice;
     *    private Category category;
     *    private String orderableStatus;
     *  }
     *
     * */
}
