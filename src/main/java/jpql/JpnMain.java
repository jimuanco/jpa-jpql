package jpql;

import javax.persistence.*;
import java.util.Collection;
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

/*
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
*/

/*
        try {
            for(int i = 0; i < 100; i++) {
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                em.persist(member);
            }

            em.flush();
            em.clear();

            //오라클로 페이징 처리시 3 depth로 쿼리 짜야 함
            List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();

            System.out.println("result = " + result.size());
            for (Member member1 : result) {
                System.out.println("member1 = " + member1);
            }

            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
*/

/*
        try {

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
//            member.setUsername("member1");
            member.setUsername("teamA");
            member.setAge(10);

            member.changeTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            //@ManyToOne(fetch = FetchType.LAZY) 안해주면 select 쿼리 한 번 더 나감
            String query = "select m from Member m inner join m.team t";
            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();

            //세타 조인 (Member * Team)
            String query2 = "select m from Member m, Team t where m.username = t.name";
            List<Member> result2 = em.createQuery(query2, Member.class)
                    .getResultList();

            //조인 대상 필터링
            String query3 = "select m from Member m left join m.team t on t.name = 'teamA'";
            List<Member> result3 = em.createQuery(query3, Member.class)
                    .getResultList();

            //연관관계 없는 엔티티 외부 조인 (left 빼면 내부 조인)
            String query4 = "select m from Member m left join Team t on m.username = t.name";
            List<Member> result4 = em.createQuery(query4, Member.class)
                    .getResultList();

            System.out.println("result4 = " + result4);

            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
*/

/*
        try {

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
//            member.setUsername("member1");
            member.setUsername("teamA");
            member.setAge(10);
            member.setType(MemberType.ADMIN);

            member.changeTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

//            String query = "select m.username, 'HELLO', TRUE from Member m " +
//                            "where m.type = jpql.MemberType.ADMIN";
//            List<Object[]> result = em.createQuery(query)
//                    .getResultList();

            String query = "select m.username, 'HELLO', TRUE from Member m " +
                    "where m.type = :userType";

            List<Object[]> result = em.createQuery(query)
                    .setParameter("userType", MemberType.ADMIN)
                    .getResultList();

            for (Object[] objects : result) {
                System.out.println("objects = " + objects[0]);
                System.out.println("objects = " + objects[1]);
                System.out.println("objects = " + objects[2]);
            }

            //엔티티 타입 (상속 관계)
//            em.createQuery("select i from Item i where type(i) = Book", Item.class)
//                            .getResultList();

            String query2 = "select m.username, 'HELLO', TRUE from Member m " +
                    "where m.username is not null";

            String query3 = "select m.username, 'HELLO', TRUE from Member m " +
                    "where m.age between 0 and 10";


            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
*/

/*
        try {

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
//            member.setUsername("member1");
//            member.setUsername("teamA");
//            member.setUsername(null);
            member.setUsername("관리자");
            member.setAge(10);
            member.setType(MemberType.ADMIN);

            member.changeTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

//            String query =
//                    "select " +
//                            "case when m.age <= 10 then '학생요금' " +
//                            "     when m.age >= 60 then '경로요금' " +
//                            "     else '일반요금' " +
//                            "end " +
//                    "from Member m";
//            List<String> result = em.createQuery(query, String.class).
//                    getResultList();
//            for (String s : result) {
//                System.out.println("s = " + s);
//            }

//            String query = "select coalesce(m.username, '이름 없는 회원') from Member m ";
            String query = "select nullif(m.username, '관리자') from Member m ";

            List<String> result = em.createQuery(query, String.class).
                    getResultList();
            for (String s : result) {
                System.out.println("s = " + s);
            }

            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
*/

/*
        try {

            Team team = new Team();
            em.persist(team);

            Member member1 = new Member();
            member1.setUsername("관리자1");
            member1.setTeam(team);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            em.persist(member2);

            em.flush();
            em.clear();

//            String query = "select concat('a', 'b') from Member m ";
//            String query = "select 'a' || 'b' from Member m "; //하이버네이트 제공 (위랑 같음)
//            String query = "select substring(m.username, 2, 3) from Member m";
//            String query = "select locate('de', 'abcdefg') from Member m"; //Integer 반환
            //Integer 반환, 컬렉션 사이즈 반환, 사이즈 0일 때 버그로 s = 0이 찍히지 않음
//            String query = "select size(t.members) from Team t";
//            String query = "select index(t.members) from Team t"; //값 타입 컬렉션에서 @OrderColumn 쓸 때.. 걍 쓰지마
//            String query = "select function('group_concat', m.username) from Member m";
            String query = "select group_concat(m.username) from Member m"; //하이버네이트 제공 (위랑 같음)

            List<String> result = em.createQuery(query, String.class).
                    getResultList();
            for (String s : result) {
                System.out.println("s = " + s);
            }

            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
*/

