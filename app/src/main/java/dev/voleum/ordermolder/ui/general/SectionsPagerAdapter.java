package dev.voleum.ordermolder.ui.general;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.ui.cashreceipts.PlaceholderCashReceiptFragment;
import dev.voleum.ordermolder.ui.orders.PlaceholderOrderFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public static final int TYPE_ORDER = 0;
    public static final int TYPE_CASH_RECEIPT = 1;

    @StringRes
    private int[] TAB_TITLES;
    private final Context context;
//    private Fragment fragmentMain;
//    private Fragment fragmentSecondary;
    private int typeDoc;

    public SectionsPagerAdapter(Context context, FragmentManager fm, int typeDoc) {
        super(fm);
        this.context = context;
        this.typeDoc = typeDoc;
        switch (typeDoc) {
            case TYPE_ORDER:
                TAB_TITLES = new int[]{R.string.tab_main, R.string.tab_order_goods};
                break;
            case TYPE_CASH_RECEIPT:
                TAB_TITLES = new int[]{R.string.tab_main, R.string.tab_cash_receipt_objects};
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (typeDoc) {
            case TYPE_ORDER:
                return PlaceholderOrderFragment.newInstance(position + 1);
            case TYPE_CASH_RECEIPT:
                return PlaceholderCashReceiptFragment.newInstance(position + 1);
        }
        return PlaceholderOrderFragment.newInstance(position + 1);
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

//    @NonNull
//    @Override
//    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
//        switch (position) {
//            case 0:
//                fragmentMain = createdFragment;
//                break;
//            case 1:
//                fragmentSecondary = createdFragment;
//                break;
//        }
//        return createdFragment;
//    }
}