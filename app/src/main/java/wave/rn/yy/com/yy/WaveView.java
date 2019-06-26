package wave.rn.yy.com.yy;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class WaveView extends View {

    static final String TAG = "WaveView";

    /**
     * 波浪圆圈颜色
     */
    private int mColor = getResources().getColor(R.color.yellow);
    /**
     * 第一个圆圈的半径(也就是圆形图片的半径)
     */
    private int mImageRadius = 50;
    /**
     * 波浪圆之间间距
     */
    private int mWidth = 3;
    /**
     * 最大宽度
     */
    private Integer mMaxRadius = 100;
    /**
     * 是否正在扩散中
     */
    private boolean mIsWave = false;
    // 透明度集合
    private List<Integer> mAlphas = new ArrayList<>();
    // 扩散圆半径集合
    private List<Integer> mRadius = new ArrayList<>();
    private Paint mPaint;
    //扩散的圆形是否是实心圆
    private boolean isFill = false;
    android.os.Handler mHandler = new android.os.Handler();

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WaveView, defStyleAttr, 0);
        mColor = a.getColor(R.styleable.WaveView_wave_color, mColor);
        mWidth = a.getDimensionPixelOffset(R.styleable.WaveView_wave_width, mWidth);
        mImageRadius = a.getDimensionPixelOffset(R.styleable.WaveView_wave_coreImageRadius,
                mImageRadius);
        mMaxRadius = a.getDimensionPixelOffset(R.styleable.WaveView_wave_maxRadius, mMaxRadius);

        a.recycle();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);


    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    /**
     * 防止window是去焦点时，也就是应用在后台时，停止View的绘制
     */
    @Override
    public void invalidate() {
        if (hasWindowFocus()) {
            super.invalidate();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        // 绘制扩散圆
        mPaint.setColor(mColor);
        for (int i = 0; i < mAlphas.size(); i++) {
            // 设置透明度
            Integer alpha = mAlphas.get(i);
            mPaint.setAlpha(alpha);
            // 绘制波浪圆
            Integer radius = mRadius.get(i);
            // Log.i(TAG,
            //         "处理前 radius=" + radius + "  mImageRadius=" + mImageRadius + "
            // mMaxRadius=" + mMaxRadius);
            if (mImageRadius + radius < mMaxRadius && alpha > 0) {
                if (isFill) {
                    mPaint.setStyle(Paint.Style.FILL);
                    canvas.drawCircle(getWidth() / 2, getHeight() / 2, mImageRadius + radius,
                            mPaint);
                    mPaint.setStyle(Paint.Style.STROKE);
                    mPaint.setStrokeWidth(5);
                    canvas.drawCircle(getWidth() / 2, getHeight() / 2, mImageRadius + radius,
                            mPaint);
                } else {
                    mPaint.setStyle(Paint.Style.STROKE);
                    mPaint.setStrokeWidth(5);
                    canvas.drawCircle(getWidth() / 2, getHeight() / 2, mImageRadius + radius,
                            mPaint);
                }
                //alpha = (int) (255.0F * (1.0F - (mImageRadius + radius) * 1.0f / mMaxRadius));
                alpha = (int) (255.0f * (1 - radius * 1.0f / (mMaxRadius - mImageRadius)));
                Log.i(TAG,
                        "radius=" + radius + "   mMaxRadius=" + mMaxRadius + " mImageRadius= "
                                + mImageRadius + " mMaxRadius= " + mMaxRadius);
                mAlphas.set(i, alpha);
                mRadius.set(i, radius + 1);
                Log.i(TAG, "alpha=" + alpha);
            } else if (alpha <= 3 || mImageRadius + radius > mMaxRadius) {
                // 当最外面那个圆达到了View的宽度时，移除，保证内存的回收
                Log.i(TAG, "remove i" + i);
                mRadius.remove(i);
                mAlphas.remove(i);
            }
        }
        // 判断当波浪圆扩散到指定宽度时添加新扩散圆
        if (mRadius.size() > 0 && mRadius.get(mRadius.size() - 1) > mWidth) {
            addWave();
        }
        Log.i(TAG, "mRadius.size=" + mRadius.size());

        if (mIsWave) {
            mHandler.postDelayed(mRunnable, 15);
        }
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            invalidate();

        }
    };

    /**
     * 开始扩散
     */
    public void start() {
        mIsWave = true;
        mAlphas.add(255);
        mRadius.add(0);
        invalidate();
    }

    /**
     * 停止扩散
     */
    public void stop() {
        mAlphas.clear();
        mRadius.clear();
        invalidate();
        mIsWave = false;
    }

    /**
     * 是否扩散中
     */
    public boolean isWave() {
        return mIsWave;
    }

    /**
     * 设置波浪圆颜色
     */
    public void setColor(int colorId) {
        mColor = colorId;
    }

    /**
     * 设置波浪圆之间间距
     */
    public void setWidth(int width) {
        mWidth = width;
    }

    /**
     * 设置中心圆半径
     */
    public void setMaxRadius(int maxRadius) {
        mMaxRadius = maxRadius;
    }

    public void setImageRadius(int imageRadius) {
        mImageRadius = imageRadius;
    }

    public boolean isFill() {
        return isFill;
    }

    public void setFill(boolean fill) {
        isFill = fill;
    }

    public void addWave() {
        mAlphas.add(255);
        mRadius.add(0);
    }
}