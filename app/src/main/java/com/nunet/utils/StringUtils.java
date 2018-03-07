package com.nunet.utils;

import android.text.TextUtils;

public class StringUtils {

	public static boolean equalsNoCase(String a, String b) {
		if (!TextUtils.isEmpty(a))
			a = a.toLowerCase().trim();

		if (!TextUtils.isEmpty(a))
			b = b.toLowerCase().trim();
		return TextUtils.equals(a, b);
	}

}
