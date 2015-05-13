package ir.rayacell.mahda.view;

import ir.rayacell.mahda.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageButton;

public class CustomToggleButton extends ImageButton implements Checkable {
	private OnCheckedChangeListener onCheckedChangeListener;

	public CustomToggleButton(Context context) {
		super(context);
	}

	public CustomToggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		setChecked(attrs);
	}

	public CustomToggleButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setChecked(attrs);
	}

	private void setChecked(AttributeSet attrs) {
//		TypedArray a = getContext().obtainStyledAttributes(attrs,
//				R.styleable.CustomToggleButton);
//		setChecked(a.getBoolean(R.styleable.CustomToggleButton_android_checked,
//				false));
//		a.recycle();
	}

	@Override
	public boolean isChecked() {
		return isSelected();
	}

	@Override
	public void setChecked(boolean checked) {
		setSelected(checked);

		if (onCheckedChangeListener != null) {
//			onCheckedChangeListener.onCheckedChanged(this, checked);
		}
	}

	@Override
	public void toggle() {
//		setChecked(!isChecked());
	}

	@Override
	public boolean performClick() {
		toggle();
		return super.performClick();
	}

	public OnCheckedChangeListener getOnCheckedChangeListener() {
		return onCheckedChangeListener;
	}

	public void setOnCheckedChangeListener(
			OnCheckedChangeListener onCheckedChangeListener) {
		this.onCheckedChangeListener = onCheckedChangeListener;
	}

	public static interface OnCheckedChangeListener {
		public void onCheckedChanged(CustomToggleButton buttonView,
				boolean isChecked);
	}
}
