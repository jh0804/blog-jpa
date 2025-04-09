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

    public Board findById(Integer id) { // LAZY 로딩이므로 join X
        return em.find(Board.class, id); // 이걸 써야 캐싱한다!
    }

    public Board findByIdJoinUser(Integer id) { // Board를 조회할건데 User를 join
        // 객체지향쿼리 - board 객체 안에 있는 user와 join(relation해서 join) 해야하므로 User가 아닌 b.user과 join해야한다.
        // select b : board에 있는 필드만 projection
        // join fetch : b.user 안에 있는 것까지 전부 projection
        Query query = em.createQuery("select b from Board b join fetch b.user u where b.id = :id", Board.class); // inner join의 경우 pk, fk 연결 시 on절 생략 가능
        query.setParameter("id", id);
        return (Board) query.getSingleResult();
    }

    // Eager로 해결 가능하지만 LAZY 쓰고 join 쿼리 그냥 짜라
    public Board findByIdJoinUserAndReplies(Integer id) {
        // 댓글 안의 user와 join 되어야 한다.
        Query query = em.createQuery("select b from Board b join fetch b.user left join fetch b.replies r join fetch r.user where b.id = :id", Board.class);
        query.setParameter("id", id);
        return (Board) query.getSingleResult();
    }
}
