package cn.com.bjjdsy.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeUtils {

	public static final String PATTERN = "yyyyMMddHHmmss";

	public static LocalDateTime parseStringToDateTime(String time, String format) {
		DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
		return LocalDateTime.parse(time, df);
	}
}
