package com.example.camera.origincamera;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by libo on 2017/11/23.
 */

public class CiclePercentView extends View{
    private Paint paint;
    private int curAngle;
    private int curPercentate;
    private Paint bgPaint,centerPaint;
    private int radius;
    private int ringColor;
    private int startAngle;
    private int countdownTime;
    private CountDownTimer countDownTimer;

    public CiclePercentView(Context context) {
        super(context);
        init();
    }

    public CiclePercentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.CiclePercentView);
        radius = array.getInt(R.styleable.CiclePercentView_radius,85);
        ringColor = array.getColor(R.styleable.CiclePercentView_ring_color,Color.GREEN);
        array.recycle();

        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(ringColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(14);

        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(getResources().getColor(R.color.halfwhite));

        centerPaint = new Paint();
        centerPaint.setAntiAlias(true);
        centerPaint.setColor(Color.WHITE);

        //起始角度
        startAngle = -90;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画圆弧
        RectF rectf = new RectF(6,6,dp2px(radius-2),dp2px(radius-2));
        canvas.drawCircle(getMeasuredWidth()/2,getMeasuredHeight()/2,dp2px(radius)/2,bgPaint);
        canvas.drawCircle(getMeasuredWidth()/2,getMeasuredHeight()/2,dp2px(radius/3)/2,centerPaint);
        canvas.drawArc(rectf,startAngle,curAngle,false,paint);
    }

    private void percentToAngle(int percentage){
        curAngle =  (int) (percentage/100f*360);
        invalidate();
    }

    public void setCountdownTime(int countdownTime){
        this.countdownTime = countdownTime;
    }

    public void countDown(final int totalTime){
        countDownTimer = new CountDownTimer(totalTime, (long)(totalTime/100f)) {
            @Override
            public void onTick(long millisUntilFinished) {
                curPercentate = (int) ((totalTime-millisUntilFinished)/(float)totalTime*100);
                percentToAngle(curPercentate);
            }

            @Override
            public void onFinish() {
                curPercentate = 0;
                percentToAngle(curPercentate);
            }
        }.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                countDown(countdownTime);
                break;
            case MotionEvent.ACTION_UP:
                countDownTimer.cancel();
                curPercentate = 0;
                percentToAngle(curPercentate);
                break;
        }
        return true;
    }

    private int dp2px(int dp){
        return (int) (getContext().getResources().getDisplayMetrics().density*dp + 0.5);
    }
}
