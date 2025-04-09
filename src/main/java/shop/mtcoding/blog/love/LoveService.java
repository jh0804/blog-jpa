package shop.mtcoding.blog.love;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public LoveResponse.DeleteDTO 좋아요취소(Integer id) {
        Love lovePs = loveRepository.findById(id);
        if (lovePs == null) throw new RuntimeException("취소할 수 있는 좋아요가 없습니다.");

        // 권한체크() : lovePs.getUser().getId()와 sessionUserId 비교

        Integer boardId = lovePs.getBoard().getId();

        loveRepository.deleteById(id);

        Long loveCount = loveRepository.findByBoardId(boardId);

        return new LoveResponse.DeleteDTO(loveCount);
    }
}
