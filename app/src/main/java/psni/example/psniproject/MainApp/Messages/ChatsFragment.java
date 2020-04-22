package psni.example.psniproject.MainApp.Messages;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import psni.example.psniproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private View view;
    private FirebaseAuth firebaseAuth;
    private TextView signIn;
    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chats, container, false);

        signIn = view.findViewById(R.id.signIn);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            signIn.setVisibility(View.VISIBLE);
            signIn.setText("Please log in to see your messages");
            signIn.setTextSize(20);
        }
        else {
            signIn.setVisibility(View.GONE);
        }


        return view;
    }

}
