package com.demus.mayor.adapters;

import com.demus.mayor.R;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
 
/**
 * Created by hp1 on 28-12-2014.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {
     
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
 
    private String mNavTitles[];
    private int mIcons[];
     
    private String name, walletId;
 
    public static class ViewHolder extends RecyclerView.ViewHolder {
        int holderID;      
         
        TextView textView; 
        ImageView imageView;
        TextView lblName, lblWalletId;
 
        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            
            if(ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
                holderID = 1;
            }
            else if(ViewType == TYPE_HEADER) {
            	lblName = (TextView) itemView.findViewById(R.id.name);
                lblWalletId = (TextView) itemView.findViewById(R.id.walletId);
                holderID = 0;
            }
            else {
            	lblName = (TextView) itemView.findViewById(R.id.name);
                lblWalletId = (TextView) itemView.findViewById(R.id.walletId);
                holderID = 2;
            }
        }
    }
 
    public DrawerAdapter(String titles[], int icons[], String name, String walletId) {
        mNavTitles = titles;
        mIcons = icons;
        this.name = name;
        this.walletId = walletId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_item_row, parent, false);
 
            ViewHolder vhItem = new ViewHolder(v,viewType);
 
            return vhItem;
        }
        else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header, parent, false);
 
            ViewHolder vhHeader = new ViewHolder(v, viewType);
 
            return vhHeader;
        }
        else if (viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_footer, parent, false);
 
            ViewHolder vhFooter = new ViewHolder(v, viewType);
 
            return vhFooter;
        }
        
        return null;
    }
 
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(holder.holderID == 1) {
            holder.textView.setText(mNavTitles[position - 1]);
            holder.imageView.setImageResource(mIcons[position -1]);
        }
        else if(holder.holderID == 0) {
            holder.lblName.setText(name);
            holder.lblWalletId.setText(walletId);
        }
    }
 
    @Override
    public int getItemCount() {
        return mNavTitles.length + 2;
    }
    
    @Override
    public int getItemViewType(int position) { 
        if (isPositionHeader(position))
            return TYPE_HEADER;
        if (isPositionFooter(position))
            return TYPE_FOOTER;
 
        return TYPE_ITEM;
    }
 
    private boolean isPositionHeader(int position) {
        return position == 0;
    }
    
    private boolean isPositionFooter(int position) {
        return position == mNavTitles.length + 1;
    }
}
