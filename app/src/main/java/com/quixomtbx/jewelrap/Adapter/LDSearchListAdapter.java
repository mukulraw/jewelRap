package com.quixomtbx.jewelrap.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quixomtbx.jewelrap.Global.Global_variable;

import com.quixomtbx.jewelrap.Listener.OnLoadMoreListener;
import com.quixomtbx.jewelrap.Model.JewelRapItem;
import com.quixomtbx.jewelrap.R;
import com.quixomtbx.jewelrap.SearchItemDetails;

import org.json.JSONObject;

import java.util.ArrayList;


public class LDSearchListAdapter extends RecyclerView.Adapter<LDSearchListAdapter.ViewHolder> {
    Context mContext;
    JewelRapItem item;
    String TAG = "JEWELRAP1";
    Activity activity;
    private ArrayList<JewelRapItem> itemList;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    static float span2Size=0.7f;
    String notApplicable="-NA-";

    public LDSearchListAdapter(Context context, ArrayList<JewelRapItem> itemList, RecyclerView recyclerView, Activity activity) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loosediamond_search_list_item, parent, false);
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
        String color, price, shade, purity, seller, detail,discount;
        final JSONObject object = item.getJsonObject();
        final String categoryName=item.getCategoryName();
        String str_shape = item.getShapeTitle();



        String size = item.getSize();
        if(size.equals("null") || size==null)
        {
            holder.size.setText(notApplicable);
        }
        else
        {
            holder.size.setText(size);
        }

        color = item.getColor();
        //Log.e("JEWELRAP"," color "+ color);
        if(color.equals("null") || color == null || color.isEmpty() ){
            holder.color.setText(notApplicable);
        }
        else {
            holder.color.setText(color);
        }
        shade = item.getShade();
        if(shade.equals("null") || shade == null ){
            holder.shade.setText(notApplicable);
        }
        else {
            holder.shade.setText(shade);
        }

        purity = item.getPurity();
        if(purity.equals("null") || purity == null || purity.isEmpty() ){
            holder.purity.setText(notApplicable);
        }
        else {
            holder.purity.setText(purity);
        }

        holder.shape.setText(item.getShapeTitle());

        price = String.valueOf(item.getPrice());

       // seller = item.getFirstName() + " " + item.getLastName();
        seller=item.getFirmname();
        discount=item.getDiscount();

        if(Global_variable.logEnabled) {
            Log.e("price search", "" + price);
            Log.e("discount search", "" + item.getDiscount());
        }
        if (!price.equals("null") && price!=null ) {
            holder.price_dis.setText(mContext.getResources().getString(R.string.rs) + " " + price);
        }
      /*  else if(!discount.equals("null") && discount!=null){
            holder.price_dis.setText(item.getDiscount() + " %");
        }*/
       /* else
        {
            holder.price_dis.setText(notApplicable);
        }
*/


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SearchItemDetails.class);
                intent.putExtra("JsonObject", object.toString());
                intent.putExtra("categoryid","2");
                intent.putExtra("categoryName",categoryName);
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
        public TextView shape, size, color,purity,shade, price_dis,sizelable,shadelable,colorlable,puritylable;
        public CardView cardView;
        public LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            shape = (TextView) itemView.findViewById(R.id.ld_adapter_Shape);
            price_dis = (TextView) itemView.findViewById(R.id.ld_adapter_PriceDiscount);
            shade = (TextView) itemView.findViewById(R.id.ld_adapter_shade);
            size=(TextView)itemView.findViewById(R.id.ld_adapter_size);
            color=(TextView)itemView.findViewById(R.id.ld_adapter_Color);
            purity=(TextView)itemView.findViewById(R.id.ld_adapter_purity);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            layout = (LinearLayout) itemView.findViewById(R.id.linlayout);
            sizelable = (TextView) itemView.findViewById(R.id.ld_adapter_sizeLabel);
            shadelable = (TextView) itemView.findViewById(R.id.ld_adapter_shadeLabel);
            colorlable = (TextView) itemView.findViewById(R.id.ld_adapter_colorLabel);
            puritylable = (TextView) itemView.findViewById(R.id.ld_adapter_purityLabel);
           /* fluorlabel = (TextView) itemView.findViewById(R.id.tvFluorLabel);
            measlabel = (TextView) itemView.findViewById(R.id.tvMeasLabel);
            sellerlabel = (TextView) itemView.findViewById(R.id.tvSellerLabel);*/




            DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
            // getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            switch(displayMetrics.densityDpi){
                case DisplayMetrics.DENSITY_LOW:
                    //set text-size for low-density devices.
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    //set text-size for medium-density devices.
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    //set text-size for high-density devices.
                    setFontSize(14,18,0.7f,10);
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                   setFontSize(14,18,0.7f,10);
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                  //  Log.e("jewelrap","xxh");
                    setFontSize(15,20,0.75f,11);
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                   // Log.e("jewelrap"," xxxh");
                    setFontSize(17,22,0.8f,12);
                    break;
                case DisplayMetrics.DENSITY_280:
                    break;
                case DisplayMetrics.DENSITY_400:
                    break;
                case DisplayMetrics.DENSITY_560:
                    break;
                case DisplayMetrics.DENSITY_TV:
                    setFontSize(18,22,0.8f,15);
                    break;
                default:

            }



        }

        public void setFontSize(int size,int shapeSize,float spanSize,int meaSize)
        {
            colorlable.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            shadelable.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            sizelable.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            puritylable.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
          /*  measlabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            sellerlabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);*/

            //size.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            /*pol.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            sym.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);*/
            purity.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            color.setTextSize(TypedValue.COMPLEX_UNIT_SP,size);
            shape.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            price_dis.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            shape.setTextSize(TypedValue.COMPLEX_UNIT_SP, shapeSize);
            span2Size=spanSize;
        }
    }


}
