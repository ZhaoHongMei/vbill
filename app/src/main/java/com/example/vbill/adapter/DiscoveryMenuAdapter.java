package com.example.vbill.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vbill.R;
import com.example.vbill.bean.DiscoveryMenu;

import java.util.List;

public class DiscoveryMenuAdapter extends RecyclerView.Adapter<DiscoveryMenuAdapter.ViewHolder> {

    private List<DiscoveryMenu> discoveryMenuList;

    public DiscoveryMenuAdapter(List<DiscoveryMenu> discoveryMenus){
        discoveryMenuList = discoveryMenus;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView discoveryMenuImage;
        TextView discoveryMenuName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            discoveryMenuImage = (ImageView) itemView.findViewById(R.id.discovery_menu_image);
            discoveryMenuName = (TextView)itemView.findViewById(R.id.discovery_menu_name);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_home_discovery_menu, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        DiscoveryMenu discoveryMenu = discoveryMenuList.get(i);
        holder.discoveryMenuImage.setImageResource(discoveryMenu.getDiscoveryMenuImage());
        holder.discoveryMenuName.setText(discoveryMenu.getDiscoveryMenuName());

    }

    @Override
    public int getItemCount() {
        return discoveryMenuList.size();
    }
}
