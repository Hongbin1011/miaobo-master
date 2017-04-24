package com.anzhuo.video.app.manager.fuli2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.constant.Constants;
import com.anzhuo.video.app.search.entity.FoundInfo;
import com.anzhuo.video.app.search.load.ImageLoaderManager;
import com.anzhuo.video.app.utils.DisplayUtil;

import java.util.List;


/**
 * Created by husong on 2017/2/20.
 */

public class FoundItemHorizontalAdapter extends RecyclerView.Adapter<FoundItemHorizontalAdapter.ViewHoler> {

    private Context mContext;
    private List<FoundInfo> mDatas;
    public FoundItemVerticalAdapter.OnFoundItemClickListener mListener;

    public FoundItemHorizontalAdapter(Context context, List<FoundInfo> data, FoundItemVerticalAdapter.OnFoundItemClickListener listener){
        this.mContext = context;
        this.mDatas = data;
        this.mListener = listener;
    }

    @Override
    public ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_found_item_horizontal, parent,
                false);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Constants.Pwide/ 2, DisplayUtil.dp2px(mContext, 60f));
//        lp.leftMargin = DisplayUtil.dip2px(mContext, 10f);
        view.setLayoutParams(lp);
        FoundItemHorizontalAdapter.ViewHoler holder = new FoundItemHorizontalAdapter.ViewHoler(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHoler holder, int position) {
        holder.setDatas(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHoler extends RecyclerView.ViewHolder{

        private ImageView mImageView;
        private TextView mTextView;

        public ViewHoler(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.vertical_img);
            mTextView = (TextView) itemView.findViewById(R.id.vertical_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.OnFoundItemClick(mDatas.get(getPosition()));
                }
            });
        }

        public void setDatas(int position){
            FoundInfo fi = mDatas.get(position);
            ImageLoaderManager.displayImageByUrl(mContext, mImageView, fi.getLogo_url(), R.drawable.logo);
            mTextView.setText(fi.getName());
        }
    }
}