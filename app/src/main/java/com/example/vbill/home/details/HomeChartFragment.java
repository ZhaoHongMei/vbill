package com.example.vbill.home.details;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.vbill.R;
import com.example.vbill.adapter.DateAdapter;
import com.example.vbill.adapter.HomeChartSpinnerAdapter;
import com.example.vbill.bean.DateItem;
import com.example.vbill.bean.Point;
import com.example.vbill.customizeUI.HorizontalChartView;
import com.example.vbill.util.ChartUtil;
import com.example.vbill.util.Constants;
import com.example.vbill.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class HomeChartFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "HomeChartFragment";
    private static HomeChartFragment fragment;
    private static Calendar calendar = Calendar.getInstance();
    private static int year = calendar.get(Calendar.YEAR);
    private static int month = calendar.get(Calendar.MONTH) + 1;
    private static int day = calendar.get(Calendar.DAY_OF_MONTH);
    public SharedPreferences pref;
    public SharedPreferences loginPref;
    public FragmentActivity activity;
    public SharedPreferences.Editor editor;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private LinearLayout dateLayout;


    ImageView imageView;
    TextView textView;
    private Spinner incomePaymentSpinner;

    private OnFragmentInteractionListener mListener;
    private TextView weekView;
    private TextView monthView;
    private TextView yearView;
    private TextView totalSummaryLabelView;
    private TextView totalSummaryValueView;
    private TextView noDataView;
    private PieChartView pieChart;
    private LineChartView lineChart;
    private RecyclerView dateRecyclerView;
    private DateAdapter dateAdapter;
    private int dateType;
    private int dateNumber;
    private String accountType;
    private String customerId;
    private LinearLayoutManager linearLayoutManager;
    //    private View spinnerSelectedView;
    private LinearLayout selectedItemLayout;
    private ImageView selectedImageView;
    private TextView selectedTextView;
    private PopupWindow popWindow;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        Log.d("HomeChartFragment", "onCreateView: ");
        activity = getActivity();
        pref = activity.getSharedPreferences("chart", activity.MODE_PRIVATE);
        editor = pref.edit();
        loginPref = activity.getSharedPreferences("login", activity.MODE_PRIVATE);
        dateType = pref.getInt("dateType", Constants.DATE_TYPE_WEEK);
        dateNumber = pref.getInt(Constants.WEEK_DATE_NUMBER, DateUtil.getCurrentWeek());
        accountType = pref.getString("accountType", Constants.ACCOUNT_OUT);

        view = inflater.inflate(R.layout.fragment_home_chart, container, false);
        weekView = view.findViewById(R.id.week);
        monthView = view.findViewById(R.id.month);
        yearView = view.findViewById(R.id.year);
        totalSummaryLabelView = view.findViewById(R.id.total_summary_label);
        totalSummaryValueView = view.findViewById(R.id.total_summary_value);
        noDataView = view.findViewById(R.id.no_data_text);
        dateRecyclerView = view.findViewById(R.id.date_recycler_view);
        lineChart = (LineChartView) view.findViewById(R.id.all_analysis_line);
        pieChart = (PieChartView) view.findViewById(R.id.customer_pie_show);
        selectedItemLayout = view.findViewById(R.id.selected_item_layout);
        selectedImageView = view.findViewById(R.id.selected_image);
        selectedTextView = view.findViewById(R.id.selected_text);
        dateLayout = view.findViewById(R.id.date_layout);
        customerId = String.valueOf(loginPref.getInt("userId", -1));

        weekView.setOnClickListener(this);
        monthView.setOnClickListener(this);
        yearView.setOnClickListener(this);
        selectedItemLayout.setOnClickListener(this);

        initSelectedItem();
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        dateRecyclerView.setLayoutManager(linearLayoutManager);
        List<DateItem> dateItems = DateUtil.getDateItems(dateType);
        dateAdapter = new DateAdapter(dateItems, this, dateNumber);
        dateRecyclerView.setAdapter(dateAdapter);
        Log.d(TAG, "onCreateView: dateNumber" + dateNumber);

        //自动滚动到最后一个Item
//            linearLayoutManager.setStackFromEnd(linearLayoutManager.findLastVisibleItemPosition() - linearLayoutManager.findFirstVisibleItemPosition() < dateAdapter.getItemCount() - 1);

