package shop.mtcoding.blog.love;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class LoveRepository {
    private final EntityManager em;

    public Love findByUserIdAndBoardId(Integer userId, Integer boardId) {
        Query query = em.createQuery("select lo from Love lo where lo.user.id = :userId and lo.board.id = :boardId", Love.class);
        query.setParameter("userId", userId);
        query.setParameter("boardId", boardId);
        try {
            return (Love) query.getSingleResult(); // unique 제약조건이므로 동일한 데이터가 있을 수 없기 때문에 Single
        } catch (Exception e) {
            return null;
        }
    }

    public Long findByBoardId(int boardId) {
        Query query = em.createQuery("select count(lo) from Love lo where lo.board.id = :boardId");
        query.setParameter("boardId", boardId);

        Long count = (Long) query.getSingleResult();
        return count;
    }

    public Love save(Love love) {
        em.persist(love);
        return love; // PK가 담긴 love
    }

    public void deleteById(Integer id) {
        em.createQuery("delete from Love lo where lo.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    public Love findById(Integer id) {
        return em.find(Love.class, id);
    }
}