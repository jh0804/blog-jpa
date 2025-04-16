package shop.mtcoding.blog._core.error;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import shop.mtcoding.blog._core.error.ex.Exception400;

import java.util.List;

// Aspect, PointCut, Advice
@Aspect // 관점 관리
@Component
public class GlobalValidationHandler {

    // Advice를 통해 관심사(핵심로직)를 분리시킴
    // 400번만 처리
    // PostMapping 혹은 PutMapping (PointCut)이 붙어있는 메서드를 실행하기 직전에 Adivce를 호출하라
    @Before("@annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.PutMapping)") // "@annotation()" = Before 내부에서만 쓰는 문법
    // DS -> badRequestAdvice(Proxy) -> Controller
    public void badRequestAdvice(JoinPoint jp){ // jp는 실행될 실제 메서드의 모든 것을 투영하고 있다.(reflection)
        Object[] args =  jp.getArgs(); // 메서드의 매개변수들 (없어도 0개의 크기로 리턴됨)
        for(Object arg : args){ // 매개변수 개수만큼 반복 (annotation은 제외)
            // instanceOf : 타입 검증 -> 다형성 작동ㅇ?
            // Errors 타입이 매개변수에 존재하고!!
            if(arg instanceof Errors){ // 매개변수가 몇번지에 어떤게 있을지는 랜덤
                System.out.println("에러 400 처리 필요함!!");
                Errors errors = (Errors) arg; // arg는 Object type이므로 다운캐스팅

                // 에러가 존재한다면!!
                if(errors.hasErrors()) {
                    List<FieldError> fErrors = errors.getFieldErrors(); // 에러를 다 들고와서

                    // 필드가 여러 개지만 에러 메세지를 유저에게 여러개 전달X
                    for(FieldError fieldError : fErrors) { // for문 돌려서 Exception
                        // 순서 보장X
                        throw new Exception400(fieldError.getField()+":"+fieldError.getDefaultMessage()); // fieldError.getField() = > e.g. username
                    }
                }
            }

        }
    }
}
