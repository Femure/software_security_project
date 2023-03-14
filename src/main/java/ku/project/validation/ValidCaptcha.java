package ku.project.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = CaptchaConstraintValidator.class)
@Documented
public @interface ValidCaptcha {

    String message() default "Invalide Captcha!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String captcha();

    String hiddenCaptcha();

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        ValidCaptcha[] value();
    }
}