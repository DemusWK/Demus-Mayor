package com.demus.mayor.home.ui.profile;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demus.mayor.R;
import com.demus.mayor.models.User;
import com.demus.mayor.utils.Utility;
import com.hbb20.GThumb;

public class ProfileFragment extends Fragment {

    private User currentUser;
    private GThumb profileImage;
    private TextView profileName;
    private TextView profileWallet;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        currentUser = Utility.getUserAccount(getContext());
        View root = inflater.inflate(R.layout.profile_fragment, container, false);
        profileImage = root.findViewById(R.id.profile_image);
        profileName = root.findViewById(R.id.profile_username);
        profileWallet = root.findViewById(R.id.profile_wallet_id);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();
    }

    private void initViews() {
        profileImage.loadThumbForName(currentUser.getProfilePictureURL(), currentUser.getFirstName(), currentUser.getLastName());
        String userFullName = currentUser.getFirstName() + " " + currentUser.getLastName();
        profileName.setText(userFullName);
        String userWalletIdInfo = getString(R.string.wallet_id) + currentUser.getPhoneNumber();
        profileWallet.setText(userWalletIdInfo);
    }
}
