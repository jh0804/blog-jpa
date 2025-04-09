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
}
