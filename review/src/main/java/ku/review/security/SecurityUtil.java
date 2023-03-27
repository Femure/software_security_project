package ku.review.security;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.util.StringUtils;

public class SecurityUtil {

    private SecurityUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     *  Remove escape characters like Html/Js scripts from input if present
     * @param str Input string
     * @return sanitize string
     */
    public static String cleanIt(String str) {
        return Jsoup.clean(
                StringEscapeUtils.escapeHtml4(StringEscapeUtils.escapeEcmaScript (StringUtils.replace(str, "'", "''")))
                , Safelist.basic());
    }
}