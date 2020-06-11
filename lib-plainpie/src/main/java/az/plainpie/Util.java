package az.plainpie;

import android.content.Context;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

/**
 * Created by vmoudyp on 7/5/16.
 */
public class Util {

    public static final String ABEL_REGULAR = "abel-regular";

    public static Typeface setTypeface(Context context, String fontName) {
        if (fontName == null)
            return ResourcesCompat.getFont(context, R.font.abel_regular);
        else {
            if (fontName.equals(ABEL_REGULAR)) {
                return ResourcesCompat.getFont(context, R.font.abel_regular);
            } else {
                return ResourcesCompat.getFont(context, R.font.abel_regular);
            }
        }
    }
}
