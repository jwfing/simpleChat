package com.leancloud.im.guide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhangxiaobo on 15/4/27.
 */
public class MessageAdapter extends BaseAdapter {
  private Context context;
  List<AVIMTextMessage> messageList = new LinkedList<>();

  public MessageAdapter(Context context) {
    this.context = context;
  }

  @Override
  public int getCount() {
    return messageList.size();
  }

  @Override
  public Object getItem(int position) {
    return messageList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;
    if (convertView == null) {
      convertView = LayoutInflater.from(context).inflate(R.layout.message, null);
      holder = new ViewHolder();
      holder.message = (TextView) convertView.findViewById(R.id.message);
      holder.sender = (TextView) convertView.findViewById(R.id.sender);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    AVIMTextMessage avimMessage = messageList.get(position);
    holder.message.setText(avimMessage.getText());
    holder.sender.setText(avimMessage.getFrom());
    return convertView;
  }

  public void addMessage(AVIMTextMessage message) {
    messageList.add(message);
    notifyDataSetChanged();
  }

  private class ViewHolder {
    TextView message;
    TextView sender;
  }
}
