package com.ohli.ioslauncher;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

public final class WallpaperArt extends Drawable {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

    @Override public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        float width = bounds.width();
        float height = bounds.height();
        if (width <= 0 || height <= 0) return;
        canvas.save();
        canvas.translate(bounds.left, bounds.top);
        paint.setShader(new LinearGradient(0, 0, width * 0.2f, height,
                new int[]{0xFF0753A1, 0xFF167BC1, 0xFF59C5D5, 0xFFB7E8E2},
                null, Shader.TileMode.CLAMP));
        canvas.drawRect(0, 0, width, height, paint);
        paint.setShader(new RadialGradient(width * 0.18f, height * 0.25f, width * 0.62f,
                new int[]{0xAA87D8FF, 0x5577C8F2, 0x001A70BD}, null, Shader.TileMode.CLAMP));
        canvas.drawRect(0, 0, width, height, paint);
        paint.setShader(new RadialGradient(width * 0.88f, height * 0.72f, width * 0.70f,
                new int[]{0x6678F0D1, 0x3375C9D0, 0x001B63A5}, null, Shader.TileMode.CLAMP));
        canvas.drawRect(0, 0, width, height, paint);
        paint.setShader(null);
        paint.setColor(0x1DFFFFFF);
        Path wave = new Path();
        wave.moveTo(0, height * 0.58f);
        wave.cubicTo(width * 0.23f, height * 0.43f, width * 0.54f, height * 0.76f, width, height * 0.51f);
        wave.lineTo(width, height);
        wave.lineTo(0, height);
        wave.close();
        canvas.drawPath(wave, paint);
        paint.setColor(0x12FFFFFF);
        canvas.drawCircle(width * 0.82f, height * 0.16f, width * 0.27f, paint);
        canvas.restore();
    }

    public static Bitmap createBitmap(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        WallpaperArt art = new WallpaperArt();
        art.setBounds(0, 0, width, height);
        art.draw(canvas);
        return bitmap;
    }

    @Override public void setAlpha(int alpha) { paint.setAlpha(alpha); invalidateSelf(); }
    @Override public void setColorFilter(android.graphics.ColorFilter colorFilter) { paint.setColorFilter(colorFilter); invalidateSelf(); }
    @Override public int getOpacity() { return android.graphics.PixelFormat.OPAQUE; }
}
