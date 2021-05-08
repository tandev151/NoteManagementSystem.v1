package com.example.notemanagement.ui.category;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notemanagement.Entity.Category;
import com.example.notemanagement.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.example.notemanagement.Utils.CONSTANT.DELETE_CODE;
import static com.example.notemanagement.Utils.CONSTANT.UPDATE_CODE;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Category> categoryList;
    private Context context;

    public CategoryAdapter(List<Category> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
        notifyDataSetChanged();
    }
    public  void setAdapter(List<Category> listCategory){
        this.categoryList =listCategory;
    }
    public CategoryAdapter(Context context){
        this.context=context;
    }
    public List<Category> getList(){ return categoryList;}

    /*
   class holds structure of view
    */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView tvName;
        public TextView tvCreateDate;
        public Context context;
        public List<Category> categoryList;

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(),UPDATE_CODE,1,"Edit");
            menu.add(getAdapterPosition(),DELETE_CODE,1,"Delete");
        }

        public ViewHolder(View view, List<Category> listCategory, Context context) {
            super(view);

            tvName = (TextView) view.findViewById(R.id.tvName);
            tvCreateDate = (TextView) view.findViewById(R.id.tvCreateDate);
            this.context = context;
            this.categoryList = listCategory;

            view.setOnCreateContextMenuListener(this);
        }
    }

    /*
    Class is used to set layout for view holder
     */
    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View statusView =
                inflater.inflate(R.layout.status_item, parent, false);

        CategoryAdapter.ViewHolder viewHolder = new ViewHolder(statusView, categoryList, context);
        return viewHolder;
    }
    /*
    class is used to assign data for view holder
     */
    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, int position) {
        Category category = categoryList.get(position);

        holder.tvName.setText(category.getName());

        //get date and format it
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.tvCreateDate.setText(dateFormat.format(category.getCreateDate()));
    }
    @Override
    public int getItemCount() {
        if(categoryList !=null)
            return categoryList.size();
        return 0;
    }
}
