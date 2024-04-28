package com.example.konigguide;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProfileFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View rootView  = inflater.inflate(R.layout.fragment_profile, container, false);

         Dialog dialog = new Dialog(getContext());

         Intent p = new Intent(getActivity(),ProfileSettings.class);
         Intent s = new Intent(getActivity(),Settings.class);
         Intent r = new Intent(getActivity(),Rating.class);

         View profileButton = rootView.findViewById(R.id.backLineProfile);
         View settingsButton = rootView.findViewById(R.id.backLineSettings);
         View ratingButton = rootView.findViewById(R.id.backLineRating);
         View feedbackButton = rootView.findViewById(R.id.backLineFeedback);

                 profileButton.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) {startActivity(p);}});
                 settingsButton.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) {startActivity(s);}});
                 ratingButton.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) {startActivity(r);}});
                 feedbackButton.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         dialog.setContentView(R.layout.feedback_dialog);
                         dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                         dialog.show();
                     }
                 });



         return rootView ;
    }


}