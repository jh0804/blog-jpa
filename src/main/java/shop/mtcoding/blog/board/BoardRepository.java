package shop.mtcoding.blog.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepository {
    private final EntityManager em;

    public List<Board> findAll(Integer userId) {
        // JPQL은 keyword binding된다.
        // user는 객체이므로 b.user.id로 조회해야한다.
        // userId가 null일때도 userId를 조회하면 느려진다. (or 조건에 의해 isPublic이 true인 것들 찾고 다시 userId가 일치하는 것 찾으므로)
        // 따라서 동적 쿼리가 낫다! => 오버로딩으로 해결할 수도 있다.
        String s1 = "select b from Board b where b.isPublic = true or b.user.id = :userId order by b.id desc";
        String s2 = "select b from Board b where b.isPublic = true order by b.id desc";

        Query query = null;
        if (userId == null) {
            query = em.createQuery(s2, Board.class);
        } else {
            query = em.createQuery(s1, Board.class);
            query.setParameter("userId", userId);
        }

        return query.getResultList();
    }

    public void save(Board board) {
        em.persist(board);
    }
}
