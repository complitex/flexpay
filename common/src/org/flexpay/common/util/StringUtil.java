package org.flexpay.common.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {
	public static String getRandomString() {
		return System.currentTimeMillis() + "-" + Math.random();
	}
	
	public static List<String> tokenize(String line, String delim) {
		int ind1 = 0;
		int ind2 = -1;
		List<String> fieldList = new ArrayList<String>();
		while((ind2 = line.indexOf(delim, ind1)) != -1) {
		    fieldList.add(line.substring(ind1, ind2));
		    ind1 = ind2 + 1;
		}
		fieldList.add(line.substring(ind1, line.length()));
		
		return fieldList;
	}

}
