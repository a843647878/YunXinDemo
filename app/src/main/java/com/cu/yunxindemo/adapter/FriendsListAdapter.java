package com.cu.yunxindemo.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.cu.yunxindemo.R;
import com.cu.yunxindemo.view.widget.BaseArrayRecyclerAdapter;
import com.cu.yunxindemo.view.widget.BaseRecyclerAdapter;

/**
 * Description
 * Created by chengwanying on 2017/3/15.
 * Company BeiJing guokeyuzhou
 */

public class FriendsListAdapter extends BaseArrayRecyclerAdapter<String>{
    @Override
    public int bindView(int viewtype) {
        return R.layout.item_friends;
    }

    @Override
    public void onBindHoder(ViewHolder holder, String s, int position) {
        if (s == null) return;
        TextView tvname = holder.obtainView(R.id.name);
        tvname.setText(s);

    }
}
