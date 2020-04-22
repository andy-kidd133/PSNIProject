package psni.example.psniproject.MainApp;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import psni.example.psniproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUs extends Fragment {

    private View view;

    public ContactUs() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contact_us, container, false);

        WebView webView = view.findViewById(R.id.webView3);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("https://www.victimsupportni.com/about-us/contact-us/");
        webView.setWebViewClient(new WebViewClient());

        return view;
    }

}
