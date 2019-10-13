package com.weihuagu.receiptnotice;

import android.app.Notification;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TaoDaiNotificationHandle extends NotificationHandle {
    public TaoDaiNotificationHandle(String pkgtype, Notification notification, IDoPost postpush) {
        super(pkgtype, notification, postpush);
    }

    public void handleNotification() {

        if (content != null && !content.equals("")) {
            if (content.indexOf("验证码") != -1) {
                String money = extractMoney(content);
                Log.d("tbdf",content);
                if (money != null) {
                    Map<String, String> postmap = new HashMap<String, String>();
                    postmap.put("type", "mess");
                    postmap.put("time", notitime);
                    postmap.put("title", title);
                    postmap.put("money", money);
                    postmap.put("content", content);
                    postpush.doPost(postmap);
                    return;
                }
            }

        }


    }

    @Override
    protected  String extractMoney(String content){
        int index = content.indexOf("]");
        if (index != -1) {
            content = content.substring(index + 1);
        }

        Pattern p = Pattern.compile("([0-9]\\d*\\.?\\d*)|(0\\.\\d*[0-9])");
        Matcher m = p.matcher(content);
        boolean result = m.find();
        String find_result = null;
        if (result) {
            find_result = m.group(1);
        }
        return find_result;

    }



}
