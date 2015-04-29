package com.leancloud.im.guide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiaobo on 15/4/16.
 */
public class RobotActivity extends ActionBarActivity {
  private static final String EXTRA_CONVERSATION_ID = "conversation_id";
  private static final String EXTRA_CLIENT_ID = "client_id";
  private static final String TAG = RobotActivity.class.getSimpleName();

  AVIMClient avimClient;
  ReplyRobot robot;
  private AVIMConversation conversation;
  MessageAdapter adapter;

  private EditText messageEditText;
  private ListView listView;
  private View sendButton;
  private RobotHandler handler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);

    // init component
    listView = (ListView) findViewById(R.id.listview);
    sendButton = findViewById(R.id.send);
    messageEditText = (EditText) findViewById(R.id.message);

    adapter = new MessageAdapter(RobotActivity.this);
    listView.setAdapter(adapter);

    // get argument
    String clientId = getIntent().getStringExtra(EXTRA_CLIENT_ID);
    if (TextUtils.isEmpty(clientId)) {
      clientId = "me";
    }

    // register callback
    AVIMMessageManager.registerDefaultMessageHandler(new CustomMessageHandler());
    handler = new RobotHandler(adapter);
    AVIMMessageManager.registerMessageHandler(AVIMTextMessage.class, handler);

    // open im client
    avimClient = AVIMClient.getInstance(clientId);
    robot = new ReplyRobot();
    avimClient.open(new AVIMClientCallback() {
      @Override
      public void done(AVIMClient avimClient, AVException exception) {
        if (exception == null) {
          Toast.makeText(RobotActivity.this, "client open!", Toast.LENGTH_LONG).show();

          List<String> clientIds = new ArrayList<String>();
          clientIds.add(ReplyRobot.ROBOT_NAME);

          Map<String, Object> attr = new HashMap<String, Object>();
          attr.put("type", 0);

          avimClient.createConversation(clientIds, attr, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation conversation, AVException exception) {
              if (null != conversation) {
                Log.d("jacob", conversation.getConversationId());
                RobotActivity.this.conversation = conversation;
              } else {
                exception.printStackTrace();
              }
            }
          });
        } else {
          Toast.makeText(RobotActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
        }
      }
    });

    sendButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        final AVIMTextMessage message = new AVIMTextMessage();
        message.setText(messageEditText.getText().toString());
        conversation.sendMessage(message, new AVIMConversationCallback() {
          @Override
          public void done(AVException exception) {
            if (null != exception) {
              exception.printStackTrace();
            } else {
              Log.d(TAG, "发送成功，msgId=" + message.getMessageId());
            }
          }
        });
        adapter.addMessage(message);
        InputMethodManager imm =
            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(messageEditText.getWindowToken(), 0);
      }
    });

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    avimClient.close(new AVIMClientCallback() {
      @Override
      public void done(AVIMClient avimClient, AVException exception) {
        if (exception == null) {
          Log.d(TAG, "退出连接");
        }
      }
    });
    AVIMMessageManager.unregisterMessageHandler(AVIMTextMessage.class, handler);
  }



  public static void startActivity(Context context, String clientId) {
    Intent intent = new Intent(context, RobotActivity.class);
    intent.putExtra(EXTRA_CLIENT_ID, clientId);
    context.startActivity(intent);
  }

  public class RobotHandler extends AVIMTypedMessageHandler<AVIMTextMessage> {
    MessageAdapter adapter;

    public RobotHandler(MessageAdapter adapter) {
      this.adapter = adapter;
    }

    @Override
    public void onMessage(AVIMTextMessage message, AVIMConversation conversation,
        AVIMClient client) {
      if (!conversation.getConversationId().equals(ConversationActivity.CONVERSATION_ID)) {
        // 自动回复机器人
        if (client.getClientId().equals(ReplyRobot.ROBOT_NAME)) {
          AVIMTextMessage replyMessage = new AVIMTextMessage();
          replyMessage.setText(message.getText() + " -- send from robot");
          conversation.sendMessage(replyMessage, new AVIMConversationCallback() {
            @Override
            public void done(AVException exception) {
              Log.d(TAG, "send message done");
            }
          });
        } else {
          adapter.addMessage(message);
        }
      } else {
        Log.d(TAG, "这是来自聊天室的消息，不在这里显示 " + message.getContent());
      }
    }
  }
}
