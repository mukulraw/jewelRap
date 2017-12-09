package com.quixom.jewelrap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.quixom.jewelrap.Adapter.InventoryAdapter;
import com.quixom.jewelrap.Global.Global_variable;
import com.quixom.jewelrap.Global.SessionManager;
import com.quixom.jewelrap.Model.Inventory;
import com.quixom.jewelrap.Utils.RestClientVolley;
import com.quixom.jewelrap.Utils.VolleyCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class InvetoryFragment extends Fragment {

    String invetoryURL = Global_variable.invetory_api;
    String version = BuildConfig.VERSION_NAME;
    SharedPreferences sharedPreferences;
    String userName, token, nextURL, stockID;
    String TAG = this.getClass().getSimpleName();
    RecyclerView recyclerViewStock;
    RecyclerView.Adapter invetoryAdapter;
    ArrayList<Inventory> inventoryArrayList;
    LinearLayoutManager layoutManager;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invetory, container, false);
        recyclerViewStock = (RecyclerView) view.findViewById(R.id.recyclerViewStock);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewStock.setLayoutManager(layoutManager);
        inventoryArrayList = new ArrayList<>();
        invetoryAdapter = new InventoryAdapter(getActivity(), inventoryArrayList);
        recyclerViewStock.setAdapter(invetoryAdapter);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharedPreferences = context.getSharedPreferences(SessionManager.MyPREFERENCES, context.MODE_PRIVATE);
        userName = sharedPreferences.getString(SessionManager.Name, null);
        token = sharedPreferences.getString(SessionManager.TOKEN, null);

        RestClientVolley restClientVolley = new RestClientVolley(context, Request.Method.GET, invetoryURL, new VolleyCallBack() {
            @Override
            public void processCompleted(String response) {
                parseJsonData(response);
            }
        });
        restClientVolley.addHeader("Authorization", "ApiKey " + userName + ":" + token);
        restClientVolley.addHeader("X-Android", version);
        restClientVolley.executeRequest();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    public void parseJsonData(String response) {
        try {
            final JSONObject jsonObject = new JSONObject(response);
            nextURL = jsonObject.getJSONObject("meta").getString("next");
            JSONArray jsonArray = jsonObject.getJSONArray("objects");
            for (int i = 0; i < jsonArray.length(); i++) {
                Inventory inventory = new Inventory();
                JSONObject stockObject = jsonArray.getJSONObject(i);
                inventory.setStockObject(stockObject);
                inventory.setStockInfo(stockObject.getString("shape") + " " + stockObject.getString("size") + ", " + stockObject.getString("color") + ", " + stockObject
                        .getString("purity") + ", " + stockObject.getString("lab"));
                inventory.setCut(stockObject.getString("cut_grade"));
                inventory.setFluor(stockObject.getString("fluorescence"));
                inventory.setPolish(stockObject.getString("polish"));
                inventory.setMeas(stockObject.getString("measurement"));
                inventory.setSymmetry(stockObject.getString("symmetry"));
                inventory.setCertNumber(stockObject.getString("certificate_num"));
                inventory.setId(stockObject.getString("id"));
                if (stockObject.getJSONArray("images").length() > 0) {
                    for (int j = 0; j < stockObject.getJSONArray("images").length(); j++) {
                        inventory.setImage(stockObject.getJSONArray("images").get(j).toString());
                    }
                }
                inventoryArrayList.add(inventory);
            }
            if (jsonObject.getJSONObject("meta").getBoolean("is_available") == false) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity(), R.style.DesignDemo);
                dlgAlert.setMessage(jsonObject.getJSONObject("meta").getString("disable_message"));
                dlgAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                getActivity().overridePendingTransition(R.layout.push_right_out, R.layout.push_right_in);
                            }
                        });
                dlgAlert.setCancelable(false);
                dlgAlert.create().show();
            } else {
                invetoryAdapter.notifyDataSetChanged();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
