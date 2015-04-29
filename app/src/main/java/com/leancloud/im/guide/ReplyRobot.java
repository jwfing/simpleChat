package com.leancloud.im.guide;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

/**
 * Created by zhangxiaobo on 15/4/27.
 */
public class ReplyRobot {
  public static final String ROBOT_NAME = "robot";
  private static final String TAG = ReplyRobot.class.getSimpleName();
  private AVIMClient avimClient;



  public ReplyRobot() {
    avimClient = AVIMClient.getInstance(ROBOT_NAME);

    avimClient.open(new AVIMClientCallback() {
      @Override
      public void done(AVIMClient client, AVException exception) {
        if (exception == null) {
          Log.d(TAG, "open client done");
        } else {
          Log.w(TAG, "open client with exception");
          exception.printStackTrace();
        }
      }
    });
  }
}
