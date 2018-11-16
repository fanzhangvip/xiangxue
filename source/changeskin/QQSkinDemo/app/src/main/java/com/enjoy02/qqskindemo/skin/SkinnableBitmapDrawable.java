package com.enjoy02.qqskindemo.skin;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import java.io.InputStream;


public class SkinnableBitmapDrawable extends Drawable {
	private static final int DEFAULT_PAINT_FLAGS = 6;
	private boolean mApplyGravity;
	private Bitmap mBitmap;
	private int mBitmapHeight;
	private BitmapState mBitmapState;
	private int mBitmapWidth;
	private final Rect mDstRect = new Rect();
	private boolean mMutated;
	private boolean mRebuildShader;
	private int mTargetDensity;

	SkinnableBitmapDrawable() {
		this.mBitmapState = new BitmapState((Bitmap) null);
	}

	public SkinnableBitmapDrawable(Resources res) {
		this.mBitmapState = new BitmapState((Bitmap) null);
		this.mBitmapState.mTargetDensity = this.mTargetDensity;
	}

	public SkinnableBitmapDrawable(Resources res, Bitmap bitmap) {
		this(new BitmapState(bitmap), res);
		this.mBitmapState.mTargetDensity = this.mTargetDensity;
	}

	public SkinnableBitmapDrawable(Resources res,
			InputStream is) {
		this(new BitmapState(BitmapFactory.decodeStream(is)),
				null);
		this.mBitmapState.mTargetDensity = this.mTargetDensity;
		if (this.mBitmap == null) {
			Log.w("BitmapDrawable", "BitmapDrawable cannot decode "+is);
		}
	}

	public SkinnableBitmapDrawable(Resources res, String filepath) {
		this(new BitmapState(BitmapFactory.decodeFile(filepath)), null);
		this.mBitmapState.mTargetDensity = this.mTargetDensity;
		if (this.mBitmap == null) {
			Log.w("BitmapDrawable", "BitmapDrawable cannot decode "+ filepath);
		}
	}

	@Deprecated
	public SkinnableBitmapDrawable(Bitmap bitmap) {
		this(new BitmapState(bitmap), null);
	}

	SkinnableBitmapDrawable(BitmapState state,Resources res) {
		this.mBitmapState = state;
		if (res != null) {
			this.mTargetDensity = res.getDisplayMetrics().densityDpi;
		}

		setBitmap(state.mBitmap);

		if (state.mBuildFromXml) {
			this.mRebuildShader = true;
			this.mApplyGravity = true;
		}
		if (state != null) {
			this.mTargetDensity = state.mTargetDensity;
		} else {
			this.mTargetDensity = 160;
		}

	}

	@Deprecated
	public SkinnableBitmapDrawable(InputStream is) {
		this(new BitmapState(BitmapFactory.decodeStream(is)),null);
		if (this.mBitmap == null) {
			Log.w("BitmapDrawable", "BitmapDrawable cannot decode "+ is);
		}
	}

	@Deprecated
	public SkinnableBitmapDrawable(String filepath) {
		this(new BitmapState(BitmapFactory.decodeFile(filepath)), null);
		if (this.mBitmap == null) {
			Log.w("BitmapDrawable", "BitmapDrawable cannot decode "+ filepath);
		}
	}

	private void computeBitmapSize() {
		updateBitmap();
		this.mBitmapWidth = this.mBitmap.getScaledWidth(this.mTargetDensity);
		this.mBitmapHeight = this.mBitmap.getScaledHeight(this.mTargetDensity);
	}

	

	private void setBitmap(Bitmap bitmap) {
		this.mBitmap = bitmap;
		if (bitmap != null) {
			computeBitmapSize();
			return;
		}
		this.mBitmapHeight = -1;
		this.mBitmapWidth = -1;
	}

