package com.cu.yunxindemo.adapter;

import android.widget.TextView;

import com.cu.yunxindemo.R;
import com.cu.yunxindemo.view.widget.BaseArrayRecyclerAdapter;
import com.cu.yunxindemo.view.widget.BaseRecyclerAdapter;
import com.netease.nimlib.sdk.team.model.Team;

/**
 * Description
 * Created by chengwanying on 2017/3/15.
 * Company BeiJing guokeyuzhou
 */

public class GroupListAdapter extends BaseArrayRecyclerAdapter<Team>{
    @Override
    public int bindView(int viewtype) {
        return R.layout.item_friends;
    }


    @Override
    public void onBindHoder(ViewHolder holder, Team team, int position) {
        if (team == null) return;
        TextView tvname = holder.obtainView(R.id.name);
        tvname.setText(team.getName());
    }
}
