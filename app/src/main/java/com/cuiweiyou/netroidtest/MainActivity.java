package com.cuiweiyou.netroidtest;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cuiweiyou.cvudownloadfilellbrary.CVUDownLoadUtil;
import com.cuiweiyou.cvudownloadfilellbrary.CVUPermissionManager;
import com.cuiweiyou.cvudownloadfilellbrary.ICVUProgressBack;

/**
 * 类的说明：文件下载框架demo
 *
 * @author：崔维友
 *
 * @version：1.0.0
 * @created：028,16/9/28
 */
public class MainActivity extends AppCompatActivity {


	private ProgressDialog progressDialog;

	private String remote = "http://example.pdf";
	private String local = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/x.pdf";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.e("ard", "本地地址：" + local);

		progressDialog = new ProgressDialog(MainActivity.this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("准备···");

		findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick (View v){
				int result = CVUPermissionManager.requestPermission(
						MainActivity.this,
						1002,
						new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});

				if(0 == result){
					doDownload();
				}
			}
		});
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if(1002 == requestCode){
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				//用户同意授权
				doDownload();
			} else {
				Toast.makeText(this, "未授权", Toast.LENGTH_SHORT).show();
			}
		}
	}


	private void doDownload() {
		progressDialog.show();

		CVUDownLoadUtil.downloadFile(
				remote,
				local,
				new ICVUProgressBack() {
					double max = 0;

					@Override
					public void onProgressing(double progress) {
						double sp = progress / max * 100;
						progressDialog.setMessage(String.format("%.2f", sp) + "%100");
					}

					@Override
					public void setProgressMax(double max) {
						this.max = max;
						progressDialog.setMax(100);
					}

					@Override
					public void onComplete() {
						progressDialog.setMessage("完成");

						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						progressDialog.dismiss();
					}

					@Override
					public void onError(String msg) {
						if ("eacces".equals(msg)) {
							progressDialog.dismiss();
							Toast.makeText(MainActivity.this, "请检查读写SD卡应用权限", Toast.LENGTH_SHORT).show();
						}
						else {
							progressDialog.setMessage("错误");

							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							progressDialog.dismiss();
						}
					}
				});
	}
}
