package com.quixom.jewelrap.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quixom.jewelrap.Global.Global_variable;
import com.quixom.jewelrap.Listener.OnLoadMoreListener;
import com.quixom.jewelrap.Model.JewelRapItem;
import com.quixom.jewelrap.R;
import com.quixom.jewelrap.SearchItemDetails;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;


public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {
    Context mContext;
    JewelRapItem item;
    String TAG = "JEWELRAP1";
    Activity activity;
    private ArrayList<JewelRapItem> itemList;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    static float span2Size = 0.7f;
    String notApplicable = "-NA-";

    public SearchListAdapter(Context context, ArrayList<JewelRapItem> itemList, RecyclerView recyclerView, Activity activity) {
        this.mContext = context;
        this.itemList = itemList;
        this.activity = activity;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();
            recyclerView
                    .setOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item, parent, false);
        return new ViewHolder(view);
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        item = itemList.get(position);
        final String id = item.getId();
        final String categoryName;
        String color, price, cut, pol, sym, fluor, col, meas, purity, cert, seller, detail, discount, certNumber, city;
        final JSONObject object = item.getJsonObject();
        String str_shape = item.getShapeTitle();
        categoryName = item.getCategoryName();


        String size = item.getSize();
        if (size.equals("null") || size == null) {
            size = notApplicable + ", ";
        } else {
            size = size + "ct,  ";
        }

        color = item.getColor() + ", ";
        purity = item.getPurity() + ",  ";
        cert = item.getCertificate();

        SpannableString span1 = new SpannableString(str_shape);
        span1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, str_shape.length(), 0);

        if (cert.equals("None")) {
            detail = size;
            detail = detail.replace(",", " ");
        } else {
            detail = size + color + purity + cert;
        }

        SpannableString span2 = new SpannableString(detail);
        span2.setSpan(new RelativeSizeSpan(span2Size), 0, detail.length(), 0);
        span2.setSpan(new ForegroundColorSpan(Color.GRAY), 0, detail.length(), 0);
        CharSequence result = TextUtils.concat(span1, " ", span2);
        holder.shape.setText(result);

        price = String.valueOf(item.getPrice());
        cut = item.getCut();
        pol = item.getPolish();
        sym = item.getSym();
        fluor = item.getFluor();
        col = item.getColor();
        meas = item.getMeas();
        // seller = item.getFirstName() + " " + item.getLastName();
        seller = item.getFirmname();
        discount = item.getDiscount();
        certNumber = item.getCertificateNumber();
        city = item.getCity();

        if (meas.equals("null") || meas == null || meas.equals("")) {
            holder.meas.setText(notApplicable);
        } else {
            holder.meas.setText(meas);

        }

        if (cut.equals("null") || cut == null || cut.equals("")) {
            holder.cut.setText(notApplicable);
        } else {
            holder.cut.setText(cut);
        }
        if (pol.equals("null") || pol == null || pol.equals("")) {
            holder.pol.setText(notApplicable);
        } else {
            holder.pol.setText(pol);
        }

        if (sym.equals("null") || sym == null || sym.equals("")) {
            holder.sym.setText(notApplicable);
        } else {
            holder.sym.setText(sym);
        }

        if (fluor.equals("null") || fluor == null || fluor.equals("")) {
            holder.fluor.setText(notApplicable);
        } else {
            holder.fluor.setText(fluor);
        }


        if (seller.equals("null") || seller == null || seller.equals("")) {
            holder.seller.setText(notApplicable);
        } else {
            holder.seller.setText(seller);
        }

        if (certNumber.equals("null") || certNumber == null || certNumber.equals("")) {
            holder.certNum.setText(notApplicable);
        } else {
            holder.certNum.setText(certNumber);
        }

        if (city.equals("null") || city == null || city.equals("")) {
            holder.city.setText(notApplicable);
        } else {
            holder.city.setText(city);
        }

        if (Global_variable.logEnabled) {
            Log.e("price search", "" + price);
            Log.e("discount search", "" + item.getDiscount());
        }

        if (!price.equals("null") && price != null) {
            holder.price_dis.setText(Currency.getInstance(Locale.US).getSymbol() + " " + price);
        } else if (!discount.equals("null") && discount != null) {
            holder.price_dis.setText(item.getDiscount() + " %");
        } /*else {
            holder.price_dis.setText(notApplicable);
        }*/


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SearchItemDetails.class);
                intent.putExtra("JsonObject", object.toString());
                intent.putExtra("categoryid", "1");
                intent.putExtra("categoryName", categoryName);
                intent.putExtra("inquiry_id",id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.layout.left_enter, R.layout.right_exit);
            }
        });
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView shape, size, price_dis, cut, pol, sym, fluor, meas, seller, certNum, city, cutlabel, pollabel, symlabel, fluorlabel, measlabel, sellerlabel, tvCertLabel,
                tvCityLabel;
        public CardView cardView;
        public LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            shape = (TextView) itemView.findViewById(R.id.tvShape);
            price_dis = (TextView) itemView.findViewById(R.id.tvPriceDiscount);
            cut = (TextView) itemView.findViewById(R.id.tvCut);
            pol = (TextView) itemView.findViewById(R.id.tvPolish);
            sym = (TextView) itemView.findViewById(R.id.tvSym);
            fluor = (TextView) itemView.findViewById(R.id.tvFluor);
            meas = (TextView) itemView.findViewById(R.id.tvMeas);
            certNum = (TextView) itemView.findViewById(R.id.tvCert);
            city = (TextView) itemView.findViewById(R.id.tvCity);

            //size=(TextView)itemView.findViewById(R.id.tvSize);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            layout = (LinearLayout) itemView.findViewById(R.id.linlayout);
            seller = (TextView) itemView.findViewById(R.id.tvSeller);
            cutlabel = (TextView) itemView.findViewById(R.id.tvCutLabel);
            pollabel = (TextView) itemView.findViewById(R.id.tvPolishLabel);
            symlabel = (TextView) itemView.findViewById(R.id.tvSymLabel);
            fluorlabel = (TextView) itemView.findViewById(R.id.tvFluorLabel);
            measlabel = (TextView) itemView.findViewById(R.id.tvMeasLabel);
            sellerlabel = (TextView) itemView.findViewById(R.id.tvSellerLabel);
            tvCertLabel = (TextView) itemView.findViewById(R.id.tvCertLabel);
            tvCityLabel = (TextView) itemView.findViewById(R.id.tvCityLabel);


            DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
            // getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            switch (displayMetrics.densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    //set text-size for low-density devices.
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    //set text-size for medium-density devices.
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    //set text-size for high-density devices.
                    setFontSize(14, 18, 0.7f, 10);
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    setFontSize(14, 18, 0.7f, 10);
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    setFontSize(15, 20, 0.75f, 11);
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    setFontSize(17, 22, 0.8f, 12);
                    break;
                case DisplayMetrics.DENSITY_280:
                    break;
                case DisplayMetrics.DENSITY_400:
                    break;
                case DisplayMetrics.DENSITY_560:
                    break;
                case DisplayMetrics.DENSITY_TV:
                    setFontSize(18, 22, 0.8f, 15);
                    break;
                default:

            }


        }

        public void setFontSize(int size, int shapeSize, float spanSize, int meaSize) {
            cutlabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            pollabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            symlabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            fluorlabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            measlabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            sellerlabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);

            cut.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            pol.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            sym.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            fluor.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            meas.setTextSize(TypedValue.COMPLEX_UNIT_SP, meaSize);
            seller.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            price_dis.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            shape.setTextSize(TypedValue.COMPLEX_UNIT_SP, shapeSize);
            span2Size = spanSize;
        }
    }


}
