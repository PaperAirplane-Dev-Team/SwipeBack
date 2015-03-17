package info.papdt.swipeback.helper;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

public class WindowInsetsColorDrawable extends Drawable
{
	private Drawable mDef, mTop;
	private int mTopInset = 0;
	
	public WindowInsetsColorDrawable(Drawable def) {
		mDef = def;
	}

	@Override
	public void draw(Canvas canvas) {
		mDef.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		mDef.draw(canvas);
		
		if (mTop != null) {
			mTop.setBounds(0, 0, canvas.getWidth(), mTopInset);
			mTop.draw(canvas);
		}
	}

	@Override
	public void setColorFilter(ColorFilter p1) {
		// nothing is here
	}

	@Override
	public void setAlpha(int alpha) {
		mDef.setAlpha(alpha);
		
		if (mTop != null) {
			mTop.setAlpha(alpha);
		}
	}

	@Override
	public int getOpacity() {
		return 0;
	}
	
	public void setTopDrawable(Drawable d) {
		mTop = d;
		invalidateSelf();
	}
	
	public void setTopInset(int inset) {
		mTopInset = inset;
		invalidateSelf();
	}
}
