package com.anzhuo.video.app.movie;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.utils.RecycleViewDivider;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by husong on 2017/2/21.
 */

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHoler>{

    private Context mContext;
    private List<RecommendFilmInfo> mDatas = new ArrayList<RecommendFilmInfo>();
    private List<ADVInfo> mBannerDatas = new ArrayList<ADVInfo>();
    private MovieAdapter.OnMovieItemClickListener2 onMovieItemClickListener;
    private onMovieBannerClickListener onMovieBannerClickListener;

    public void setOnMovieItemClickListener(MovieAdapter.OnMovieItemClickListener2 onMovieItemClickListener) {
        this.onMovieItemClickListener = onMovieItemClickListener;
    }

    public RecommendAdapter.onMovieBannerClickListener getOnMovieBannerClickListener() {
        return onMovieBannerClickListener;
    }

    public void setOnMovieBannerClickListener(RecommendAdapter.onMovieBannerClickListener onMovieBannerClickListener) {
        this.onMovieBannerClickListener = onMovieBannerClickListener;
    }

    public RecommendAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHoler holder = new ViewHoler(LayoutInflater.from(mContext).inflate(R.layout.flim_recommend_item,null));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHoler holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setDatas(List<RecommendFilmInfo> datas){
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    public void setBannerData(List<ADVInfo>  data){
        this.mBannerDatas = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ViewHoler extends RecyclerView.ViewHolder{
        public TextView mRecommendTitle;
        public RecyclerView mRecommendRecyclerView;
        public Banner mBannerView;

        public ViewHoler(View itemView) {
            super(itemView);

            mRecommendTitle = (TextView) itemView.findViewById(R.id.recommend_title);
            mRecommendRecyclerView = (RecyclerView) itemView.findViewById(R.id.itemRecyclerview);
            mBannerView = (Banner) itemView.findViewById(R.id.bannerView);
        }

        public void setData(int position){
            RecommendFilmInfo mi = mDatas.get(position);
            mRecommendTitle.setText(mi.getTitle());
            MovieAdapter movieAdapter = new MovieAdapter(mContext);

            GridLayoutManager layoutManager = new GridLayoutManager(mContext,3);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecommendRecyclerView.setLayoutManager(layoutManager);
            movieAdapter.setDatas(mi.getDatas());
            movieAdapter.setOnMovieItemClickListener2(onMovieItemClickListener);
            mRecommendRecyclerView.setAdapter(movieAdapter);
            mRecommendRecyclerView.addItemDecoration(new RecycleViewDivider(LinearLayoutManager.HORIZONTAL, 20, mContext.getResources().getColor(R.color.white)));
            mRecommendRecyclerView.addItemDecoration(new RecycleViewDivider(LinearLayoutManager.VERTICAL, 20, mContext.getResources().getColor(R.color.white)));
            mRecommendRecyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
                @Override
                public void onViewRecycled(RecyclerView.ViewHolder holder) {

                }
            });

            if(position==0 && mBannerDatas.size()>0){
                mBannerView.setVisibility(View.VISIBLE);
                mBannerView.setImageLoader(new AdImageLoader());
                mBannerView.setImages(mBannerDatas);
                mBannerView.isAutoPlay(true);
                int time = Integer.parseInt(mBannerDatas.get(position).getSeconds())*1000;
                mBannerView.setDelayTime(time);
                mBannerView.setIndicatorGravity(BannerConfig.CENTER);
                mBannerView.setOnBannerClickListener(new OnBannerClickListener() {
                    @Override
                    public void OnBannerClick(int pos) {
                        if(onMovieBannerClickListener!=null)
                            onMovieBannerClickListener.onBannerClick(mBannerDatas.get(pos-1));
                    }
                });
                mBannerView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        if(position <= mBannerDatas.size()) {
                            try{
                                int time = Integer.parseInt(mBannerDatas.get(position-1).getSeconds())*1000;
                                mBannerView.setDelayTime(time);
                            }catch (Exception e){

                            }

                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                mBannerView.start();
            }else {
                mBannerView.setVisibility(View.GONE);
            }

        }
    }

    public interface onMovieBannerClickListener{
        public void onBannerClick(ADVInfo advInfo);
    }
}
