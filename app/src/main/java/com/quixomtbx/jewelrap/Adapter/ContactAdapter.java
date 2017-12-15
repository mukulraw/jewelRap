package com.quixomtbx.jewelrap.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quixomtbx.jewelrap.Global.Global_variable;
import com.quixomtbx.jewelrap.Model.JewelRapItem;
import com.quixomtbx.jewelrap.R;

import java.util.ArrayList;


public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    Context mcontext;
    Activity mActivity;
    private ArrayList<JewelRapItem> mItems;


    public ContactAdapter(Context context, ArrayList<JewelRapItem> jewellist) {
        mcontext = context;
        this.mItems = jewellist;
        this.mActivity = (Activity) context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        ViewHolder vh=null;

            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.contactus_item, parent, false);
            vh = new ViewHolder(v,mcontext);
            return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            JewelRapItem jitem = mItems.get(position);
            final String id = jitem.getId();
            holder.phoneTextview.setText(jitem.getMobile().toString());
            holder.emailTextview.setText(jitem.getEmail().toString());
            holder.websiteTextview.setText(jitem.getWebsite().toString());
            if(Global_variable.logEnabled) {
                Log.e("website", " " + jitem.getWebsite().toString());
            }
            holder.addressTextview.setText(jitem.getAddress().toString());

        }


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView phoneTextview, emailTextview, websiteTextview,addressTextview;
        LinearLayout linearLayoutMail,linearWebsite,linearPhone;
        ImageView IVphone,IVweb;
        Context context;
        public ViewHolder(View itemView,Context context) {
            super(itemView);
            phoneTextview = (TextView) itemView.findViewById(R.id.textview_phone);
            emailTextview = (TextView) itemView.findViewById(R.id.tv_userEmail_ordDetail);
            websiteTextview = (TextView) itemView.findViewById(R.id.tv_userWebsite);
            addressTextview = (TextView) itemView.findViewById(R.id.tv_userAddress);
            linearLayoutMail = (LinearLayout)itemView.findViewById(R.id.ll_inquiry_detail_mail);
            linearWebsite=(LinearLayout)itemView.findViewById(R.id.linear_website);
            linearPhone=(LinearLayout)itemView.findViewById(R.id.linear_phone);
            IVphone = (ImageView)itemView.findViewById(R.id.iv_inquiry_detail_phone);
            IVweb=(ImageView)itemView.findViewById(R.id.iv_web);
            IVphone.setOnClickListener(this);
            IVweb.setOnClickListener(this);
           linearLayoutMail.setOnClickListener(this);
            linearPhone.setOnClickListener(this);
            linearWebsite.setOnClickListener(this);
            this.context=context;

            colorFilter(context,R.drawable.ic_phone_black_24dp);
            colorFilter(context,R.drawable.ic_gmail_black_24dp);
            colorFilter(context,R.drawable.ic_web_black);
            colorFilter(context,R.drawable.ic_map_marker_black_24dp);


        }

        public void colorFilter(Context context, int resource)
        {
            int iColor = Color.parseColor("#747474");

            int red = (iColor & 0xFF0000) / 0xFFFF;
            int green = (iColor & 0xFF00) / 0xFF;
            int blue = iColor & 0xFF;

            float[] matrix = {0, 0, 0, 0, red,
                    0, 0, 0, 0, green,
                    0, 0, 0, 0, blue,
                    0, 0, 0, 1, 0};

            Drawable mDrawable = ContextCompat.getDrawable(context, resource);
            ColorFilter colorFilter = new ColorMatrixColorFilter(matrix);
            mDrawable.setColorFilter(colorFilter);


        }
        @Override
        public void onClick(View v) {
            Intent intent,chooser;
            switch (v.getId())
            {
                /*case R.id.textview_phone:
                    intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+phoneTextview.getText().toString()));
                    context.startActivity(intent);
                    break;*/

                case  R.id.linear_phone:
                    intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+phoneTextview.getText().toString()));
                    context.startActivity(intent);
                    break;

                case R.id.ll_inquiry_detail_mail:
                    String email = emailTextview.getText().toString().trim();
                    intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", email, null));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                    intent.putExtra(Intent.EXTRA_TEXT, "Body");
                    context.startActivity(Intent.createChooser(intent, "Send email..."));
                    break;

                /*case R.id.linear_website:
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+websiteTextview.getText().toString()));
                    chooser = Intent.createChooser(intent, "Open with");
                    context.startActivity(chooser);
                    break;
*/
                case R.id.linear_website:
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteTextview.getText().toString()));
                    if(Global_variable.logEnabled) {
                        Log.e("***website", "" + websiteTextview.getText().toString());
                    }
                    chooser = Intent.createChooser(intent, "Open with");
                    context.startActivity(chooser);
                    break;
            }
        }
    }


}

