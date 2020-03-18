package com.example.analogclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

public class AnalogClock extends View {
    private int hour;
    private int minute;
    private int second;

    private Paint bigCirclePaint;
    private Paint smallCirclePaint;
    private Paint hourTipPaint;
    private Paint minuteTipPaint;
    private Paint secondTipPaint;
    private Paint bigNumberPaint;
    private Paint smallNumberPaint;

    public AnalogClock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        bigCirclePaint = new Paint();
        bigCirclePaint.setColor(Color.BLACK);
        bigCirclePaint.setStyle(Paint.Style.STROKE);
        bigCirclePaint.setStrokeWidth(10);
        bigCirclePaint.setAntiAlias(true);

        smallCirclePaint = new Paint();
        smallCirclePaint.setColor(Color.BLACK);
        smallCirclePaint.setStyle(Paint.Style.FILL);
        smallCirclePaint.setAntiAlias(true);

        hourTipPaint = new Paint();
        hourTipPaint.setColor(Color.BLACK);
        hourTipPaint.setStrokeWidth(7);
        hourTipPaint.setAntiAlias(true);
        hourTipPaint.setStrokeCap(Paint.Cap.ROUND);

        minuteTipPaint = new Paint();
        minuteTipPaint.setColor(Color.BLACK);
        minuteTipPaint.setStrokeWidth(5);
        minuteTipPaint.setAntiAlias(true);
        minuteTipPaint.setStrokeCap(Paint.Cap.ROUND);

        secondTipPaint = new Paint();
        secondTipPaint.setColor(Color.BLACK);
        secondTipPaint.setStrokeWidth(3);
        secondTipPaint.setAntiAlias(true);
        secondTipPaint.setStrokeCap(Paint.Cap.ROUND);

        bigNumberPaint = new Paint();
        bigNumberPaint.setColor(Color.BLACK);
        bigNumberPaint.setTextSize(100);
        bigNumberPaint.setTextAlign(Paint.Align.CENTER);
        bigNumberPaint.setStrokeCap(Paint.Cap.ROUND);

        smallNumberPaint = new Paint();
        smallNumberPaint.setColor(Color.BLACK);
        smallNumberPaint.setTextSize(66);
        smallNumberPaint.setTextAlign(Paint.Align.CENTER);
        smallNumberPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setTime(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;

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
                bigCircleRadius - bigCirclePaint.getStrokeWidth(),
                bigCirclePaint
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