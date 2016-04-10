package com.hangon.outer.zxing;





import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.fd.ourapplication.R;


public class WebViewActivity extends Activity {
	private WebView webview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);

		Intent intent = getIntent();
		String url = intent.getStringExtra("url");
		if (url != null && !"".equals(url)) {
			webview = (WebView) findViewById(R.id.webView);
			// 设置WebView属性，能够执行JavaScript脚本
			webview.getSettings().setJavaScriptEnabled(true);
			// 加载URL内容
			webview.loadUrl(url);
			// 设置web视图客户端
			webview.setWebViewClient(new MyWebViewClient());
		} else {
			Toast.makeText(WebViewActivity.this, "没有获取到二维码信息！",
					Toast.LENGTH_SHORT).show();
		}

	}

	// 设置回退
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
			webview.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// web视图客户端
	public class MyWebViewClient extends WebViewClient {
		public boolean shouldOverviewUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
}