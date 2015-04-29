package com.leancloud.im.guide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

/**
 * Created by zhangxiaobo on 15/4/16.
 */
public class ConversationActivity extends ActionBarActivity {
  public static final String CONVERSATION_ID = "551a2847e4b04d688d73dc54";
  private static final String EXTRA_CLIENT_ID = "client_id";

  String clientId;
  private TextView clientIdTextView;
  private View joinButton;
  private View createButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_conversation);

    // init
    clientIdTextView = (TextView) findViewById(R.id.client_id);
    joinButton = findViewById(R.id.join_conversation);
    createButton = findViewById(R.id.create_conversation);

    this.clientId = getIntent().getStringExtra(EXTRA_CLIENT_ID);
    if (TextUtils.isEmpty(this.clientId)) {
      this.clientId = "me";
    }

    clientIdTextView.setText(getString(R.string.welcome) + clientId);

    joinButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ChatActivity.startActivity(ConversationActivity.this, ConversationActivity.this.clientId,
            CONVERSATION_ID);
      }
    });
    createButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // ChatActivity.startActivity(ConversationActivity.this, ConversationActivity.this.clientId,
        // "551a2847e4b04d688d73dc54");
        RobotActivity.startActivity(ConversationActivity.this, ConversationActivity.this.clientId);
      }
    });

  }

  public static void startActivity(Context context, String clientId) {
    Intent intent = new Intent(context, ConversationActivity.class);
    intent.putExtra(EXTRA_CLIENT_ID, clientId);
    context.startActivity(intent);
  }
}
