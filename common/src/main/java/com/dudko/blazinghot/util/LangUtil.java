package com.dudko.blazinghot.util;

import java.util.Locale;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

public class LangUtil {
	public static String titleCaseConversion(String inputString) {
		if (StringUtils.isBlank(inputString)) {
			return "";
		}

		if (StringUtils.length(inputString) == 1) {
			return inputString.toUpperCase(Locale.ROOT);
		}

		StringBuffer resultPlaceHolder = new StringBuffer(inputString.length());

		Stream.of(inputString.split(" ")).forEach(stringPart -> {
			if (stringPart.length() > 1) resultPlaceHolder
					.append(stringPart.substring(0, 1).toUpperCase(Locale.ROOT))
					.append(stringPart.substring(1).toLowerCase(Locale.ROOT));
			else resultPlaceHolder.append(stringPart.toUpperCase(Locale.ROOT));

			resultPlaceHolder.append(" ");
		});
		return StringUtils.trim(resultPlaceHolder.toString());
	}
}
