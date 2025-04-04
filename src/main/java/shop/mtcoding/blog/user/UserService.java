package shop.mtcoding.blog.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRespository userRespository;

    @Transactional
    public void join(UserRequest.JoinDTO joinDTO) {
        User user = joinDTO.toEntity(); // 1. 비영속 객체
        System.out.println("비영속 user : " + user.getId());
        userRespository.save(user);
        // 4. user : id가 있는 영속 객체 (동기화 된)
        System.out.println("영속/동기화 user : " + user.getId());

        // TODO : 정리하기
//        System.out.println("------------");
//        // select query 안날라간다. -> pc에 이미 있는 값으로 찾으므로
//        userRespository.findById(4); // pc에서 찾는다. = DB가 아닌 상대적으로 가까운 곳에서 찾았기 때문에 캐싱
//        System.out.println("------------");
    }

    public User login(UserRequest.LoginDTO loginDTO) {
        // 1. username 일치 검사
        User user = userRespository.findByUsername(loginDTO.getUsername());
        // 2. username 불일치 -> Exception
        if (user == null) throw new RuntimeException("해당 username이 존재하지 않습니다.");
        // 3. password 불일치 -> Exception
        if (!(user.getPassword().equals(loginDTO.getPassword()))) throw new RuntimeException("password가 일치하지 않습니다.");

        // 4. username & password 전부 일치
        return user;
    }
}
