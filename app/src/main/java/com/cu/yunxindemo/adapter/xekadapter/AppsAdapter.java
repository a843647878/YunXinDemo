package com.cu.yunxindemo.adapter.xekadapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.cu.yunxindemo.R;
import com.cu.yunxindemo.entity.im.AppBean;
import com.cu.yunxindemo.view.widget.BaseArrayRecyclerAdapter;
import com.cu.yunxindemo.view.widget.BaseRecyclerAdapter;


public class AppsAdapter extends BaseArrayRecyclerAdapter<AppBean> {

    @Override
    public void onBindHoder(BaseRecyclerAdapter.ViewHolder holder, AppBean appBean, int position) {
        if (appBean == null) return;
        ImageView iv_icon = holder.obtainView(R.id.iv_icon);
        TextView tv_name = holder.obtainView(R.id.tv_name);
        iv_icon.setBackgroundResource(appBean.getIcon());
        tv_name.setText(appBean.getFuncName());
    }

    @Override
    public int bindView(int viewtype) {
        return R.layout.item_app;
    }

}