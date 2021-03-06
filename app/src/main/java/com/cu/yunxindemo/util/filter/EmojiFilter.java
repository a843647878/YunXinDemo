package com.cu.yunxindemo.util.filter;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.widget.EditText;

import com.sj.emoji.EmojiDisplay;
import com.sj.emoji.EmojiSpan;

import java.util.regex.Matcher;

import sj.keyboard.interfaces.EmoticonFilter;
import sj.keyboard.utils.EmoticonsKeyboardUtils;

public class EmojiFilter extends EmoticonFilter {

    private int emojiSize = -1;

    @Override
    public void filter(EditText editText, CharSequence text, int start, int lengthBefore, int lengthAfter) {
        emojiSize = emojiSize == -1 ? EmoticonsKeyboardUtils.getFontHeight(editText) : emojiSize;
        clearSpan(editText.getText(), start, text.toString().length());
        Matcher m = EmojiDisplay.getMatcher(text.toString().substring(start, text.toString().length()));
        if (m != null) {
            while (m.find()) {
                EmojiDisplay.filterFromResource(editText.getContext(),
                        new SpannableStringBuilder(editText.getText()),
                        EmojiDisplay.getFontHeight(editText), EmojiDisplay.HEAD_NAME, null);
            }
        }
    }

    private void clearSpan(Spannable spannable, int start, int end) {
        if (start == end) {
            return;
        }
        EmojiSpan[] oldSpans = spannable.getSpans(start, end, EmojiSpan.class);
        for (int i = 0; i < oldSpans.length; i++) {
            spannable.removeSpan(oldSpans[i]);
        }
    }
}
