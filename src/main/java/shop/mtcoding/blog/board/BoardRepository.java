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

    // 그룹함수 -> Long으로 return
    // 일단 로그인 안 했을때부터 생각하고 코드짜기!!!)
    // 1. 로그인 안했을때 -> 4개
    // 2.1 로그인 했을때 => ssar -> 5개
    // 2.2 로그인 했을때 => ssar이 아니면 -> 4개
    // 1. 로그인 안 했을 때 : 4개
    public Long totalCount(String keyword) {
        String sql = "";
        if (!(keyword.isBlank()))
            sql += "select count(b) from Board b where b.isPublic = true and b.title like :keyword";
        else sql += "select count(b) from Board b where b.isPublic = true";
        Query query = em.createQuery(sql, Long.class);
        // keyword를 포함 : title like %keyword%
        if (!(keyword.isBlank())) query.setParameter("keyword", "%" + keyword + "%");
        return (Long) query.getSingleResult();
    }
    public Long totalCount(int userId, String keyword) {
        String sql = "";
        if (!(keyword.isBlank()))
            sql += "select count(b) from Board b where b.isPublic = true or b.user.id = :userId and b.title like :keyword";
        else sql += "select count(b) from Board b where b.isPublic = true or b.user.id = :userId";
        Query query = em.createQuery(sql, Long.class);
        // keyword를 포함 : title like %keyword%
        if (!(keyword.isBlank())) query.setParameter("keyword", "%" + keyword + "%");
        query.setParameter("userId", userId);
        return (Long) query.getSingleResult();
    }

    // locahost:8080?page=0
    public List<Board> findAll(int page, String keyword) {
        String sql ;
        if (keyword.isBlank()) {
            sql = "select b from Board b where b.isPublic = true order by b.id desc";
        }else{
            sql = "select b from Board b where b.isPublic = true and b.title like :keyword order by b.id desc";
        }


        Query query = em.createQuery(sql, Board.class);
        if (!keyword.isBlank()){
            query.setParameter("keyword","%" + keyword + "%");
        }
        // select * from board_tb limit 3, 3;
        query.setFirstResult(page * 3);
        query.setMaxResults(3);

        return query.getResultList();
    }
    
    // 동적 쿼리 vs 오버로딩(추천) vs 동적 바인딩(더 추천)
    // findAll을 합칠 수는 있지만 if가 많아져서 복잡
    public List<Board> findAll(Integer userId, int page, String keyword) {

        String sql ;
        if (keyword.isBlank()){
            sql = "select b from Board b where b.isPublic = true or b.user.id = :userId order by b.id desc";
        }else{
            sql = "select b from Board b where b.isPublic = true or b.user.id = :userId and b.title like :keyword order by b.id desc";
        }

        Query query = em.createQuery(sql, Board.class);
        query.setParameter("userId", userId);
        if (!keyword.isBlank()){
            query.setParameter("keyword","%" + keyword + "%");
        }
        query.setFirstResult(page * 3);
        query.setMaxResults(3);
        return query.getResultList();
    }

//    public List<Board> findAll(Integer userId) {
//        // JPQL은 keyword binding된다.
//        // user는 객체이므로 b.user.id로 조회해야한다.
//        // userId가 null일때도 userId를 조회하면 느려진다. (or 조건에 의해 isPublic이 true인 것들 찾고 다시 userId가 일치하는 것 찾으므로)
//        // 따라서 동적 쿼리가 낫다! => 오버로딩으로 해결할 수도 있다.
//        String s1 = "select b from Board b where b.isPublic = true or b.user.id = :userId order by b.id desc";
//        String s2 = "select b from Board b where b.isPublic = true order by b.id desc";
//
//        Query query = null;
//        if (userId == null) {
//            query = em.createQuery(s2, Board.class);
//        } else {
//            query = em.createQuery(s1, Board.class);
//            query.setParameter("userId", userId);
//        }
//
//        return query.getResultList();
//    }

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
        Query query = em.createQuery("select b from Board b join fetch b.user left join fetch b.replies r left join fetch r.user where b.id = :id order by r.id desc", Board.class);
        query.setParameter("id", id);
        return (Board) query.getSingleResult();
    }
}
