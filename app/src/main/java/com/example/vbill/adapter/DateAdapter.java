package com.example.vbill.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vbill.R;
import com.example.vbill.bean.DateItem;
import com.example.vbill.home.details.HomeChartFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {
    private List<DateItem> mDateItemList;
    private HomeChartFragment mFragment;
    private int mInitDateNumber;

    private Map viewHolderMap = new HashMap<>();

    public Map getViewHolderMap() {
        return viewHolderMap;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View dateView;
        TextView dateNameView;
        public View dateUnderLineView;

        public ViewHolder(View view) {
            super(view);
            dateView = view;
            dateNameView = view.findViewById(R.id.date_name);
            dateUnderLineView = view.findViewById(R.id.date_underline);
        }
    }

    public DateAdapter(List<DateItem> dateItemList, HomeChartFragment fragment, int initDateNumber) {
        mDateItemList = dateItemList;
        mFragment = fragment;
        mInitDateNumber = initDateNumber;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.date_item, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        //click events
        viewHolder.dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();

                DateItem dateItem = mDateItemList.get(position);
                mFragment.generateCharts(position + 1);
                Toast.makeText(v.getContext(), "You clicked view " + dateItem.getNumber() + " : " + dateItem.getDateName(), Toast.LENGTH_SHORT).show();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DateItem dateItem = mDateItemList.get(position);
        holder.dateNameView.setText(dateItem.getDateName());

        if(position==mInitDateNumber-1){
            holder.dateUnderLineView.setVisibility(View.VISIBLE);
        }
        viewHolderMap.put(position, holder);
    }

    @Override
    public int getItemCount() {
        return mDateItemList.size();
    }

}
