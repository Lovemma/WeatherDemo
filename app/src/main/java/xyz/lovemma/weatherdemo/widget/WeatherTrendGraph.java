package xyz.lovemma.weatherdemo.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.List;

import xyz.lovemma.weatherdemo.R;
import xyz.lovemma.weatherdemo.entity.DailyForecast;
import xyz.lovemma.weatherdemo.utils.DateUtil;
import xyz.lovemma.weatherdemo.utils.DensityUtil;
import xyz.lovemma.weatherdemo.utils.SharedPreferencesUtil;

import static android.content.ContentValues.TAG;

/**
 * Created by OO on 2017/4/17.
 */

public class WeatherTrendGraph extends View {
    private Context mContext;
    private float width, height;
    private Path maxTmpPath = new Path();
    private Path minTmpPath = new Path();
    private Paint mPaint;
    private Paint mPointPaint;
    private List<DailyForecast> mWeathers;

    private int maxTmp, minTmp;
    private float xInterval, yInterval;

    public void setWeathers(List<DailyForecast> weathers) {
        mWeathers = weathers;
        maxTmp = getMaxTmp();
        minTmp = getMinTmp();
        invalidate();
    }

    public WeatherTrendGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initPaint();
        sharedPreferencesUtil = new SharedPreferencesUtil(context);

    }

    private TextPaint mTextPaint;

    private void initPaint() {

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);

        mPointPaint = new Paint();
        mPointPaint.setColor(Color.BLACK);
        mPointPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new TextPaint();
        mTextPaint.setColor(Color.parseColor("#434343"));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(DensityUtil.sp2px(mContext, 14f));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMySize(100, widthMeasureSpec);
        Log.d(TAG, "onMeasure: " + widthMeasureSpec);
        Log.d(TAG, "onMeasure: width=" + width);
        int height = getMySize(DensityUtil.dp2px(mContext, 256), heightMeasureSpec);
        Log.d(TAG, "onMeasure: height=" + height);
        setMeasuredDimension(width, height);
    }

    private int getMySize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;

        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED: {//如果没有指定大小，就设置为默认大小
                mySize = defaultSize;
                break;
            }
            case MeasureSpec.AT_MOST: {//如果测量模式是最大取值为size
                //我们将大小取最大值,你也可以取其他值
                mySize = size;
                break;
            }
            case MeasureSpec.EXACTLY: {//如果是固定的大小，那就不要去改变它
                mySize = size;
                break;
            }
        }
        return mySize;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        height = getHeight();
        width = getWidth();
    }

    private float textOffset;
    private float averTmp;
    private float max2min;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mWeathers == null || mWeathers.size() == 0) {
            return;
        }
        xInterval = width / mWeathers.size();
        yInterval = height / 18f;
        textOffset = getTextPaintOffset(mPaint);
        averTmp = (maxTmp + minTmp) / 2f;
        max2min = Math.abs(maxTmp - minTmp);
        drawLine(canvas);
    }

    public static float getTextPaintOffset(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return -(fontMetrics.bottom - fontMetrics.top) / 2f - fontMetrics.top;
    }

    private SharedPreferencesUtil sharedPreferencesUtil;

    private void drawLine(Canvas canvas) {
        float X, Y, offsetPercent;
        maxTmpPath.reset();
        minTmpPath.reset();

        //高温折线
        for (int i = 0; i < mWeathers.size(); i++) {
            X = xInterval * i + xInterval / 2;
            offsetPercent = (mWeathers.get(i).getTmp().getMax() - averTmp) / max2min;
            Y = yInterval * 6 - yInterval * 8 * offsetPercent;

            if (i == 0) {
                maxTmpPath.moveTo(X, Y);
            } else {
                maxTmpPath.lineTo(X, Y);
            }
            canvas.drawText(mWeathers.get(i).getTmp().getMax() + "°", X, Y - yInterval + textOffset, mTextPaint);
            canvas.drawCircle(X, Y, 8, mPointPaint);
        }

        //低温折线
        for (int i = 0; i < mWeathers.size(); i++) {
            X = xInterval * i + xInterval / 2;
            offsetPercent = (mWeathers.get(i).getTmp().getMin() - averTmp) / max2min;
            Y = yInterval * 6 - yInterval * 8 * offsetPercent;

            if (i == 0) {
                minTmpPath.moveTo(X, Y);
            } else {
                minTmpPath.lineTo(X, Y);
            }
            canvas.drawText(mWeathers.get(i).getTmp().getMin() + "°", X, Y + yInterval + textOffset, mTextPaint);
            canvas.drawCircle(X, Y, 8, mPointPaint);
        }
        int resId;
        for (int i = 0; i < mWeathers.size(); i++) {
            X = xInterval * i + xInterval / 2;
            canvas.drawText(DateUtil.dayForWeek(mWeathers.get(i).getDate()), X, yInterval * 13f, mTextPaint);
            canvas.drawText(mWeathers.get(i).getCond().getTxt_d(), X, yInterval * 16.5f + textOffset, mTextPaint);

            resId = (int) sharedPreferencesUtil.get(mWeathers.get(i).getCond().getTxt_d(), R.drawable.ic_unknow);
            Bitmap bitmap = drawableToBitmap(getResources().getDrawable(resId));
            canvas.drawBitmap(bitmap, X - bitmap.getWidth() / 2, yInterval * 13.5f + textOffset, mPaint);
        }
        canvas.drawPath(maxTmpPath, mPaint);
        canvas.drawPath(minTmpPath, mPaint);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public void setAnimation(WeatherTrendGraph weatherTrendGraph, int duration) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(weatherTrendGraph, "alpha", 1, 0, 1);
        anim.setDuration(duration);
        anim.setInterpolator(new LinearInterpolator());
        anim.start();
    }

    private int getMaxTmp() {
        int max = mWeathers.get(0).getTmp().getMax();
        for (DailyForecast weather : mWeathers) {
            if (max < weather.getTmp().getMax()) {
                max = weather.getTmp().getMax();
            }
        }
        return max;
    }

    private int getMinTmp() {
        int min = mWeathers.get(0).getTmp().getMin();
        for (DailyForecast weather : mWeathers) {
            if (min > weather.getTmp().getMin()) {
                min = weather.getTmp().getMin();
            }
        }
        return min;
    }
}
