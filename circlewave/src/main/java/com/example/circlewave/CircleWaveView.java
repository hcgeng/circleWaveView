package com.example.circlewave;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by HongchaoGeng on 2015/10/29.
 */
public class CircleWaveView extends View {
    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    private float mWidth;
    private float mHeight;
    private volatile boolean started = false;
    private float centerX; // 圆心X
    private float centerY; // 圆心Y
    private float floatRadius; // 变化的半径
    private float maxRadius = -1; // 圆半径
    private float minRadius = -1; // 圆半径
    private int sleepPeriod;//动画间隔

    public CircleWaveView(Context context) {
        this(context, null);
    }

    public CircleWaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleWaveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        paint = new Paint();


        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.CircleWaveView);

        //获取自定义属性和默认值
        roundColor = mTypedArray.getColor(R.styleable.CircleWaveView_roundColor, Color.RED);
        roundWidth = mTypedArray.getDimension(R.styleable.CircleWaveView_roundWidth, 5);
        minRadius = mTypedArray.getDimension(R.styleable.CircleWaveView_minRadius, 20);
        sleepPeriod = mTypedArray.getInt(R.styleable.CircleWaveView_peroid, 20);

        mTypedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = (int) floatRadius;//圆环的半径
        paint.setColor(roundColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(centerX, centerY, radius, paint); //画出圆环
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            init();
        } else {
            stop();
        }
    }

    /**
     * 初始化数据
     */
    private void init() {
        mWidth = getWidth();
        mHeight = getHeight();
        centerX = mWidth / 2.0F;
        centerY = mHeight / 2.0F;

        if (mWidth >= mHeight) {
            maxRadius = mHeight / 2.0F;
        } else {
            maxRadius = mWidth / 2.0F;
        }

        floatRadius = minRadius;
        start();
    }

    /**
     * 启动动画
     */
    public void start() {
        if (!started) {
            started = true;
            new Thread(thread).start();
        }
    }

    /**
     * 停止动画
     */
    public void stop() {
        // Thread.interrupted();
        started = true;
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    /**
     * 通过标志位start来控制运行还是停止
     */
    private Runnable thread = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (started) {
                floatRadius = 4.0F + floatRadius;
                if (floatRadius > maxRadius) {
                    floatRadius = minRadius;
                    postInvalidate();
                }
                postInvalidate();
                try {
                    Thread.sleep(sleepPeriod);
                } catch (InterruptedException localInterruptedException) {
                    localInterruptedException.printStackTrace();
                }
            }
        }
    };
}
