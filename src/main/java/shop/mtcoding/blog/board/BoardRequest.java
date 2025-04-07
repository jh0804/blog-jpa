package shop.mtcoding.blog.board;

import lombok.Data;
import shop.mtcoding.blog.user.User;

public class BoardRequest {

    @Data
    public static class SaveDTO {
        private String title;
        private String content;
        private String isPublic;

        // insert는 toEntity가 필요하다.
        // query가 아니라 객체로 만들어서 집어넣을 것이므로 toEntity를 만들어야한다.
        public Board toEntity(User user) {
            return Board.builder()
                    .title(title)
                    .content(content)
                    // null 아니면 무조건 true가 됨
                    .isPublic(isPublic == null ? false : true)
                    .user(user) // user 객체 필요 -> 이전에는 session으로 id를 받아왔지만 session 객체 그대로 넣으면 됨
                    .build();
        }
    }

}
