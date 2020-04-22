package psni.example.psniproject.MainApp.Messages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import psni.example.psniproject.LoginScreen.Models.OfficerProfile;
import psni.example.psniproject.LoginScreen.Models.UserType;
import psni.example.psniproject.LoginScreen.Models.VictimProfile;
import psni.example.psniproject.MainApp.MainActivity;
import psni.example.psniproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profileImage;
    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Intent intent;

    ImageButton sendButton;
    EditText textSend;

    MessageAdapter messageAdapter;
    List<Chat> chats;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.messagesToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profileImage = findViewById(R.id.profileImage);
        username = findViewById(R.id.username);
        sendButton = findViewById(R.id.buttonSend);
        textSend = findViewById(R.id.textSend);

        intent = getIntent();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //userID coming through the intent from whichever user is clicked on in the "USERS" tab
        final String userid = intent.getStringExtra("userid");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = textSend.getText().toString();
                if (!message.equals("")) {
                    sendMessage(firebaseUser.getUid(), userid, message);
                }
                else {
                    Toast.makeText(MessageActivity.this, "You can't send empty messages..", Toast.LENGTH_SHORT).show();
                }
                textSend.setText("");
            }
        });


        if (MainActivity.userType == UserType.VICTIM) {

            //IF VICTIM = GO TO OFFICER REFERENCE TO GET THE LINKING PROFILE/

            reference = FirebaseDatabase.getInstance().getReference("officers/" ).child(userid);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    OfficerProfile officerProfile = dataSnapshot.getValue(OfficerProfile.class);
                    username.setText(officerProfile.getmFirstName() + " " + officerProfile.getmSurname());
                    profileImage.setImageResource(R.drawable.psni_badge);

                    readMessages(firebaseUser.getUid(), userid);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        else if (MainActivity.userType == UserType.OFFICER) {

            //IF OFFICER = GO TO VICTIM REFERENCE TO GET THE LINKING PROFILE/

            reference = FirebaseDatabase.getInstance().getReference("victims").child(userid);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    VictimProfile victimProfile = dataSnapshot.getValue(VictimProfile.class);
                    username.setText(victimProfile.getfName() + " " + victimProfile.getsName());
                    profileImage.setImageResource(R.drawable.user_icon);

                    readMessages(firebaseUser.getUid(), userid);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void sendMessage(String sender, String receiver, String message) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);


        reference.child("chats").push().setValue(hashMap);

    }


    private void readMessages (final String myid, final String userid) {

        chats = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);

                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {

                        chats.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, chats);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
