package ku.project.validation;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CaptchaConstraintValidator implements ConstraintValidator<ValidCaptcha, Object> {

    private String captcha;
    private String hiddenCaptcha;
    private String message;

    public void initialize(ValidCaptcha constraintAnnotation) {
        this.captcha = constraintAnnotation.captcha();
        this.hiddenCaptcha = constraintAnnotation.hiddenCaptcha();
        this.message = constraintAnnotation.message();
    }

    public boolean isValid(Object value,
            ConstraintValidatorContext context) {

        Object captchaValue = new BeanWrapperImpl(value)
                .getPropertyValue(captcha);
        Object hiddenCaptchaValue = new BeanWrapperImpl(value)
                .getPropertyValue(hiddenCaptcha);

        System.out.printf("captcha= %s and hidden = %s\n", captchaValue, hiddenCaptchaValue);
        boolean isValid = false;
        if (captchaValue != null) {
            isValid = captchaValue.equals(hiddenCaptchaValue);
        }

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(captcha)
                    .addConstraintViolation();
        }
        return isValid;

    }

}
