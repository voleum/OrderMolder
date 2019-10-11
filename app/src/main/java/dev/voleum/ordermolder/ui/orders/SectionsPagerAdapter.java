package dev.voleum.ordermolder.ui.orders;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.collect.ArrayListMultimap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Objects;

import dev.voleum.ordermolder.Adapter.GoodsOrderRecyclerViewAdapter;
import dev.voleum.ordermolder.Helper.DbHelper;
import dev.voleum.ordermolder.Object.Company;
import dev.voleum.ordermolder.Object.Good;
import dev.voleum.ordermolder.Object.Partner;
import dev.voleum.ordermolder.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static int[] TAB_TITLES = { R.string.order_tab_main, R.string.order_tab_goods };
    private final Context mContext;
    private Fragment fragmentMain;
    private Fragment fragmentGoods;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        switch (position) {
            case 0:
                fragmentMain = createdFragment;
                break;
            case 1:
                fragmentGoods = createdFragment;
                break;
        }
        return createdFragment;
    }

    public Hashtable<String, String> getMainInfo() {
        Hashtable<String, String> info = new Hashtable<>();

        FragmentActivity activity = fragmentMain.getActivity();
        String companyTin = ((Company) ((Spinner) activity.findViewById(R.id.spinnerCompanies)).getSelectedItem()).getTin();
        String partnerTin = ((Partner) ((Spinner) activity.findViewById(R.id.spinnerPartners)).getSelectedItem()).getTin();

        String dateTime = (((TextView) activity.findViewById(R.id.tvDate)).getText().toString().replaceAll("\\.", "-"))
                        .concat(" ")
                        .concat(((TextView) activity.findViewById(R.id.tvTime)).getText().toString())
                        .concat(".000");

        info.put("date", dateTime);
        info.put("companyTin", companyTin);
        info.put("partnerTin", partnerTin);

        return info;
    }

    public Hashtable<String, Double> getGoodsInfo() {
        Hashtable<String, Double> info = new Hashtable<>();
        RecyclerView rv = fragmentGoods.getActivity().findViewById(R.id.recycler);
        ArrayList<Good> goodsList = ((GoodsOrderRecyclerViewAdapter) rv.getAdapter()).getGoodsList();
        HashMap<Good, HashMap<String, Double>> goodsValues = ((GoodsOrderRecyclerViewAdapter) rv.getAdapter()).getGoodsValues();
        for (int i = 0; i < goodsList.size(); i++) {
            Good good = goodsList.get(i);
            HashMap<String, Double> values = goodsValues.get(good);
            info.put("price", values.get("price"));
            info.put("quantity", values.get("quantity"));
        }
        return info;
    }
}