package com.quixomtbx.jewelrap;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
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
import com.quixomtbx.jewelrap.Adapter.ColorAdapter;
import com.quixomtbx.jewelrap.Adapter.PurityAdapter;
import com.quixomtbx.jewelrap.Adapter.ShadeAdapter;
import com.quixomtbx.jewelrap.Adapter.ShapeAdapter;
import com.quixomtbx.jewelrap.Adapter.SizeAdapter;
import com.quixomtbx.jewelrap.Global.Global_variable;
import com.quixomtbx.jewelrap.Global.SessionManager;
import com.quixomtbx.jewelrap.Model.JewelRapItem;
import com.quixomtbx.jewelrap.Utils.RestClientVolley;
import com.quixomtbx.jewelrap.Utils.VolleyCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;


public class LooseDiamondSearchFragment extends Fragment implements View.OnClickListener,
        ShapeAdapter.ViewHolder.ClickListener,
        ColorAdapter.ViewHolder.ClickListener,
        PurityAdapter.ViewHolder.ClickListener,
        SizeAdapter.ViewHolder.ClickListener,
        ShadeAdapter.ViewHolder.ClickListener {

    RecyclerView recyclerViewShape, recyclerViewSize, recyclerViewColor, recyclerViewPurity, recyclerViewShade;
    RecyclerView.LayoutManager layoutManagerShape, layoutManagerSize, layoutManagerShade, layoutManagerColor, layoutManagerPurity;
    Button btnSearch;
    Context context;
    String URL = Global_variable.ld_categories_api;
    SharedPreferences sharedPreferences;
    String name, token, uniqueid;
    String TAG = "JEWELRAP";
    ShapeAdapter shapeAdapter;
    SizeAdapter sizeAdapter;
    ColorAdapter colorAdapter;
    PurityAdapter purityAdapter;
    ShadeAdapter shadeAdapter;
    ArrayList<JewelRapItem> listLdShape = new ArrayList<>();
    ArrayList<JewelRapItem> listColor = new ArrayList<>();
    ArrayList<JewelRapItem> listPurity = new ArrayList<>();
    ArrayList<JewelRapItem> listShade = new ArrayList<>();
    ArrayList<JewelRapItem> listSize = new ArrayList<>();
    String shape = null, color = null, purity = null, shade = null, size = null;
    List<String> listShapeOrder, listSizeOrder, listPurityOrder, listColorOrder, listShadeOrder;
    List<JewelRapItem> listItemOrderShape = new ArrayList<>();
    List<JewelRapItem> listItemOrderColor = new ArrayList<>();
    List<JewelRapItem> listItemOrderSize = new ArrayList<>();
    List<JewelRapItem> listItemOrderPurity = new ArrayList<>();
    List<JewelRapItem> listItemOrderShade = new ArrayList<>();
    int countFragmentVisibility = 1;
    boolean visibleToUser;
    FirebaseAnalytics firebaseAnalytics;

    LinearLayout mRevealView;
    boolean hidden = true;
    String searchid;
    Menu menu;
    EditText editTextSearchbyid;
    Dialog dialog;
    MenuItem item;
    JSONObject responseObject;
    LinearLayout linearLayoutLD;
    String categoryName;
    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(SessionManager.MyPREFERENCES, Context.MODE_PRIVATE);
        name = sharedPreferences.getString(SessionManager.Name, null);
        token = sharedPreferences.getString(SessionManager.TOKEN, null);
        uniqueid = sharedPreferences.getString(SessionManager.UNIQUEID, null);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loose_diamond_search, container, false);
        context = getActivity();
        Glide.get(context).clearMemory();
        mRevealView = (LinearLayout) view.findViewById(R.id.reveal_items);
        mRevealView.setVisibility(View.INVISIBLE);
        btnSearch = (Button) view.findViewById(R.id.btn_ld_search);
        btnSearch.setOnClickListener(this);
        linearLayoutLD = (LinearLayout) view.findViewById(R.id.linearlayoutLD);
        recyclerViewShape = (RecyclerView) view.findViewById(R.id.rv_ld_shape);
        recyclerViewShape.setNestedScrollingEnabled(false);
        setLayoutManager(recyclerViewShape, layoutManagerShape, 6);

        recyclerViewSize = (RecyclerView) view.findViewById(R.id.rv_ld_size);
        recyclerViewSize.setNestedScrollingEnabled(false);
        setLayoutManager(recyclerViewSize, layoutManagerSize, 4);

        recyclerViewColor = (RecyclerView) view.findViewById(R.id.rv_ld_color);
        setLayoutManager(recyclerViewColor, layoutManagerColor, 8);
        recyclerViewColor.setNestedScrollingEnabled(false);
        ((SimpleItemAnimator) recyclerViewColor.getItemAnimator()).setSupportsChangeAnimations(false);

        recyclerViewShade = (RecyclerView) view.findViewById(R.id.rv_ld_shade);
        recyclerViewShade.setNestedScrollingEnabled(false);
        setLayoutManager(recyclerViewShade, layoutManagerShade, 5);

        recyclerViewPurity = (RecyclerView) view.findViewById(R.id.rv_ld_purity);
        setLayoutManager(recyclerViewPurity, layoutManagerPurity, 6);
        recyclerViewPurity.setNestedScrollingEnabled(false);
        firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        editTextSearchbyid = (EditText) view.findViewById(R.id.edittext_searchbyid);
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
        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        visibleToUser = isVisibleToUser;
        if (mContext != null) {
            if (visibleToUser && countFragmentVisibility == 1) {
                if (listLdShape != null) {
                    listLdShape.clear();
                }
                RestClientVolley restClientVolley = new RestClientVolley(getActivity(), Request.Method.GET, URL, new VolleyCallBack() {
                    @Override
                    public void processCompleted(String response) {
                        try {
                            linearLayoutLD.setVisibility(View.VISIBLE);
                            responseObject = new JSONObject(response);
                            populateDataJsonObject(responseObject.toString());
                            if (Global_variable.logEnabled) {
                                Log.e(TAG, "search Response" + responseObject.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                restClientVolley.addHeader("Authorization", "ApiKey " + name + ":" + token);
                restClientVolley.executeRequest();
                countFragmentVisibility++;
            } else if (visibleToUser && countFragmentVisibility > 1) {
                if (responseObject != null) {
                    if (listLdShape != null) {
                        listLdShape.clear();
                    }
                    populateDataJsonObject(responseObject.toString());
                }
            }
        }
        setHasOptionsMenu(true);

    }

    public void populateDataJsonObject(String response) {
        try {
            JSONObject responseObject = new JSONObject(response);
            categoryName = responseObject.getString("category_name");
            populateShapeSize(responseObject, listLdShape, "shape", "shape_name", "shape_image");
            populateData(responseObject, listSize, "size", "size_name");
            populateData(responseObject, listColor, "color", "color_name");
            populateData(responseObject, listShade, "shade", "shade_name");
            populateData(responseObject, listPurity, "purity", "purity_name");

            //  calculateHeight();
            shapeAdapter = new ShapeAdapter(this, listLdShape, context, 2, 13);
            sizeAdapter = new SizeAdapter(this, listSize, context);
            colorAdapter = new ColorAdapter(this, listColor, context);
            shadeAdapter = new ShadeAdapter(this, listShade, context);
            purityAdapter = new PurityAdapter(this, listPurity, context);


            setAdapter(recyclerViewShape, shapeAdapter);
            setAdapter(recyclerViewSize, sizeAdapter);
            setAdapter(recyclerViewColor, colorAdapter);
            setAdapter(recyclerViewShade, shadeAdapter);
            setAdapter(recyclerViewPurity, purityAdapter);
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
        layoutManager = new GridLayoutManager(context, gridSize);
        recylcerView.setLayoutManager(layoutManager);
    }

    private void setAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);

    }

    private void performSearch() {
        hideRevealView();
        searchid = editTextSearchbyid.getText().toString();
        if ((!shapeAdapter.getSelectedItems().isEmpty() || !sizeAdapter.getSelectedItems().isEmpty() || !shadeAdapter.getSelectedItems().isEmpty() ||
                !purityAdapter.getSelectedItems().isEmpty() || !colorAdapter.getSelectedItems().isEmpty() || (!searchid.isEmpty() && !searchid.equals("null")))) {


            if (!shapeAdapter.getSelectedItems().isEmpty()) {
                if (!listItemOrderShape.isEmpty())
                    listItemOrderShape.clear();
                listItemOrderShape = shapeAdapter.getSelectedItems();
                for (int i = 0; i < listItemOrderShape.size(); i++) {
                    listShapeOrder = convertListToString(listItemOrderShape);
                }
                shape = TextUtils.join(", ", listShapeOrder);

            } else {
                shape = null;
            }

            if (!sizeAdapter.getSelectedItems().isEmpty()) {
                if (!listItemOrderSize.isEmpty())
                    listItemOrderSize.clear();
                listItemOrderSize = sizeAdapter.getSelectedItems();
                for (int i = 0; i < listItemOrderSize.size(); i++) {
                    listSizeOrder = convertListToString(listItemOrderSize);
                }
                size = TextUtils.join(", ", listSizeOrder);

            } else {
                size = null;
            }

            if (!shadeAdapter.getSelectedItems().isEmpty()) {
                if (!listItemOrderShade.isEmpty())
                    listItemOrderShade.clear();
                listItemOrderShade = shadeAdapter.getSelectedItems();
                for (int i = 0; i < listItemOrderShade.size(); i++) {
                    listShadeOrder = convertListToString(listItemOrderShade);
                }
                shade = TextUtils.join(", ", listShadeOrder);

            } else {
                shade = null;
            }

            if (!colorAdapter.getSelectedItems().isEmpty()) {
                if (!listItemOrderColor.isEmpty())
                    listItemOrderColor.clear();
                listItemOrderColor = colorAdapter.getSelectedItems();
                for (int i = 0; i < colorAdapter.getSelectedItems().size(); i++) {
                    listColorOrder = convertListToString(listItemOrderColor);
                }
                color = TextUtils.join(", ", listColorOrder);

            } else {
                color = null;
            }

            if (!purityAdapter.getSelectedItems().isEmpty()) {

                if (!listItemOrderPurity.isEmpty())
                    listItemOrderPurity.clear();
                listItemOrderPurity = purityAdapter.getSelectedItems();
                for (int i = 0; i < purityAdapter.getSelectedItems().size(); i++) {
                    listPurityOrder = convertListToString(listItemOrderPurity);

                }
                purity = TextUtils.join(", ", listPurityOrder);

            } else {
                purity = null;
            }


            if (Global_variable.logEnabled) {
                Log.e(TAG + " Select value :", " shape " + shape + " size: " + size + " shade : " + shade + " color: " + color + " purity : " + purity);
            }

            Intent in = new Intent(context, LooseDiamondSearchList.class);
            in.putExtra("shape", shape);
            in.putExtra("size", size);
            in.putExtra("color", color);
            in.putExtra("purity", purity);
            in.putExtra("shade", shade);
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
            bundle.putString("size", size);
            bundle.putString("color", color);
            bundle.putString("purity", purity);
            bundle.putString("shade", shade);
            bundle.putString("search_id", searchid);

            //bundle.putString("search_id", searchid);
            firebaseAnalytics.logEvent("loosediamond_view_stock", bundle);
        } else {
            Toast.makeText(getActivity(), "Please select any value or give the value range for size", Toast.LENGTH_LONG).show();
        }
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
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        sharedPreferences = mContext.getSharedPreferences(SessionManager.MyPREFERENCES, Context.MODE_PRIVATE);
        name = sharedPreferences.getString(SessionManager.Name, null);
        token = sharedPreferences.getString(SessionManager.TOKEN, null);

        RestClientVolley restClientVolley = new RestClientVolley(getActivity(), Request.Method.GET, URL, new VolleyCallBack() {
            @Override
            public void processCompleted(String response) {
                try {
                    linearLayoutLD.setVisibility(View.VISIBLE);
                    responseObject = new JSONObject(response);
                    populateDataJsonObject(responseObject.toString());
                    if (Global_variable.logEnabled) {
                        Log.e(TAG, "search Response" + responseObject.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        restClientVolley.addHeader("Authorization", "ApiKey " + name + ":" + token);
        restClientVolley.executeRequest();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ld_search:
                performSearch();
                break;
            default:
                break;
        }
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

    @Override
    public void onItemClickedShape(int position) {
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
    public void onItemClickedSize(int position) {
        sizeAdapter.toggleSelection(position);
    }

    @Override
    public void onItemClickedShade(int position) {
        shadeAdapter.toggleSelection(position);
    }
}
