package com.cu.yunxindemo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cu.yunxindemo.R;
import com.cu.yunxindemo.adapter.FriendsListAdapter;
import com.cu.yunxindemo.adapter.GroupListAdapter;
import com.cu.yunxindemo.view.widget.BaseRecyclerAdapter;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;

import java.util.List;

/**
 * Description
 * Created by chengwanying on 2017/3/15.
 * Company BeiJing guokeyuzhou
 */

public class GroupList extends Activity {

    RecyclerView recyclerView;
    GroupListAdapter groupListAdapter;

    public static void launch(Context context) {
        Intent intent = new Intent(context, GroupList.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_main);
        initView(this);
    }


    public void initView(Activity activity){
        recyclerView = (RecyclerView) activity.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        groupListAdapter = new GroupListAdapter();
        groupListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerAdapter adapter, BaseRecyclerAdapter.ViewHolder holder, View view, int position) {
                groupListAdapter = (GroupListAdapter) adapter;
                GroupChatActivity.launch(GroupList.this,groupListAdapter.getItem(position).getId());
            }
        });
        recyclerView.setAdapter(groupListAdapter);

        initData();

    }

    public void initData(){
        List<Team> teams = NIMClient.getService(TeamService.class).queryTeamListBlock();
        groupListAdapter.addItems(teams);
        groupListAdapter.notifyDataSetChanged();
    }


}
