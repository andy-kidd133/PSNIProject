package psni.example.psniproject.MainApp.Messages;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import psni.example.psniproject.LoginScreen.Models.OfficerProfile;
import psni.example.psniproject.LoginScreen.Models.UserType;
import psni.example.psniproject.LoginScreen.Models.VictimProfile;
import psni.example.psniproject.MainApp.MainActivity;
import psni.example.psniproject.R;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<OfficerProfile> officerProfiles;
    private ArrayList<VictimProfile> victimProfiles;

    public UserAdapter(Context context, List<OfficerProfile> officerProfiles, ArrayList<VictimProfile> victimProfiles) {
        this.context = context;
        this.officerProfiles = officerProfiles;
        this.victimProfiles = victimProfiles;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(MainActivity.userType == UserType.VICTIM) {
            final OfficerProfile officerProfile = officerProfiles.get(position);
            holder.username.setText(officerProfile.getmFirstName() + " " + officerProfile.getmSurname());
            holder.profileImage.setImageResource(R.drawable.psni_badge);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MessageActivity.class);
                    intent.putExtra("userid", officerProfile.getuID());
                    context.startActivity(intent);
                }
            });
        }

        else if(MainActivity.userType == UserType.OFFICER) {
            final VictimProfile victimProfile = victimProfiles.get(position);
            holder.username.setText(victimProfile.getfName() + " " + victimProfile.getsName());
            holder.profileImage.setImageResource(R.mipmap.ic_launcher);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MessageActivity.class);
                    intent.putExtra("userid", victimProfile.getuID());
                    context.startActivity(intent);
                }
            });
        }


    }

    @Override
    public int getItemCount() {

        if(MainActivity.userType == UserType.OFFICER) {
            return victimProfiles.size();
        }
        else if (MainActivity.userType == UserType.VICTIM) {
            return officerProfiles.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profileImage;

        public ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profileImage = itemView.findViewById(R.id.profileImage);
        }
    }
}