/*
        try {

            Team team = new Team();
            em.persist(team);

            Member member1 = new Member();
            member1.setUsername("관리자1");
            member1.setTeam(team);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            member2.setTeam(team);
            em.persist(member2);

            em.flush();
            em.clear();

//            String query = "select m.username from Member m";
//
//            List<String> result = em.createQuery(query, String.class).
//                    getResultList();
//
//            for (String s : result) {
//                System.out.println("s = " + s);
//            }

            //묵시적 내부 조인 발생 -> 쿼리 튜닝 어려움
            //join을 어떻게 걸면서 어떻게 쿼리 하는지는 성능에 크게 영향을 미침
            //jpql이랑 sql이랑 비슷하게 짜야한다.
//            String query = "select m.team from Member m";
//
//            List<Team> result = em.createQuery(query, Team.class)
//                    .getResultList();
//
//            for (Team team : result) {
//                System.out.println("team = " + team);
//            }

            //묵시적 내부 조인 발생, 탐색X, size는 얻을 수 있음
//            String query = "select t.members from Team t";
//
//            List<Collection> result = em.createQuery(query, Collection.class)
//                    .getResultList();
//
//            System.out.println("result = " + result);

            //FROM 절에서 명시적 조인을 통해 별칭을 얻어 탐색
            String query = "select m.username from Team t join t.members m";

            List<String> result = em.createQuery(query, String.class)
                    .getResultList();

            for (String s : result) {
                System.out.println("s = " + s);
            }

            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
*/


/*
        try {

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

//            String query = "select m From Member m";
            String query = "select m From Member m join fetch m.team";

            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();

            //지연 로딩 -> 프록시로 들어옴, N + 1
            //즉시 로딩 -> 진짜 객체 들어오나 N + 1
            //페치 조인(+지연 로딩) -> 진짜 객체 + select 쿼리 한 번
            //참고로 그냥 조인 사용(+지연 로딩)시 team의 데이터 조회 없이 그냥 조인만 한다, 프록시 + N + 1
            System.out.println("team class = " + result.get(0).getTeam().getClass());

            for (Member member : result) {
                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
                //회원1, 팀A(SQL)
                //회원2, 팀A(1차캐시)
                //회원3, 팀B(SQL)

                //회원 100명 -> N + 1
                //지연 로딩이든 즉시 로딩이든 결국 N + 1 문제가 발생한다. -> 따라서 페치 조인 사용
            }

            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
*/

/*
        try {

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            //DB 입장에서 일대다 조인은 데이터 뻥튀기 (다대일은 뻥튀기 안됨)
//            String query = "select t From Team t join fetch t.members";
//            String query = "select distinct t From Team t join fetch t.members";
            //컬렉션은 프록시는 아니지만 아무튼 데이터 로딩이 안됐기 때문에 N + 1
            //그냥 조인시 where 절에서 join 된거 쓰면 됨
            String query = "select t From Team t join t.members";

            List<Team> result = em.createQuery(query, Team.class)
                    .getResultList();

            for (Team team : result) {
                System.out.println("team = " + team.getName() + "|members=" + team.getMembers().size());
                for (Member member : team.getMembers()) {
                    System.out.println("-> member = " + member);
                }
            }

            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
*/

/*
        try {

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

//            String query = "select m From Member m join fetch m.team t"; //다대일로 페이징 처리 한다.
            //두개의 team이 조회됨 각각 LAZY 로딩 한 번씩 (쿼리 3번) -> N + 1
            //@BatchSize(size = 100) 설정시 LAZY 로딩으로 members를 가져올 때 List에 담긴 Team을 in query로 100개씩 select 한다.
            String query = "select t from Team t";

            List<Team> result = em.createQuery(query, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();

            System.out.println("result = " + result.size());

            for (Team team : result) {
                System.out.println("team = " + team.getName() + "|members=" + team.getMembers().size());
                for (Member member : team.getMembers()) {
                    System.out.println("-> member = " + member);
                }
            }

            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
*/

/*
        try {

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

//            String query = "select m from Member m where m = :member";
//
//            Member findMember  = em.createQuery(query, Member.class)
//                    .setParameter("member", member1)
//                    .getSingleResult();
//
//            System.out.println("findMember = " + findMember);

            String query = "select m from Member m where m.team = :team";

            List<Member> members = em.createQuery(query, Member.class)
                    .setParameter("team", teamA)
                    .getResultList();

            for (Member member : members) {
                System.out.println("member = " + member);
            }

            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
*/

/*
        try {

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            List<Member> resultList = em.createNamedQuery("Member.findByUsername", Member.class)
                    .setParameter("username", "회원1")
                    .getResultList();

            for (Member member : resultList) {
                System.out.println("member = " + member);
            }

            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
*/

        try {

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

//            em.flush();
//            em.clear();

            //flush 후 벌크 연산 수행한다.
            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();

            //안해주면 데이터 정합성이 맞지 않는다.
            em.clear();

            System.out.println("resultCount = " + resultCount);

            Member findMember = em.find(Member.class, member1.getId());

            System.out.println("findMember = " + findMember.getAge());

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
