package shop.mtcoding.blog.user;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRespository {
    private final EntityManager em;

    // 쓸 일은 없지만 cache
    // PK로 찾을 때 사용
    public User findById(int id) {
        // find(어느 객체 안에서 찾을 건지, PK)
        return em.find(User.class, id); // em.find = pc에서 찾는 것
    }

    /*
        1. createNativeQuery -> 기본 쿼리
        2. createQuery -> JPA가 제공해주는 객체 지향 쿼리 (db가 변경되어도 거기에 맞춰서
        3. NamedQuery -> Query Method는 함수 이름으로 쿼리 생성 - 사용X
        4. EntityGraph -> 지금 이해 못함 -> 연관 관계 배우고 나서(?
    */

    // u : User 객체 안의 모든 필드를 가리킴
    // ? 대신 키워드 바인딩
    public User findByUsername(String username) {
        return em.createQuery("select u from User u where u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    public void save(User user) {
        // persist = pc에 넣는 것
        em.persist(user); // 2. user : 영속 객체
        // 3. user : db와 동기화 됨
    }

}
