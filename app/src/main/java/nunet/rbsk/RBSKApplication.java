package nunet.rbsk;

import android.annotation.SuppressLint;
import android.app.Application;

public class RBSKApplication extends Application {

	private static RBSKApplication instance;

	public static RBSKApplication getContext() {
		return instance;
	}

	@SuppressLint("Assert") @Override
	public void onCreate() {
		super.onCreate();  
		instance = this;
	}

}
