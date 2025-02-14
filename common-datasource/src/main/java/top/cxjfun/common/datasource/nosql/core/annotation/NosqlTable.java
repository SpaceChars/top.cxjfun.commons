package top.cxjfun.common.datasource.nosql.core.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NosqlTable {

    String name() default "";
}
