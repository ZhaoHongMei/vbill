package com.example.vbill.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vbill.R;
import com.example.vbill.bean.ParentBill;

import java.util.List;

public class ParentBillRecyclerAdapter extends RecyclerView.Adapter<ParentBillRecyclerAdapter.ViewHolder>{
    private Context mContext;
    private List mParentList;

    public ParentBillRecyclerAdapter(Context context, List parentList){
        this.mContext = context;
        this.mParentList = parentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_bill,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ParentBill parentBill =(ParentBill)mParentList.get(i);
        viewHolder.time.setText(parentBill.getDate());
        if (viewHolder.recyclerView.getAdapter() == null) {
            viewHolder.recyclerView.setAdapter(new ChildBillRecyclerAdapter(mContext, parentBill.getBillList()));
        } else {
            viewHolder.recyclerView.getAdapter().notifyDataSetChanged();
        }
    }


    @Override
    public int getItemCount() {
        return mParentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView time;
        RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            recyclerView = (RecyclerView)itemView.findViewById(R.id.child_bill);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(itemView.getContext());
            recyclerView.setLayoutManager(manager);
        }
    }
}
