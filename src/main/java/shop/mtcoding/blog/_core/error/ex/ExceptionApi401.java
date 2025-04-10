package shop.mtcoding.blog._core.error.ex;

// Ajax는 이걸 터트려야 함!
public class ExceptionApi401 extends RuntimeException {
    public ExceptionApi401(String message) {
        super(message);
    }
}
