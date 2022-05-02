package com.inflexsyscamerapreview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class CustomRectangle extends View {
    Paint paint;
    float radius = 15;
    String color = "#ffffff";
    RectF rectf;

    public CustomRectangle(Context context, RectF rectf) {
        super(context);
        this.rectf = rectf;
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint = new Paint();
        paint.setColor(Color.parseColor(color));
        paint.setAlpha(90);
        canvas.drawRoundRect(rectf, radius, radius, paint);
    }
}
