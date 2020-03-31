package com.example.eco_drive;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


public class HomeFragment extends Fragment {

    private Button StartARide;
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        StartARide = v.findViewById(R.id.startrideF);
        StartARide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isServiceOk()) {
                    Intent i = new Intent(getActivity(), StartARide.class);
                    startActivity(i);
                   // Toast.makeText(getActivity(), "Welcome...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

    public Boolean isServiceOk(){
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());

        if(available == ConnectionResult.SUCCESS){
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(),available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(getActivity(),"You can't make map request",Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
