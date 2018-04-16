package com.example.elena.shopeasy.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.elena.shopeasy.R;
import com.example.elena.shopeasy.utils.MyAnimationUtils;
import com.example.elena.shopeasy.utils.BitmapUtils;

/**
 * Created by elena on 2/4/2018.
 */

public class BrandsRecyclerViewAdapter extends
        android.support.v7.widget.RecyclerView.Adapter<BrandsRecyclerViewAdapter.BrandsViewHolder> {

    private int[] mDrawablesBrands ={
            R.drawable.barni_brand,
            R.drawable.cinniminis_brand,
            R.drawable.kinder_brand,
            R.drawable.milka_brand,
            R.drawable.nesquik_brand,
            R.drawable.nestle_brand,
            R.drawable.oreo_brand
    };

    @NonNull
    @Override
    public BrandsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.item_brand;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, parent, false);
        return new BrandsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandsViewHolder holder, int position) {
        Glide.with(holder.imageView.getContext()).load(mDrawablesBrands[position])
                .into(holder.imageView);
        BitmapUtils.blackWhiteImage(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mDrawablesBrands.length;
    }

    class BrandsViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;

        BrandsViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.brand_image);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyAnimationUtils.temporaryOpaqueView(imageView);
                    MyAnimationUtils.temporaryColorImage(imageView);
                }
            });
        }
    }

}
