package com.example.jeffr.capstone_stage2.adapters;

import com.example.jeffr.capstone_stage2.data.Restaurant;
import java.util.List;

public class SimpleRecyclerViewAdapter<E> extends MyBaseAdapter {

  private final int layoutId;
  private final List<E> items;

  public SimpleRecyclerViewAdapter(List<E> items, int layoutId, RecyclerViewOnClick recyclerViewOnClick) {
    this.items = items;
    this.layoutId =layoutId;
    setRecyclerViewOnClick(recyclerViewOnClick);
  }

  @Override protected Object getObjForPosition(int position) {
    return items.get(position);
  }

  @Override protected int getLayoutIdForPosition(int position) {
    return layoutId;
  }



  @Override public int getItemCount() {
    return items.size();
  }
}