	private void updateBitmap() {
		if (this.mBitmap != this.mBitmapState.mBitmap) {
			this.mBitmap = this.mBitmapState.mBitmap;
			if (this.mBitmapState.mBuildFromXml) {
				this.mRebuildShader = true;
				this.mApplyGravity = true;
			}
			this.mBitmapWidth = this.mBitmap
					.getScaledWidth(this.mTargetDensity);
			this.mBitmapHeight = this.mBitmap
					.getScaledHeight(this.mTargetDensity);
		}
	}
	@Override
	public void draw(Canvas canvas) {
		updateBitmap();
		Bitmap bitmap = this.mBitmap;
		BitmapState state = null;
		
		if (bitmap != null) {
			state = this.mBitmapState;
			if (this.mRebuildShader) {
				
				
			}
		}
		Shader shader = state.mPaint.getShader();
		if (shader == null) {
			this.mRebuildShader = false;

			if (mApplyGravity) {
				Gravity.apply(state.mGravity, this.mBitmapWidth,mBitmapHeight, getBounds(), this.mDstRect);
				mApplyGravity = false;
			}
			canvas.drawBitmap(bitmap, null, mDstRect,
					state.mPaint);
		} else {
			if (mApplyGravity) {
				mDstRect.set(getBounds());
				mApplyGravity = false;
			}
			canvas.drawRect(mDstRect, state.mPaint);
		}

	}

	public final Bitmap getBitmap() {
		updateBitmap();
		return this.mBitmap;
	}

	public int getChangingConfigurations() {
		return super.getChangingConfigurations()
				| this.mBitmapState.mChangingConfigurations;
	}

	public final ConstantState getConstantState() {
		return mBitmapState;
	}

	public int getGravity() {
		return this.mBitmapState.mGravity;
	}
	@Override
	public int getIntrinsicHeight() {
		updateBitmap();
		return this.mBitmapHeight;
	}
	@Override
	public int getIntrinsicWidth() {
		updateBitmap();
		return this.mBitmapWidth;
	}
	@Override
	public int getOpacity() {

		if (mBitmapState.mGravity != Gravity.FILL) {
			return PixelFormat.TRANSLUCENT;
		}
		updateBitmap();
		Bitmap bm = mBitmap;
		return (bm == null || bm.hasAlpha() || mBitmapState.mPaint.getAlpha() < 255) ? PixelFormat.TRANSLUCENT
				: PixelFormat.OPAQUE;

	}

	public final Paint getPaint() {
		return this.mBitmapState.mPaint;
	}


	public Drawable mutate() {
		return this;
	}

	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
		this.mApplyGravity = true;
	}
	@Override
	public void setAlpha(int alpha) {
		this.mBitmapState.mPaint.setAlpha(alpha);
	}

	public void setAntiAlias(boolean aa) {
		this.mBitmapState.mPaint.setAntiAlias(aa);
	}
	@Override
	public void setColorFilter(ColorFilter cf) {
		this.mBitmapState.mPaint.setColorFilter(cf);
	}

	public void setDither(boolean dither) {
		this.mBitmapState.mPaint.setDither(dither);
	}

	public void setFilterBitmap(boolean filter) {
		this.mBitmapState.mPaint.setFilterBitmap(filter);
	}

	public void setGravity(int gravity) {
		this.mBitmapState.mGravity = gravity;
		this.mApplyGravity = true;
	}

	public void setTargetDensity(int density) {
		if (density == 0) {
			density = 160;
		}
		this.mTargetDensity = density;
		updateBitmap();
		if (this.mBitmap != null) {
			computeBitmapSize();
		}
	}

	public void setTargetDensity(Canvas canvas) {
		setTargetDensity(canvas.getDensity());
	}

	public void setTargetDensity(DisplayMetrics metrics) {
		this.mTargetDensity = metrics.densityDpi;
		updateBitmap();
		if (this.mBitmap != null) {
			computeBitmapSize();
		}
	}
	static final class BitmapState extends ConstantState {
		Bitmap mBitmap;
		boolean mBuildFromXml = false;
		int mChangingConfigurations;
		int mGravity = 119;
		Paint mPaint = new Paint(6);
		int mTargetDensity = 160;

		public BitmapState(Bitmap bitmap) {
			this.mBitmap = bitmap;
		}

		public BitmapState(BitmapState bitmapState) {
			this(bitmapState.mBitmap);
			this.mChangingConfigurations = bitmapState.mChangingConfigurations;
			this.mTargetDensity = bitmapState.mTargetDensity;
		}

		public int getChangingConfigurations() {
			return this.mChangingConfigurations;
		}

		public Drawable newDrawable() {
			return new SkinnableBitmapDrawable(this, null);
		}

		public Drawable newDrawable(Resources res) {
			return new SkinnableBitmapDrawable(this, res);
		}
	}
}

