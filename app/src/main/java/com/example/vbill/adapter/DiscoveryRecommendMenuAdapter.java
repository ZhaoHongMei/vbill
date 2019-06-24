package com.example.vbill.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.vbill.R;
import com.example.vbill.bean.discovery.recommend.RecommendMenu;

import java.util.List;

import butterknife.ButterKnife;

public class DiscoveryRecommendMenuAdapter extends RecyclerView.Adapter<DiscoveryRecommendMenuAdapter.ViewHolder> {

    private List<RecommendMenu> recommendMenuList;
    private Context mContext;

    public DiscoveryRecommendMenuAdapter(Context context, List<RecommendMenu> recommendMenus){
        recommendMenuList = recommendMenus;
        mContext = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView recommendMenuImg;
        TextView recommendMenuDescription1;
        TextView recommendMenuDescription2;
        View recommendMenuView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recommendMenuView = itemView;
            recommendMenuImg = (ImageView) itemView.findViewById(R.id.discovery_recommend_menu_img);
            recommendMenuDescription1 = (TextView)itemView.findViewById(R.id.discovery_recommend_menu_description1);
            recommendMenuDescription2 = (TextView)itemView.findViewById(R.id.discovery_recommend_menu_description2);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.discovery_recommend_menu, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.recommendMenuView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                RecommendMenu recommendMenu = recommendMenuList.get(position);
                    Log.d("Click View:" , "111");
                Intent intent = new Intent("android.intent.action.DiscoveryRecommend");
                intent.putExtra("recommendCId", recommendMenu.getCid());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        RecommendMenu recommendMenu = recommendMenuList.get(i);
        String imgMenuPath = recommendMenu.getImagePath();
        Glide.with(mContext).load(imgMenuPath).override(500,300).into(holder.recommendMenuImg);
        holder.recommendMenuDescription1.setText(recommendMenu.getDescription1());
        holder.recommendMenuDescription2.setText(recommendMenu.getDescription2());

    }

    @Override
    public int getItemCount() {
        return recommendMenuList.size();
    }
}
