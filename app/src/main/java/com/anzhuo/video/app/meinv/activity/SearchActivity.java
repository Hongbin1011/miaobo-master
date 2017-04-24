package com.anzhuo.video.app.meinv.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.meinv.base.BaseActivity;
import com.anzhuo.video.app.meinv.fragment.SearchRecordFragment;
import com.anzhuo.video.app.meinv.fragment.SearchResultFragment;
import com.anzhuo.video.app.meinv.manager.MyObserver;


public class SearchActivity extends BaseActivity implements View.OnClickListener, MyObserver {

    private static final int STATUS_SEARCH = 0;
    private static final int STATUS_BACK = 1;
    private static final int STATUS_NO_RESULT = 2;

    private FragmentManager mFragmentMan;
    private Fragment mContent;
    private SearchRecordFragment searchRecordFragment;
    private SearchResultFragment searchResultFragment;
    private int SEARCH_STATUS = STATUS_BACK;
    private TextView mTvSearch;
    private RelativeLayout mRlNoResult;
    private EditText mEditText;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_search;
    }

    @Override
    protected void bindViews() {
        super.bindViews();
        setupStatus();

        mFragmentMan = getSupportFragmentManager();




        mTvSearch = findViewAttachOnclick(R.id.tv_search);
        findViewAttachOnclick(R.id.search_back);
        mRlNoResult = findView(R.id.rl_no_result);
        mEditText = findView(R.id.et_search);

        searchRecordFragment = new SearchRecordFragment();
        searchResultFragment = new SearchResultFragment();
        searchRecordFragment.registObserver(searchResultFragment);
        searchRecordFragment.registObserver(this);

        mFragmentMan.beginTransaction().add(R.id.fl_container, searchRecordFragment).commit();
        mContent = searchRecordFragment;

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_search://搜索
                String key = mEditText.getText().toString();
                switchContent(mContent, searchResultFragment);
                searchResultFragment.isReload = true;
                searchResultFragment.notifySearchDataChanged(mEditText.getEditableText().toString());
                searchRecordFragment.requestData(key);
                closeKeyboard(mContext, mTvSearch);
                break;
            case R.id.search_back://返回
                onBackPressed();
                break;
        }
    }

    public void switchContent(Fragment from, final Fragment to) {
        if (mContent != to) {
            mContent = to;
            FragmentTransaction transaction = mFragmentMan.beginTransaction();
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(from).add(R.id.fl_container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
            to.onResume();
        }
    }

    //强制隐藏键盘
    public void closeKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void updata(String data) {
        mEditText.setText(data);
        switchContent(mContent, searchResultFragment);
//        SEARCH_STATUS = STATUS_BACK;
//        mTvSearch.setText("返回");
    }
}
