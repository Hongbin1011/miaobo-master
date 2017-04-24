/*
package com.anzhuo.video.app.ui.fragment;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.ui.adapter.SearchNoteAdapter;
import com.anzhuo.video.app.ui.base.BaseFragment;
import com.anzhuo.video.app.utils.ViewUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;


*/
/**
 * A simple {@link Fragment} subclass.
 *//*

public class SearchFragment extends BaseFragment implements View.OnClickListener {

    private static final int STATUS_SEARCH = 0;
    private static final int STATUS_BACK = 1;
    private static final int STATUS_NO_RESULT = 2;

    private FragmentManager mFragmentMan;
    private Fragment mContent;
    private int SEARCH_STATUS = STATUS_BACK;
    private TextView mTvSearch;
    private RelativeLayout mRlNoResult;
    private EditText mEditText;
    private RecyclerView mRv_search_note;
    private RecyclerView mRv_search_come;
    private ArrayList mSearchNoteList;


    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_search;
    }

    @Override
    public void bindViews() {
        mFragmentMan = getActivity().getSupportFragmentManager();
        mTvSearch = ViewUtil.findView(mRootView, R.id.tv_search);
        mTvSearch.setOnClickListener(this);
        mEditText = ViewUtil.findView(mRootView, R.id.et_search);//输入框
        mEditText.setOnClickListener(this);
        mRv_search_note = ViewUtil.findView(mRootView, R.id.rv_take_note); //记录rv
        mRv_search_come = ViewUtil.findView(mRootView, R.id.rv_search_come);  //收索结果
        mSearchNoteList = new ArrayList();//收索记录list

        RecyclerViewSearchNote();//收索记录
        RecyclerViewSearchDone();//收索结果
        //监听键盘的 收索键
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    Logger.i("cq=============[IME_ACTION_SEARCH]===========");
                    SearchClick();//收索点击
                    return true;
                }
                return false;
            }
        });
    }

    */
/**
     * 收索记录
     *//*

    private void RecyclerViewSearchNote() {
        mRv_search_note.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.HORIZONTAL));
        mRv_search_note.setAdapter(new SearchNoteAdapter(mSearchNoteList));
    }

    */
/**
     * 收索结果
     *//*

    private void RecyclerViewSearchDone() {

    }

    */
/**
     * 懒加载
     *//*

    @Override
    public void onLazyLoad() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search:
                SearchClick();//收索点击
                break;
        }

    }

    */
/**
     * 收索按钮
     *//*

    private void SearchClick() {

    }
}
*/
