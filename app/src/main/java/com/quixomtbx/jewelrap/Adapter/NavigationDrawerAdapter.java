package com.quixomtbx.jewelrap.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quixomtbx.jewelrap.GCM.GCMNotificationIntentService;
import com.quixomtbx.jewelrap.Global.Global_variable;
import com.quixomtbx.jewelrap.Global.SessionManager;
import com.quixomtbx.jewelrap.Model.JewelRapItem;
import com.quixomtbx.jewelrap.R;

import java.util.Collections;
import java.util.List;


public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    public static int selected_item = 0;
    List<JewelRapItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    SharedPreferences sharedPreferences;
    String count;
    static MyViewHolder holder;
    static int newscount;
    int[] title_image;

    public NavigationDrawerAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    public NavigationDrawerAdapter(Context context, List<JewelRapItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        holder = new MyViewHolder(view);
        sharedPreferences = context.getSharedPreferences(SessionManager.MyPREFERENCES, Context.MODE_PRIVATE);
        count = sharedPreferences.getString(SessionManager.NEWS_COUNT, null);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        JewelRapItem current = data.get(position);
        holder.title.setText(current.getCategoryName());
        holder.title_iv.setImageResource(current.getIcon());

        if (position == 4 || position == 3) {
            if (Global_variable.logEnabled) {
                Log.e("News count = ", "" + GCMNotificationIntentService.newsCount);
            }
            if (position == 4) {
                if (newscount > 0) {
                    holder.newaBadge.setText(String.valueOf(newscount));
                    holder.newaBadge.setVisibility(View.VISIBLE);
                } else {
                    holder.newaBadge.setVisibility(View.INVISIBLE);
                }
            } else {
                holder.newaBadge.setVisibility(View.INVISIBLE);
            }


            if (position == 3) {
                holder.solitaireSaleBadge.setVisibility(View.VISIBLE);
            } else {
                holder.solitaireSaleBadge.setVisibility(View.INVISIBLE);
            }

            /*if(HomeFragment.newsCount>0)
            {
              //  holder.newaBadge.setText(String.valueOf(GCMNotificationIntentService.newsCount));
                holder.newaBadge.setText(String.valueOf(HomeFragment.newsCount));
                holder.newaBadge.setVisibility(View.VISIBLE);
            }*/
        } else {
            holder.newaBadge.setVisibility(View.GONE);
        }


        int[] title_image = {
                R.drawable.ic_home,
                R.drawable.ic_inquiry,
                R.drawable.ic_magnify_white,
                R.drawable.ic_sale_white_24dp,
                R.drawable.ic_notification_icon_white,
                R.drawable.ic_feedback,
                R.drawable.ic_aboutus,
                R.drawable.ic_contactus,
                R.drawable.rupee,
                R.drawable.ic_profile,
                R.drawable.ic_logout
        };

        Log.e(Global_variable.LOG_TAG,"Selected_item==="+NavigationDrawerAdapter.selected_item);
        if (selected_item == 0 || selected_item == 1 || selected_item == 2 ||
                selected_item == 3 || selected_item == 4 || selected_item == 5 || selected_item == 6 ||
                selected_item == 7 || selected_item == 8 || selected_item==9 || selected_item==10) {

            if (position == selected_item) {

                holder.linearLayout.setBackgroundResource(R.color.colorPrimary);

                holder.title.setTextColor(Color.WHITE);
                if (position == 0) {
                    holder.title_iv.setImageResource(title_image[0]);
                } else if (position == 1) {
                    holder.title_iv.setImageResource(title_image[1]);
                } else if (position == 2) {
                    holder.title_iv.setImageResource(title_image[2]);
                } else if (position == 3) {
                    holder.title_iv.setImageResource(title_image[3]);
                } else if (position == 4) {
                    holder.title_iv.setImageResource(title_image[4]);
                } else if (position == 5) {
                    holder.title_iv.setImageResource(title_image[5]);
                } else if (position == 6) {
                    holder.title_iv.setImageResource(title_image[6]);
                } else if (position == 7) {
                    holder.title_iv.setImageResource(title_image[7]);
                } else if (position == 8) {
                    holder.title_iv.setImageResource(title_image[8]);
                } else if (position == 9) {
                    holder.title_iv.setImageResource(title_image[9]);
                }
                else if(position==10)
                {
                    holder.title_iv.setImageResource(title_image[8]);
                }
                else {
                    holder.title.setTextColor(Color.BLACK);
                }
            } else {
                holder.linearLayout.setBackgroundResource(R.color.white);
                holder.title.setTextColor(Color.BLACK);
                colorFilter(context,current.getIcon());
                holder.title_iv.setImageResource(current.getIcon());

            }

            if (selected_item == 4) {
                holder.newaBadge.setVisibility(View.INVISIBLE);
            }
        }

    }

    public static void setNewsCount(String count) {
        newscount = Integer.parseInt(count);
        //holder.newaBadge.setText(count);
        //holder.newaBadge.setVisibility(View.VISIBLE);

    }

    public void colorFilter(Context context, int resource) {
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
    public int getItemCount() {
        return data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, newaBadge, solitaireSaleBadge;
        ImageView title_iv;
        LinearLayout linearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            title_iv = (ImageView) itemView.findViewById(R.id.titleicon);
            newaBadge = (TextView) itemView.findViewById(R.id.newbadge);
            solitaireSaleBadge = (TextView) itemView.findViewById(R.id.solitaireSaleBadge);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.drawerlayout);

        }
    }
}
