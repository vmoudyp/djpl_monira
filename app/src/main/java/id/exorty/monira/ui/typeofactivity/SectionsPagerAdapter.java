package id.exorty.monira.ui.typeofactivity;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

import id.exorty.monira.R;
import id.exorty.monira.service.DataService;
import id.exorty.monira.ui.model.DataInfo;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_all, R.string.tab_employee, R.string.tab_goods, R.string.tab_capital};
    private final Context mContext;
    private static JsonObject mJsonObject;

    public SectionsPagerAdapter(Context context, FragmentManager fm, JsonObject jsonObject) {
        super(fm);
        mContext = context;
        mJsonObject = jsonObject;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(position + 1, mJsonObject);
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

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    public void updateData(JsonObject jsonObject){
        mJsonObject = jsonObject;
        notifyDataSetChanged();
    }
}