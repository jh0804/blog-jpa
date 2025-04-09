package shop.mtcoding.blog.love;

import lombok.Data;

public class LoveResponse {

    @Data
    public static class SaveDTO {
        private Integer loveId;
        private Long loveCount;

        // @AllArg~을 안하고 생성자 직접 만드는 이유 = 커스터마이징 가능하니까
        public SaveDTO(Integer loveId, Long loveCount) {
            this.loveId = loveId;
            this.loveCount = loveCount;
        }
    }

    @Data
    public static class DeleteDTO {
        private Long loveCount;

        public DeleteDTO(Long loveCount) {
            this.loveCount = loveCount;
        }
    }
}
