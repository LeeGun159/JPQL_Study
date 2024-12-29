package jpql;


import jakarta.persistence.*;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Member member = new Member();
            member.setName("member1");
            member.setAge(10);
            em.persist(member);
            //반환 타입이 명확할때 --> TypedQuery
            TypedQuery<Member> query = em.createQuery("select m from Member m where m.id=10", Member.class);
            TypedQuery<String > query2 = em.createQuery("select m.username from Member m", String.class);
            //반환 타입이 명확하지 않을때 --> Query
            Query query3 = em.createQuery("select m.username, m.age from Member m");

            //결과가 하나로 명확할때 ,하나 이상일때는 query.getResultList()
            Member result = query.getSingleResult();
            System.out.println("result = " + result);


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
