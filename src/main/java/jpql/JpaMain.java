package jpql;


import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamA.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setName("회원1");
            member1.setTeam(teamA);
            member1.setAge(10);
            em.persist(member1);

            Member member2 = new Member();
            member2.setName("회원2");
            member2.setTeam(teamA);
            member2.setAge(10);
            em.persist(member2);

            Member member3 = new Member();
            member3.setName("회원3");
            member3.setTeam(teamB);
            member3.setAge(10);
            em.persist(member3);

            em.flush();
            em.clear();

            //반환 타입이 명확할때 --> TypedQuery
            TypedQuery<Member> query1 = em.createQuery("select m from Member m where m.id=10", Member.class);
            TypedQuery<String > query2 = em.createQuery("select m.username from Member m", String.class);
            //반환 타입이 명확하지 않을때 --> Query
            Query query3 = em.createQuery("select m.username, m.age from Member m");

            //결과가 하나로 명확할때 ,하나 이상일때는 query.getResultList()
            Member result = query1.getSingleResult();
            System.out.println("result = " + result);

            //페이징 API
            String jpql = "select m from Member m order by m.name desc";
            List<Member> resultList = em.createQuery(jpql, Member.class)
                    .setFirstResult(10) // 10부터 시작
                    .setMaxResults(20) //20개의 결과가 출력되게
                    .getResultList();

            //fetch Join
            //페치조인과 일반조인의 차이 -> 일반 조인 실행시 연관된 엔티티를 함께 조회하지 않음
            String query = "select m From Member m join fetch m.team";
            List<Member> result1 = em.createQuery(query, Member.class)
                    .getResultList();
            for (Member member : result1) {
                System.out.println("member = " + member.getName() + " , " + member.getTeam().getName());
            }
              //distinct : 같은 식별자를 가진 Team 엔티티 제거
            String query4 = "select distinct t From Team t join fetch t.member";
            List<Team> result2 = em.createQuery(query4, Team.class)
                    .getResultList();
            for (Team team : result2) {
                System.out.println("team = " + team.getName() + " , members= " + member.getTeam().getName());
                for (Member member : team.getMembers()){
                    System.out.println("=> member = " + member);
                }
            }
            //Named 쿼리
            em.createQuery("Member.findByUsername", Member.class)
                    .setParameter("username", "회원1")
                    .getResultList();
            for (Member member : resultList) {
                System.out.println("member =" + member);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
