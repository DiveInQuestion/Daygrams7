package com.example.a19360.daygrams7;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class YearAdapter extends RecyclerView.Adapter<YearAdapter.ViewHolder>{

    private List<String> yearNames;
    private int selected_year;
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView yearButton;
        public ViewHolder(View view){
            super(view);
            yearButton = (TextView) view.findViewById(R.id.year_button);
        }
    }
    public YearAdapter(List<String>yearNames,int selected_year){
        this.yearNames = yearNames;
        this.selected_year = selected_year;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if(viewType==0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.year_select, parent, false);
            final ViewHolder holder = new ViewHolder(view);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    String yearName = yearNames.get(position);
                    selected_year = Integer.parseInt(yearName);
                    //Toast.makeText(v.getContext(),""+selected_year,Toast.LENGTH_SHORT).show();
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, yearName);
                    }
                }
            });

            return holder;
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.year_selected, parent, false);
            final ViewHolder holder = new ViewHolder(view);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    String yearName = yearNames.get(position);
                    selected_year = Integer.parseInt(yearName);
                    //Toast.makeText(v.getContext(),""+selected_year,Toast.LENGTH_SHORT).show();
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, yearName);
                    }
                }
            });

            return holder;
        }
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        holder.yearButton.setText(yearNames.get(position));
    }

    public int getItemCount() {
        return yearNames.size();
    }

    public int getItemViewType(int position) {
        if(position==selected_year-2010)
            return 1;
        else
            return 0;
    }

    public interface OnItemClickListener{
        public void onItemClick(View view, String data);
    }
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}