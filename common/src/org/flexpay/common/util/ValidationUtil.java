package org.flexpay.common.util;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Utility class which provides validation functionality 
 */
public class ValidationUtil {

    private static Pattern tcResultPattern = Pattern.compile("^(-)?(\\d)+([\\.,]?\\d{0,4})$");

    public static boolean checkTariffCalculationResultValue(String value) {

        if (StringUtils.isEmpty(value)) {
            return false;
        }
        
        Matcher matcher = tcResultPattern.matcher(value);
        return matcher.matches();
    }
}
