package top.cxjfun.common.datasource.nosql.core.annotation;

import top.cxjfun.common.datasource.nosql.core.enums.PrimaryType;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NosqlTablePrimary {

    String value() default "";
    PrimaryType type() default PrimaryType.DEFAULT;
}
