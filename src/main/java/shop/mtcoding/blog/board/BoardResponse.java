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
        private Integer size;
        private Integer totalCount;
        private Integer totalPage;
        private Integer current; // service에서 integer page는 currentPasge인데 넘어가는 값이 아니므로 current가 따로 있어야
        private Boolean isFirst; // currentPage를 알아야 된다.
        private Boolean isLast; // totalCount(<- 그룹함수로 count해야 됨), size = 3, totalPage == current
        private List<Integer> numbers; // 20개 [1, 2, 3, 4, 5, 6, 7] -> model.numbers -> 오브젝트 필드명이 없음 => {{.}}

        public MainDTO(List<Board> boards, Integer current, Integer totalCount) {
            this.boards = boards;
            this.prev = current - 1;
            this.next = current + 1;
            this.size = 3; // 일관성 유지를 위해 원래는 final로 만들어놓고 써야 됨
            this.totalCount = totalCount;  // given으로 두고 먼저 만들고 나서 수정하면 됨! 현재 boards.size()는 3이므로 db에서 들고와야 됨!
            this.totalPage = makeTotalPage(totalCount, size);
            System.out.println("totalPage: " + totalPage);
            this.isFirst = current == 0;
            // 마지막 페이지
            this.isLast = (totalPage-1) == current; // current는 0부터고 페이지는 2이므로
//            System.out.println("isLast: " + isLast); // 디버깅

            this.numbers = makeNumbers(current, totalPage);
//            System.out.println("numbers: " + numbers);

        }

        private Integer makeTotalPage(int totalCount, int size) {
            // rest = 남은 페이지
            int rest = totalCount % size > 0 ? 1 : 0; // 6 - > 0, 7 -> 1, 8 -> 2
            return totalCount / size + rest; // 전체 페이지
        }

//        private List<Integer> makeNumbers(int totalPage, int pageNumberSize) {
//            for (int i = 0; i < totalPage; i++) {
//                this.numbers.add(i);
//            }
//            return numbers;
//        }

        // 현재 페이지가 0~4이면 current / pageNumberSize = 0 => numbers = [0, 1, 2, 3, 4]
        // 현재 페이지가 5~6이면 current / pageNumberSize = 1 => numbers = [5, 6]
        // current % pageNumberSize = 0 ~ 4
        // totalPage = 7
        private List<Integer> makeNumbers(int current, int totalPage) {
            List<Integer> numbers = new ArrayList<>();

            int start = (current / 5) * 5;
            int end = Math.min(start + 5, totalPage);

            for (int i = start; i < end; i++) {
                numbers.add(i);
            }

            return numbers;
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
