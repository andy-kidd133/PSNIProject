package psni.example.psniproject.LoginScreen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import psni.example.psniproject.R;

public class AdminLoginFragment extends Fragment {

    //private Button backButton;
    private Button loginButton;
    private EditText username;
    private EditText password;
    private int counter = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_admin_login, container, false);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.adminDark));


        Button backButton = view.findViewById(R.id.backBtn);
        loginButton = view.findViewById(R.id.loginBtn);
        username = view.findViewById(R.id.etUsername);
        password = view.findViewById(R.id.etPassword);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity)getActivity()).setViewPager(0);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateCredentials(username.getText().toString(), password.getText().toString());
            }
        });

        return view;
    }


    /**
     * method to validate admin login details. If wrong:
     * decrement counter. If counter = 0, disable login button.
     * @param adminUsername
     * @param adminPassword
     */
    private void validateCredentials(String adminUsername, String adminPassword) {
        if((adminUsername.equals("Admin")) && (adminPassword.equals("admin1234"))) {
            ((LoginActivity)getActivity()).setViewPager(2);
            resetEditTexts();
        }else {
            counter --;
            Toast.makeText(getActivity(), "You have: " + counter + " attempts remaining", Toast.LENGTH_SHORT).show();
            if (counter == 0) {
                loginButton.setEnabled(false);
                Toast.makeText(getActivity(), "Too many incorrect attempts. Account Locked.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void resetEditTexts() {
        username.getText().clear();
        password.getText().clear();
    }
}
