package top.cxjfun.common.web.thread;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@org.springframework.scheduling.annotation.Async("customServiceExecutor")
public @interface Async {
}
