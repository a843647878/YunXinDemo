package com.cu.yunxindemo.adapter;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cu.yunxindemo.R;
import com.cu.yunxindemo.util.Preferences;
import com.cu.yunxindemo.util.TimeUtil;
import com.cu.yunxindemo.view.widget.BaseArrayRecyclerAdapter;
import com.cu.yunxindemo.view.widget.BaseRecyclerAdapter;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.sj.emoji.EmojiDisplay;

/**
 * Description
 * Created by chengwanying on 2017/3/15.
 * Company BeiJing guokeyuzhou
 */

public class ChatAdapter extends BaseArrayRecyclerAdapter<IMMessage> {

    @Override
    public int getItemViewType(int position) {
        IMMessage imMessage = getItem(position);
        if (imMessage != null) {
            if (imMessage.getDirect().getValue() == 0) {
                //发出去的
                return 1;
            } else {
                //收到的
                return 0;
            }
        }
        return 1;
    }

    @Override
    public int bindView(int viewtype) {
        if (viewtype == 1) {
            return R.layout.chat_item_list_right;
        } else {
            return R.layout.chat_item_list_left;
        }
    }

    @Override
    public void onBindHoder(ViewHolder holder, IMMessage imMessage, int position) {
        if (imMessage == null) return;

        TextView tv_date = holder.obtainView(R.id.chat_item_date);//时间
        ImageView img_avatar = holder.obtainView(R.id.chat_item_avatar);//用户头像
        TextView tv_chatcontent = holder.obtainView(R.id.chat_item_content_text);//文字内容
        ImageView img_chatimage = holder.obtainView(R.id.chat_item_content_image);//发送的图片
        ImageView img_sendfail = holder.obtainView(R.id.chat_item_fail);//发送失败
        ProgressBar progress = holder.obtainView(R.id.chat_item_progress);//发送进度
        RelativeLayout layout_content = holder.obtainView(R.id.chat_item_layout_content);
        TextView chat_name = holder.obtainView(R.id.chat_name);
        View length = holder.obtainView(R.id.id_recorder_length);
        TextView id_recorder_time = holder.obtainView(R.id.id_recorder_time);
        LinearLayout dynamic_linear = holder.obtainView(R.id.dynamic_linear);
        TextView dynamic_title = holder.obtainView(R.id.dynamic_title);
        ImageView dynamic_image = holder.obtainView(R.id.dynamic_image);
        TextView dynamic_details = holder.obtainView(R.id.dynamic_details);
        LinearLayout dynamic_course_linear = holder.obtainView(R.id.dynamic_course_linear);
        TextView dynamic_course_title = holder.obtainView(R.id.dynamic_course_title);
        ImageView dynamic_course_image = holder.obtainView(R.id.dynamic_course_image);
        TextView dynamic_course_details = holder.obtainView(R.id.dynamic_course_details);
        TextView id_recorder_message = holder.obtainView(R.id.id_recorder_message);


        ImageView chat_item_dot = holder.obtainView(R.id.chat_item_dot);//音频的小圆点

        holder.bindChildClick(img_chatimage);
        holder.bindChildClick(img_sendfail);
        holder.bindChildClick(length);
        holder.bindChildClick(dynamic_linear);
        holder.bindChildClick(dynamic_course_linear);


        holder.bindChildLongClick(layout_content);
        holder.bindChildLongClick(R.id.dynamic_course_linear);
        holder.bindChildLongClick(R.id.dynamic_linear);
        holder.bindChildLongClick(R.id.chat_item_content_image);
        holder.bindChildLongClick(length);

        if (position > 1) {
            if (TimeUtil.is5Minute(getData(position - 1).getTime(), imMessage.getTime())) {
                tv_date.setText(TimeUtil.getChatTime(imMessage.getTime()));
                tv_date.setVisibility(View.VISIBLE);
            } else {
                tv_date.setVisibility(View.GONE);
            }
        } else {
            tv_date.setVisibility(View.GONE);
        }

        switch (imMessage.getMsgType()) {
            case text:
                chat_item_dot.setVisibility(View.GONE);

                img_chatimage.setVisibility(View.GONE);
                tv_chatcontent.setVisibility(View.VISIBLE);
                length.setVisibility(View.GONE);
                id_recorder_time.setVisibility(View.GONE);
                dynamic_linear.setVisibility(View.GONE);
                dynamic_course_linear.setVisibility(View.GONE);
//                tv_chatcontent = UrlUtils.handleText(tv_chatcontent, textInterception(message));
//格式化emoji表情

                Spannable spannable = EmojiDisplay.filterFromResource(tv_chatcontent.getContext(),
                        new SpannableStringBuilder(imMessage.getContent()),
                        EmojiDisplay.getFontHeight(tv_chatcontent),EmojiDisplay.HEAD_NAME, null);
                tv_chatcontent.setText(spannable);

                if (getItemViewType(position) == 1) {
                    layout_content.setBackgroundResource(R.drawable.chatto_bg_normal);
                } else {
                    layout_content.setBackgroundResource(R.drawable.chatfrom_bg_normal);
                }
                break;
            case image:
                break;
            case audio:
                break;
            case undef:
                break;

        }
    }
}
