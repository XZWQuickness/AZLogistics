package cn.exz.xugaung.activity.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * SharePreference工具�?
 */
public final class SPutils {

	public static void save(Context context, String key, Object value) {
		@SuppressWarnings("static-access")
		SharedPreferences  sp= context.getSharedPreferences(ConstantValue.PFNAME,
				context.MODE_PRIVATE);
		if (value instanceof String) {
			sp.edit().putString(key, (String) value).commit();
		} else if (value instanceof Boolean) {
			sp.edit().putBoolean(key, (Boolean) value).commit();
		} else if (value instanceof Integer) {
			sp.edit().putInt(key, (Integer) value).commit();
		}
		
	}


	public static String getString(Context context, String key) {
		@SuppressWarnings("static-access")
		SharedPreferences sp = context.getSharedPreferences(ConstantValue.PFNAME,
				context.MODE_PRIVATE);
		return sp.getString(key, "");

	}

	public static Boolean getBoolean(Context context, String key) {
		@SuppressWarnings("static-access")
		SharedPreferences sp = context.getSharedPreferences(ConstantValue.PFNAME,
				context.MODE_PRIVATE);
		return sp.getBoolean(key, false);

	}

	public static int getInt(Context context, String key) {
		@SuppressWarnings("static-access")
		SharedPreferences sp = context.getSharedPreferences(ConstantValue.PFNAME,
				context.MODE_PRIVATE);
		return sp.getInt(key, -1);

	}
}
