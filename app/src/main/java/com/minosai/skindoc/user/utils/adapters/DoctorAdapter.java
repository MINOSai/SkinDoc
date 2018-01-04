package com.minosai.skindoc.user.utils.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.minosai.skindoc.R;
import com.minosai.skindoc.api.ApiClient;
import com.minosai.skindoc.api.ApiInterface;
import com.minosai.skindoc.auth.data.AuthResponse;
import com.minosai.skindoc.chat.ChatActivity;
import com.minosai.skindoc.user.data.ApDetail;
import com.minosai.skindoc.user.data.Plist;
import com.minosai.skindoc.user.data.api.ResolveBody;
import com.minosai.skindoc.user.utils.UserDataStore;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by minos.ai on 30/12/17.
 */

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    private List<Plist> pList;
    Context context;

    public DoctorAdapter(List<Plist> pList, Context context) {
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
        Plist currentPDetail = pList.get(position);
        holder.txtPatientName.setText(currentPDetail.getUserName());
        holder.txtDescription.setText(currentPDetail.getDescription());

        holder.chatImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String right = UserDataStore.getInstance().getUser(context).getUser();
                String left = pList.get(position).getUser();

                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra(ChatActivity.USER_NODE, left+"-"+right);
                context.startActivity(intent);
            }
        });

        holder.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apptDone(position);
            }
        });
    }

    private void apptDone(final int position) {
        String doctor = UserDataStore.getInstance().getUser(context).getUser();
        String user = pList.get(position).getUser();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<AuthResponse> call = apiInterface.resolveAppointment(new ResolveBody(user, doctor));
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if(response.isSuccessful()) {
                    pList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, pList.size());
                } else {
                    Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return pList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtPatientName, txtDescription;
        ImageView chatImgBtn;
        Button btnDone;

        public ViewHolder(View itemView) {
            super(itemView);

            txtPatientName = (TextView) itemView.findViewById(R.id.txt_doctor_patient_name);
            txtDescription = (TextView) itemView.findViewById(R.id.txt_doctor_info);
            chatImgBtn = (ImageView) itemView.findViewById(R.id.img_btn_doctor_chat);
            btnDone = (Button) itemView.findViewById(R.id.btn_doctor_done);
        }
    }
}
