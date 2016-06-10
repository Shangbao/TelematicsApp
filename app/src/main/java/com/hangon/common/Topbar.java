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
public class Topbar extends RelativeLayout {
    private Button leftButton, rightButton;//左右按钮
    private TextView tvTitle;//标题

    //左边button
    private int leftTextColor;
    private Drawable leftBackground;
    private String leftText;
    private Drawable leftSrc;


    //右边button
    private int rightTextColor;
    private Drawable rightBackground;
    private String rightText;
    private Drawable rightSrc;


    //标题
    private float titleTextSize;
    private int titleTextColor;
    private String title;


    private RelativeLayout.LayoutParams leftParams, rightParams, titileParams;

    private topbarClickListener listener;

    public void setOnTopbarClickListener(topbarClickListener topbarClickListener) {
        this.listener = topbarClickListener;
    }


    public interface topbarClickListener {
        public void leftClick();

        public void rightClick();
    }


    public Topbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Topbar);
        leftTextColor = typedArray.getColor(R.styleable.Topbar_leftTextColor, 0);
        leftBackground = typedArray.getDrawable(R.styleable.Topbar_leftBackground);
        leftText = typedArray.getString(R.styleable.Topbar_leftText);
        leftSrc=typedArray.getDrawable(R.styleable.Topbar_leftSrc);

        rightTextColor = typedArray.getColor(R.styleable.Topbar_rightTextColor, 0);
        rightBackground = typedArray.getDrawable(R.styleable.Topbar_rightBackground);
        rightText = typedArray.getString(R.styleable.Topbar_rightText);
        rightSrc=typedArray.getDrawable(R.styleable.Topbar_rightSrc);

        titleTextSize = typedArray.getDimension(R.styleable.Topbar_mTitleTextSize, 0);
        titleTextColor = typedArray.getColor(R.styleable.Topbar_mTitleTextColor, 0);
        title = typedArray.getString(R.styleable.Topbar_mTitle);


        typedArray.recycle();


        leftButton = new Button(context);
        rightButton = new Button(context);
        tvTitle = new TextView(context);

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

        leftParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
        addView(leftButton, leftParams);

        rightParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        addView(rightButton, rightParams);

        titileParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        titileParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(tvTitle, titileParams);

        /**
         * Topbar的左按钮设置点击事件
         */
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.leftClick();
            }
        });

        /**
         * topbar的右边按钮设置点击事件
         */

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.rightClick();
            }
        });

    }

    /**
     * 做按钮设置是否显示
     */

    public void setLeftIsVisible(boolean flag) {
        if (flag) {
            leftButton.setVisibility(View.VISIBLE);
        } else {
            leftButton.setVisibility(View.GONE);
        }
    }

    /**
     * Topbar的右边按钮设置是否显示的属性
     *
     * @param flag
     */
    public void setRightIsVisible(boolean flag) {
        if (flag) {
            rightButton.setVisibility(View.VISIBLE);
        } else {
            rightButton.setVisibility(View.GONE);
        }
    }

    /**
     * Topbar的左右按钮同时设置是否显示
     */

    public void setBtnIsVisible(boolean flag) {
        if (flag) {
            leftButton.setVisibility(View.VISIBLE);
            rightButton.setVisibility(View.VISIBLE);
        } else {
            leftButton.setVisibility(View.GONE);
            rightButton.setVisibility(View.GONE);
        }
    }


}
