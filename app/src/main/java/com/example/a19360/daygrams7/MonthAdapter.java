package com.example.a19360.daygrams7;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.ViewHolder>{
    private static String[] monthNames={
            "JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"
    };
    private int selected_month;
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView month;
        public ViewHolder(View view){
            super(view);
            month = (TextView) view.findViewById(R.id.month_button);
        }
    }
    public MonthAdapter(int selected_month){
        this.selected_month = selected_month-1;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.month_selected, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    selected_month = position;
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });
            return holder;
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.month_select, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    selected_month = position;
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });
            return holder;
        }
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        holder.month.setText(monthNames[position]);
    }

    public int getItemCount() {
        return 12;
    }

    public int getItemViewType(int position) {
        if(position==selected_month)
            return 1;
        else
            return 0;
    }
    /**
     * 点击事件接口
     * */
    public interface OnItemClickListener{
         public void onItemClick(View view, int position);
    }
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}