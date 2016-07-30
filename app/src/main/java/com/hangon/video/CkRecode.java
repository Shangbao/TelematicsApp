package com.hangon.video;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import com.example.fd.ourapplication.R;

import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CkRecode extends Activity implements OnItemClickListener{
	private ImageButton topLeft;
	private ImageButton topbar_right;
	private TextView topTittle;
	//private String path=Environment.getExternalStorageDirectory().getCanonicalFile()+ "XCJL" ;
	private String cur_path=Environment.getExternalStorageDirectory()+"/xinchejilu";
	private List<CarRecode> listPictures;
	ListView listView ;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == 0) {
				List<CarRecode> listPictures = (List<CarRecode>) msg.obj;
//				Toast.makeText(getApplicationContext(), "handle"+listPictures.size(), 1000).show();
				MyAdapter adapter = new MyAdapter(listPictures);
				listView.setAdapter(adapter);
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ck_activity_main);
		loadVaule();
		init();
	}
	void init(){
		topLeft=(ImageButton)findViewById(R.id.topbar_left);
		topTittle=(TextView)findViewById(R.id.topbar_title);
		topTittle.setText("行车记录");

		topbar_right=(ImageButton)findViewById(R.id.topbar_right);
		topbar_right.setVisibility(View.GONE);
		topLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}
	private void loadVaule(){
		File file = new File(cur_path);
		File[] files  = null;
		files = file.listFiles();
		listPictures = new ArrayList<CarRecode>();
		for (int i = 0; i < files.length; i++) {
			CarRecode picture = new CarRecode();
			picture.setBitmap(getVideoThumbnail(files[i].getPath(), 200, 200, MediaStore.Images.Thumbnails.MICRO_KIND));
			picture.setPath(files[i].getPath());
			listPictures.add(picture);
			
		}
		listView = (ListView) findViewById(R.id.lv_show);
		listView.setOnItemClickListener(this);
		Message msg = new Message();
		msg.what = 0;
		msg.obj = listPictures;

		handler.sendMessage(msg);

	}
	
	
	            //获取视频的缩略图
			    private Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {   
		        Bitmap bitmap = null;  
		        // 获取视频的缩略图  
		        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);  
//		        System.out.println("w"+bitmap.getWidth());  
//		        System.out.println("h"+bitmap.getHeight());  
		        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,  
		                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
		        return bitmap;  
			    }
			    
			    
			    
			    
			    
			    public class MyAdapter extends BaseAdapter {
					private List<CarRecode> listPictures;

					public MyAdapter(List<CarRecode> listPictures) {
						super();
						this.listPictures = listPictures;

					}

					@Override
					public int getCount() {
						return listPictures.size();
					}

					@Override
					public Object getItem(int position) {
						return listPictures.get(position);
					}

					@Override
					public long getItemId(int position) {
						return position;
					}

					@Override
					public View getView(int position, View v, ViewGroup arg2) {
						View view = getLayoutInflater().inflate(R.layout.ck_item,null);
						ImageView imageView = (ImageView) view.findViewById(R.id.iv_show);
						TextView textView = (TextView) view.findViewById(R.id.tv_show);
						
						imageView.setImageBitmap(listPictures.get(position).getBitmap());
						textView.setText(listPictures.get(position).getPath());
						return view;

					}
			    }

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					//Toast.makeText(getApplicationContext(), "点击了"+arg2, 200).show();
					playVideo(listPictures.get(arg2).getPath());
					Log.e("path", listPictures.get(arg2).getPath());
				}
	          //调用系统播放器   					   Intent intent = new Intent(Intent.ACTION_V播放视频
					private void playVideo(String videoPath){
//IEW);
//					   String strend="";
//					   if(videoPath.toLowerCase().endsWith(".mp4")){
//						   strend="mp4";
//					   }
//					   else if(videoPath.toLowerCase().endsWith("
//					   }.3gp")){
//						   strend="3gp";
//					   else if(videoPath.toLowerCase().endsWith(".mov")){
//						   strend="mov";
//					   }
//					   else if(videoPath.toLowerCase().endsWith(".avi")){
//						   strend="avi";
//					   }
//					   intent.setDataAndType(Uri.parse(videoPath), "video/*");
//					   startActivity(intent);
					
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					File file = new File(videoPath);
					intent.setDataAndType(Uri.fromFile(file), "video/*");
					startActivity(intent);
				   }	    


}
