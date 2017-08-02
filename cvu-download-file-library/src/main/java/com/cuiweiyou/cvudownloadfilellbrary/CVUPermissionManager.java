package com.cuiweiyou.cvudownloadfilellbrary;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 类的说明：
 *
 * @author：崔维友
 * @version：1.0.0
 * @created：2016/09/028,16/9/28
 */

public class CVUPermissionManager {

	private CVUPermissionManager(){}

	/**
	 * 函数功能：执行权限请求
	 *
	 * @param activity 请求者
	 * @param requestCode 请求区分码
	 * @param permissions 全部的权限
	 *
	 * @return 0:没有需要申请的权限。非0值无需特殊处理，在onRequestPermissionsResult中处理
	 *
	 * @author：崔维友
	 * @version：1.0.0
	 * @time：028,16/9/28_19:07
	 */
	public static int requestPermission(Activity activity, int requestCode, String... permissions) {
		List<String> list = new ArrayList<String>();

		for (int i = 0; i < permissions.length; i++) {

			//进入到这里代表没有权限.
			if (ContextCompat.checkSelfPermission(activity, permissions[i]) != PackageManager.PERMISSION_GRANTED) {

				if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])) {//已经禁止提示了

					Toast.makeText(activity, "请从系统设置重新开启权限", Toast.LENGTH_SHORT).show();
				}

				list.add(permissions[i]);
			}
		}

		if(list.size() > 0) {

			String[] ps = new String[list.size()];
			list.toArray(ps);

			ActivityCompat.requestPermissions(activity, ps, requestCode);
		} else {

			return 0;
		}

		return -1;
	}
}
