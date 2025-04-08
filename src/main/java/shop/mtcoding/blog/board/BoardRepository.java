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

    // 한방쿼리 (h2 query, om -> dto)
    // BoardResponse$DetailDTO : static일때만 $를 사용한다.
    public BoardResponse.DetailDTO findDetail(Integer boardId, Integer userId) {
        String sql = """
                SELECT new shop.mtcoding.blog.board.BoardResponse$DetailDTO(
                    b.id,
                    b.title,
                    b.content,
                    b.isPublic,
                    CASE WHEN b.user.id = :userId THEN true ELSE false END,
                    b.user.username,
                    b.createdAt,
                    (SELECT COUNT(l.id) FROM Love l WHERE l.board.id = :boardId),
                    (SELECT CASE WHEN COUNT(l2) > 0 THEN true ELSE false END
                     FROM Love l2
                     WHERE l2.board.id = :boardId AND l2.user.id = :userId)
                )
                FROM Board b
                WHERE b.id = :boardId
                """;
        Query query = em.createQuery(sql);
        query.setParameter("boardId", boardId);
        query.setParameter("userId", userId);
        return (BoardResponse.DetailDTO) query.getSingleResult();
    }

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
}
