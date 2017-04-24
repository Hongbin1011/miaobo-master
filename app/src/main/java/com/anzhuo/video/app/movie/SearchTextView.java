package com.anzhuo.video.app.movie;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.config.VideoApplication;
import com.anzhuo.video.app.constant.AppServerUrl;
import com.anzhuo.video.app.utils.ViewUtil;

import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by husong on 2017/2/21.
 */

public class SearchTextView extends LinearLayout implements View.OnClickListener {
    private TextView mContentText;
    static List<String> topSearchTerms;//热门搜索词
    private int number = 0;
    private static final int SWITCH_TEXT  = 0x11;
    private static final int SWITCH_TEXT_DELAY = 5000;
    private static int statisticsCount = 0;
    public static final String TOP_SEARCH_WORD = "top_search_word";
    private String fromWhere;
    private String shareUrl;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SWITCH_TEXT:
                    sendEmptyMessageDelayed(SWITCH_TEXT,SWITCH_TEXT_DELAY);
                    if(topSearchTerms == null||topSearchTerms.size()==0||mContentText==null){
                        return;
                    }
                    String topWord = topSearchTerms.get(number%topSearchTerms.size());
                    statisticsCount = number++;
                    if (TextUtils.isEmpty(topWord))
                        return;
                    setText(topWord);
                    break;
                default:
                    break;
            }
        }
    };
    public SearchTextView(final Context context) {
        this(context, null, 0);
    }

    public SearchTextView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchTextView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupViews();
    }

    private void setupViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.search_text_view, this);
        mContentText = ViewUtil.findViewAttachOnclick(this, R.id.tvSearchText, this);
        TextView searchImg = ViewUtil.findViewAttachOnclick(this, R.id.search_img, this);
        ImageView shard = ViewUtil.findViewAttachOnclick(this, R.id.title_share, this);
        ImageView back = ViewUtil.findViewAttachOnclick(this, R.id.search_back, this);
        searchImg.setTypeface(ViewUtil.setIconFont(VideoApplication.getContext()));
        handler.sendEmptyMessage(SWITCH_TEXT);
        number = statisticsCount;
    }

    @Override
    public void onClick(final View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tvSearchText:
                //TODO 跳转至搜索
//                if (topSearchTerms!=null&&topSearchTerms.size()>0){
//                    String topWord = topSearchTerms.get(number%topSearchTerms.size());
//                    if (!TextUtils.isEmpty(topWord)){
//                        Bundle bundle = new Bundle();
//                        bundle.putString(TOP_SEARCH_WORD,topWord.trim());
//                        PageSwitcher.switchToPage(getContext(), FragmentFactory.FRAGMENT_TYPE_SEARCH);
//                        break;
//                    }
//                }
                Bundle bundle = new Bundle();
                bundle.putString(SearchFragment.FROM_PAGE,fromWhere);
                PageSwitcher.switchToPage(getContext(), FragmentFactory.FRAGMENT_TYPE_SEARCH,bundle);
                break;

            case R.id.title_share:
                final AlertDialog myDialog = new AlertDialog.Builder(getContext(), 3).create();
                myDialog.show();
                myDialog.getWindow().setContentView(R.layout.movie_share_dialog);
                final TextView textView = (TextView) myDialog.getWindow().findViewById(R.id.movie_share_url);
                myDialog.getWindow()
                        .findViewById(R.id.ll_copy)
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ClipboardManager myClipboard = (ClipboardManager)getContext().getSystemService(CLIPBOARD_SERVICE);
                                String text = textView.getText().toString();
                                ClipData myClip = ClipData.newPlainText("text", text);
                                myClipboard.setPrimaryClip(myClip);
                                myDialog.dismiss();
                            }
                        });

                textView.setText(TextUtils.isEmpty(AppServerUrl.SHARE_URL)? "": AppServerUrl.SHARE_URL);
                break;

            case R.id.search_back:
                if(mActivity!=null)
                    mActivity.finish();
                break;

        }

    }

    public static List<String> getTopSearchTerms() {
        return topSearchTerms;
    }

    public static void setTopSearchTerms(List<String> topSearchs) {
        SearchTextView.topSearchTerms = topSearchs;
    }

    public void setText(String text) {
        mContentText.setText(text);
    }

    public void setFromWhere(String text){
        fromWhere = text;
    }

    public void setShareUrl(String url){
        this.shareUrl = url;
    }

    public Activity mActivity;

}
