package com.quixomtbx.jewelrap;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.quixomtbx.jewelrap.Global.Global_variable;

public class AboutUsFragment extends Fragment {

    TextView textViewfirst, textViewsecond, textViewlast;
    WebView webView;

    public AboutUsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        webView = (WebView) view.findViewById(R.id.activity_main_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(Global_variable.base_url + "/about/");
     /*   textViewfirst=(TextView)view.findViewById(R.id.first_textcard);
        textViewsecond=(TextView)view.findViewById(R.id.second_textcard);
        textViewlast=(TextView)view.findViewById(R.id.last_textcard);
        String string_second="To help cater to Retailers demand we have with us a huge base of reputed wholesalers servicing" +
                " to our main focused categories:";
        String string_first="JewelRap stands out as a Shining Star in this online world of E - Commerce , Digitalisation & " +
                "Mobile retail space which has at the heart of its ecosystem - the Empowerment of Retailers and Wholesalers." +
                "\n"+"\n" +"JewelRap offers a Unique & Simplified Model of transacting jewellery business online to all our " +
                "registered Retailers and Wholesalers enjoying unmatched services, enhancing their buying and selling " +
                "experience."+"\n";
        String string_last="JewelRap is conscious to the huge competition prevailing in the jewellery market whereby one can " +
                "not afford to lose on the spot customer or for that matter any customer\n" +
                "\n" +
                "JewelRap is an Online Portal bridging the Demand-Supply gap between retailers and wholesalers where retailers " +
                "can raise their regular & urgent demand on this Portal to fulfil their on the spot customer requirements, " +
                "to be " +"met through genuine and reliable wholesaler member base of this Portal through this App.";
        textViewfirst.setText(string_first);
        textViewsecond.setText(string_second);
        textViewlast.setText(string_last);*/
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
