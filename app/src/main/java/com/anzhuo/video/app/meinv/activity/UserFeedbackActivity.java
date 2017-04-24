package com.anzhuo.video.app.meinv.activity;

import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.meinv.base.BaseActivity;
import com.anzhuo.video.app.meinv.manager.MyInterface;
import com.anzhuo.video.app.meinv.manager.NIManage;
import com.anzhuo.video.app.search.utils.ToastUtil;

import okhttp3.Call;

/**
 * Created by wbb on 2016/8/2.
 * 用户反馈 详情界面
 */
public class UserFeedbackActivity extends BaseActivity {

    private EditText feedbackContent;
    private EditText feedbackContact;

    @Override
    protected int getLayoutRes() {
        return R.layout.user_feedback_layout;
    }

    @Override
    protected void bindViews() {
        super.bindViews();
        setupStatus();
        setHeadTitle("用户反馈");
        feedbackContent = findView(R.id.feedback_content);
        feedbackContact = findView(R.id.feedback_contact);
        findViewAttachOnclick(R.id.feedback_submit_btn);
    }

    private void getFeedbackContent(){
        String content=feedbackContent.getText().toString().trim();
        String contact=feedbackContact.getText().toString().trim();
        if ("".equals(content)){
            ToastUtil.toastLong(mContext,"内容不能为空");
            return;
        }else {
            if(content.length()<10) {
                ToastUtil.toastLong(mContext,"内容不能少于10个字");
                return;
            }
        }
        if("".equals(contact)) {
            ToastUtil.toastLong(mContext,"联系方式不能为空");
            return;
        }
        getData(content,contact);
    }

    private void getData(String content,String contact){
        NIManage.SuggestionFeedback(contact, content, new MyInterface() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.toastLong(mContext,"意见反馈提交失败，请稍后再试。");
            }

            @Override
            public void onSucceed(int state, String data, JSONObject obj) {
                if (state==200){
                    ToastUtil.toastLong(mContext,"意见反馈提交成功，谢谢您的宝贵意见。");
                    finish();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_back://返回
                onBackPressed();
                break;
            case R.id.feedback_submit_btn://提交
                getFeedbackContent();
                break;
        }
    }
}
