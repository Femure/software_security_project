package ku.review.validation;

import org.passay.*;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

import me.legrange.haveibeenpwned.HaveIBeenPwndApi;
import me.legrange.haveibeenpwned.HaveIBeenPwndException;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context){

        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(12, 128),
                new UppercaseCharacterRule(1),
                new DigitCharacterRule(1),
                new SpecialCharacterRule(1),
                new NumericalSequenceRule(3, false),
                new AlphabeticalSequenceRule(3, false),
                new QwertySequenceRule(3, false),
                new WhitespaceRule()));

        // Try if the password select by the member appeared in password data breach
        HaveIBeenPwndApi hibp = me.legrange.haveibeenpwned.HaveIBeenPwndBuilder.create("Project").build();

        RuleResult result = validator.validate(new PasswordData(password));

        boolean pwned = true;
        String inDataBreach = "";
        // System.out.printf("%s\n", password);
        try {
            pwned = hibp.isPlainPasswordPwned(password);
            if (pwned) {
                inDataBreach = "Your password appeared in password data breach. Try to change it";
            }
        } catch (HaveIBeenPwndException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (result.isValid() && !pwned) {
            return true;
        }
        List<String> messages = validator.getMessages(result);
        String messageTemplate = String.join(" ", messages);
        messageTemplate += inDataBreach;

        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();

        return false;
    }
}
