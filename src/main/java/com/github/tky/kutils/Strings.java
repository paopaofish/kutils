package com.github.tky.kutils;

import com.github.tky.kutils.str.InPropertyHandler;
import com.github.tky.kutils.str.TokenHandler;
import com.google.common.base.CaseFormat;

/**
 * 
 * @author Kenny
 *
 */
public class Strings extends org.apache.commons.lang3.StringUtils {

	/**
	 * 将字符串调整成可以在 in 条件中使用的形式<br/>
	 * 
	 * <pre>
	 *      eg: a,b,c 将转化成 'a','b','c'
	 * </pre>
	 * 
	 * @param inList
	 * @return string can append to a sql
	 */
	public static String splitAsInCondation(String inList) {
		String[] list = inList.split(",");
		StringBuffer condation = new StringBuffer(" ");
		for (int i = 0; i < list.length; i++) {
			if (!isBlank(list[i]))
				condation.append("'").append(list[i].trim()).append("',");
		}
		condation.replace(condation.length() - 1, condation.length(), " ");
		return condation.toString();
	}

	/**
	 * 
	 * @param <T>
	 * @param inList
	 * @param handler
	 * @return string can append to a sql
	 * 
	 */
	public static <T> String splitAsInCondation(java.util.List<T> inList, InPropertyHandler<T> handler) {
		StringBuffer condation = new StringBuffer("");
		for (int i = 0; i < inList.size(); i++) {
			String inItem = null;
			if (handler != null) {
				inItem = handler.inItem(inList.get(i));
			} else {
				inItem = inList.get(i).toString();
			}
			if (!isBlank(inItem))
				condation.append("'").append(inItem.trim()).append("',");
		}
		condation.replace(condation.length() - 1, condation.length(), "");
		return condation.toString();
	}

	/**
	 * 将大写开始的驼峰命名转换为小写开始的驼峰命名字符串
	 * 
	 * @param string
	 * @return 小写开始的驼峰命名字符串
	 */
	public static String uppperCamelToLowerCamel(String string) {
		return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, string);
	}

	/**
	 * 将小写开始的驼峰命名转换为大写开始的驼峰命名
	 * 
	 * @param string
	 * @return 大写开始的驼峰命名字符串
	 */
	public static String lowerCamelToUpperCamel(String string) {
		return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, string);
	}

	/**
	 * 将下划线转换为小写开始驼峰命名
	 * 
	 * @param string
	 * @return 小写开始的驼峰命名字符串
	 */
	public static String lowerUnderscoreToLowerCamel(String string) {
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, string);
	}

	/**
	 * 将下划线转换为大写开始驼峰命名
	 * 
	 * @param string
	 * @return 大写开始的驼峰命名字符串
	 */
	public static String lowerUnderscoreToUpperCamel(String string) {
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, string);
	}

	/**
	 * 替换含有占位符的字符串
	 * 
	 * @param text
	 * @param openToken
	 * @param closeToken
	 * @param handler
	 * @return
	 */
	public static String parse(String text, String openToken, String closeToken, TokenHandler handler) {
		if (text == null || text.isEmpty()) {
			return "";
		}
		int start = text.indexOf(openToken);
		if (start == -1) {
			return text;
		}
		char[] src = text.toCharArray();
		int offset = 0;
		final StringBuilder builder = new StringBuilder();
		StringBuilder expression = null;
		do {
			if (start > 0 && src[start - 1] == '\\') {
				builder.append(src, offset, start - offset - 1).append(openToken);
				offset = start + openToken.length();
			} else {
				if (expression == null) {
					expression = new StringBuilder();
				} else {
					expression.setLength(0);
				}
				builder.append(src, offset, start - offset);
				offset = start + openToken.length();
				int end = text.indexOf(closeToken, offset);
				while (end > -1) {
					if (end > offset && src[end - 1] == '\\') {
						expression.append(src, offset, end - offset - 1).append(closeToken);
						offset = end + closeToken.length();
						end = text.indexOf(closeToken, offset);
					} else {
						expression.append(src, offset, end - offset);
						break;
					}
				}
				if (end == -1) {
					builder.append(src, start, src.length - start);
					offset = src.length;
				} else {
					builder.append(handler.handleToken(expression.toString()));
					offset = end + closeToken.length();
				}
			}
			start = text.indexOf(openToken, offset);
		} while (start > -1);
		if (offset < src.length) {
			builder.append(src, offset, src.length - offset);
		}
		return builder.toString();
	}
}
