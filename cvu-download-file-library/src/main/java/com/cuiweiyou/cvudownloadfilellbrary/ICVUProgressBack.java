package com.cuiweiyou.cvudownloadfilellbrary;

import android.os.Handler;
import android.os.Message;

/**
 * 类的说明：下载回调器
 *
 * @author：崔维友
 * @version：1.0.0
 * @created：2016/09/028,16/9/28
 */

public abstract class ICVUProgressBack extends Handler {

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);

		if("max".equals(msg.obj.toString())){
			setProgressMax(msg.what);

		} else if("prs".equals(msg.obj.toString())) {
			onProgressing(msg.what);

		} else if("com".equals(msg.obj.toString())) {
			onComplete();

		} else if("eacces".equals(msg.obj.toString())) {
			onError("eacces");

		} else {
			onError("error");
		}
	}

	/** 正在下载过程中，数据量 */
	public abstract void onProgressing(double progress);
	/** 总共下载的数据量，即文件的大小 */
	public abstract void setProgressMax(double max);
	/** 下载完成 */
	public abstract void onComplete();
	/** 出现意外
	 * @param msg 错误信息*/
	public abstract void onError(String msg);
}
