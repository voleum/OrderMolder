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

import java.util.HashMap;

import dev.voleum.ordermolder.Adapter.GoodsOrderRecyclerViewAdapter;
import dev.voleum.ordermolder.Object.Company;
import dev.voleum.ordermolder.Object.Partner;
import dev.voleum.ordermolder.Object.Warehouse;
import dev.voleum.ordermolder.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static int[] TAB_TITLES = { R.string.order_tab_main, R.string.order_tab_goods };
    private final Context context;
    private Fragment fragmentMain;
    private Fragment fragmentGoods;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
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
        return context.getResources().getString(TAB_TITLES[position]);
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

    public double getSum() {
        return ((PlaceholderFragment) fragmentGoods).getSum();
    }

    public HashMap<String, Object> getMainInfo() {
        HashMap<String, Object> info = new HashMap<>();

        FragmentActivity activity = fragmentMain.getActivity();
        String companyUid = ((Company) ((Spinner) activity.findViewById(R.id.order_spinner_companies)).getSelectedItem()).getUid();
        String partnerUid = ((Partner) ((Spinner) activity.findViewById(R.id.order_spinner_partners)).getSelectedItem()).getUid();
        String warehouseUid = ((Warehouse) ((Spinner) activity.findViewById(R.id.order_spinner_warehouses)).getSelectedItem()).getUid();
        double sum = Double.parseDouble(((TextView) activity.findViewById(R.id.order_tv_sum)).getText().toString());

        String dateTime = (((TextView) activity.findViewById(R.id.order_tv_date)).getText().toString().replaceAll("\\.", "-"))
                        .concat(" ")
                        .concat(((TextView) activity.findViewById(R.id.order_tv_time)).getText().toString())
                        .concat(".000");

        info.put("date", dateTime);
        info.put("company_uid", companyUid);
        info.put("partner_uid", partnerUid);
        info.put("warehouse_uid", warehouseUid);
        info.put("sum", sum);

        return info;
    }

    public HashMap<Integer, HashMap<String, Object>> getGoodsInfo() {
        RecyclerView rv = fragmentGoods.getActivity().findViewById(R.id.recycler_goods);
        return ((GoodsOrderRecyclerViewAdapter) rv.getAdapter()).getGoods();
    }
}