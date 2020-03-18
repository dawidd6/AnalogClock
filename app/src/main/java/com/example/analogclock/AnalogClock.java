package com.example.analogclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;

import java.util.Calendar;

public class AnalogClock extends View {
    private AnalogClockRealTimeThread thread;
    private class AnalogClockRealTimeThread extends Thread {
        @Override
        public void run() {
            while(!isInterrupted() && isAlive()) {
                try {
                    setTime(
                            Calendar.getInstance().get(Calendar.HOUR),
                            Calendar.getInstance().get(Calendar.MINUTE),
                            Calendar.getInstance().get(Calendar.SECOND)
                    );
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    private boolean night;

    private int hour;
    private int minute;
    private int second;

    private Paint ringPaint;
    private Paint bigCirclePaint;
    private Paint smallCirclePaint;
    private Paint hourTipPaint;
    private Paint minuteTipPaint;
    private Paint secondTipPaint;
    private Paint bigNumberPaint;
    private Paint smallNumberPaint;

    public void init() {
        int frontColor = night ? Color.WHITE : Color.BLACK;
        int backColor = night ? Color.BLACK : Color.WHITE;

        bigCirclePaint = new Paint();
        bigCirclePaint.setColor(backColor);

        ringPaint = new Paint();
        ringPaint.setColor(frontColor);
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setStrokeWidth(10);
        ringPaint.setAntiAlias(true);

        smallCirclePaint = new Paint();
        smallCirclePaint.setColor(frontColor);
        smallCirclePaint.setStyle(Paint.Style.FILL);
        smallCirclePaint.setAntiAlias(true);

        hourTipPaint = new Paint();
        hourTipPaint.setColor(frontColor);
        hourTipPaint.setStrokeWidth(7);
        hourTipPaint.setAntiAlias(true);
        hourTipPaint.setStrokeCap(Paint.Cap.ROUND);

        minuteTipPaint = new Paint();
        minuteTipPaint.setColor(frontColor);
        minuteTipPaint.setStrokeWidth(5);
        minuteTipPaint.setAntiAlias(true);
        minuteTipPaint.setStrokeCap(Paint.Cap.ROUND);

        secondTipPaint = new Paint();
        secondTipPaint.setColor(frontColor);
        secondTipPaint.setStrokeWidth(3);
        secondTipPaint.setAntiAlias(true);
        secondTipPaint.setStrokeCap(Paint.Cap.ROUND);

        bigNumberPaint = new Paint();
        bigNumberPaint.setColor(frontColor);
        bigNumberPaint.setTextSize(100);
        bigNumberPaint.setTextAlign(Paint.Align.CENTER);
        bigNumberPaint.setStrokeCap(Paint.Cap.ROUND);

        smallNumberPaint = new Paint();
        smallNumberPaint.setColor(frontColor);
        smallNumberPaint.setTextSize(66);
        smallNumberPaint.setTextAlign(Paint.Align.CENTER);
        smallNumberPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public AnalogClock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void startRealTime() {
        thread = new AnalogClockRealTimeThread();
        thread.start();
    }

    public void stopRealTime() {
        thread.interrupt();
    }

    public void setTime(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;

        Log.d("TIME", hour+":"+minute+":"+second);

        invalidate();
    }

    public void setNight(boolean night) {
        this.night = night;

        init();
        invalidate();
    }

    private double getHourAngle(int hour, int minute) {
        if (hour > 12)
            hour = hour - 12;

        double angle = (hour + minute / 60f) * 360 / 12f;

        return Math.toRadians(angle - 90);
    }

    private double getMinuteAngle(int minute, int second) {
        double angle = (minute + second / 60f) * 360 / 60f;

        return Math.toRadians(angle - 90);
    }

    private double getSecondAngle(int second) {
        double angle = second * 360 / 60f;

        return Math.toRadians(angle - 90);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        int bigCircleRadius = Math.min(getWidth(), getHeight()) / 2;
        int smallCircleRadius = bigCircleRadius / 40;

        int hourTipLength = bigCircleRadius * 6 / 12;
        int minuteTipLength = bigCircleRadius * 8 / 12;
        int secondTipLength = bigCircleRadius * 10 / 12;

        double hourTipAngle = getHourAngle(hour, minute);
        double minuteTipAngle = getMinuteAngle(minute, second);
        double secondTipAngle = getSecondAngle(second);

        double hourTipEndX = hourTipLength * Math.cos(hourTipAngle) + centerX;
        double hourTipEndY = hourTipLength * Math.sin(hourTipAngle) + centerY;

        double minuteTipEndX = minuteTipLength * Math.cos(minuteTipAngle) + centerX;
        double minuteTipEndY = minuteTipLength * Math.sin(minuteTipAngle) + centerY;

        double secondTipEndX = secondTipLength * Math.cos(secondTipAngle) + centerX;
        double secondTipEndY = secondTipLength * Math.sin(secondTipAngle) + centerY;

        canvas.drawCircle(
                centerX,
                centerY,
                bigCircleRadius - ringPaint.getStrokeWidth(),
                bigCirclePaint
        );

        canvas.drawCircle(
                centerX,
                centerY,
                bigCircleRadius - ringPaint.getStrokeWidth(),
                ringPaint
        );

        canvas.drawCircle(
                centerX,
                centerY,
                smallCircleRadius,
                smallCirclePaint
        );

        canvas.drawLine(
                centerX,
                centerY,
                (float)hourTipEndX,
                (float)hourTipEndY,
                hourTipPaint
        );

        canvas.drawLine(
                centerX,
                centerY,
                (float)minuteTipEndX,
                (float)minuteTipEndY,
                minuteTipPaint
        );

        canvas.drawLine(
                centerX,
                centerY,
                (float)secondTipEndX,
                (float)secondTipEndY,
                secondTipPaint
        );

        for(int i = 1; i <= 12; i++) {
            boolean bigNumber =  i % 3 == 0;
            double numberPosOffsetY;
            if(bigNumber)
                numberPosOffsetY = (bigNumberPaint.descent() + bigNumberPaint.ascent()) / 2;
            else
                numberPosOffsetY = (smallNumberPaint.descent() + smallNumberPaint.ascent()) / 2;
            double numberAngle = getHourAngle(i, 0);
            double numberPosX = bigCircleRadius * 3/4f * Math.cos(numberAngle) + centerX;
            double numberPosY = bigCircleRadius * 3/4f * Math.sin(numberAngle) + centerY - numberPosOffsetY;

            canvas.drawText(
                    String.valueOf(i),
                    (float)numberPosX,
                    (float)numberPosY ,
                    bigNumber ? bigNumberPaint : smallNumberPaint
            );
        }
    }
}
