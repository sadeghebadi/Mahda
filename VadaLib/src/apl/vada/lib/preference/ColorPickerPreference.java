package apl.vada.lib.preference;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import apl.vada.lib.R;

/**
 * A preference type that allows a user to choose a time
 * 
 * @author Sergey Margaritov
 */
public class ColorPickerPreference extends Preference implements
		Preference.OnPreferenceClickListener,
		ColorPickerDialog.OnColorChangedListener {

	View mView;
	ColorPickerDialog mDialog;
	private int mValue = Color.BLACK;
	private float mDensity = 0;
	private boolean mAlphaSliderEnabled = false;
	private boolean mHexValueEnabled = false;
	private COLORDIR mViewDirection;

	public ColorPickerPreference(Context context) {
		super(context);
		init(context, null);
	}

	public ColorPickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
		TypedArray customAttrs = context
				.obtainStyledAttributes(
						attrs,
						R.styleable.RTLColorPreference);
		mViewDirection = COLORDIR.values()[customAttrs.getInt(
				R.styleable.RTLColorPreference_DIRCOLOR, 0)];
		if (mViewDirection == COLORDIR.RTL) {
			setLayoutResource(R.layout.preference_checkbox);
		}
		customAttrs.recycle();
		customAttrs.recycle();
	}

	public ColorPickerPreference(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getColor(index, Color.BLACK);
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		onColorChanged(restoreValue ? getPersistedInt(mValue)
				: (Integer) defaultValue);
	}

	private void init(Context context, AttributeSet attrs) {
		mDensity = getContext().getResources().getDisplayMetrics().density;
		setOnPreferenceClickListener(this);
		if (attrs != null) {
			mAlphaSliderEnabled = attrs.getAttributeBooleanValue(null,
					"alphaSlider", false);
			mHexValueEnabled = attrs.getAttributeBooleanValue(null, "hexValue",
					false);
		}
	}

	@Override
	protected View onCreateView(ViewGroup parent) {
		// TODO Auto-generated method stub

		View view = super.onCreateView(parent);
		if (mViewDirection == COLORDIR.RTL) {
			RelativeLayout layout = (RelativeLayout) ((LinearLayout) view)
					.getChildAt(1);
			RelativeLayout.LayoutParams lpTitle = new LayoutParams(
					view.getLayoutParams());
			RelativeLayout.LayoutParams lpDescription = new LayoutParams(
					view.getLayoutParams());

			
			
			lpTitle.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lpDescription.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

			TextView tvTitle = (TextView) view.findViewById(android.R.id.title);
			tvTitle.setLayoutParams(lpTitle);
			tvTitle.setGravity(Gravity.RIGHT);

//			ImageView iv = (ImageView)view.findViewById(android.R.id.);
//			iv.setVisibility(view.INVISIBLE);
			
			lpDescription.addRule(RelativeLayout.BELOW, tvTitle.getId());

			TextView tvDescription = (TextView) view
					.findViewById(android.R.id.summary);
			tvDescription.setGravity(Gravity.RIGHT);
			tvDescription.setLayoutParams(lpDescription);
		}

		return view;
	}

	@Override
	protected void onBindView(View view) {
		super.onBindView(view);
		mView = view;
		setPreviewColor();
	}

	private void setPreviewColor() {
		if (mView == null)
			return;
//		ImageView iView = (ImageView)mView.findViewById(R.id.imagev);
		ImageView iView = new ImageView(getContext());
		LinearLayout widgetFrameView = ((LinearLayout) mView
				.findViewById(android.R.id.widget_frame));

		widgetFrameView.setGravity(Gravity.CENTER_VERTICAL);
		if (widgetFrameView == null)
			return;
		widgetFrameView.setVisibility(View.VISIBLE);

		widgetFrameView.setPadding(0, 10, (int) (mDensity * 8),
				widgetFrameView.getPaddingBottom());
		// remove already create preview image
		int count = widgetFrameView.getChildCount();
		if (count > 0) {
			widgetFrameView.removeViews(0, count);
		}
		widgetFrameView.addView(iView);
		widgetFrameView.setMinimumWidth(0);
		
		iView.setBackgroundDrawable(new AlphaPatternDrawable(
				(int) (5 * mDensity)));
		iView.setImageBitmap(getPreviewBitmap());
	}

	private Bitmap getPreviewBitmap() {
		int d = (int) (mDensity * 31); // 30dip
		int color = mValue;
		Bitmap bm = Bitmap.createBitmap(d, d, Config.ARGB_8888);
		int w = bm.getWidth();
		int h = bm.getHeight();
		int c = color;
		for (int i = 0; i < w; i++) {
			for (int j = i; j < h; j++) {
				c = (i <= 1 || j <= 1 || i >= w - 2 || j >= h - 2) ? Color.GRAY
						: color;
				bm.setPixel(i, j, c);
				if (i != j) {
					bm.setPixel(j, i, c);
				}
			}
		}

		return bm;
	}

	@Override
	public void onColorChanged(int color) {
		if (isPersistent()) {
			persistInt(color);
		}
		mValue = color;
		setPreviewColor();
		try {
			getOnPreferenceChangeListener().onPreferenceChange(this, color);
		} catch (NullPointerException e) {

		}
	}

	public boolean onPreferenceClick(Preference preference) {
		showDialog(null);
		return false;
	}

	protected void showDialog(Bundle state) {
		mDialog = new ColorPickerDialog(getContext(), mValue);
		mDialog.setOnColorChangedListener(this);
		if (mAlphaSliderEnabled) {
			mDialog.setAlphaSliderVisible(true);
		}
		if (mHexValueEnabled) {
			mDialog.setHexValueEnabled(true);
		}
		if (state != null) {
			mDialog.onRestoreInstanceState(state);
		}
		mDialog.show();
	}

	/**
	 * Toggle Alpha Slider visibility (by default it's disabled)
	 * 
	 * @param enable
	 */
	public void setAlphaSliderEnabled(boolean enable) {
		mAlphaSliderEnabled = true;
	}

	/**
	 * Toggle Hex Value visibility (by default it's disabled)
	 * 
	 * @param enable
	 */
	public void setHexValueEnabled(boolean enable) {
		mHexValueEnabled = enable;
	}

	/**
	 * For custom purposes. Not used by ColorPickerPreferrence
	 * 
	 * @param color
	 * @author Unknown
	 */
	public static String convertToARGB(int color) {
		String alpha = Integer.toHexString(Color.alpha(color));
		String red = Integer.toHexString(Color.red(color));
		String green = Integer.toHexString(Color.green(color));
		String blue = Integer.toHexString(Color.blue(color));

		if (alpha.length() == 1) {
			alpha = "0" + alpha;
		}

		if (red.length() == 1) {
			red = "0" + red;
		}

		if (green.length() == 1) {
			green = "0" + green;
		}

		if (blue.length() == 1) {
			blue = "0" + blue;
		}

		return "#" + alpha + red + green + blue;
	}

	/**
	 * For custom purposes. Not used by ColorPickerPreference
	 * 
	 * @param color
	 * @author Charles Rosaaen
	 * @return A string representing the hex value of color, without the alpha
	 *         value
	 */
	public static String convertToRGB(int color) {
		String red = Integer.toHexString(Color.red(color));
		String green = Integer.toHexString(Color.green(color));
		String blue = Integer.toHexString(Color.blue(color));

		if (red.length() == 1) {
			red = "0" + red;
		}

		if (green.length() == 1) {
			green = "0" + green;
		}

		if (blue.length() == 1) {
			blue = "0" + blue;
		}

		return "#" + red + green + blue;
	}

	/**
	 * For custom purposes. Not used by ColorPickerPreferrence
	 * 
	 * @param argb
	 * @throws NumberFormatException
	 * @author Unknown
	 */
	public static int convertToColorInt(String argb)
			throws IllegalArgumentException {

		if (!argb.startsWith("#")) {
			argb = "#" + argb;
		}

		return Color.parseColor(argb);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		final Parcelable superState = super.onSaveInstanceState();
		if (mDialog == null || !mDialog.isShowing()) {
			return superState;
		}

		final SavedState myState = new SavedState(superState);
		myState.dialogBundle = mDialog.onSaveInstanceState();
		return myState;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state == null || !(state instanceof SavedState)) {
			// Didn't save state for us in onSaveInstanceState
			super.onRestoreInstanceState(state);
			return;
		}

		SavedState myState = (SavedState) state;
		super.onRestoreInstanceState(myState.getSuperState());
		showDialog(myState.dialogBundle);
	}

	private static class SavedState extends BaseSavedState {
		Bundle dialogBundle;

		public SavedState(Parcel source) {
			super(source);
			dialogBundle = source.readBundle();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeBundle(dialogBundle);
		}

		public SavedState(Parcelable superState) {
			super(superState);
		}

		@SuppressWarnings("unused")
		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

	public enum COLORDIR {
		RTL, LTR
	}
}