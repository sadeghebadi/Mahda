package ir.rayacell.mahda.util;

import ir.rayacell.mahda.R;
import ir.rayacell.mahda.security.Caesar;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

public class Util {
	
	
	 public static String stringToBinary(String text)
	 {
//		 return   Caesar.encryptText(text);
		 return text;
	      
	 }
	 public static String BinaryToString(String binaryCode)
	 {
	  return binaryCode;
//		 return   Caesar.decryptText(binaryCode);
	 }
	public static boolean checkEmpty(Context context , EditText... args){
		for (int i = 0; i < args.length; i++) {
			if(args[i].getText().toString().matches("")){
				Toast.makeText(context, "اطلاعات را پر کنید", 3000).show();
				return true;
			}
		}
		return false;
		
	}
	private static void set_numberpicker_text_colour(NumberPicker number_picker , Context context){
	    final int count = number_picker.getChildCount();
	    final int color = context.getResources().getColor(R.color.white_color);

	    for(int i = 0; i < count; i++){
	        View child = number_picker.getChildAt(i);

	        try{
	            Field wheelpaint_field = number_picker.getClass().getDeclaredField("mSelectorWheelPaint");
	            wheelpaint_field.setAccessible(true);

	            ((Paint)wheelpaint_field.get(number_picker)).setColor(color);
	            ((EditText)child).setTextColor(color);
	            number_picker.invalidate();
	        }
	        catch(NoSuchFieldException e){
	            Log.w("setNumberPickerTextColor", e);
	        }
	        catch(IllegalAccessException e){
	            Log.w("setNumberPickerTextColor", e);
	        }
	        catch(IllegalArgumentException e){
	            Log.w("setNumberPickerTextColor", e);
	        }
	    }
	}
	public static void set_timepicker_text_colour(TimePicker time_picker , Context context){
		Resources system;
	    system = Resources.getSystem();
	    int hour_numberpicker_id = system.getIdentifier("hour", "id", "android");
	    int minute_numberpicker_id = system.getIdentifier("minute", "id", "android");
	    int ampm_numberpicker_id = system.getIdentifier("amPm", "id", "android");

	    NumberPicker hour_numberpicker = (NumberPicker) time_picker.findViewById(hour_numberpicker_id);
	    NumberPicker minute_numberpicker = (NumberPicker) time_picker.findViewById(minute_numberpicker_id);
	    NumberPicker ampm_numberpicker = (NumberPicker) time_picker.findViewById(ampm_numberpicker_id);

	    set_numberpicker_text_colour(hour_numberpicker , context);
	    set_numberpicker_text_colour(minute_numberpicker, context);
	    set_numberpicker_text_colour(ampm_numberpicker, context);
	}
}
