package NumUtil;

import android.text.TextUtils;

import com.nunet.utils.StringUtils;

public class IntegerParse {

	public static int parseInt(String mString) {

		if (TextUtils.isEmpty(mString))
			return 0;
		try {
			int parseInt = java.lang.Integer.parseInt(mString);
			return parseInt;
		} catch (Exception e) {
			e.printStackTrace();
			if (StringUtils.equalsNoCase(mString, "True")
					|| StringUtils.equalsNoCase(mString, "Yes"))
				return 1;
			return 0;
		}
	}
}
