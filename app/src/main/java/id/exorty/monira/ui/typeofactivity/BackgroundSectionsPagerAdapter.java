package id.exorty.monira.ui.typeofactivity;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.eclipsesource.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

import id.exorty.monira.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class BackgroundSectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_all, R.string.tab_employee, R.string.tab_goods, R.string.tab_capital};
    private final Context mContext;
    private static int mIdTypeOfActivity;
    private static int mYear;
    private static String mToken;
    private List<PlaceholderFragment> mPlaceholderFragments = new ArrayList<PlaceholderFragment>();

    public BackgroundSectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;

    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        PlaceholderFragment placeholderFragment = PlaceholderFragment.newInstance(position + 1, null);
        mPlaceholderFragments.add(placeholderFragment);
        return placeholderFragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 4;
    }

    public void updateData(JsonObject jsonObject){
        mPlaceholderFragments.get(0).updateDate(jsonObject);
        mPlaceholderFragments.get(1).updateDate(jsonObject);
        mPlaceholderFragments.get(2).updateDate(jsonObject);
        mPlaceholderFragments.get(3).updateDate(jsonObject);
        notifyDataSetChanged();
    }

}