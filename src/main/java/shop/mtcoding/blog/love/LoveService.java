package shop.mtcoding.blog.love;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.user.User;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor // DI
@Service // IoC
public class LoveService {
    private final LoveRepository loveRepository;

    @Transactional
    public Map<String, Integer> save(LoveRequest.SaveDTO saveDTO, User sessionUser) {
        Love love = saveDTO.toEntity(sessionUser);
        loveRepository.save(love);

        // loveId와 loveCount를 반환
        Map<String, Integer> dto = new HashMap<>();

        Integer loveId = love.getId();
        Integer loveCount = loveRepository.findByBoardId(saveDTO.getBoardId()).size();

        dto.put("loveId", loveId);
        dto.put("loveCount", loveCount);

        return dto;
    }
}
