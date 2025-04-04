package shop.mtcoding.blog.user;

import lombok.Data;

public class UserRequest {

    // insert 용도의 DTO에는 toEntity 메서드를 만든다.
    @Data
    public static class JoinDTO {
        private String username;
        private String password;
        private String email;

        // static이 안붙었기 때문에 JoinDTO가 new가 되어야 쓸 수 있음
        // DTO -> User 객체로 변환
        public User toEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .build();
        }
    }

    @Data
    public static class LoginDTO {
        private String username;
        private String password;
        private String rememberMe; // check 되면 on, 안되면 null 
    }
}
