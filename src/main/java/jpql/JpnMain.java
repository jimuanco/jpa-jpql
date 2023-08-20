package jpql;

import javax.persistence.*;
import java.util.List;

public class JpnMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

/*
        try {

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
//            List<Member> resultList = query1.getResultList();
//
//            for (Member member1 : resultList) {
//                System.out.println("member1 = " + member1);
//            }

//            Member result = query1.getSingleResult();
//            //Spring Data JPA -> 결과 없으면 null, Optional 반환 (NoResultException을 try, catch로 잡음)
//            System.out.println("result = " + result);

//            TypedQuery<Member> query4 = em.createQuery("select m from Member m where m.username = :username", Member.class);
//            query4.setParameter("username", "member1");
//            Member singleResult = query4.getSingleResult();
//            System.out.println("singleResult = " + singleResult.getUsername());

            Member result = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();

            System.out.println("result = " + result.getUsername());

            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
            //타입 정보를 받을 수 없을때
            Query query3 = em.createQuery("select m.username, m.age from Member m");

            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
*/

        try {

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            //모든 Member가 영속성 컨텍스트에 관리 된다. (엔티티 프로젝션)
            List<Member> result = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            Member findMember = result.get(0);
            findMember.setAge(20); //영속성 컨텍스트에 관리 된다. -> update 쿼리 나감

            //엔티티 프로젝션, join 쿼리 나감 (묵시적 join)
            List<Team> result2 = em.createQuery("select m.team from Member m", Team.class)
                    .getResultList();

            //위랑 쿼리가 똑같이 나간다. 이렇게 써야한다.
            //join은 성능에 영향을 줄 수 있는, 튜닝할 수 있는 요소들이 많다. 그래서 눈에 보여야 한다. (join을 예측할 수 있다.)
            //운영을 하면서 쿼리 튜닝을 해야 되는 입장에서 join은 명시적으로 해야한다.
            //sql이랑 비슷하게 쓴다고 생각하면 된다.
            List<Team> result3 = em.createQuery("select t from Member m join m.team t", Team.class)
                    .getResultList();

            //임베디드 타입 프로젝션, from에 엔티티 넣어줘야 함
            em.createQuery("select o.address from Order o", Address.class)
                    .getResultList();

            //스칼라 타입 프로젝션, Query 타입으로 조회
//            List resultList = em.createQuery("select distinct m.username, m.age from Member m")
//                    .getResultList();
//
//            Object o = resultList.get(0);
//            Object[] result4 = (Object[]) o;
//            System.out.println("result4 = " + result4[0]);
//            System.out.println("result4 = " + result4[1]);

            //Object[] 타입으로 조회
//            List<Object[]> resultList = em.createQuery("select distinct m.username, m.age from Member m")
//                    .getResultList();
//
//            Object[] result4 = resultList.get(0);
//            System.out.println("result4 = " + result4[0]);
//            System.out.println("result4 = " + result4[1]);

            //new 명령어로 조회
            List<MemberDTO> result4 = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();

            MemberDTO memberDTO = result4.get(0);
            System.out.println("memberDTO = " + memberDTO.getUsername());
            System.out.println("memberDTO = " + memberDTO.getAge());

            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }
}
