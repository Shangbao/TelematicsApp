package com.hangon.video;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Text;

;
import com.example.fd.ourapplication.R;
import com.hangon.video.CarRecode;

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
import android.graphics.Paint.Join;
import android.service.notification.NotificationListenerService;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CkRecode extends Activity {
	//private String path=Environment.getExternalStorageDirectory().getCanonicalFile()+ "XCJL" ;
	private String cur_path=Environment.getExternalStorageDirectory()+"/xinchejilu";
	private List<CarRecode> listPictures;
	private ListView listView ,listViews;
	private MyAdapter adapter;
	private MyAdapters adapters;
	private ViewHolder holder;
	private TextView editVedio;
	private int checkNum;
	private  ImageView back_grzx;
	int judge=1;

	//全选
	private LinearLayout all_select_list;
	private CheckBox editListAllCheckbox;
	//删除数据
	private TextView deleteList;
	//装载MAP数据LIst
	private List<HashMap<String, Object>> mapList;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				List<CarRecode> listPictures = (List<CarRecode>) msg.obj;
//				Toast.makeText(getApplicationContext(), "handle"+listPictures.size(), 1000).show();
				adapter = new MyAdapter(listPictures);
				listView.setAdapter(adapter);
			}
			if (msg.what == 1) {
				List<HashMap<String, Object>> listPictures = (List<HashMap<String, Object>>) msg.obj;
//				Toast.makeText(getApplicationContext(), "handle"+listPictures.size(), 1000).show();
				adapters = new MyAdapters(listPictures);
				listViews.setAdapter(adapters);
				DataChange();

			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ck_activity_main);
		loadVaule();
		back_grzx=(ImageView)findViewById(R.id.back_grzx);
		back_grzx.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		editVedio=(TextView)findViewById(R.id.edit_list);
		deleteList=(TextView)findViewById(R.id.delete_list);
		editListAllCheckbox=(CheckBox)findViewById(R.id.edit_lists_allcheckbox);
		all_select_list=(LinearLayout)findViewById(R.id.all_selected_list);
		editVedio.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(listPictures!=null&&listPictures.size()!=0&editVedio.getText().toString().trim().equals("编辑")){
					all_select_list.setVisibility(View.VISIBLE);
					deleteList.setVisibility(View.VISIBLE);
					editVedio.setText("取消");
					editListAllCheckbox.setVisibility(View.VISIBLE);
					listViews = (ListView) findViewById(R.id.lv_show);
					loadVaule();
					Message message=new Message();
					message.what=1;
					message.obj = mapList;
					handler.sendMessage(message);
					init();
					editListAllCheckbox.setChecked(false);
					holder.deleteRecode.setFocusableInTouchMode(false);
					holder.deleteRecode.setFocusable(false);

				}
				else{
					all_select_list.setVisibility(View.GONE);
					deleteList.setVisibility(View.GONE);
					editVedio.setText("编辑");
					editListAllCheckbox.setVisibility(View.GONE);
					editListAllCheckbox.setChecked(false);
					loadVaule();
					DataChange();
				}

			}
		});
	}
	//初始化MapList;
	private void initData(List<CarRecode> carRecodes){
		mapList=new ArrayList<HashMap<String,Object>>();
		if(carRecodes==null||carRecodes.size()==0){
			editVedio.setVisibility(View.GONE);
			return;
		}
		else{
			for(int i=0;i<carRecodes.size();i++){
				HashMap<String, Object> map=new HashMap<String, Object>();
				map.put("flag", "false");
				map.put("bitmap", listPictures.get(i).getBitmap());
				map.put("path", listPictures.get(i).getPath());
				mapList.add(map);
			}
		}
	}
	private void init() {
		listViews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getApplicationContext(),"cccc",Toast.LENGTH_SHORT).show();
				//取得ViewHorder对象，
				ViewHolder holder = (ViewHolder) view.getTag();
				holder.deleteRecode.toggle();
				if(editListAllCheckbox.isChecked()){
					editListAllCheckbox.setChecked(false);
				}
				if (holder.deleteRecode.isChecked() == true) {
					mapList.get(position).put("flag", "true");
					checkNum++;
				} else {
					mapList.get(position).put("flag", "false");
					checkNum--;
				}
				//用TextView 显示
			}
		});
		//全选
		editListAllCheckbox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mapList==null||mapList.size()==0){
					Toast.makeText(getApplicationContext(), "可删除列表为空",Toast.LENGTH_SHORT);
				}
				if(editListAllCheckbox.isChecked()){
					for(int i=0;i<mapList.size();i++){
						mapList.get(i).put("flag", "true");
					}
					//checkNum=mapList.size();
				}
				if(!editListAllCheckbox.isChecked()){
					for(int i=0;i<mapList.size();i++){
						if(mapList.get(i).get("flag").equals("true")){
							mapList.get(i).put("flag", "flase");
							//checkNum--;
						}
					}
					//checkNum=mapList.size();
					judge=1;
				}
				judge=0;
				DataChange();
			}
		});
		deleteList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if(mapList!=null||mapList.size()!=0){
					boolean state=false;
					for(int i=0;i<mapList.size();i++){
						if(mapList.get(i).get("flag").equals("true")){
							state=true;
							Toast.makeText(getApplicationContext(), mapList.get(i).get("path").toString(), Toast.LENGTH_SHORT).show();
							File file=new File(mapList.get(i).get("path").toString());
							file.delete();
						}
					}

					editListAllCheckbox.setVisibility(View.GONE);
					editVedio.setText("编辑");
					loadVaule();
					all_select_list.setVisibility(View.GONE);
					adapters.notifyDataSetChanged();
					adapter.notifyDataSetChanged();
					deleteList.setVisibility(View.GONE);

				}
			}
		});

	}
	private void DataChange() {
		adapters.notifyDataSetChanged();
	}
	public class MyAdapters extends BaseAdapter {
		private List<HashMap<String, Object>> listPictures;
		public MyAdapters(List<HashMap<String, Object>> listPictures) {
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
		public View getView(final int position, View view, ViewGroup arg2) {
			if(view==null){
				holder=new ViewHolder();
				view = getLayoutInflater().inflate(R.layout.ck_item,null);
				holder.imageView = (ImageView) view.findViewById(R.id.iv_show);
				holder.textView = (TextView) view.findViewById(R.id.tv_show);
				holder.deleteRecode=(CheckBox)view.findViewById(R.id.edit_lists_checkbox);
				view.setTag(holder);
			}else{
				holder=(ViewHolder)view.getTag();
			}
			holder.imageView.setImageBitmap((Bitmap)listPictures.get(position).get("bitmap"));
			holder.textView.setText(listPictures.get(position).get("path").toString());
			holder.deleteRecode.setVisibility(View.VISIBLE);
			holder.deleteRecode.setChecked(listPictures.get(position).get("flag").equals("true"));
			if(holder.deleteRecode.isChecked()){
				notifyDataSetChanged();
			}
			if(!holder.deleteRecode.isChecked()){

				notifyDataSetChanged();
			}

			return view;

		}
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
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int arg2, long arg3) {
				playVideo(listPictures.get(arg2).getPath());
				//loadVaule();
				Toast.makeText(getApplicationContext(),listPictures.get(arg2).getPath() , Toast.LENGTH_SHORT).show();
//				Log.e("path", listPictures.get(arg2).getPath());
			}
		});
		Message msg = new Message();
		msg.what = 0;
		msg.obj = listPictures;
		handler.sendMessage(msg);
		if(listPictures!=null&&listPictures.size()!=0){
			initData(listPictures);
		}
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
		public View getView(final int position, View view, ViewGroup arg2) {
			if(view==null){
				holder=new ViewHolder();
				view = getLayoutInflater().inflate(R.layout.ck_item,null);
				holder.imageView = (ImageView) view.findViewById(R.id.iv_show);
				holder.textView = (TextView) view.findViewById(R.id.tv_show);
				holder.deleteRecode=(CheckBox)view.findViewById(R.id.edit_lists_checkbox);
				view.setTag(holder);
			}else{
				holder=(ViewHolder)view.getTag();
			}
			holder.imageView.setImageBitmap(listPictures.get(position).getBitmap());
			holder.textView.setText(listPictures.get(position).getPath());
			return view;

		}
	}

	//调用系统播放器   播放视频
	private void playVideo(String videoPath){
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		File file = new File(videoPath);
		intent.setDataAndType(Uri.fromFile(file), "video/*");
		startActivity(intent);
	}
	class ViewHolder{
		ImageView imageView;
		TextView textView;
		CheckBox deleteRecode;
	}


}
