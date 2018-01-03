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

import java.util.List;

/**
 * Created by minos.ai on 30/12/17.
 */

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    private List<String> pList;
    Context context;

    public DoctorAdapter(List<String> pList, Context context) {
        this.pList = pList;
        this.context = context;
    }

    @Override
    public DoctorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String currentPDetail = pList.get(position);
        holder.txtPatientName.setText(currentPDetail);

        holder.chatImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String right = UserDataStore.getInstance().getUser(context).getUser();
                String left = pList.get(position);

                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra(ChatActivity.USER_NODE, left+"-"+right);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtDate, txtPatientName;
        ImageView chatImgBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            txtDate = (TextView) itemView.findViewById(R.id.txt_doctor_date);
            txtPatientName = (TextView) itemView.findViewById(R.id.txt_doctor_patient_name);

            chatImgBtn = (ImageView) itemView.findViewById(R.id.img_btn_doctor_chat);
        }
    }
}
