package shop.mtcoding.blog.board;

import lombok.Data;

import java.sql.Timestamp;

public class BoardResponse {

    @Data
    public static class DetailDTO {
        private Integer id;
        private String title;
        private String content;
        private Boolean isPublic;
        private Boolean isOwner; // 값이 안 들어갈 경우: Boolean - null / boolean - 0
        private Boolean isLove;
        private Long loveCount;
        private String username; // User 객체를 다 들고 갈 필요X
        private Timestamp createdAt;
        private Integer loveId;

        // model에 있는 것을 옮기는 것
        // 깊은 복사 : 객체를 그대로 가져와서 getId 등으로 넣는게 낫다!
        public DetailDTO(Board board, Integer sessionUserId, Boolean isLove, Long loveCount, Integer loveId) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.isPublic = board.getIsPublic();
            this.isOwner = sessionUserId == board.getUser().getId();
            this.username = board.getUser().getUsername();
            this.createdAt = board.getCreatedAt();
            this.isLove = isLove;
            this.loveCount = loveCount;
            this.loveId = loveId;
        }
    }
}
