package com.minosai.skindoc.user.utils.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.minosai.skindoc.auth.data.TokenString;
import com.minosai.skindoc.chat.ChatActivity;
import com.minosai.skindoc.user.data.ApDetail;
import com.minosai.skindoc.user.data.api.ResolveBody;
import com.minosai.skindoc.user.utils.UserDataStore;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        holder.txtDocName.setText("Dr. "+currentApDetail.getDoctorName());
        holder.txtDocQual.setText(currentApDetail.getQualification());
        holder.txtDocDesc.setText(currentApDetail.getDescription());

        holder.chatImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String left = UserDataStore.getInstance().getUser(context).getUser();
                String right = apDetails.get(position).getDoctor();

                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra(ChatActivity.USER_NODE, left+"-"+right);
                intent.putExtra(ChatActivity.RECIEVER, "Dr. "+currentApDetail.getDoctorName());
                context.startActivity(intent);
            }
        });

        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                cancelAppointment(position);
                final AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(context);
                builder.setTitle("Cancel appointment")
                        .setMessage("Are you sure you want to cancel this appointment?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                cancelAppointment(position);
                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    private void cancelAppointment(final int position) {
        String user = UserDataStore.getInstance().getUser(context).getUser();
        String doctor = apDetails.get(position).getDoctor();
        String token = UserDataStore.getInstance().getToken(context);

        Log.i("API-CANCELAPPT-DATA", "user: "+user+" doc: "+doctor);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<AuthResponse> call = apiInterface.resolveAppointment(new ResolveBody(token, user, doctor));
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.i("API-CANCELAPPT-RESPONSE", response.toString());
                if(response.isSuccessful()) {
                    getNewToken(position);
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

    private void getNewToken(final int position) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<AuthResponse> call = apiInterface.newToken(new TokenString(UserDataStore.getInstance().getToken(context)));
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.i("API-NEWTOKEN-RESPONSE", response.toString());
                if (response.isSuccessful()) {
                    apDetails.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, apDetails.size());
                    AuthResponse authResponse = response.body();
                    UserDataStore.getInstance().saveToken(context, authResponse.getToken());
                } else {
                    Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(context, "error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return apDetails.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtDocName, txtDocQual, txtDocDesc;
        ImageView chatImgBtn;
        Button btnCancel;

        public ViewHolder(View itemView) {
            super(itemView);

            txtDocName = (TextView) itemView.findViewById(R.id.txt_patient_dr_name);
            txtDocQual = (TextView) itemView.findViewById(R.id.txt_patient_doc_qual);
            txtDocDesc = (TextView) itemView.findViewById(R.id.txt_patient_doc_desc);
            chatImgBtn = (ImageView) itemView.findViewById(R.id.btn_patient_chat);
            btnCancel = (Button) itemView.findViewById(R.id.btn_patient_cancel);
        }
    }
}
