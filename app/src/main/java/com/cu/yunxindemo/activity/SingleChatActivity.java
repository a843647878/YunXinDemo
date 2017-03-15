package com.cu.yunxindemo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cu.yunxindemo.R;
import com.cu.yunxindemo.adapter.ChatAdapter;
import com.cu.yunxindemo.adapter.xekadapter.AppsAdapter;
import com.cu.yunxindemo.entity.AppsMenu;
import com.cu.yunxindemo.entity.im.AppBean;
import com.cu.yunxindemo.util.LogUtils;
import com.cu.yunxindemo.util.T;
import com.cu.yunxindemo.util.xekutil.Constants;
import com.cu.yunxindemo.util.xekutil.SimpleAppsGridView;
import com.cu.yunxindemo.util.xekutil.SimpleCommonUtils;
import com.cu.yunxindemo.view.refresh.MaterialRefreshLayout;
import com.cu.yunxindemo.view.refresh.MaterialRefreshListener;
import com.cu.yunxindemo.view.widget.BaseRecyclerAdapter;
import com.netease.nimlib.sdk.InvocationFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.sj.emoji.DefEmoticons;
import com.sj.emoji.EmojiBean;
import com.sj.emoji.EmojiDisplay;
import com.sj.emoji.EmojiParse;
import com.sj.emoji.EmojiSpan;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

import sj.keyboard.XhsEmoticonsKeyBoard;
import sj.keyboard.adpater.EmoticonsAdapter;
import sj.keyboard.adpater.PageSetAdapter;
import sj.keyboard.data.EmoticonEntity;
import sj.keyboard.data.EmoticonPageEntity;
import sj.keyboard.data.EmoticonPageSetEntity;
import sj.keyboard.interfaces.EmoticonClickListener;
import sj.keyboard.interfaces.EmoticonDisplayListener;
import sj.keyboard.interfaces.EmoticonFilter;
import sj.keyboard.interfaces.PageViewInstantiateListener;
import sj.keyboard.utils.EmoticonsKeyboardUtils;
import sj.keyboard.utils.imageloader.ImageBase;
import sj.keyboard.widget.EmoticonPageView;
import sj.keyboard.widget.EmoticonsEditText;
import sj.keyboard.widget.FuncLayout;

/**
 * Description
 * Created by chengwanying on 2017/3/15.
 * Company BeiJing guokeyuzhou
 * 单聊界面
 */

