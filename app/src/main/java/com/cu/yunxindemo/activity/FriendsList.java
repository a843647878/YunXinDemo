package com.cu.yunxindemo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cu.yunxindemo.R;
import com.cu.yunxindemo.adapter.FriendsListAdapter;
import com.cu.yunxindemo.view.widget.BaseRecyclerAdapter;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.friend.FriendService;

import java.util.List;

/**
 * Description
 * Created by chengwanying on 2017/3/15.
 * Company BeiJing guokeyuzhou
 */

public class FriendsList extends Activity {

    RecyclerView recyclerView;
    FriendsListAdapter friendsListAdapter;

    public static void launch(Context context) {
        Intent intent = new Intent(context, FriendsList.class);
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
        friendsListAdapter = new FriendsListAdapter();
        friendsListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerAdapter adapter, BaseRecyclerAdapter.ViewHolder holder, View view, int position) {
                friendsListAdapter = (FriendsListAdapter) adapter;
                String name = ((FriendsListAdapter) adapter).getItem(position);
                SingleChatActivity.launch(FriendsList.this,name);
            }
        });
        recyclerView.setAdapter(friendsListAdapter);

        initData();

    }

    public void initData(){
        List<String> friends = NIMClient.getService(FriendService.class).getFriendAccounts();
        friendsListAdapter.addItems(friends);
        friendsListAdapter.notifyDataSetChanged();
    }


}
