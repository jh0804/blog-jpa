package shop.mtcoding.blog.board;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import(BoardRepository.class) // BoardRepository가 메모리(IoC)에 뜬다.
@DataJpaTest // EntityManager, PC가 메모리(IoC)에 뜬다.
public class BoardRepositoryTest {
    @Autowired // DI : JUnit에서는 생성자 주입이 불가능하다.
    private BoardRepository boardRepository;

    @Test
    public void findAll_test() {
        // given -> 함수의 매개변수(e.g. findById에서는 id)
        Integer userId = null;

        // when -> 테스트할 함수 호출 -> 쿼리 확인
        List<Board> boardList = boardRepository.findAll(userId);

        // eye
        for (Board board : boardList) {
            System.out.print(board.getId() + ", " + board.getTitle());
            System.out.println();
        }

        // Lazy -> Board -> User(id=1)
        // Eager -> N+1  -> Board조회 -> 연관된 User 유저 수 만큼 주회
        // Eager -> Join -> 한방쿼리
//        System.out.println("--------------------");
//        boardList.get(0).getUser().getUsername();
//        System.out.println("--------------------");
    }

    /*
     * 1. 쿼리가 어떻게 동작하는지
     * 2. fetch가 없으면
     * 3. b.user가 아니면
     */
    @Test
    public void findByIdJoinUser_test() {
        // given
        Integer boardId = 4;
        Integer userId = 1;
        // when
        BoardResponse.DetailDTO detailDTO = boardRepository.findDetail(boardId, userId);
        // eye
        System.out.println(detailDTO);
    }

}
