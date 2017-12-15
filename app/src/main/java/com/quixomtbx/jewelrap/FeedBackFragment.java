package com.quixomtbx.jewelrap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.quixomtbx.jewelrap.Adapter.NavigationDrawerAdapter;
import com.quixomtbx.jewelrap.Global.Global_variable;
import com.quixomtbx.jewelrap.Global.SessionManager;
import com.quixomtbx.jewelrap.Utils.RestClientVolley;
import com.quixomtbx.jewelrap.Utils.VolleyCallBack;

import org.json.JSONException;
import org.json.JSONObject;

public class FeedBackFragment extends Fragment {

    EditText etFeddback;
    Button btnSubmit;
    SharedPreferences sharedPreferences;
    String name, token, id, userrole;
    String url = Global_variable.feedback_api;

    public FeedBackFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feed_back, container, false);
        sharedPreferences = getActivity().getSharedPreferences(SessionManager.MyPREFERENCES, Context.MODE_PRIVATE);
        name = sharedPreferences.getString(SessionManager.Name, null);
        token = sharedPreferences.getString(SessionManager.TOKEN, null);
        id = sharedPreferences.getString(SessionManager.Id, null);
        userrole = sharedPreferences.getString(SessionManager.USERROLE, null);
        etFeddback = (EditText) view.findViewById(R.id.edit_text_feedback);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmitFeedback);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RestClientVolley restClientVolley = new RestClientVolley(getActivity(), Request.Method.POST, url, new VolleyCallBack() {
                    @Override
                    public void processCompleted(String response) {
                        feedbackPosted(response);
                    }
                });

                restClientVolley.addParameter("user_id", id);
                restClientVolley.addParameter("feedback_msg", etFeddback.getText().toString());
                restClientVolley.executeRequest();
            }
        });
        return view;
    }

    private void feedbackPosted(String response) {
        try {
            String status, message;
            JSONObject jsonObject = new JSONObject(response);
            status = jsonObject.getString("status");
            message = jsonObject.getString("message");
            if (status.equals("ok")) {
                Intent in = new Intent(getActivity(), MainActivity.class);
                startActivity(in);
                NavigationDrawerAdapter.selected_item = 0;
                MainActivity.id_pos = 0;
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
