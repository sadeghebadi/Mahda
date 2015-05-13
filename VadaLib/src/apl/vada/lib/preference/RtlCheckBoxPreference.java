package apl.vada.lib.preference;


import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import apl.vada.lib.R;

public class RtlCheckBoxPreference extends CheckBoxPreference {

	private AlertDialog mAlertDialog;
	private CheckBox mCheckBox;
	private String mPrevValidValue;
	private DEFAULT_VALUE mValue;
	private CHECkDIRECTION mViewDirection;

	public RtlCheckBoxPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray customAttrs = context.obtainStyledAttributes(attrs,
				R.styleable.RTLCheckBoxPreference);

		mValue = DEFAULT_VALUE.values()[customAttrs.getInt(
				R.styleable.RTLCheckBoxPreference_defaultValue, 0)];
		/*
		 * LTR is the default.
		 */
		mViewDirection = CHECkDIRECTION.values()[customAttrs.getInt(
				R.styleable.RTLCheckBoxPreference_checkdirection, 0)];

		if (mViewDirection == CHECkDIRECTION.RTL) {
			setLayoutResource(R.layout.preference_checkbox);
		}
		customAttrs.recycle();

	}

	@Override
	protected void onClick() {
		// TODO Auto-generated method stub
		super.onClick();
	}

	@Override
	protected View onCreateView(ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = super.onCreateView(parent);

		if (mViewDirection == CHECkDIRECTION.RTL) {
			setLayoutResource(R.layout.preference_checkbox);
//			 RelativeLayout layout = (RelativeLayout) ((LinearLayout) view)
//			 .getChildAt(1);
//			 RelativeLayout.LayoutParams lpTitle = new LayoutParams(
//			 view.getLayoutParams());
//			 // RelativeLayout.LayoutParams lpcheck = new LayoutParams(
//			 // view.getLayoutParams());
//			 lpTitle.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//			 // lpcheck.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			
//			 TextView tvTitle = (TextView) layout.getChildAt(0);
//			
//			 CheckBox cbcheck =
//			 (CheckBox)view.findViewById(android.R.id.checkbox);
//			 cbcheck.setGravity(Gravity.LEFT);
//			 // cbcheck.setLayoutParams(lpcheck);
//			 tvTitle.setLayoutParams(lpTitle);

		}
		return view;

	}

	private enum DEFAULT_VALUE {
		TRUE, FALSE
	}

	private enum CHECkDIRECTION {
		RTL, LTR
	}
}