public class SingleChatActivity extends Activity implements Observer<List<IMMessage>> ,FuncLayout.OnFuncKeyBoardListener,BaseRecyclerAdapter.OnItemChildClickListener,
        BaseRecyclerAdapter.OnItemClickListener{
    public static final String KEY_SESSIONID = "sessionId";

    private static final String ACTION_SCROLL_TO_POS = "action_scroll_to_pos";


    public static void launch(Context context,String sessionId){
        if (sessionId == null) return;
        Intent intent = new Intent(context,SingleChatActivity.class);
        intent.putExtra(KEY_SESSIONID,sessionId);
        context.startActivity(intent);
    }


    XhsEmoticonsKeyBoard ekBar;
    LinearLayoutManager linearLayoutManager;

    MaterialRefreshLayout refresh;
    RecyclerView chatRecyclerview;

    String sessionId;

    ChatAdapter chatAdapter;
    SimpleAppsGridView simpleAppsGridView;

    List<IMMessage> datas = new ArrayList<IMMessage>();

    AppsAdapter adapterr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_main);
        sessionId = getIntent().getStringExtra(KEY_SESSIONID);
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(this, true);
        initView(this);
    }

    GestureDetector gestureDetector;
    GestureDetector.SimpleOnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            LogUtils.d("----------------1");
            if (ekBar != null) {// && ekBar.isSoftKeyboardPop()
                ekBar.reset();
            }
            return super.onSingleTapUp(e);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            LogUtils.d("-------------------------3");
            super.onShowPress(e);
        }
    };

    public void initView(Activity activity) {
        ekBar = (XhsEmoticonsKeyBoard) activity.findViewById(R.id.ek_bar);
        refresh = (MaterialRefreshLayout) activity.findViewById(R.id.refresh);
        chatRecyclerview = (RecyclerView) activity.findViewById(R.id.chat_recyclerview);
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                getData();
            }
        });
        linearLayoutManager = new LinearLayoutManager(this);
        chatRecyclerview.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter();
        gestureDetector = new GestureDetector(this, onGestureListener);
        chatRecyclerview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
        chatAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerAdapter adapter, BaseRecyclerAdapter.ViewHolder holder, View view, int position) {
                if (ekBar != null) {// && ekBar.isSoftKeyboardPop()
                    ekBar.reset();
                }
            }
        });

        chatRecyclerview.setAdapter(chatAdapter);
        initEmoticonsKeyBoardBar();
        getData();

    }

    public void getData(){
        if (chatAdapter.getItemCount() > 0) {
            IMMessage message = chatAdapter.getItem(0);
            NIMClient.getService(MsgService.class).queryMessageListEx(message, QueryDirectionEnum.QUERY_NEW, 10, true).setCallback(new RequestCallback<List<IMMessage>>() {
                 @Override
                 public void onSuccess(List<IMMessage> param) {
                     chatAdapter.addItems(0, param);
                     refresh.finishRefresh();
                 }

                 @Override
                 public void onFailed(int code) {

                 }

                 @Override
                 public void onException(Throwable exception) {

                 }
             });
        } else {
            NIMClient.getService(MsgService.class).queryMessageList(sessionId, SessionTypeEnum.P2P, 0, 10).setCallback(new RequestCallback<List<IMMessage>>() {
                @Override
                public void onSuccess(List<IMMessage> param) {
                    chatAdapter.addItems(param);
                    refresh.finishRefresh();
                    scrollToBottomSend();
                }

                @Override
                public void onFailed(int code) {

                }

                @Override
                public void onException(Throwable exception) {

                }
            });
        }

    }


    //初始化底部键盘
    private void initEmoticonsKeyBoardBar() {
        SimpleCommonUtils.initEmoticonsEditText(ekBar.getEtChat());
        ekBar.setAdapter(SimpleCommonUtils.getCommonAdapter(this, emoticonClickListener));
        ekBar.addOnFuncKeyBoardListener(this);
        simpleAppsGridView = new SimpleAppsGridView(this, 1);
        simpleAppsGridView.setOnItemClickListener(this);
        ekBar.addFuncView(simpleAppsGridView);

        ekBar.getEtChat().setOnSizeChangedListener(new EmoticonsEditText.OnSizeChangedListener() {
            @Override
            public void onSizeChanged(int w, int h, int oldw, int oldh) {
                scrollToBottomSend();
            }
        });
        ekBar.getBtnSend().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextMessage(ekBar.getEtChat().getText().toString());
                ekBar.getEtChat().setText("");
            }
        });

