package com.minosai.skindoc.user.utils.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.minosai.skindoc.R;
import com.minosai.skindoc.chat.ChatActivity;
import com.minosai.skindoc.user.data.ApDetail;
import com.minosai.skindoc.user.utils.UserDataStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by minos.ai on 30/12/17.
 */

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> {

    private List<ApDetail> apDetails;
    private Context context;

    public PatientAdapter(List<ApDetail> apDetails, Context context) {
        this.apDetails = apDetails;
        this.context = context;
    }

    @Override
    public PatientAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PatientAdapter.ViewHolder holder, final int position) {
        final ApDetail currentApDetail = apDetails.get(position);
        holder.txtDocName.setText(currentApDetail.getDoctor());

        holder.chatImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String left = UserDataStore.getInstance().getUser(context).getUser();
                String right = apDetails.get(position).getDoctor();

                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra(ChatActivity.USER_NODE, left+"-"+right);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return apDetails.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate, txtDocName;
        ImageView chatImgBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            txtDate = (TextView) itemView.findViewById(R.id.txt_patient_date);
            txtDocName = (TextView) itemView.findViewById(R.id.txt_patient_doctor_name);

            chatImgBtn = (ImageView) itemView.findViewById(R.id.img_btn_patient_chat);
        }
    }
}
