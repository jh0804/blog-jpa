package shop.mtcoding.blog.board.reply;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ReplyRepository {
    private final EntityManager em;

    public List<Reply> findAllByBoardId(int boardId) {
        Query query = em.createQuery("select r from Reply r join fetch r.user where r.board.id = :boardId", Reply.class); // inner join
        query.setParameter("boardId", boardId);
        List<Reply> replies = query.getResultList();
        return replies;
    }

    public Reply save(Reply reply) {
        em.persist(reply);
        return reply; // 혹시나 자기자신을 돌려받아야 하는 상황이 생길수도 있으므로
    }

    // pk -> index를 탄다 / 캐싱
    public Reply findById(Integer id) {
        return em.find(Reply.class, id);
    }

    public void deleteById(Integer id) {
        em.createQuery("delete from Reply r where r.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
