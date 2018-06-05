package bit.basemodule.utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.inputmethod.InputMethodManager;

import java.text.DecimalFormat;

public class Utils {
    public static Drawable getDrawableFromSvg(Context context, int resource) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(resource, null);
        } else {
            return VectorDrawableCompat.create(context.getResources(), resource, null);
        }
    }
    public static String decimalFormatter(float value) {
        return new DecimalFormat("##.00").format(value);
    }
    public static void hideKeyboardHard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
