package com.example.jeffr.capstone_stage2.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.example.jeffr.capstone_stage2.R;
import com.example.jeffr.capstone_stage2.data.User;
import com.example.jeffr.capstone_stage2.databinding.FragmentHomePageBinding;
import com.squareup.picasso.Picasso;
import timber.log.Timber;

public class HomePageFragment extends Fragment {
  Fragment restaurantListFragment;
  ImageButton menuButton;

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
    setHasOptionsMenu(true);
    View rootView = binding.getRoot();
    menuButton = rootView.findViewById(R.id.expanded_menu_button);
    menuButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {

      }
    });
    binding.setUser((User) getArguments().getSerializable("User"));
    return rootView;
  }

}
