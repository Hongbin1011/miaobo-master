package com.anzhuo.video.app.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.constant.AppServerUrl;
import com.anzhuo.video.app.message.Message;
import com.anzhuo.video.app.utils.HttpUtils;
import com.anzhuo.video.app.utils.RecycleViewDivider;
import com.anzhuo.video.app.utils.ViewUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;


/**
 * Created by husong on 2017/2/21.
 */

public class SearchFragment extends BaseFragment implements MovieAdapter.OnMovieItemClickListener {

    private EditText mEtSearchContent;
    private ImageView mIvDeleteContent;

    private RelativeLayout mRlServerResultContainer;
    private RecyclerView mGridView;

    private MovieAdapter mAdapter;
    public static final String FROM_PAGE = "fromPage";
    private List<MovieInfo> mDatas;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.search_main_layout;
    }

    @Override
    public void bindViews() {

        mStateView = findView(R.id.stateView);
        findViewAttachOnclick(R.id.iv_SearchBack);
        findViewAttachOnclick(R.id.btnSearch);
        mIvDeleteContent = findViewAttachOnclick(R.id.iv_empty_search_words);
        mEtSearchContent = findView(R.id.search_edit_baidu);
        mRlServerResultContainer = findView(R.id.serverResultContainer);
        mGridView = findView(R.id.resultGridView);
        findViewIcon(R.id.search_img_icon);
//        mHome = findViewAttachOnclick(R.id.btnHome);

        GridLayoutManager layoutManager = new GridLayoutManager(mContext,3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mGridView.setLayoutManager(layoutManager);
        mGridView.addItemDecoration(new RecycleViewDivider(LinearLayoutManager.HORIZONTAL, 20, getResources().getColor(R.color.white)));
        mGridView.addItemDecoration(new RecycleViewDivider(LinearLayoutManager.VERTICAL, 20, getResources().getColor(R.color.white)));
        mGridView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {

            }
        });

        mAdapter = new MovieAdapter(mContext);
        mAdapter.setOnMovieItemClickListener(this);
        mGridView.setAdapter(mAdapter);
        configEditText();
//        configWebView();
        DisplayUtil.showKeyboard(getActivity());
    }


    private void configEditText() {
        mEtSearchContent.setFocusable(true);
        mEtSearchContent.setFocusableInTouchMode(true);
        mEtSearchContent.addTextChangedListener(textWatcher);
        mEtSearchContent.setOnEditorActionListener(new SearchActionListener());
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            showEditTextEmpty(s.length());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onMovieItemClick(View v, int position) {
        if(mDatas!=null && mDatas.size()>0){
            MovieInfo mi = mDatas.get(position);
            if(mi!=null){
                Intent intent = new Intent();
                intent.setClass(mContext , FilmPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("d_id", mi.getD_id());
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        }
    }


    private class SearchActionListener implements TextView.OnEditorActionListener {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                if (mStateView.getViewState() == MultiStateView.VIEW_STATE_EMPTY) {
//                    mStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
//                }
                showLoadingView();
                String key = mEtSearchContent.getText().toString().replace(" ", "");

                queryResult(key);
                showEditTextEmpty(key.length());
                DisplayUtil.hideKeyboard(getActivity());
                return true;
            }
            return false;
        }
    }

    private void queryResult(String key){
        String url = AppServerUrl.MOVIE_LIST + "&keyword="+key+"&id=0";
        Log.d("husong", "url :"+url);
        HttpUtils.getStringAsync(url, "", new StringCallback() {
            @Override
            public void onError(Call call, Exception e,int id) {
                showErrorView();
            }

            @Override
            public void onResponse(String response,int id) {
                JSONObject jsonObject = JSON.parseObject(response);
                String dataJson = jsonObject.getString("data");
                mDatas = JSON.parseArray(dataJson, MovieInfo.class);
                if(mDatas == null || mDatas.size()==0){
                    showEmptyView();
                    return;
                }
                showContentView();
                mRlServerResultContainer.setVisibility(View.VISIBLE);
                mAdapter.setDatas(mDatas);
            }
        });
    }

    /**
     * 显示(隐藏)搜索输入框清除按钮
     *
     * @param length
     */
    private void showEditTextEmpty(int length) {
        if (length > 0) {
            ViewUtil.setViewVisible(mIvDeleteContent);
        } else {
            ViewUtil.setViewGone(mIvDeleteContent);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_SearchBack:
                DisplayUtil.hideKeyboard(getActivity());
                mActivity.onBackPressed();
                break;
            case R.id.iv_empty_search_words://Empty the search words
                mEtSearchContent.setText("");
                break;
            case R.id.btnSearch:
                String searchKeyword = mEtSearchContent.getText().toString();
                String hintContent = mEtSearchContent.getHint().toString();
                if (!TextUtils.isEmpty(searchKeyword.replace(" ", ""))) {
//                    if (mStateView.getViewState() == MultiStateView.VIEW_STATE_EMPTY) {
//                        mStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
//                    }
                    showLoadingView();

                    queryResult(searchKeyword);
                    DisplayUtil.hideKeyboard(getActivity());
                } else if (!TextUtils.isEmpty(hintContent.replace(" ", ""))) {
//                    queryResult(hintContent);
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mWebView.destroy();
    }



    @Override
    protected void attachAllMessage() {
        super.attachAllMessage();
//        attachMessage(com.movie.beauty.message.Message.Type.CLOSE_SEARCH_FRAGMENT);
    }

    @Override
    public void onReceiveMessage(Message message) {
        super.onReceiveMessage(message);
        switch (message.type){
//            case CLOSE_SEARCH_FRAGMENT:
//                mActivity.finish();
//                break;
        }
    }
}