//        incomePaymentSpinner = view.findViewById(R.id.income_payment_spinner);
//        //data : List<Map<String,Object>>
//        List<Map<String, Object>> listMaps = HomeChartSpinnerAdapter.getListMaps();
//        SimpleAdapter simpleAdapter = new SimpleAdapter(this.getContext(), listMaps, R.layout.chart_spinner_item, new String[]{"logo", "itemName"}, new int[]{R.id.spinner_image, R.id.spinner_text});
//        incomePaymentSpinner.setAdapter(simpleAdapter);
//        incomePaymentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.d(TAG, "onItemSelected: " + view);
//                if (view == null) {
//                    Log.d(TAG, "onItemSelected: getChildAt "+view);
//                }
//                Map selectItem = (Map) incomePaymentSpinner.getItemAtPosition(position);
//                if (view != null) {
//                    imageView = view.findViewById(R.id.spinner_image);
//                    textView = view.findViewById(R.id.spinner_text);
//                    if ("收入".equals(selectItem.get("itemName"))) {
//                        accountType = Constants.ACCOUNT_IN;
//                        editor.putString("accountType", Constants.ACCOUNT_IN);
//                        generateCharts();
//                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.income));
//                        totalSummaryLabelView.setText(R.string.total_summary_in);
//                    } else {
//                        accountType = Constants.ACCOUNT_OUT;
//                        editor.putString("accountType", Constants.ACCOUNT_OUT);
//                        generateCharts();
//                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.outcome));
//                        totalSummaryLabelView.setText(R.string.total_summary_out);
//                    }
//                    editor.apply();
//                    textView.setTextColor(Color.WHITE);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        changeDateType(dateType);
        return view;
    }

    private void initPopWindow(View v) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.account_popup, null, false);
        LinearLayout incomeLayout = view.findViewById(R.id.income_layout);
        LinearLayout outcomeLayout = view.findViewById(R.id.outcome_layout);
        incomeLayout.setOnClickListener(this);
        outcomeLayout.setOnClickListener(this);
        popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popWindow.setAnimationStyle(R.style.pop_anim);
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popWindow.setBackgroundDrawable(new ColorDrawable(0xffffffff));
        popWindow.showAsDropDown(dateLayout, 0, 2);
    }

    private void initSelectedItem() {
        String accountType = pref.getString("accountType", Constants.ACCOUNT_IN);
        Log.d(TAG, "initSelectedItem: " + accountType);
        if (Constants.ACCOUNT_IN.equals(accountType)) {
            totalSummaryLabelView.setText(R.string.total_summary_in);
            selectedTextView.setText(R.string.create_header_income);
            selectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.income));
        } else {
            totalSummaryLabelView.setText(R.string.total_summary_out);
            selectedTextView.setText(R.string.create_header_outcome);
            selectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.outcome));
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.week:
                changeDateType(Constants.DATE_TYPE_WEEK);
                break;
            case R.id.month:
                changeDateType(Constants.DATE_TYPE_MONTH);
                break;
            case R.id.year:
                changeDateType(Constants.DATE_TYPE_YEAR);
                break;
            case R.id.income_layout:
                accountType = Constants.ACCOUNT_IN;
                editor.putString("accountType", Constants.ACCOUNT_IN);
                generateCharts();
                selectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.income));
                selectedTextView.setText(R.string.create_header_income);
                totalSummaryLabelView.setText(R.string.total_summary_in);
                editor.apply();
                popWindow.dismiss();
                break;
            case R.id.outcome_layout:
                accountType = Constants.ACCOUNT_OUT;
                editor.putString("accountType", Constants.ACCOUNT_OUT);
                generateCharts();
                selectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.outcome));
                selectedTextView.setText(R.string.create_header_outcome);
                totalSummaryLabelView.setText(R.string.total_summary_out);
                editor.apply();
                popWindow.dismiss();
                break;
            case R.id.selected_item_layout:
                initPopWindow(v);
                break;
            default:
                break;
        }
    }

    private void changeDateType(int mdateType) {
        Log.d(TAG, "changeDateType:dateType " + mdateType);
        TextView view = weekView;
        editor.putInt("dateType", mdateType);
        editor.apply();
        dateType = mdateType;
        switch (mdateType) {
            case Constants.DATE_TYPE_WEEK:
                dateNumber = pref.getInt(Constants.WEEK_DATE_NUMBER, DateUtil.getCurrentNumber(Constants.DATE_TYPE_WEEK));
                view = weekView;
                break;
            case Constants.DATE_TYPE_MONTH:
                dateNumber = pref.getInt(Constants.MONTH_DATE_NUMBER, DateUtil.getCurrentNumber(Constants.DATE_TYPE_MONTH));
                view = monthView;
                break;
            case Constants.DATE_TYPE_YEAR:
                dateNumber = pref.getInt(Constants.YEAR_DATE_NUMBER, DateUtil.getCurrentNumber(Constants.DATE_TYPE_YEAR));
                view = yearView;
                break;
            default:
                break;
        }
        Log.d(TAG, "changeDateType:dateNumber " + dateNumber);
        refreshRecyclerView(mdateType, dateNumber);
        generateCharts();
        GradientDrawable weekDrawable = (GradientDrawable) weekView.getBackground();
        weekDrawable.setColor(Color.parseColor("#008577"));
        weekView.setTextColor(Color.parseColor("#ffffff"));

        GradientDrawable monthDdrawable = (GradientDrawable) monthView.getBackground();
        monthDdrawable.setColor(Color.parseColor("#008577"));
        monthView.setTextColor(Color.parseColor("#ffffff"));

        GradientDrawable yearDrawable = (GradientDrawable) yearView.getBackground();
        yearDrawable.setColor(Color.parseColor("#008577"));
        yearView.setTextColor(Color.parseColor("#ffffff"));

        GradientDrawable drawable = (GradientDrawable) view.getBackground();
        drawable.setColor(Color.parseColor("#ffffff"));
        view.setTextColor(Color.parseColor("#008577"));
    }

    private void generateCharts() {
        ChartUtil.generateCharts(view, dateType, dateNumber, customerId, accountType, this, lineChart, pieChart, noDataView, totalSummaryValueView);
    }

    public void generateCharts(int dateNumber) {
        storeDateNumberToSharePreference(dateNumber);
        int firstVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
        int lastVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
        int size = dateAdapter.getItemCount();

        Log.d(TAG, "generateCharts: " + firstVisibleItemPosition + " " + lastVisibleItemPosition + " " + dateNumber);
        if (dateNumber < size && dateNumber - 1 == lastVisibleItemPosition) {
            dateRecyclerView.smoothScrollToPosition(dateNumber);
        } else if (dateNumber > 1 && dateNumber - 1 == firstVisibleItemPosition) {
            dateRecyclerView.smoothScrollToPosition(dateNumber - 2);
        }
        defaultRecyclerViewUnderLine(dateNumber);
        ChartUtil.generateCharts(view, dateType, dateNumber, customerId, accountType, this, lineChart, pieChart, noDataView, totalSummaryValueView);
    }

    private void refreshRecyclerView(int dateType, int dateNumber) {
        Log.d(TAG, "refreshRecyclerView: " + dateNumber);
        List<DateItem> dateItems = DateUtil.getDateItems(dateType);
        dateAdapter = new DateAdapter(dateItems, this, dateNumber);
        dateRecyclerView.smoothScrollToPosition(dateNumber);
        dateRecyclerView.setAdapter(dateAdapter);
    }

    private void storeDateNumberToSharePreference(int dateNumberX) {
        dateNumber = dateNumberX;
        switch (dateType) {
            case Constants.DATE_TYPE_WEEK:
                editor.putInt(Constants.WEEK_DATE_NUMBER, dateNumber);

                break;
            case Constants.DATE_TYPE_MONTH:
                editor.putInt(Constants.MONTH_DATE_NUMBER, dateNumber);
                break;
            case Constants.DATE_TYPE_YEAR:
                editor.putInt(Constants.YEAR_DATE_NUMBER, dateNumber);
                break;
            default:
                break;
        }

        editor.apply();
        Log.d(TAG, "storeDateNumberToSharePreference: " + pref.getInt(Constants.WEEK_DATE_NUMBER, 0));
    }

    //将RecyclerView看的见的子View下划线恢复默认
    private void defaultRecyclerViewUnderLine(int dateNumberX) {
        DateAdapter.ViewHolder viewHolder;
        for (int i = 0; i < dateAdapter.getItemCount(); i++) {
            viewHolder = (DateAdapter.ViewHolder) dateAdapter.getViewHolderMap().get(i);
            if (viewHolder != null) {
                View underLine = viewHolder.dateUnderLineView;
                underLine.setVisibility(View.GONE);
            }
        }
        viewHolder = (DateAdapter.ViewHolder) dateAdapter.getViewHolderMap().get(dateNumberX - 1);
        if (viewHolder != null) {
            View underLine = viewHolder.dateUnderLineView;
            underLine.setVisibility(View.VISIBLE);
        }
    }


    public HomeChartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public static HomeChartFragment getInstance() {
        try {
            if (fragment == null) {
                fragment = new HomeChartFragment();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //get arguments
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }

}
