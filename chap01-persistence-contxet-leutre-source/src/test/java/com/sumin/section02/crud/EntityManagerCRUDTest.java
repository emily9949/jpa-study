package com.sumin.section02.crud;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class EntityManagerCRUDTest {
    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initManager() {

        /* 설명. EntityManager가 생성될 때마다 고유의 새로운 영속성 컨텍스트(Entity를 관리하는 자바의 창고)가 생성된다. */
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterEach
    public void closeManager() {
        entityManager.close();
    }

    @AfterAll
    public static void closeFactory() {
        entityManagerFactory.close();
    }

    @Test
    public void 메뉴코드로_메뉴_조회_테스트() {

        int menuCode = 2;

        /* 설명. entityManager를 통해 여러번 find를 해도 select은 한번만 동작한다. */
        Menu foundMenu = entityManager.find(Menu.class, menuCode);
//        Menu foundMenu2 = entityManager.find(Menu.class, menuCode);
//        Menu foundMenu3 = entityManager.find(Menu.class, menuCode);

        /* 설명. 단정문은 두 개 이상도 가능 */
        Assertions.assertNotNull(foundMenu);
        Assertions.assertEquals(menuCode, foundMenu.getMenuCode());
        System.out.println("foundMenu = " + foundMenu);
    }

    @Test
    public void 새로운_메뉴_추가_테스트() {
        Menu menu = new Menu();
        menu.setMenuName("꿀발린추어탕");
        menu.setMenuPrice(7000);
        menu.setCategoryCode(4);
        menu.setOrderableStatus("Y");

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            entityManager.persist(menu);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    @Test
    public void 메뉴_이름_수정_테스트() {
        Menu menu = entityManager.find(Menu.class, 2);
        System.out.println("menu = " + menu);

        String menuNameToChange = "갈치스무디";

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            menu.setMenuName(menuNameToChange);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }

    }

    @Test
    public void 메뉴_삭제하기_테스트() {

        /* 설명. 지울 대상을 DB로부터 가져와서 엔티티로 객체로 받음 */
        Menu menuToRemove = entityManager.find(Menu.class, 3);

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            entityManager.remove(menuToRemove);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }


}