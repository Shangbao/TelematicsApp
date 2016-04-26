package com.hangon.map.util;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Administrator on 2016/4/23.
 */
public class AnimAsyncTask extends AsyncTask<Integer, String, Integer> {

    private CustomProgressDialog progressDialog = null;
    Context context;
    String message;
    public static int  progress=0;
    public int getProgress() {
        return progress;
    }
    public void setProgress(int progress) {
        this.progress = progress;
    }
    @Override
    protected Integer doInBackground(Integer... params) {
        while (progress==0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            progress=this.getProgress();
        }
        return null;
    }
    public  AnimAsyncTask(Context context,String message) {
        this.context=context;
        this.message=message;
    }
    public AnimAsyncTask(){

    }
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        stopProgressDialog();
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.progress=0;
        startProgressDialog();
    }
    @Override
    protected void onCancelled() {
        stopProgressDialog();
        this.progress=0;
        super.onCancelled();
    }
    public  void startProgressDialog(){
        if (progressDialog == null){
            progressDialog = CustomProgressDialog.createDialog(context);
            progressDialog.setMessage(message);
        }
        progressDialog.show();
    }

    public void stopProgressDialog(){
        if (progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
