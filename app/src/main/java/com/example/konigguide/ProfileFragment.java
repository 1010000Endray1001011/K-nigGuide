package com.example.konigguide;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    String Username = "KonigUser";
    FirebaseDatabase FD = FirebaseDatabase.getInstance("https://konigguide-f057f-default-rtdb.europe-west1.firebasedatabase.app/");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View rootView  = inflater.inflate(R.layout.fragment_profile, container, false);

        DatabaseReference usernameReference = FD.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        usernameReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Username = snapshot.child("username").getValue(String.class);
                TextView LogUs = rootView.findViewById(R.id.UserLogin);
                LogUs.setText(Username);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("FirebaseData", "loadData:onCancelled", error.toException());
            }
        });
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