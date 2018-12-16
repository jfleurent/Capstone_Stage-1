package com.example.jeffr.capstone_stage2.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.jeffr.capstone_stage2.R;
import com.example.jeffr.capstone_stage2.data.User;
import com.example.jeffr.capstone_stage2.databinding.FragmentHomePageBinding;
import com.squareup.picasso.Picasso;

public class HomePageFragment extends Fragment {
  Fragment restaurantListFragment;

  public HomePageFragment() {

  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    FragmentHomePageBinding binding = DataBindingUtil.inflate(
        inflater, R.layout.fragment_home_page, container, false);
    View rootView = binding.getRoot();
    binding.setUser((User) getArguments().getSerializable("User"));
    return rootView;
  }

}
