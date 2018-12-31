package com.example.jeffr.capstone_stage2.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.jeffr.capstone_stage2.R;

public abstract class MyBaseAdapter extends RecyclerView.Adapter<MyBaseAdapter.MyViewHolder> {

  private RecyclerViewOnClick recyclerViewOnClick;

  public MyViewHolder onCreateViewHolder(ViewGroup parent,
      int viewType) {
    LayoutInflater layoutInflater =
        LayoutInflater.from(parent.getContext());
    ViewDataBinding binding = DataBindingUtil.inflate(
        layoutInflater, viewType, parent, false);
    return new MyViewHolder(binding);
  }

  public void onBindViewHolder(MyViewHolder holder,
      int position) {
    final Object obj = getObjForPosition(position);
    holder.bind(obj);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        recyclerViewOnClick.rowSelected(obj);
      }
    });
    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override public boolean onLongClick(View v) {
        v.setBackgroundColor(Color.parseColor("#6bdfd3"));
        recyclerViewOnClick.deleteSelectedRow(obj);
        return true;
      }
    });
  }
  @Override
  public int getItemViewType(int position) {
    return getLayoutIdForPosition(position);
  }

  protected abstract Object getObjForPosition(int position);

  protected abstract int getLayoutIdForPosition(int position);

  public void setRecyclerViewOnClick(RecyclerViewOnClick recyclerViewOnClick){
    this.recyclerViewOnClick = recyclerViewOnClick;
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    private final ViewDataBinding binding;

    public MyViewHolder(ViewDataBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(Object obj) {
      binding.setVariable(com.example.jeffr.capstone_stage2.BR.obj, obj);
      binding.executePendingBindings();
    }
  }
}
