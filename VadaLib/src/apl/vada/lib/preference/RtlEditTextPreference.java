package apl.vada.lib.preference;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import apl.vada.lib.R;


public class RtlEditTextPreference extends EditTextPreference {

	private AlertDialog	mAlertDialog;
	private EditText	mEditText;
	private String		mPrevValidValue;
	private INPUT_TYPE	mInputType;
	private DIRECTION	mViewDirection;

	public RtlEditTextPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mEditText = getEditText();

		setPositiveButtonText(R.string.accept);
		setNegativeButtonText(R.string.cancel);

		TypedArray customAttrs = context.obtainStyledAttributes(attrs, R.styleable.RTLEditTextPreference);

		/*
		 * STRING is the default.
		 */
		mInputType = INPUT_TYPE.values()[customAttrs.getInt(R.styleable.RTLEditTextPreference_inputType, 0)];
		/*
		 * LTR is the default.
		 */
		mViewDirection = DIRECTION.values()[customAttrs.getInt(R.styleable.RTLEditTextPreference_direction, 0)];

		customAttrs.recycle();
	}

	@Override
	protected void onClick() {
		super.onClick();

		mAlertDialog = (AlertDialog) getDialog();

		mPrevValidValue = mEditText.getText().toString();

		Button negativeBtn = mAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
		Button positiveBtn = mAlertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
		positiveBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setSummary(mEditText.getText());
				mAlertDialog.dismiss();
			}
		});
		negativeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
		
				mEditText.setText(mPrevValidValue);
				mAlertDialog.dismiss();
			}
		});
	}

	@Override
	public void onDismiss(DialogInterface dialog) {

		super.onDismiss(dialog);

		String userInput = mEditText.getText().toString();

		try {
			if (mInputType == INPUT_TYPE.FLOAT) {

				Float.parseFloat(userInput);
				mPrevValidValue = userInput;

			} else if (mInputType == INPUT_TYPE.DOUBLE) {

				Double.parseDouble(userInput);
				mPrevValidValue = userInput;

			} else if (mInputType == INPUT_TYPE.INTEGER) {
				
				Integer.parseInt(userInput);
				mPrevValidValue = userInput;
			}
		} catch (NumberFormatException e) {
			((Dialog) dialog).show();
		}
	}

	@Override
	protected View onCreateView(ViewGroup parent) {

		View view = super.onCreateView(parent);

		/*
		 * Align all views to the right, if the developer chose it to be RTL.
		 */
		if (mViewDirection == DIRECTION.RTL) {
			RelativeLayout layout = (RelativeLayout) ((LinearLayout) view).getChildAt(1);
			RelativeLayout.LayoutParams lpTitle = new LayoutParams(view.getLayoutParams());
			RelativeLayout.LayoutParams lpDescription = new LayoutParams(view.getLayoutParams());

			lpTitle.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lpDescription.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

			TextView tvTitle = (TextView) layout.getChildAt(0);
			tvTitle.setLayoutParams(lpTitle);

			lpDescription.addRule(RelativeLayout.BELOW, tvTitle.getId());

			TextView tvDescription = (TextView) layout.getChildAt(1);
			tvDescription.setLayoutParams(lpDescription);
		}

		return view;
	}

	private enum INPUT_TYPE {
		STRING, FLOAT, DOUBLE, INTEGER
	}

	private enum DIRECTION {
		LTR, RTL
	}
}
