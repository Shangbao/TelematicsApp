package com.hangon.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fd.ourapplication.R;

/**
 * Created by Administrator on 2016/4/8.
 */
public class Topbar extends RelativeLayout{
    private Button leftButton,rightButton;
    private TextView tvTitle;

    //左边button
    private int leftTextColor;
    private Drawable leftBackground;
    private String leftText;

    //右边button
    private int rightTextColor;
    private Drawable rightBackground;
    private String rightText;

    //标题
    private float titleTextSize;
    private  int titleTextColor;
    private String title;



    private RelativeLayout.LayoutParams leftParams,rightParams,titileParams;

    private topbarClickListener listener;

    public void setOnTopbarClickListener(topbarClickListener topbarClickListener) {
        this.listener=topbarClickListener;
    }


    public interface  topbarClickListener{
        public void leftClick();
        public void rightClick();
    }





    public Topbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.Topbar);
        leftTextColor =typedArray.getColor(R.styleable.Topbar_leftTextColor, 0);
        leftBackground=typedArray.getDrawable(R.styleable.Topbar_leftBackground);
        leftText=typedArray.getString(R.styleable.Topbar_leftText);

        rightTextColor =typedArray.getColor(R.styleable.Topbar_rightTextColor, 0);
        rightBackground=typedArray.getDrawable(R.styleable.Topbar_rightBackground);
        rightText=typedArray.getString(R.styleable.Topbar_rightText);

        titleTextSize=typedArray.getDimension(R.styleable.Topbar_mTitleTextSize, 0);
        titleTextColor=typedArray.getColor(R.styleable.Topbar_mTitleTextColor, 0);
        title=typedArray.getString(R.styleable.Topbar_mTitle);


        typedArray.recycle();

        leftButton=new Button(context);
        rightButton=new Button(context);
        tvTitle=new TextView(context);

        leftButton.setTextColor(leftTextColor);
        leftButton.setBackground(leftBackground);
        leftButton.setText(leftText);

        rightButton.setTextColor(rightTextColor);
        rightButton.setBackground(rightBackground);
        rightButton.setText(rightText);

        tvTitle.setTextColor(titleTextColor);
        tvTitle.setTextSize(titleTextSize);
        tvTitle.setText(title);
        tvTitle.setGravity(Gravity.CENTER);



        setBackgroundColor(0xffffff);

        leftParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
        addView(leftButton, leftParams);

        rightParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        addView(rightButton, rightParams);

        titileParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        titileParams.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);
        addView(tvTitle, titileParams);


        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.leftClick();

            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.rightClick();
            }
        });
    }
}
