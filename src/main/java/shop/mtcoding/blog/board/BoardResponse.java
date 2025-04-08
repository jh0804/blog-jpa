package shop.mtcoding.blog.board;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

public class BoardResponse {

    @AllArgsConstructor
    @Data
    public static class DetailDTO {
        private Integer id;
        private String title;
        private String content;
        private Boolean isPublic;
        private Boolean isOwner; // 값이 안 들어갈 경우: Boolean - null / boolean - 0
        private String username; // User 객체를 다 들고 갈 필요X
        private Timestamp createdAt;
        private Long loveCount; // 그룹함수로 리턴되는 숫자는 Long 타입
        private Boolean isLove;
    }
}
