package com.minosai.skindoc.user.utils.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.minosai.skindoc.R;
import com.minosai.skindoc.user.data.ApDetail;

import java.util.List;

/**
 * Created by minos.ai on 30/12/17.
 */

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    private List<ApDetail> apDetails;

    public DoctorAdapter(List<ApDetail> apDetails) {
        this.apDetails = apDetails;
    }

    @Override
    public DoctorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ApDetail currentApDetail = apDetails.get(position);
        holder.txtPatientName.setText(currentApDetail.getDoctor());
    }

    @Override
    public int getItemCount() {
        return apDetails.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtDate, txtPatientName;

        public ViewHolder(View itemView) {
            super(itemView);

            txtDate = (TextView) itemView.findViewById(R.id.txt_doctor_date);
            txtPatientName = (TextView) itemView.findViewById(R.id.txt_doctor_patient_name);
        }
    }
}
