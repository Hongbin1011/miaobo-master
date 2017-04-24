package com.anzhuo.video.app.movie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.constant.AppServerUrl;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by husong on 2017/2/21.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHoler>{

    private Context mContext;
    private List<MovieInfo> mDatas = new ArrayList<MovieInfo>();
    private OkHttpClient client;
    private Picasso picasso;
    private OnMovieItemClickListener onMovieItemClickListener;
    private OnMovieItemClickListener2 onMovieItemClickListener2;

    public void setOnMovieItemClickListener(OnMovieItemClickListener onMovieItemClickListener) {
        this.onMovieItemClickListener = onMovieItemClickListener;
    }

    public void setOnMovieItemClickListener2(OnMovieItemClickListener2 onMovieItemClickListener) {
        this.onMovieItemClickListener2 = onMovieItemClickListener;
    }

    public MovieAdapter(Context context){
        this.mContext = context;

        client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Referer", AppServerUrl.MOVIE_REFERER)
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .build();

        picasso = new Picasso.Builder(mContext)
                .downloader(new OkHttp3Downloader(client)).build();
    }

    @Override
    public ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHoler holder = new ViewHoler(LayoutInflater.from(mContext).inflate(R.layout.movie_item, parent,
                false));
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

    public void setDatas(List<MovieInfo> datas){
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ViewHoler extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView movieImg;
        public TextView movieDate;
        public TextView movieRemark;
        public TextView movieName;

        public ViewHoler(View itemView) {
            super(itemView);

            movieImg = (ImageView) itemView.findViewById(R.id.movie_img);
            movieDate = (TextView) itemView.findViewById(R.id.movie_date);
            movieRemark = (TextView) itemView.findViewById(R.id.movie_remark);
            movieName = (TextView) itemView.findViewById(R.id.movie_name);
            itemView.setOnClickListener(this);
        }

        public void setData(int position){
            MovieInfo mi = mDatas.get(position);
//            String url = "http://img.bjshenlai.com/"+mi.getD_picthumb();
            loadPicture(mi.getD_picthumb());
//            Glide.with(mContext).load(mi.getD_picthumb()).error(R.drawable.dy1).into(movieImg);
            movieDate.setText(formartDate(mi.getD_time()));
            movieRemark.setText(mi.getD_remarks());
            movieName.setText(mi.getD_name());

        }

        private void loadPicture(String url){
            picasso.load(url).error(R.drawable.dy1).into(movieImg);
        }

        private String formartDate(String dateStr){
            SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                if (!TextUtils.isEmpty(dateStr)) {
                    date = dateFormater.parse(dateStr);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd");
            String time = "";
            if (null != date) {
                time = sdf2.format(date);
            }
            return time;
        }


        @Override
        public void onClick(View v) {
            if(onMovieItemClickListener!=null)
                onMovieItemClickListener.onMovieItemClick(v, getPosition());

            if(onMovieItemClickListener2 != null)
                onMovieItemClickListener2.onMovieItemClick(v , mDatas.get(getPosition()).getD_id());
        }
    }

    public interface OnMovieItemClickListener{
        public void onMovieItemClick(View v, int position);
    }

    public interface OnMovieItemClickListener2{
        public void onMovieItemClick(View v, String id);
    }
}
