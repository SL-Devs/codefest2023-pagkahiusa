package com.example.codefest_cdo.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.codefest_cdo.R;
import com.example.codefest_cdo.databinding.ActivityAdminHomeBinding;
import com.example.codefest_cdo.databinding.ActivityHomeBinding;
import com.example.codefest_cdo.home.HomeFragment;
import com.example.codefest_cdo.post.PostFragment;
import com.example.codefest_cdo.rewards.RewardsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminHome extends AppCompatActivity {


    ActivityAdminHomeBinding binding;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new AdminHomeFragment());

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    replaceFragment(new AdminHomeFragment());
                    break;
                case R.id.rewardClaims:
                    replaceFragment(new PostFragment());
                    break;
                case R.id.rewards:
                    replaceFragment(new AdminRewardPane());
                    break;
//                case R.id.profile:
//                    replaceFragment(new ProfileFragment());
//                    break;

            }

            return true;
        });

    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();


    }
}