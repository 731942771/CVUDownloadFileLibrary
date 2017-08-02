package com.cuiweiyou.cvudownloadfilellbrary;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.Message;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 类的说明：文件下载器
 *
 * @author：崔维友
 * @version：1.0.0
 * @created：2016/09/028,16/9/28
 */
public class CVUDownLoadUtil {

	/**
	 * 函数功能：执行下载
	 *
	 * @param remote 远程文件地址
	 * @param local 保存到本地的地址
	 * @param back 回调器
	 *
	 * @return 0:成功，1:SD卡不可用，2:网络失败
	 *
	 * @author：崔维友
	 * @version：1.0.0
	 * @time：028,16/9/28_14:31
	 */
	public static void downloadFile(final String remote, final String local, final ICVUProgressBack back){

		// 在一个子线程中执行下载任务。
		new AsyncTask<Void, Void, Integer>() {
			@Override
			protected Integer doInBackground(Void... params) {

				int res = getFileFromServer(remote, local, back);

				return res;
			}

			@Override
			protected void onPostExecute(Integer res) {
				super.onPostExecute(res);

				if(0 != res && 1 != res && 2 != res){
					Message msg = back.obtainMessage();
					msg.obj = "err";
					back.sendMessage(msg);             // 发回任务失败消息
				}
			}
		}.execute();
	}

	private static int getFileFromServer(String remote, String local, ICVUProgressBack back) {


		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			try {
				URL url = new URL(remote);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5000);

				Message msg = back.obtainMessage();
				msg.obj = "max";
				msg.what = conn.getContentLength();
				back.sendMessage(msg);                  // 发回文件大小

				InputStream is = conn.getInputStream();
				File file = new File(local);

				if (file.exists()) {
					file.delete();
					file.createNewFile();
				}

				FileOutputStream fos = new FileOutputStream(file);
				BufferedInputStream bis = new BufferedInputStream(is);

				byte[] buffer = new byte[1024];
				int len;
				int total = 0;

				while ((len = bis.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					total += len;

					msg = back.obtainMessage();
					msg.obj = "prs";
					msg.what = total;
					back.sendMessage(msg);             // 发回当前下载进度
				}

				fos.close();
				bis.close();
				is.close();

				msg = back.obtainMessage();
				msg.obj = "com";
				back.sendMessage(msg);                // 发回任务完成消息

				return 0;
			} catch (Exception e){
				e.printStackTrace();
				String msg = e.getMessage();

				if(msg.contains("EACCES")){
					Message msge = back.obtainMessage();
					msge.obj = "eacces";
					back.sendMessage(msge);            // 写SD卡权限不足。有些手机很，即使AM.xml里注册过

					return 2;
				}

				return 3;
			}
		} else {

			return 1;
		}
	}
}