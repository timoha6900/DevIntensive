package com.softdesign.devintensive.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class RoundedDrawable extends Drawable {

	private final RectF mRect = new RectF();
	private final BitmapShader mBitmapShader;
	private final Paint mPaint;
	private final Matrix mShaderMatrix;
	private final int mBitmapWidth, mBitmapHeight;

	public RoundedDrawable(Bitmap bitmap) {

		mBitmapWidth = bitmap.getWidth();
		mBitmapHeight = bitmap.getHeight();

		mShaderMatrix = new Matrix();

		mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
		mBitmapShader.setLocalMatrix(mShaderMatrix);

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setShader(mBitmapShader);

	}

	public RoundedDrawable(Resources res, int resId) {
		this(BitmapFactory.decodeResource(res, resId));
	}

	public RoundedDrawable(Uri imageUri) {
		this(BitmapFactory.decodeFile(imageUri.getEncodedPath()));
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
		mRect.set(bounds);

		updateShaderMatrix();
	}

	@Override
	public void draw(@NonNull Canvas canvas) {
		canvas.drawOval(mRect, mPaint);
	}

	@Override
	public void setAlpha(int alpha) {
		mPaint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(@Nullable ColorFilter colorFilter) {
		mPaint.setColorFilter(colorFilter);
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

	private void updateShaderMatrix() {
		float scale;
		if(mBitmapWidth * mRect.height() > mBitmapHeight * mRect.width()){
			scale = mRect.height() / mBitmapHeight;
		} else {
			scale = mRect.width() / mBitmapWidth;
		}

		mShaderMatrix.reset();
		mShaderMatrix.setScale(scale, scale);

		updatePaint();
	}

	private void updatePaint() {
		mBitmapShader.setLocalMatrix(mShaderMatrix);
		mPaint.setShader(mBitmapShader);
	}

}
