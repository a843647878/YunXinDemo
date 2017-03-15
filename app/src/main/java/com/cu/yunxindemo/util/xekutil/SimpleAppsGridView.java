package com.cu.yunxindemo.util.xekutil;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;


import com.cu.yunxindemo.R;
import com.cu.yunxindemo.adapter.xekadapter.AppsAdapter;
import com.cu.yunxindemo.entity.AppsMenu;
import com.cu.yunxindemo.entity.im.AppBean;
import com.cu.yunxindemo.util.ItemDecorationUtils;
import com.cu.yunxindemo.view.widget.BaseRecyclerAdapter;

import java.util.ArrayList;

public class SimpleAppsGridView extends RelativeLayout {

    protected View view;
    protected int userType;

    public SimpleAppsGridView(Context context, int userType) {
        this(context, null,userType);

    }

    public SimpleAppsGridView(Context context, AttributeSet attrs, int userType) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.view_apps, this);
        this.userType = userType;
        init();
    }

    protected BaseRecyclerAdapter.OnItemClickListener onItemClickListener;

    public BaseRecyclerAdapter.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(BaseRecyclerAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        if (adapter == null) return;
        adapter.setOnItemClickListener(this.onItemClickListener);
    }

    AppsAdapter adapter;
    RecyclerView rv_apps;

    public void setUtype(int userType){
        this.userType = userType;
        init();
    }


    protected void init() {
        rv_apps = (RecyclerView) view.findViewById(R.id.rv_apps);
        rv_apps.setLayoutManager(new GridLayoutManager(rv_apps.getContext(), 4));
        rv_apps.addItemDecoration(ItemDecorationUtils.getCommTrans10Divider(rv_apps.getContext(),true));

        ArrayList<AppBean> mAppBeanList = new ArrayList<>();

        mAppBeanList.add(new AppBean(R.mipmap.icon_photo, "图片", AppsMenu.APPS_PICTURE));
        mAppBeanList.add(new AppBean(R.mipmap.icon_photo, "拍照",AppsMenu.APPS_CAMERA));

        if (adapter == null){
            adapter = new AppsAdapter();
            rv_apps.setAdapter(adapter);
        }
        adapter.bindData(true, mAppBeanList);

    }


}
