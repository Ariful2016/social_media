package com.cit.social_media_practice.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cit.social_media_practice.fragments.ChatFragment;
import com.cit.social_media_practice.fragments.HomeFragment;
import com.cit.social_media_practice.fragments.NotificationFragment;
import com.cit.social_media_practice.fragments.UserFragment;

public class FragmentAdapter extends FragmentPagerAdapter {

    String[] allTab = {"Home","Users","Chats","Notifications"};

    public FragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();

            case 1:
                return new UserFragment();

            case 2:
                return new ChatFragment();

            case 3:
                return new NotificationFragment();

        }
        return null;
    }

    @Override
    public int getCount() {
        return allTab.length;
    }

   /* @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return allTab[position];
    }*/
}
