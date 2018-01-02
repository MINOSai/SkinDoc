package com.minosai.skindoc.user.utils.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.minosai.skindoc.R;
import com.minosai.skindoc.user.data.ApDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by minos.ai on 30/12/17.
 */

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> {

    private List<ApDetail> apDetails;

    public PatientAdapter(List<ApDetail> apDetails) {
        this.apDetails = apDetails;
    }

    @Override
    public PatientAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PatientAdapter.ViewHolder holder, int position) {
        final ApDetail currentApDetail = apDetails.get(position);
        holder.txtDocName.setText(currentApDetail.getDoctor());
    }

    @Override
    public int getItemCount() {
        return apDetails.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate, txtDocName;

        public ViewHolder(View itemView) {
            super(itemView);

            txtDate = (TextView) itemView.findViewById(R.id.txt_patient_date);
            txtDocName = (TextView) itemView.findViewById(R.id.txt_patient_doctor_name);
        }
    }
}
