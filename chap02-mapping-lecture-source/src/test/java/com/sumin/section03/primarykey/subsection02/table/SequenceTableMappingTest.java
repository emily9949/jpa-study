package com.sumin.section03.primarykey.subsection02.table;

import com.sumin.section03.primarykey.subsection01.identity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;

public class SequenceTableMappingTest {

    private static EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initManager() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    public static void closeFactory() {
        entityManagerFactory.close();
    }

    @AfterEach
    public void closeManager() {
        entityManager.close();
    }

    @Test
    public void 식별자_매핑_테스트() {
        com.sumin.section03.primarykey.subsection02.table.Member member = new com.sumin.section03.primarykey.subsection02.table.Member();
        member.setMemberId("user01");
        member.setNickname("홍길동");
        member.setPhone("010-1234-5678");
        member.setEmail("user01@gmail.com");
        member.setAddress("서울시 서초구");
        member.setEnrollDate(new java.util.Date());
        member.setMemberRole("ROLE_MEMBER");
        member.setStatus("Y");

        com.sumin.section03.primarykey.subsection02.table.Member member2 = new com.sumin.section03.primarykey.subsection02.table.Member();
        member2.setMemberId("user02");
        member2.setNickname("유관순");
        member2.setPhone("010-2222-5678");
        member2.setEmail("user02@gmail.com");
        member2.setAddress("서울시 동작구");
        member2.setEnrollDate(new java.util.Date());
        member2.setMemberRole("ROLE_ADMIN");
        member2.setStatus("Y");

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.persist(member);
        entityManager.persist(member2);

        transaction.commit();

        /* 설명. persist 당시에는 부여되지 않은 pk 값으로 commit 이후 조회를 하면 가능할까? */
//        com.sumin.section03.primarykey.subsection01.identity.Member selectedMember = entityManager.find(com.sumin.section03.primarykey.subsection01.identity.Member.class, 1);
//        System.out.println("selectedMember = " + selectedMember);
//
//        Assertions.assertEquals(2, selectedMember.getMemberNo());

        /* 설명. 다중행 조회는 find 로는 안되고 jpql 이라는 문법을 사용해야 가능하다. */
        String jpql = "SELECT A.memberNo FROM member_section03_subsection01 A";
        List<com.sumin.section03.primarykey.subsection01.identity.Member> memberCodeList = entityManager.createQuery(jpql, Member.class).getResultList();
        memberCodeList.forEach(System.out::println);

    }

}
