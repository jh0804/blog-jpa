package shop.mtcoding.blog.love;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class LoveRepository {
    private final EntityManager em;

    public Love findByUserIdAndBoardId(int userId, int boardId) {
        Query query = em.createQuery("select lo from Love lo where lo.user.id = :userId and lo.board.id = :boardId", Love.class);
        query.setParameter("userId", userId);
        query.setParameter("boardId", boardId);
        try {
            return (Love) query.getSingleResult(); // unique 제약조건이므로 동일한 데이터가 있을 수 없기 때문에 Single
        } catch (Exception e) {
            return null;
        }
    }

    public List<Love> findByBoardId(int boardId) {
        Query query = em.createQuery("SELECT lo FROM Love lo where lo.board.id = :boardId");
        query.setParameter("boardId", boardId);
        List<Love> loves = query.getResultList();
        return loves;
    }
}