//        ekBar.getBtnVoice().setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
//            @Override
//            public void onPermission() {
//                audioPermision();
//            }
//
//            @Override
//            public void onFinish(float seconds, String filePath) {
//                sendVoiceMessage(filePath, (int) seconds);
//            }
//        });

    }




    EmoticonClickListener emoticonClickListener = new EmoticonClickListener() {
        @Override
        public void onEmoticonClick(Object o, int actionType, boolean isDelBtn) {
            if (isDelBtn) {
                SimpleCommonUtils.delClick(ekBar.getEtChat());
            } else {
                if (o == null) {
                    return;
                }
                if (actionType == Constants.EMOTICON_CLICK_BIGIMAGE) {
                    if (o instanceof EmoticonEntity) {
                        sendImageMessage(((EmoticonEntity) o).getIconUri());
                    }
                } else {
                    String content = null;
                    if (o instanceof EmojiBean) {
                        content = ((EmojiBean) o).emoji;
                    } else if (o instanceof EmoticonEntity) {
                        content = ((EmoticonEntity) o).getContent();
                    }

                    if (TextUtils.isEmpty(content)) {
                        return;
                    }
                    int index = ekBar.getEtChat().getSelectionStart();
                    Editable editable = ekBar.getEtChat().getText();
                    editable.insert(index, content);
                }
            }
        }
    };



    //发送消息方法
    //==========================================================================

    //发送文本消息
    protected void sendTextMessage(String content) {
        if (TextUtils.isEmpty(content)) return;
        IMMessage message = MessageBuilder.createTextMessage(
                sessionId, // 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
                SessionTypeEnum.P2P, // 聊天类型，单聊或群组
                content // 文本内容
        );
        sendMessage(message);
    }



    //发送语音消息
    protected void sendVoiceMessage(String filePath, int length) {
        if (TextUtils.isEmpty(filePath)) return;
        File file = new File(filePath);
        IMMessage message = MessageBuilder.createAudioMessage(
                sessionId, // 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
                SessionTypeEnum.P2P, // 聊天类型，单聊或群组
                file, // 音频文件
                length // 音频持续时间，单位是ms
        );
        sendMessage(message);
    }

    protected void sendImageMessage(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {
            String fileString = "file:///";
            if (!imagePath.startsWith(fileString)) {
                imagePath = String.format("%s%s", fileString, imagePath);
            }
            File file = new File(imagePath);
            IMMessage message = MessageBuilder.createImageMessage(
                    sessionId, // 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
                    SessionTypeEnum.P2P, // 聊天类型，单聊或群组
                    file, // 图片文件对象
                    null // 文件显示名字，如果第三方 APP 不关注，可以为 null
            );
            sendMessage(message);
        }
    }



    protected void sendMessage(final IMMessage message) {

        NIMClient.getService(MsgService.class).sendMessage(message,true).setCallback(new RequestCallback<Void>() {
            private void updateSingleItem() {
                for (int i = chatAdapter.getItemCount() - 1; i >= 0; i--) {
                    IMMessage emMessage1 = chatAdapter.getItem(i);
                    if (emMessage1 != null) {
                        LogUtils.d("----->emMessage1:" + emMessage1.getUuid() + "-----emMessage:" + message.getUuid());
                        if (TextUtils.equals(emMessage1.getUuid(), message.getUuid())) {
                            chatAdapter.updateItem(message);
                            LogUtils.d("---------------->更新成功");
                            break;
                        }
                    }
                }
            }
            @Override
            public void onSuccess(Void param) {
                updateSingleItem();
                LogUtils.d("---------------->发送成功");
            }

            @Override
            public void onFailed(int code) {
                updateSingleItem();
            }

            @Override
            public void onException(Throwable exception) {

            }
        });

        datas.add(message);
        chatAdapter.addItem(message);
        scrollToBottomSend();
    }


    private void scrollToBottom() {
        if (chatRecyclerview == null) return;
        int lastChildViewPosition = linearLayoutManager.findLastVisibleItemPosition();
        LogUtils.d("------------->lastChildViewPosition:" + lastChildViewPosition + "--------->" + chatAdapter.getItemCount());
        if (chatAdapter.getItemCount() - lastChildViewPosition < 3) {
            chatRecyclerview.scrollToPosition(chatAdapter.getItemCount() - 1);
        }
//        chatRecyclerview.scrollToPosition(adapter.getItemCount() - 1);
    }

    private void scrollToBottomSend() {
        if (chatRecyclerview == null) return;
        chatRecyclerview.scrollToPosition(chatAdapter.getItemCount() - 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //接收消息
    @Override
    public void onEvent(List<IMMessage> imMessages) {
        // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
        chatAdapter.addItems(imMessages);
        scrollToBottom();


        //导入消息到数据库
//        NIMClient.getService(MsgService.class).saveMessageToLocal(imMessages,true);
    }

    @Override
    public void OnFuncPop(int i) {
        scrollToBottom();
    }

    @Override
    public void OnFuncClose() {

    }

    //底部加号点击事件
    @Override
    public void onItemClick(BaseRecyclerAdapter adapter, BaseRecyclerAdapter.ViewHolder holder, View view, int position) {
        adapterr = (AppsAdapter) adapter;
        if (adapterr == null) return;
        AppBean appBean = adapterr.getItem(position);
        T.showShort("点击事件");
        /*
        * 事件分发
        * */
//        switch (appBean.getId()) {
//            case AppsMenu.APPS_PICTURE://图册
//                break;
//            case AppsMenu.APPS_CAMERA://相机
//                break;
//        }
    }

    @Override
    public void onItemChildClick(BaseRecyclerAdapter adapter, BaseRecyclerAdapter.ViewHolder holder, View view, int position) {

    }
}
