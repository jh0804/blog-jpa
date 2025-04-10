package shop.mtcoding.blog.love;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.error.ex.ExceptionApi403;
import shop.mtcoding.blog._core.error.ex.ExceptionApi404;

@RequiredArgsConstructor // DI
@Service // IoC
public class LoveService {
    private final LoveRepository loveRepository;

    @Transactional
    public LoveResponse.SaveDTO 좋아요(LoveRequest.SaveDTO reqDTO, Integer sessionUserId) {
        Love lovePS = loveRepository.save(reqDTO.toEntity(sessionUserId)); // persist 하기 위해서는 객체가 들어가야한다(id가 아닌 sessionUser)

        Long loveCount = loveRepository.findByBoardId(reqDTO.getBoardId());

        return new LoveResponse.SaveDTO(lovePS.getId(), loveCount);
    }

    @Transactional
    public LoveResponse.DeleteDTO 좋아요취소(Integer id, Integer sessionUserId) {
        Love lovePs = loveRepository.findById(id);
        // TODO - ExceptionApi404
        if (lovePs == null) throw new ExceptionApi404("취소할 수 있는 좋아요가 없습니다."); // 자원을 못 찾는 것 = 404

        // 권한체크() : lovePs.getUser().getId()와 sessionUserId 비교
        if (!(lovePs.getUser().getId().equals(sessionUserId))) {
            throw new ExceptionApi403("권한이 없습니다.");
        }

        Integer boardId = lovePs.getBoard().getId();

        loveRepository.deleteById(id);

        Long loveCount = loveRepository.findByBoardId(boardId);

        return new LoveResponse.DeleteDTO(loveCount);
    }
}
