package com.minosai.skindoc.user.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.minosai.skindoc.R;
import com.minosai.skindoc.user.data.ApDetail;
import com.minosai.skindoc.user.utils.UserDataStore;
import com.minosai.skindoc.user.utils.adapters.DoctorAdapter;
import com.minosai.skindoc.user.utils.adapters.PatientAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private FragmentActivity listener;

    List<ApDetail> apDetails;

    RecyclerView recyclerView;
    DoctorAdapter doctorAdapter;
    PatientAdapter patientAdapter;

    TextView txtEmpty;

    public MainActivityFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.listener = (FragmentActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        txtEmpty = (TextView) view.findViewById(R.id.txt_ap_empty);

        recyclerView = (RecyclerView) view.findViewById(R.id.user_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        new Thread(new Runnable() {
            @Override
            public void run() {
                apDetails = UserDataStore.getInstance().getUser(getContext()).getApDetails();
                if(apDetails.isEmpty()) {
                    txtEmpty.setVisibility(View.VISIBLE);
                } else {
                    txtEmpty.setVisibility(View.GONE);
                    if(UserDataStore.getInstance().getUser(getContext()).getPortal().equals("0")) {
                        doctorAdapter = new DoctorAdapter(apDetails);
                        recyclerView.setAdapter(doctorAdapter);
                    } else {
                        patientAdapter = new PatientAdapter(apDetails);
                        recyclerView.setAdapter(patientAdapter);
                    }
                }
            }
        }).start();

        return view;
    }
}
