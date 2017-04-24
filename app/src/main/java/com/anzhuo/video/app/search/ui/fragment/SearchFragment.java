package com.anzhuo.video.app.search.ui.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.search.utils.ToastUtil;
import com.anzhuo.video.app.utils.ViewUtil;
import com.anzhuo.video.app.widget.MyObserver;
import com.orhanobut.logger.Logger;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements View.OnClickListener, MyObserver {
    public static final String FROM_PAGE = "fromPage";

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

    private View mViewContainer;

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, null);
        this.mViewContainer = view;
        bindViews();
        return view;
    }

    //
    private void bindViews() {
//        setupStatus();

//        mFragmentMan = getActivity().getSupportFragmentManager();
        mFragmentMan = getChildFragmentManager();

        mTvSearch = (TextView) mViewContainer.findViewById(R.id.tv_search);
        mTvSearch.setOnClickListener(this);
//        mViewContainer.findViewById(R.id.search_back).setOnClickListener(this);
        mRlNoResult = (RelativeLayout) mViewContainer.findViewById(R.id.rl_no_result);
//        mEditText = (EditText) mViewContainer.findViewById(R.id.et_search);
        mEditText = ViewUtil.findView(mViewContainer, R.id.et_search);

//        mEditText.setFocusable(true);
        mEditText.requestFocus();
//        mEditText.setFocusable(true);
//        mEditText.setFocusableInTouchMode(true);//
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("cq=============[收索了没有3423434234]===========");
//                DisplayUtil.showKeyboard(getActivity());
                showSoftInputFromWindow(getActivity(), mEditText);
            }
        });

//        mEditText.setOnClickListener(new NoDoubleClickListener() {
//            @Override
//            public void onNoDoubleClick(View view) {
//
//            }
//        });

        searchRecordFragment = new SearchRecordFragment();
        searchResultFragment = new SearchResultFragment();
        searchRecordFragment.registObserver(searchResultFragment);
        searchRecordFragment.registObserver(this);
        mFragmentMan.beginTransaction().add(R.id.fl_container, searchRecordFragment).commit();
        mContent = searchRecordFragment;

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    Logger.i("cq=============[收索了没有]===========");
                    SearchButtonClick();//收索点击事件
                    return true;
                }
                return false;
            }
        });


        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String editText = s.toString();
                if ("".equals(editText)) {
                    switchContent(mContent, searchRecordFragment);
                }
            }
        });
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        // DisplayUtil.showKeyboard(activity);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* public void setupStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (StatusBarUtil.MIUISetStatusBarLightMode(getActivity().getWindow(), true)) {
                XUtilLog.log_i("leibown", "miui");//小米
            } else if (StatusBarUtil.FlymeSetStatusBarLightMode(getActivity().getWindow(), true)) {
                XUtilLog.log_i("leibown", "flyme");//魅族
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                XUtilLog.log_i("leibown", ">6.0");
            } else {
                XUtilLog.log_i("leibown", ">4.0");
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search://搜索
                SearchButtonClick();//搜索点击事件
                break;
        }
    }

    /**
     * 搜索 点击事件
     */
    private void SearchButtonClick() {
        String key = mEditText.getText().toString();
        if ("".equals(key)) {
            ToastUtil.toastShort("输入框不能为空");
            return;
        }
        switchContent(mContent, searchResultFragment);
        searchResultFragment.isReload = true;
        searchResultFragment.notifySearchDataChanged(mEditText.getEditableText().toString(), false);
        searchRecordFragment.requestData(key);
        closeKeyboard(getContext(), mTvSearch);
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
        mEditText.setSelection(data.length());//将光标移至文字末尾
        switchContent(mContent, searchResultFragment);
//        SEARCH_STATUS = STATUS_BACK;
//        mTvSearch.setText("返回");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (mEditText != null) {
                mEditText.setText("");
            }
        }
    }
}
