package psni.example.psniproject.MainApp.Messages;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import psni.example.psniproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private View view;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chats, container, false);

        return view;
    }

}
