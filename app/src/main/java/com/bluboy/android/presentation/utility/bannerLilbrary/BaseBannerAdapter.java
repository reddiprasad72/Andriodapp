package com.bluboy.android.presentation.utility.bannerLilbrary;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bluboy.android.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class BaseBannerAdapter extends RecyclerView.Adapter<BaseBannerAdapter.MzViewHolder> {

    private Context context;
    private List<String> urlList;
    private BannerLayout.OnBannerItemClickListener onBannerItemClickListener;

    public BaseBannerAdapter(Context context, List<String> urlList) {
        this.context = context;
        this.urlList = urlList;
    }

    public void setOnBannerItemClickListener(BannerLayout.OnBannerItemClickListener onBannerItemClickListener) {
        this.onBannerItemClickListener = onBannerItemClickListener;
    }

    @Override
    public MzViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_banner, viewGroup, false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) viewGroup.getContext())
                .getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        width = (int) (width * 0.7);

        view.setLayoutParams(new RecyclerView.LayoutParams(
                width, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new MzViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MzViewHolder holder, final int position) {
        if (urlList == null || urlList.isEmpty())
            return;
        final int P = position % urlList.size();

        String url = urlList.get(P);
        ImageView img = holder.imageView;
        Glide.with(context).load(url).into(img);

        img.setOnClickListener(v -> {
            if (onBannerItemClickListener != null) {
                onBannerItemClickListener.onItemClick(P);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (urlList != null) {
            return urlList.size();
        }
        return 0;
    }

    class MzViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CardView cardView;

        MzViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
