package shop.mtcoding.blog._core.error.ex;

// Ajax는 이걸 터트려야 함!
public class ExceptionApi403 extends RuntimeException {
    public ExceptionApi403(String message) {
        super(message);
    }
}
