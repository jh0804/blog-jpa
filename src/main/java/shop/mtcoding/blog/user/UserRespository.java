package shop.mtcoding.blog.user;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRespository {
    private final EntityManager em;

    public User findById(Integer id) {
        return em.find(User.class, id);
    }

    /*
        1. createNativeQuery -> 기본 쿼리
        2. createQuery -> JPA가 제공해주는 객체 지향 쿼리
        3. NamedQuery -> Query Method는 함수 이름으로 쿼리 생성 - 사용X
        4. EntityGraph -> 지금 이해 못함 -> 연관 관계 배우고 나서(?
    */

    // u : User 객체 안의 모든 필드를 가리킴
    // ? 대신 키워드 바인딩
    public User findByUsername(String username) {
        try {
            return em.createQuery("select u from User u where u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public void save(User user) {
        em.persist(user);
    }

}
