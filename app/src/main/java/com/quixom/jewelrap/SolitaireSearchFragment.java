package com.quixom.jewelrap;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.quixom.jewelrap.Adapter.CertificateAdapter;
import com.quixom.jewelrap.Adapter.ColorAdapter;
import com.quixom.jewelrap.Adapter.PurityAdapter;
import com.quixom.jewelrap.Adapter.SolitaireShapeAdapter;
import com.quixom.jewelrap.Global.Global_variable;
import com.quixom.jewelrap.Global.SessionManager;
import com.quixom.jewelrap.Model.JewelRapItem;
import com.quixom.jewelrap.Utils.RestClientVolley;
import com.quixom.jewelrap.Utils.VolleyCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;


public class SolitaireSearchFragment extends Fragment implements View.OnClickListener,
        SolitaireShapeAdapter.ViewHolder.ClickListener,
        CertificateAdapter.ViewHolder.ClickListener,
        ColorAdapter.ViewHolder.ClickListener,
        PurityAdapter.ViewHolder.ClickListener {


    String TAG = "JEWELRAP", userId;
    int heightShape, heightCertificate, heightColor, heightPurity;

    SolitaireShapeAdapter shapeAdapter;
    CertificateAdapter certificateAdapter;
    ColorAdapter colorAdapter;
    PurityAdapter purityAdapter;

    LinearLayout layoutSizeCarat;
    RecyclerView recyclerViewShape, recyclerViewCertificate, recyclerViewColor, recyclerViewPurity;
    RecyclerView.LayoutManager layoutManagerShape, layoutManagerCertificate, layoutManagerColor;
    TextView textViewShape, textViewSize, textViewCertificate, textViewColor, textViewPurity, textViewSizeCarat;
    Button btnSearch;
    ArrayList<JewelRapItem> listShape = new ArrayList<>();
    ArrayList<JewelRapItem> listCertificate = new ArrayList<>();
    ArrayList<JewelRapItem> listColor = new ArrayList<>();
    ArrayList<JewelRapItem> listPurity = new ArrayList<>();

    List<String> listShapeOrder, listCertificateOrder, listPurityOrder, listColorOrder;
    List<JewelRapItem> listItemOrderShape = new ArrayList<>();
    List<JewelRapItem> listItemOrderColor = new ArrayList<>();
    List<JewelRapItem> listItemOrderCertificate = new ArrayList<>();
    List<JewelRapItem> listItemOrderPurity = new ArrayList<>();
    Context context;
    String name, token, userrole, id, uniqueid;
    SharedPreferences sharedPreferences;
    EditText editTextFromSize, editTextToSize, editTextSearchbyid;
    LinearLayout layoutRetailer, layoutWholesaler;
    Dialog dialog;
    LinearLayout mRevealView;
    boolean hidden = true;
    boolean universalGems;
    String shortCutId;
    String searchid;
    String shape = null, certificate = null, color = null, purity = null, size_from = null, size_to = null;
    MenuItem item;
    Menu menu;
    FirebaseAnalytics firebaseAnalytics;
    int countFragmentVisibility = 1;
    boolean visibleToUser;
    String URL = Global_variable.get_categories_api;
    JSONObject object;
    String categoryName;

    public SolitaireSearchFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_solitaire_search, container, false);
        context = getActivity();
        Glide.get(context).clearMemory();
        mRevealView = (LinearLayout) v.findViewById(R.id.reveal_items);
        mRevealView.setVisibility(View.INVISIBLE);


        layoutWholesaler = (LinearLayout) v.findViewById(R.id.linForWholesaler);
        textViewShape = (TextView) v.findViewById(R.id.tv_title_shape);
        textViewSize = (TextView) v.findViewById(R.id.tv_title_size);
        textViewCertificate = (TextView) v.findViewById(R.id.tv_title_certificate);
        textViewPurity = (TextView) v.findViewById(R.id.tv_title_purity);
        textViewColor = (TextView) v.findViewById(R.id.tv_title_color);
        textViewSizeCarat = (TextView) v.findViewById(R.id.tv_title_size_carat);

        editTextFromSize = (EditText) v.findViewById(R.id.edit_text_size_from);
        editTextToSize = (EditText) v.findViewById(R.id.edit_text_size_to);
        editTextSearchbyid = (EditText) v.findViewById(R.id.edittext_searchbyid);


        layoutSizeCarat = (LinearLayout) v.findViewById(R.id.ll_layout_size_carat);

        recyclerViewShape = (RecyclerView) v.findViewById(R.id.rv_shape);
        recyclerViewShape.setNestedScrollingEnabled(false);
        setLayoutManager(recyclerViewShape, layoutManagerShape, 5);
        ((SimpleItemAnimator) recyclerViewShape.getItemAnimator()).setSupportsChangeAnimations(false);

        recyclerViewCertificate = (RecyclerView) v.findViewById(R.id.rv_certificate);
        recyclerViewCertificate.setNestedScrollingEnabled(false);
        setLayoutManager(recyclerViewCertificate, layoutManagerCertificate, 4);
        ((SimpleItemAnimator) recyclerViewCertificate.getItemAnimator()).setSupportsChangeAnimations(false);

        recyclerViewColor = (RecyclerView) v.findViewById(R.id.rv_color);
        recyclerViewColor.setNestedScrollingEnabled(false);
        setLayoutManager(recyclerViewColor, layoutManagerColor, 6);
        ((SimpleItemAnimator) recyclerViewColor.getItemAnimator()).setSupportsChangeAnimations(false);

        recyclerViewPurity = (RecyclerView) v.findViewById(R.id.rv_purity);
        recyclerViewPurity.setNestedScrollingEnabled(false);
        setLayoutManager(recyclerViewPurity, layoutManagerColor, 6);
        ((SimpleItemAnimator) recyclerViewPurity.getItemAnimator()).setSupportsChangeAnimations(false);

        btnSearch = (Button) v.findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this);



        /*shapeAdapter = new SolitaireShapeAdapter(this, listShape, context, 1);
        certificateAdapter = new CertificateAdapter(this, listCertificate, context);
        colorAdapter = new ColorAdapter(this, listColor, context);
        purityAdapter = new PurityAdapter(this, listPurity, context);*/
        firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());


        editTextSearchbyid.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    openSearchDialog();
                    return true;
                }
                return false;
            }
        });
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        visibleToUser = isVisibleToUser;
        if (visibleToUser && countFragmentVisibility > 1) {
            if (object != null) {
                if (listShape != null) {
                    listShape.clear();
                }
                populateDataJsonObject(object.toString());
            }
        }
        setHasOptionsMenu(true);
    }

    private void openSearchDialog() {
        dialog = new Dialog(context, android.R.style.Theme_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_searchfilter);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        dialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation;
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.BOTTOM;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LinearLayout linearSearch = (LinearLayout) dialog.findViewById(R.id.linearSearch);
        LinearLayout linearFilter = (LinearLayout) dialog.findViewById(R.id.linearFilter);
        LinearLayout linearCancel = (LinearLayout) dialog.findViewById(R.id.linearcancel);
        linearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performSearch();
                dialog.dismiss();
            }
        });

        linearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item = menu.findItem(R.id.itemSearchById);
                mRevealView.setVisibility(View.GONE);
                hidden = true;
                if (editTextSearchbyid.length() > 0) {
                    item.setTitle("id: " + editTextSearchbyid.getText());
                } else {
                    item.setTitle(Html.fromHtml("search by id"));
                }
                dialog.dismiss();

            }

        });
        linearCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideRevealView();
                editTextSearchbyid.setText("");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void populateDataJsonObject(String response) {
        try {
            JSONObject responseObject = new JSONObject(response);
            categoryName = responseObject.getString("category_name");
            populateShapeSize(responseObject, listShape, "shape", "shape_name", "shape_image");
            populateData(responseObject, listCertificate, "certification", "certified_name");
            populateData(responseObject, listColor, "color", "color_name");
            populateData(responseObject, listPurity, "purity", "purity_name");

            //  calculateHeight();
            shapeAdapter = new SolitaireShapeAdapter(this, listShape, context, 1, 14);
            certificateAdapter = new CertificateAdapter(this, listCertificate, context);
            colorAdapter = new ColorAdapter(this, listColor, context);
            purityAdapter = new PurityAdapter(this, listPurity, context);


          /*  setHeight(recyclerViewShape, heightShape);
            setHeight(recyclerViewCertificate, heightCertificate);
            setHeight(recyclerViewColor, heightColor);
            setHeight(recyclerViewPurity, heightPurity);*/

            setAdapter(recyclerViewShape, shapeAdapter);
            setAdapter(recyclerViewCertificate, certificateAdapter);
            setAdapter(recyclerViewColor, colorAdapter);
            setAdapter(recyclerViewPurity, purityAdapter);

            if (universalGems) {
                editTextSearchbyid.setText(shortCutId);
                performSearch();
                universalGems = false;
                editTextSearchbyid.setText(null);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void populateData(JSONObject jsonObject, ArrayList<JewelRapItem> list, String key, String itemName) {
        if (list.size() > 0)
            list.clear();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                JewelRapItem item = new JewelRapItem();
                item.setId(object.getString("id"));
                item.setItemName(object.getString(itemName));
                list.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void populateShapeSize(JSONObject jsonObject, ArrayList<JewelRapItem> list, String key, String name, String image) {
        try {
            JSONArray array = jsonObject.getJSONArray(key);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                JewelRapItem item = new JewelRapItem();
                item.setItemName(object.getString(name));
                item.setCategoryImage(object.getString(image));
                item.setId(object.getString("id"));
                list.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void setLayoutManager(RecyclerView recylcerView, RecyclerView.LayoutManager layoutManager, int gridSize) {
        layoutManager = new GridLayoutManager(getActivity(), gridSize);
        recylcerView.setLayoutManager(layoutManager);
    }

    private void setAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("universalgems")) {
            universalGems = true;
            shortCutId = bundle.getString("shortcut_unique_id", "12345");
        }
        sharedPreferences = context.getSharedPreferences(SessionManager.MyPREFERENCES, Context.MODE_PRIVATE);
        name = sharedPreferences.getString(SessionManager.Name, null);
        id = sharedPreferences.getString(SessionManager.Id, null);
        uniqueid = sharedPreferences.getString(SessionManager.UNIQUEID, null);
        token = sharedPreferences.getString(SessionManager.TOKEN, null);

        if (countFragmentVisibility == 1) {

            if (listShape != null) {
                listShape.clear();
            }
            RestClientVolley restClientVolley = new RestClientVolley(context, Request.Method.GET, URL, new VolleyCallBack() {
                @Override
                public void processCompleted(String response) {
                    layoutWholesaler.setVisibility(View.VISIBLE);
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        JSONArray jsonArray = responseObject.getJSONArray("objects");
                        object = jsonArray.getJSONObject(0);
                        populateDataJsonObject(object.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            restClientVolley.addHeader("Authorization", "ApiKey " + name + ":" + token);
            restClientVolley.executeRequest();
            countFragmentVisibility++;

        } else if ( countFragmentVisibility > 1) {
            if (object != null) {
                if (listShape != null) {
                    listShape.clear();
                }
                populateDataJsonObject(object.toString());
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClickedCertificate(int position) {
        certificateAdapter.toggleSelection(position);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_search) {
            performSearch();
        }

    }

    public void performSearch() {
        String fromSize = editTextFromSize.getText().toString();
        String toSize = editTextToSize.getText().toString();
        hideRevealView();
        searchid = editTextSearchbyid.getText().toString();
        // Toast.makeText(context,"id"+searchid,Toast.LENGTH_LONG).show();

        if ((!shapeAdapter.getSelectedItems().isEmpty() || !certificateAdapter.getSelectedItems().isEmpty() ||
                !purityAdapter.getSelectedItems().isEmpty() || !colorAdapter.getSelectedItems().isEmpty()) || (!fromSize.isEmpty() && !toSize.isEmpty()) || (!searchid.isEmpty()
                && !searchid.equals("null"))) {


            if (!shapeAdapter.getSelectedItems().isEmpty()) {
                if (!listItemOrderShape.isEmpty())
                    listItemOrderShape.clear();
                listItemOrderShape = shapeAdapter.getSelectedItems();
                for (int i = 0; i < listItemOrderShape.size(); i++) {
                    listShapeOrder = convertListToString(listItemOrderShape);
                }
                shape = TextUtils.join(", ", listShapeOrder);

            }

            if (!certificateAdapter.getSelectedItems().isEmpty()) {
                if (!listItemOrderCertificate.isEmpty())
                    listItemOrderCertificate.clear();
                listItemOrderCertificate = certificateAdapter.getSelectedItems();
                for (int i = 0; i < certificateAdapter.getSelectedItems().size(); i++) {
                    listCertificateOrder = convertListToString(listItemOrderCertificate);
                }
                certificate = TextUtils.join(", ", listCertificateOrder);
            }


            if (!colorAdapter.getSelectedItems().isEmpty()) {

                if (!listItemOrderColor.isEmpty())
                    listItemOrderColor.clear();
                listItemOrderColor = colorAdapter.getSelectedItems();
                for (int i = 0; i < colorAdapter.getSelectedItems().size(); i++) {
                    listColorOrder = convertListToString(listItemOrderColor);
                }
                color = TextUtils.join(", ", listColorOrder);

            }

            if (!purityAdapter.getSelectedItems().isEmpty()) {

                if (!listItemOrderPurity.isEmpty())
                    listItemOrderPurity.clear();
                listItemOrderPurity = purityAdapter.getSelectedItems();
                for (int i = 0; i < purityAdapter.getSelectedItems().size(); i++) {
                    listPurityOrder = convertListToString(listItemOrderPurity);

                }
                purity = TextUtils.join(", ", listPurityOrder);

            }

            size_from = editTextFromSize.getText().toString();
            size_to = editTextToSize.getText().toString();
            Glide.get(context).clearMemory();
            Intent in = new Intent(context, SolitaireSearchList.class);
            in.putExtra("shape", shape);
            in.putExtra("certificate", certificate);
            in.putExtra("color", color);
            in.putExtra("purity", purity);
            in.putExtra("size_to", size_to);
            in.putExtra("size_from", size_from);
            in.putExtra("searchid", searchid);
            in.putExtra("category_name", categoryName);
            if (Global_variable.logEnabled) {
                Log.e("** Search idd", searchid);
            }

            startActivity(in);
            getActivity().overridePendingTransition(R.layout.left_enter, R.layout.right_exit);

            Bundle bundle = new Bundle();
            bundle.putString("jewelrap_id", uniqueid);
            bundle.putString("user_name", name);
            bundle.putString("shape", shape);
            bundle.putString("certificate", certificate);
            bundle.putString("color", color);
            bundle.putString("purity", purity);
            bundle.putString("size_to", size_to);
            bundle.putString("size_from", size_from);
            bundle.putString("search_id", searchid);
            firebaseAnalytics.logEvent("view_stock", bundle);


        }
       else {
            Toast.makeText(getActivity(), "Please select any value or give the value range for size", Toast.LENGTH_LONG).show();

        }
    }


    private List<String> convertListToString(List<JewelRapItem> listItem) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < listItem.size(); i++) {
            list.add(listItem.get(i).getId());
            if (Global_variable.logEnabled) {
                Log.e(TAG, "id " + listItem.get(i).getId());
            }
        }
        return list;
    }

    @Override
    public void onItemClickedShape(int position) {
        Log.e(Global_variable.LOG_TAG, "***shape click");
        shapeAdapter.toggleSelection(position);
    }

    @Override
    public void onItemClickedColor(int position) {
        colorAdapter.toggleSelection(position);
    }

    @Override
    public void onItemClickedPurity(int position) {
        purityAdapter.toggleSelection(position);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        menu.clear();
        inflater.inflate(R.menu.menu_searchbyid, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemSearchById:
                editTextSearchbyid.requestFocus();
                int cx = (mRevealView.getLeft() + mRevealView.getRight());
                int cy = mRevealView.getTop();
                int radius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());

                //Below Android LOLIPOP Version
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                   /* SupportAnimator animator =
                            ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.setDuration(700);

                    SupportAnimator animator_reverse = animator.reverse();*/

                    if (hidden) {
                        SupportAnimator animator =
                                ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
                        animator.setInterpolator(new AccelerateDecelerateInterpolator());
                        mRevealView.setVisibility(View.VISIBLE);
                        animator.start();
                        hidden = false;
                    } else {
                        SupportAnimator animator =
                                ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, radius, 0);
                        animator.setInterpolator(new AccelerateDecelerateInterpolator());
                        animator.addListener(new SupportAnimator.AnimatorListener() {
                            @Override
                            public void onAnimationStart() {

                            }

                            @Override
                            public void onAnimationEnd() {
                                mRevealView.setVisibility(View.GONE);
                                hidden = true;
                                if (editTextSearchbyid.length() > 0) {
                                    item.setTitle("id: " + editTextSearchbyid.getText());
                                } else {
                                    item.setTitle(Html.fromHtml("search by id"));
                                }
                            }

                            @Override
                            public void onAnimationCancel() {

                            }

                            @Override
                            public void onAnimationRepeat() {

                            }
                        });
                        animator.start();
                    }
                }
                // Android LOLIPOP And ABOVE Version
                else {
                    if (hidden) {
                        Animator anim = android.view.ViewAnimationUtils.
                                createCircularReveal(mRevealView, cx, cy, 0, radius);
                        mRevealView.setVisibility(View.VISIBLE);
                        anim.start();
                        hidden = false;
                    } else {
                        Animator anim = android.view.ViewAnimationUtils.
                                createCircularReveal(mRevealView, cx, cy, radius, 0);
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mRevealView.setVisibility(View.GONE);
                                hidden = true;

                                if (editTextSearchbyid.length() > 0) {
                                    item.setTitle(Html.fromHtml("id: " + editTextSearchbyid.getText()));
                                } else {
                                    item.setTitle(Html.fromHtml("search by id"));
                                }
                            }
                        });
                        anim.start();
                    }
                }
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    private void hideRevealView() {
        if (mRevealView.getVisibility() == View.VISIBLE) {
            mRevealView.setVisibility(View.GONE);
            hidden = true;
        }
    }


}


