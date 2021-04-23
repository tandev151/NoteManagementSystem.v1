package com.example.notemanagement.ui.category;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notemanagement.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Category> listCategory;
    public CategoryAdapter(List listCategory){
        this.listCategory = listCategory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext())
               .inflate(R.layout.item_category,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
       holder.getNameCategory().setText(listCategory.get(position).getName());
       holder.getCreateDate().setText(listCategory.get(position).getCreateDate().toString());
    }

    @Override
    public int getItemCount() {
        return listCategory.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameCategory;
        private final TextView createDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameCategory = (TextView)itemView.findViewById(R.id.textViewNameCategory);
            createDate = (TextView)itemView.findViewById(R.id.textViewCreateDate);
        }

        public TextView getNameCategory() {
            return nameCategory;
        }

        public TextView getCreateDate() {
            return createDate;
        }
    }
}
