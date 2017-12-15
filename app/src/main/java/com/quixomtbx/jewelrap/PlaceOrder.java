package com.quixomtbx.jewelrap;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.quixomtbx.jewelrap.Adapter.CertificateAdapter;
import com.quixomtbx.jewelrap.Adapter.ColorAdapter;
import com.quixomtbx.jewelrap.Adapter.NavigationDrawerAdapter;
import com.quixomtbx.jewelrap.Adapter.PurityAdapter;
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

public class PlaceOrder extends AppCompatActivity implements View.OnClickListener,
        CheckBox.OnCheckedChangeListener,
        ShapeAdapter.ViewHolder.ClickListener,
        CertificateAdapter.ViewHolder.ClickListener,
        ColorAdapter.ViewHolder.ClickListener,
        PurityAdapter.ViewHolder.ClickListener,
        SizeAdapter.ViewHolder.ClickListener {

    String TAG = "JEWELRAP", userId, username, uniqueid;
    JSONObject jsonObject;
    int SELECTED_CATEGORY;
    int heightShape, heightCertificate, heightColor, heightPurity;
    Dialog dialog, userSelectionDialog;

    CheckBox checkBox_wholesaler, checkBox_broker;
    ImageView imageView_submit, imageView_cancel;
    ShapeAdapter shapeAdapter;
    CertificateAdapter certificateAdapter;
    ColorAdapter colorAdapter;
    PurityAdapter purityAdapter;
    SizeAdapter sizeAdapter;
    LinearLayout layoutSizePairCarat, layoutWeight, layoutQuantity, layoutPair, layoutSize;
    RecyclerView recyclerViewShape, recyclerViewSize, recyclerViewCertificate, recyclerViewColor, recyclerViewPurity;
    RecyclerView.LayoutManager layoutManagerShape, layoutManagerCertificate, layoutManagerColor;
    TextView textViewShape, textViewSize, textViewCertificate, textViewColor, textViewPurity, textViewSizeCarat;
    Button btnPreviewOrder;
    ArrayList<JewelRapItem> listShape = new ArrayList<>();
    ArrayList<JewelRapItem> listCertificate = new ArrayList<>();
    ArrayList<JewelRapItem> listColor = new ArrayList<>();
    ArrayList<JewelRapItem> listPurity = new ArrayList<>();
    ArrayList<JewelRapItem> listSize = new ArrayList<>();
    ArrayList<String> listShapeOrder, listSizeOrder, listCertificateOrder, listPurityOrder, listColorOrder;
    List<JewelRapItem> listItemOrderShape = new ArrayList<>();
    List<JewelRapItem> listItemOrderSize = new ArrayList<>();
    List<JewelRapItem> listItemOrderColor = new ArrayList<>();
    List<JewelRapItem> listItemOrderCertificate = new ArrayList<>();
    List<JewelRapItem> listItemOrderPurity = new ArrayList<>();

    SharedPreferences sharedPreferences;
    EditText editTextPrice, editTextFromSize, editTextToSize, editTextDemands, editTextWeight, editTextQuantity;
    Toolbar toolbar;
    TextView toolbartitle, textViewEtraComment;
    SessionManager sessionManager;
    CheckBox checkBoxPair;
    String pair = "0";
    FirebaseAnalytics firebaseAnalytics;

    boolean brokerSelected,wholesalerSelected,has_broker;
    String send_to="";
    List<String> shapeList,colorList,purityList,sizeList,certificateList;
    boolean pairFlag=true,brokerFlag=true,wholesalerFlag=true;
    String token;
    JSONObject jsonObject1;

    static String place_inquiry="false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        sessionManager = new SessionManager(PlaceOrder.this);
        toolbartitle = (TextView) findViewById(R.id.toolbar_title);
        toolbartitle.setText(R.string.app_name);
        textViewEtraComment = (TextView) findViewById(R.id.textview_extracomment);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Intent intent = getIntent();
        String responseObject = intent.getStringExtra("json_object");
        SELECTED_CATEGORY = intent.getIntExtra("selected_category", 1);


        try {
            jsonObject = new JSONObject(responseObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Glide.get(PlaceOrder.this).clearMemory();
        textViewShape = (TextView) findViewById(R.id.tv_title_shape);
        /*if(SELECTED_CATEGORY==6){
            textViewShape.setText("Stone");
        }*/
        textViewSize = (TextView) findViewById(R.id.tv_title_size);
        textViewCertificate = (TextView) findViewById(R.id.tv_title_certificate);
        textViewPurity = (TextView) findViewById(R.id.tv_title_purity);
        textViewColor = (TextView) findViewById(R.id.tv_title_color);
        textViewSizeCarat = (TextView) findViewById(R.id.tv_title_size_carat);

        editTextPrice = (EditText) findViewById(R.id.edit_text_price);
        editTextQuantity = (EditText) findViewById(R.id.edit_text_quantity);
        editTextWeight = (EditText) findViewById(R.id.edit_text_weight);
        editTextDemands = (EditText) findViewById(R.id.edit_text_extra_demand);
        editTextDemands.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.edit_text_extra_demand) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });
        editTextFromSize = (EditText) findViewById(R.id.edit_text_size_from);
        editTextToSize = (EditText) findViewById(R.id.edit_text_size_to);
        checkBoxPair = (CheckBox) findViewById(R.id.checkBoxPair);

        layoutSizePairCarat = (LinearLayout) findViewById(R.id.linearsizepair);
        layoutWeight = (LinearLayout) findViewById(R.id.linearweight);
        layoutQuantity = (LinearLayout) findViewById(R.id.linearquantity);
        layoutPair = (LinearLayout) findViewById(R.id.linearpair);
        layoutSize = (LinearLayout) findViewById(R.id.ll_layout_size_carat);
        recyclerViewShape = (RecyclerView) findViewById(R.id.rv_shape);

        recyclerViewShape.setNestedScrollingEnabled(false);
        if(Global_variable.logEnabled)
        {
            Log.e(" category id "," "+ SELECTED_CATEGORY);

        }
        if (SELECTED_CATEGORY == 5 || SELECTED_CATEGORY == 6) {
            setLayoutManager(recyclerViewShape, layoutManagerShape, 4);
        } else {
            setLayoutManager(recyclerViewShape, layoutManagerShape, 5);
        }
        ((SimpleItemAnimator) recyclerViewShape.getItemAnimator()).setSupportsChangeAnimations(false);

        recyclerViewSize = (RecyclerView) findViewById(R.id.rv_size);
        recyclerViewSize.setNestedScrollingEnabled(false);
        setLayoutManager(recyclerViewSize, layoutManagerShape, 4);
        ((SimpleItemAnimator) recyclerViewSize.getItemAnimator()).setSupportsChangeAnimations(false);

        recyclerViewCertificate = (RecyclerView) findViewById(R.id.rv_certificate);
        recyclerViewCertificate.setNestedScrollingEnabled(false);
        setLayoutManager(recyclerViewCertificate, layoutManagerCertificate, 4);
        ((SimpleItemAnimator) recyclerViewCertificate.getItemAnimator()).setSupportsChangeAnimations(false);

        recyclerViewColor = (RecyclerView) findViewById(R.id.rv_color);
        recyclerViewColor.setNestedScrollingEnabled(false);
        setLayoutManager(recyclerViewColor, layoutManagerColor, 6);
        ((SimpleItemAnimator) recyclerViewColor.getItemAnimator()).setSupportsChangeAnimations(false);

        recyclerViewPurity = (RecyclerView) findViewById(R.id.rv_purity);

        recyclerViewPurity.setNestedScrollingEnabled(false);
        setLayoutManager(recyclerViewPurity, layoutManagerColor, 6);
        recyclerViewPurity.setHasFixedSize(true);
        ((SimpleItemAnimator) recyclerViewPurity.getItemAnimator()).setSupportsChangeAnimations(false);

        btnPreviewOrder = (Button) findViewById(R.id.btn_preview_order);
        btnPreviewOrder.setOnClickListener(this);

        shapeAdapter = new ShapeAdapter(this, listShape, PlaceOrder.this, SELECTED_CATEGORY,14);
        sizeAdapter = new SizeAdapter(this, listSize, PlaceOrder.this);
        certificateAdapter = new CertificateAdapter(this, listCertificate, PlaceOrder.this);
        colorAdapter = new ColorAdapter(this, listColor, PlaceOrder.this);
        purityAdapter = new PurityAdapter(this, listPurity, PlaceOrder.this);

        sharedPreferences = getSharedPreferences(SessionManager.MyPREFERENCES, MODE_PRIVATE);
        userId = sharedPreferences.getString(SessionManager.Id, null);
        username = sharedPreferences.getString(SessionManager.Name, null);
        uniqueid = sharedPreferences.getString(SessionManager.UNIQUEID, null);
        token=sharedPreferences.getString(SessionManager.TOKEN,null);


        setLayoutVisibility(SELECTED_CATEGORY);


        getPairValue();


    }


    private void getPairValue(){
        layoutPair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pairFlag){
                    checkBoxPair.setChecked(true);
                    pair="1";
                    pairFlag=false;
                }
                else {
                    checkBoxPair.setChecked(false);
                    pair="0";
                    pairFlag=true;
                }
                if(Global_variable.logEnabled) {
                    Log.e("**pair ", pair);
                }

            }
        });

    }

    private void setLayoutVisibility(int selected_category) {

        switch (selected_category) {
            case 1: {

                populateShapeSize(jsonObject, listShape, "shape", "shape_name", "shape_image");
                populateData(jsonObject, listCertificate, "certification", "certified_name");
                populateData(jsonObject, listColor, "color", "color_name");
                populateData(jsonObject, listPurity, "purity", "purity_name");
                //  calculateHeight();

                shapeAdapter = new ShapeAdapter(this, listShape, PlaceOrder.this, 1,14);
                certificateAdapter = new CertificateAdapter(this, listCertificate, PlaceOrder.this);
                colorAdapter = new ColorAdapter(this, listColor, PlaceOrder.this);
                purityAdapter = new PurityAdapter(this, listPurity, PlaceOrder.this);
                setVisibility(View.VISIBLE, View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.VISIBLE);

              /*  setHeight(recyclerViewShape, heightShape);
                setHeight(recyclerViewCertificate, heightCertificate);
                setHeight(recyclerViewColor, heightColor);
                setHeight(recyclerViewPurity, heightPurity);*/

                setAdapter(recyclerViewShape, shapeAdapter);
                setAdapter(recyclerViewCertificate, certificateAdapter);
                setAdapter(recyclerViewColor, colorAdapter);
                setAdapter(recyclerViewPurity, purityAdapter);
                break;
            }
            case 2: {
                populateShapeSize(jsonObject, listShape, "shape", "shape_name", "shape_image");
                populateShapeSize(jsonObject, listSize, "size", "size_name", "size_image");

                //   calculateHeight();

                shapeAdapter = new ShapeAdapter(this, listShape, PlaceOrder.this, 2,14);
                sizeAdapter = new SizeAdapter(this, listSize, PlaceOrder.this);

                setVisibility(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE);

               /* setHeight(recyclerViewShape, heightShape);
                setHeight(recyclerViewSize, heightPurity);*/

                setAdapter(recyclerViewShape, shapeAdapter);
                setAdapter(recyclerViewSize, sizeAdapter);
                break;
            }
            case 3: {
                populateShapeSize(jsonObject, listShape, "shape", "shape_name", "shape_image");

                //    calculateHeight();
                shapeAdapter = new ShapeAdapter(this, listShape, PlaceOrder.this, 3,14);
                setVisibility(View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE);
                // setHeight(recyclerViewShape, heightShape);
                setAdapter(recyclerViewShape, shapeAdapter);
                break;
            }
            case 4: {
                populateShapeSize(jsonObject, listShape, "shape", "shape_name", "shape_image");
                //  calculateHeight();
                shapeAdapter = new ShapeAdapter(this, listShape, PlaceOrder.this, 4,14);
                setVisibility(View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE);
                //  setHeight(recyclerViewShape, heightShape);
                setAdapter(recyclerViewShape, shapeAdapter);
                break;
            }
            case 5: {
                populateShapeSize(jsonObject, listShape, "shape", "shape_name", "shape_image");
                shapeAdapter = new ShapeAdapter(this, listShape, PlaceOrder.this, 5,14);
                setVisibility(View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE);
                editTextFromSize.setInputType(InputType.TYPE_CLASS_TEXT);
                editTextToSize.setInputType(InputType.TYPE_CLASS_TEXT);
                textViewEtraComment.setText("Comments");
                TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f);
                layoutSize.setLayoutParams(params);
                setAdapter(recyclerViewShape, shapeAdapter);
                break;
            }
            case 6: {
                populateShapeSize(jsonObject, listShape, "shape", "shape_name", "shape_image");
                shapeAdapter = new ShapeAdapter(this, listShape, PlaceOrder.this, 6,14);
                setVisibility(View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE, View.GONE, View.VISIBLE, View.GONE);
                editTextFromSize.setInputType(InputType.TYPE_CLASS_TEXT);
                editTextToSize.setInputType(InputType.TYPE_CLASS_TEXT);
                textViewEtraComment.setText("Comments");
                TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f);
                layoutSize.setLayoutParams(params);
                setAdapter(recyclerViewShape, shapeAdapter);
                break;
            }
        }

    }

    private void setVisibility(int shape, int size, int certificate, int color, int purity, int sizeCarat, int weight, int quantity, int pair) {
        recyclerViewShape.setVisibility(shape);

        recyclerViewSize.setVisibility(size);
        recyclerViewCertificate.setVisibility(certificate);
        recyclerViewColor.setVisibility(color);
        recyclerViewPurity.setVisibility(purity);
        layoutSizePairCarat.setVisibility(sizeCarat);
        layoutWeight.setVisibility(weight);
        layoutQuantity.setVisibility(quantity);
        layoutPair.setVisibility(pair);
        textViewShape.setVisibility(shape);
        textViewSize.setVisibility(size);
        textViewCertificate.setVisibility(certificate);
        textViewColor.setVisibility(color);
        textViewPurity.setVisibility(purity);
        textViewSizeCarat.setVisibility(sizeCarat);
    }

    private void populateData(JSONObject jsonObject, ArrayList<JewelRapItem> list, String key, String itemName) {
        if (list.size() > 0)
            list.clear();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            Log.e("JEWELRAP"," json array res ::"+jsonArray.toString());
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

    private void setLayoutManager(RecyclerView recylcerView, RecyclerView.LayoutManager layoutManager, int gridSize) {
        layoutManager = new GridLayoutManager(getApplicationContext(), gridSize);
        recylcerView.setLayoutManager(layoutManager);
    }

    private void setAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);


    }

    private void populateShapeSize(JSONObject jsonObject, ArrayList<JewelRapItem> list, String key, String name, String image) {
        try {
            String categoryName = jsonObject.getString("category_name");
           // String categoryName = "category_name";
            textViewShape.setText(categoryName);
            if (Global_variable.logEnabled) {
                Log.e("** category name ", " " + categoryName);
            }
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


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_preview_order: {

                dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.order_preview);
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                layoutParams.copyFrom(dialog.getWindow().getAttributes());
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                layoutParams.gravity = Gravity.CENTER;
                dialog.getWindow().setAttributes(layoutParams);

                TextView textViewShape = (TextView) dialog.findViewById(R.id.tv_order_shape);
                TextView textViewColor = (TextView) dialog.findViewById(R.id.tv_order_color);
                TextView textViewSize = (TextView) dialog.findViewById(R.id.tv_order_size);
                TextView textViewPurity = (TextView) dialog.findViewById(R.id.tv_order_purity);
                TextView textViewCertificate = (TextView) dialog.findViewById(R.id.tv_order_certificate);
                TextView textViewPrice = (TextView) dialog.findViewById(R.id.tv_order_price);
                TextView textViewDescription = (TextView) dialog.findViewById(R.id.tv_order_description);
                textViewDescription.setMovementMethod(new ScrollingMovementMethod());
                TextView textViewPair = (TextView) dialog.findViewById(R.id.tv_order_pair);
                TextView textViewWeight = (TextView) dialog.findViewById(R.id.tv_order_weight);
                TextView textViewQuantity = (TextView) dialog.findViewById(R.id.tv_order_quantity);
                LinearLayout linearLayoutCertificate = (LinearLayout) dialog.findViewById(R.id.linear_certificate);
                LinearLayout linearLayoutColor = (LinearLayout) dialog.findViewById(R.id.linear_color);
                LinearLayout linearLayoutPurity = (LinearLayout) dialog.findViewById(R.id.linear_purity);
                LinearLayout linearLayoutSize = (LinearLayout) dialog.findViewById(R.id.linear_size);
                LinearLayout linearLayoutPair = (LinearLayout) dialog.findViewById(R.id.linear_pair);
                LinearLayout linearLayoutWeight = (LinearLayout) dialog.findViewById(R.id.linear_weight);
                LinearLayout linearLayoutQuantity = (LinearLayout) dialog.findViewById(R.id.linear_quantity);
                Button btnOrderCancel = (Button) dialog.findViewById(R.id.btn_order_cancel);
                Button btnOrderPlace = (Button) dialog.findViewById(R.id.btn_order_place);
                btnOrderCancel.setOnClickListener(this);
                btnOrderPlace.setOnClickListener(this);
                if (SELECTED_CATEGORY == 1) {
                    linearLayoutCertificate.setVisibility(View.VISIBLE);
                    linearLayoutColor.setVisibility(View.VISIBLE);
                    linearLayoutSize.setVisibility(View.VISIBLE);
                    linearLayoutPurity.setVisibility(View.VISIBLE);
                    linearLayoutPair.setVisibility(view.VISIBLE);
                }
                if (SELECTED_CATEGORY == 2) {
                    linearLayoutSize.setVisibility(View.VISIBLE);
                }
                if (SELECTED_CATEGORY == 5) {
                    linearLayoutSize.setVisibility(View.VISIBLE);
                    linearLayoutQuantity.setVisibility(View.VISIBLE);
                    linearLayoutWeight.setVisibility(View.VISIBLE);
                }
                if (SELECTED_CATEGORY == 6) {
                    linearLayoutSize.setVisibility(View.VISIBLE);
                    linearLayoutQuantity.setVisibility(View.VISIBLE);

                }
                if (!shapeAdapter.getSelectedItems().isEmpty()) {
                    if (!listItemOrderShape.isEmpty())
                        listItemOrderShape.clear();
                    listItemOrderShape = shapeAdapter.getSelectedItems();
                    for (int i = 0; i < listItemOrderShape.size(); i++) {
                        textViewShape.append(listItemOrderShape.get(i).getItemName() + " ");
                    }

                } else {
                    textViewShape.setText("-NA-");
                    textViewShape.setTextColor(getResources().getColor(R.color.mild_red));
                }


                if (!certificateAdapter.getSelectedItems().isEmpty()) {
                    if (!listItemOrderCertificate.isEmpty())
                        listItemOrderCertificate.clear();
                    listItemOrderCertificate = certificateAdapter.getSelectedItems();
                    for (int i = 0; i < certificateAdapter.getSelectedItems().size(); i++) {
                        textViewCertificate.append(listItemOrderCertificate.get(i).getItemName() + " ");
                    }
                } else {
                    textViewCertificate.setText("-NA-");
                    textViewCertificate.setTextColor(getResources().getColor(R.color.mild_red));
                }

                if (!colorAdapter.getSelectedItems().isEmpty()) {

                    if (!listItemOrderColor.isEmpty())
                        listItemOrderColor.clear();
                    listItemOrderColor = colorAdapter.getSelectedItems();
                    for (int i = 0; i < colorAdapter.getSelectedItems().size(); i++) {
                        textViewColor.append(listItemOrderColor.get(i).getItemName() + " ");

                    }

                } else {
                    textViewColor.setText("-NA-");
                    textViewColor.setTextColor(getResources().getColor(R.color.mild_red));
                }
                if (!purityAdapter.getSelectedItems().isEmpty()) {

                    if (!listItemOrderPurity.isEmpty())
                        listItemOrderPurity.clear();
                    listItemOrderPurity = purityAdapter.getSelectedItems();
                    for (int i = 0; i < purityAdapter.getSelectedItems().size(); i++) {
                        textViewPurity.append(listItemOrderPurity.get(i).getItemName() + " ");

                    }

                } else {
                    textViewPurity.setText("-NA-");
                    textViewPurity.setTextColor(getResources().getColor(R.color.mild_red));
                }
                if (SELECTED_CATEGORY == 2) {
                    if (!sizeAdapter.getSelectedItems().isEmpty()) {
                        if (!listItemOrderSize.isEmpty())
                            listItemOrderSize.clear();
                        listItemOrderSize = sizeAdapter.getSelectedItems();
                        for (int i = 0; i < sizeAdapter.getSelectedItems().size(); i++) {
                            textViewSize.append(listItemOrderSize.get(i).getItemName() + " ");
                        }
                    } else {
                        textViewSize.setText("-NA-");
                        textViewSize.setTextColor(getResources().getColor(R.color.mild_red));
                    }
                } else if (SELECTED_CATEGORY == 1) {
                    if (editTextFromSize.getText().toString().isEmpty() && editTextToSize.getText().toString().isEmpty()) {
                        textViewSize.setText("-NA-");
                        textViewSize.setTextColor(getResources().getColor(R.color.mild_red));
                    } else {
                        textViewSize.setText(editTextFromSize.getText() + " to " + editTextToSize.getText());
                    }
                    if (pair.equals("1")) {
                        textViewPair.setText("Yes");
                    } else {
                        textViewPair.setText("-NA-");
                        textViewPair.setTextColor(getResources().getColor(R.color.mild_red));
                    }
                } else if (SELECTED_CATEGORY == 5 || SELECTED_CATEGORY == 6) {
                    if (editTextFromSize.getText().toString().isEmpty() && editTextToSize.getText().toString().isEmpty()) {
                        textViewSize.setText("-NA-");
                        textViewSize.setTextColor(getResources().getColor(R.color.mild_red));
                    } else {
                        textViewSize.setText(editTextFromSize.getText() + " to " + editTextToSize.getText());
                    }

                }/*else if(SELECTED_CATEGORY == 6){
                    if (editTextFromSize.getText().toString().isEmpty() && editTextToSize.getText().toString().isEmpty()) {
                        textViewSize.setText("-NA-");
                        textViewSize.setTextColor(getResources().getColor(R.color.mild_red));
                    } else {
                        textViewSize.setText(editTextFromSize.getText() + " to " + editTextToSize.getText());
                    }

                }*/ else {
                    textViewSize.setText("-NA-");
                    textViewSize.setTextColor(getResources().getColor(R.color.mild_red));
                }


                if (editTextPrice.getText().toString().equals("")) {
                    textViewPrice.setText("-NA-");
                    textViewPrice.setTextColor(getResources().getColor(R.color.mild_red));
                } else {
                    textViewPrice.append(editTextPrice.getText().toString());
                }
                if (editTextDemands.getText().toString().equals("")) {
                    textViewDescription.setText("-NA-");
                    textViewDescription.setTextColor(getResources().getColor(R.color.mild_red));
                } else {
                    textViewDescription.append(editTextDemands.getText().toString().trim().replace("\n", " "));

                }
                if (editTextWeight.getText().toString().equals("")) {
                    textViewWeight.setText("-NA-");
                    textViewWeight.setTextColor(getResources().getColor(R.color.mild_red));
                } else {
                    textViewWeight.append(editTextWeight.getText().toString().trim().replace("\n", " "));
                }
                if (editTextQuantity.getText().toString().equals("")) {
                    textViewQuantity.setText("-NA-");
                    textViewQuantity.setTextColor(getResources().getColor(R.color.mild_red));
                } else {
                    textViewQuantity.append(editTextQuantity.getText().toString().trim().replace("\n", " "));
                }
                has_broker = sharedPreferences.getBoolean(SessionManager.HAS_BROKER, false);
                if (SELECTED_CATEGORY == 1) {

                    if (shapeAdapter.getSelectedItems().size() >= 1 && purityAdapter.getSelectedItems().size() >= 1 && colorAdapter.getSelectedItems().size() >= 1 && (!editTextFromSize.getText().toString().isEmpty() && !editTextToSize.getText().toString().isEmpty())) {

                        if (has_broker) {
                            openDialog();
                        } else {
                            dialog.show();

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "You've to select at least one item from each shape,color,purity and size to place enquiry.", Toast.LENGTH_LONG).show();
                    }
                } else if (SELECTED_CATEGORY == 2) {

                    if (shapeAdapter.getSelectedItems().size() >= 1 && sizeAdapter.getSelectedItems().size() >= 1) {
                        if (has_broker) {
                            openDialog();
                        } else {
                            dialog.show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "You've to select at least one item from shape and size to place enquiry.", Toast.LENGTH_LONG).show();
                    }
                } else if (SELECTED_CATEGORY == 6) {

                    if (shapeAdapter.getSelectedItems().size() >= 1 && (!editTextFromSize.getText().toString().isEmpty() && !editTextToSize.getText().toString().isEmpty()) && !editTextPrice.getText().toString().isEmpty()) {
                        if (has_broker) {
                            openDialog();
                        } else {
                            dialog.show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "You've to select at least one item from shape,size and price to place enquiry.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (shapeAdapter.getSelectedItems().size() >= 1) {
                        if (has_broker) {
                            openDialog();
                        } else {
                            dialog.show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "You've to select at least one item from shape to place enquiry.", Toast.LENGTH_LONG).show();
                    }
                }


                break;
            }
            case R.id.btn_order_cancel: {
                dialog.cancel();
                brokerSelected=false;
                wholesalerSelected=false;
                break;

            }
            case R.id.btn_order_place: {

                place_inquiry="true";
                Log.e(Global_variable.LOG_TAG,"PLACE INQUIRY=="+place_inquiry);
                shapeList = convertListToString(listItemOrderShape);
                sizeList = convertListToString(listItemOrderSize);
                certificateList = convertListToString(listItemOrderCertificate);
                purityList = convertListToString(listItemOrderPurity);
                colorList = convertListToString(listItemOrderColor);
                placeOrder(shapeList, sizeList, certificateList, purityList, colorList);
                break;
            }

            case R.id.imgCancel: {
                userSelectionDialog.dismiss();
                wholesalerSelected=false;
                brokerSelected=false;

                break;
            }
            case R.id.imgSubmit: {
                if (wholesalerSelected && !brokerSelected) {
                    send_to = "1";
                } else if (brokerSelected && !wholesalerSelected) {
                    send_to = "2";
                } else if (brokerSelected && wholesalerSelected) {
                    send_to = "0";
                } else {
                    send_to = "";
                }

                if (Global_variable.logEnabled) {
                    Log.e("send_to", "val imgSubmit " + send_to);

                }
                if (!send_to.equals("")) {
                    userSelectionDialog.dismiss();
                    dialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), "You've to select at least one or both user to place enquiry.", Toast.LENGTH_LONG).show();
                }
                break;
            }

        }
    }

    private void openDialog(){
        wholesalerFlag=true;
        brokerFlag=true;
        userSelectionDialog = new Dialog(PlaceOrder.this,R.style.DesignDemo);
        userSelectionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        userSelectionDialog.setContentView(R.layout.custom_userselectionfilter);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        userSelectionDialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation;
        layoutParams.copyFrom(userSelectionDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.BOTTOM;
        userSelectionDialog.getWindow().setAttributes(layoutParams);

        LinearLayout linearLayoutBroker=(LinearLayout)userSelectionDialog.findViewById(R.id.linearBroker);
        LinearLayout linearLayoutWholesaler=(LinearLayout)userSelectionDialog.findViewById(R.id.linearWholesaler);
        checkBox_broker=(CheckBox)userSelectionDialog.findViewById(R.id.cb_Broker);
       // checkBox_broker.setOnCheckedChangeListener(this);
        checkBox_wholesaler=(CheckBox)userSelectionDialog.findViewById(R.id.cb_Wholesaler);
       // checkBox_wholesaler.setOnCheckedChangeListener(this);
        linearLayoutBroker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(brokerFlag){
                    checkBox_broker.setChecked(true);
                    brokerSelected=true;
                    brokerFlag=false;
                }
                else {
                    checkBox_broker.setChecked(false);
                    brokerSelected=false;
                    brokerFlag=true;
                }
            }
        });
        linearLayoutWholesaler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("**** whol select  ",""+ wholesalerFlag);
                if(wholesalerFlag){
                    checkBox_wholesaler.setChecked(true);
                    wholesalerSelected=true;
                    wholesalerFlag=false;
                }
                else {
                    checkBox_wholesaler.setChecked(false);
                    wholesalerSelected=false;
                    wholesalerFlag=true;
                }


            }
        });

        imageView_submit=(ImageView)userSelectionDialog.findViewById(R.id.imgSubmit);
        imageView_submit.setOnClickListener(this);
        imageView_cancel = (ImageView) userSelectionDialog.findViewById(R.id.imgCancel);
        imageView_cancel.setOnClickListener(this);
        userSelectionDialog.show();
    }

    private void placeOrder(List<String> listShape, List<String> listSize, List<String> listCertificate, List<String> listPurity, List<String> listColor) {
        String shape = TextUtils.join(", ", listShape);
        String size = android.text.TextUtils.join(",", listSize);
        String certificate = android.text.TextUtils.join(",", listCertificate);
        String purity = android.text.TextUtils.join(",", listPurity);
        String color = android.text.TextUtils.join(",", listColor);

        RestClientVolley restClientVolley = new RestClientVolley(PlaceOrder.this, Request.Method.POST, Global_variable.inquiry_api, new VolleyCallBack() {
            @Override
            public void processCompleted(String response) {
                if (response != null) {
                    enquiryPosted(response);

                }

            }
        });

        restClientVolley.addParameter("category", String.valueOf(SELECTED_CATEGORY));
        restClientVolley.addParameter("selected_shape", shape);
        restClientVolley.addParameter("selected_color", color);
        restClientVolley.addParameter("selected_purity", purity);
        restClientVolley.addParameter("selected_certification", certificate);
        restClientVolley.addParameter("selected_size", size);
        restClientVolley.addParameter("price", editTextPrice.getText().toString());
        restClientVolley.addParameter("extra_demand", editTextDemands.getText().toString());
        restClientVolley.addParameter("from_size", editTextFromSize.getText().toString());
        restClientVolley.addParameter("to_size", editTextToSize.getText().toString());
        restClientVolley.addParameter("is_pair", pair);
        restClientVolley.addParameter("weight", editTextWeight.getText().toString());
        restClientVolley.addParameter("qty", editTextQuantity.getText().toString());
        if (Global_variable.logEnabled) {
            Log.e("send_to", " val " + send_to);
            Log.e("weight ", " " + editTextWeight.getText().toString());
            Log.e("qty ", " " + editTextQuantity.getText().toString());
        }
        restClientVolley.addParameter("send_to", send_to);
        restClientVolley.addParameter("user", userId);
        restClientVolley.executeRequest();

    }

    private void enquiryPosted(String response) {
        String message, status;
        JSONObject jsonObject = null;
        if (Global_variable.logEnabled) {
            Log.e("** inquiry post", response);
        }
        try {
            jsonObject = new JSONObject(response);

            status = jsonObject.getString("status");
            if (Global_variable.logEnabled) {
                Log.e("**status post inq", status);
            }
            if (status.equals("ok")) {
                Bundle params = new Bundle();
                params.putString("jewelrap_id", uniqueid);
                params.putString("user_name", username);
//                params.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "place_enquiry");
                params.putString("category", getCategoryName(SELECTED_CATEGORY));
                firebaseAnalytics.logEvent("place_enquiry", params);

                message = jsonObject.getString("message");
                NavigationDrawerAdapter.selected_item = 1;
                Intent in = new Intent(getApplicationContext(), MainActivity.class);
                in.putExtra("pos", 2);
                startActivity(in);
                MainActivity.id_pos = 1;
                Toast.makeText(getApplicationContext(), " " + message, Toast.LENGTH_LONG).show();

            } else {
                if (Global_variable.logEnabled) {
                    Log.e("** inquiry post else", response);
                }
                if (jsonObject.has("multiple_login")) {
                    if (Global_variable.logEnabled) {
                        Log.e("Message : ", jsonObject.getString("messages"));
                    }
                    dialog.dismiss();
                    String msg = jsonObject.optString("messages");
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PlaceOrder.this, R.style.DesignDemo);
                    dlgAlert.setMessage(msg);
                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    sessionManager.logoutUser();
                                    overridePendingTransition(R.layout.push_right_out, R.layout.push_right_in);
                                }
                            });
                    dlgAlert.setCancelable(false);
                    dlgAlert.create().show();

                } else {
                    message = jsonObject.getString("message");
                    Toast.makeText(getApplicationContext(), " " + message, Toast.LENGTH_LONG).show();
                }
            }
            Glide.get(PlaceOrder.this).clearMemory();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCategoryName(int selected_category) {
        String CATEGORY_NAME = null;
        switch (selected_category) {
            case 1:
                return "Solitaire";
            case 2:
                return "Loose Diamonds";
            case 3:
                return "Diamond Jewelry";
            case 4:
                return "Gold Jewelry";
            case 5:
                return "Silver";
            case 6:
                return "Color stone";
        }
        return CATEGORY_NAME;
    }

    private List<String> convertListToString(List<JewelRapItem> listItem) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < listItem.size(); i++) {
            list.add(listItem.get(i).getId());

        }
        return list;
    }

    @Override
    public void onItemClickedSize(int position) {
        sizeAdapter.toggleSelection(position);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(in);
        MainActivity.id_pos = 0;
        overridePendingTransition(R.layout.push_right_out, R.layout.push_right_in);
    }

    @Override
    public void onItemClickedCertificate(int position) {
        certificateAdapter.toggleSelection(position);
    }

    @Override
    public void onItemClickedShape(int position) {
        shapeAdapter.toggleSelection(position);
    }

    @Override
    public void onItemClickedPurity(int position) {
        purityAdapter.toggleSelection(position);
    }

    @Override
    public void onItemClickedColor(int position) {
        colorAdapter.toggleSelection(position);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_Broker: {
                brokerSelected = isChecked;
            }
            break;
            case R.id.cb_Wholesaler: {
                wholesalerSelected = isChecked;
            }
            break;

        }
    }
}