package com.hangon.order.activity;

import android.support.v4.app.Fragment;

/**
 * Created by fd on 2016/5/11.
 */
public abstract class LazyFragment extends Fragment{
    protected  boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            isVisible=true;
            onInvisible();
        }
        else {
            isVisible=false;
            onInvisible();
        }
    }
    protected  void onInvisible(){

    }
    protected  void Onvisible(){
        lazyLoad();
    }
    protected  abstract  void lazyLoad();
}
