package com.leancloud.im.guide;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by zhangxiaobo on 15/4/15.
 */
public class Application extends android.app.Application {
  @Override
  public void onCreate() {
    super.onCreate();
    AVOSCloud.setDebugLogEnabled(true);
    // 这是用于 SimpleChat 的 app id 和 app key，如果更改将不能进入 demo 中相应的聊天室
    AVOSCloud.initialize(this, "9p6hyhh60av3ukkni3i9z53q1l8yy3cijj6sie3cewft18vm",
        "nhqqc1x7r7r89kp8pggrme57i374h3vyd0ukr2z3ayojpvf4");
  }
}
