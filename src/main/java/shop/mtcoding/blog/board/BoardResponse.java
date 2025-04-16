package shop.mtcoding.blog.board;

import lombok.Data;
import shop.mtcoding.blog.board.reply.Reply;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BoardResponse {

    // 페이징 -> 현재 페이지 번호와 다음 페이지 번호를 넘겨줘야 됨 -> DTO 만듦
    @Data
    public static class MainDTO {
        List<Board> boards;
        private Integer prev;
        private Integer next;
        private Boolean isFirst;
        private Boolean isLast;

        public MainDTO(List<Board> boards, Integer prev, Integer next) {
            this.boards = boards;
            this.prev = prev;
            this.next = next;
        }
    }

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

        private List<ReplyDTO> replies;

        // DetailDTO 안에서만 쓸거니까 내부클래스로
        @Data
        public class ReplyDTO {
            private Integer id;
            private String content;
            // 유저 객체가 굳이 필요 없고 username만 필요
            private String username;
            private Boolean isOwner;

            // Board의 Reply를 for문 돌려서 여기에 옮기면 됨
            public ReplyDTO(Reply reply, Integer sessionUserId) {
                this.id = reply.getId();
                this.content = reply.getContent();
                this.username = reply.getUser().getUsername(); // join했기 때문에 Lazy 로딩 X
                this.isOwner = reply.getUser().getId().equals(sessionUserId); // wrapping class는 equals로 비교
            }
        }

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

            // ReplyDTO를 repliesDTO 컬렉션으로 옮기기
            List<ReplyDTO> repliesDTO = new ArrayList<>();
            for (Reply reply : board.getReplies()) {
                ReplyDTO replyDTO = new ReplyDTO(reply, sessionUserId);
                repliesDTO.add(replyDTO);
            }
            this.replies = repliesDTO;
        }
    }
}
