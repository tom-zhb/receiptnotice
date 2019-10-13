package com.weihuagu.receiptnotice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.weihuagu.receiptnotice.event.SMSEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * 以BroadcastReceiver接收SMS短信
 * */
public class BroadCastTest2_SMS extends BroadcastReceiver{
    public static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (ACTION.equals(intent.getAction())) {
            SmsMessage[] msgs = getMessageFromIntent(intent);
            StringBuilder sBuilder = new StringBuilder();
            if (msgs != null && msgs.length > 0 ) {
                for (SmsMessage msg : msgs) {

                    SMSEvent smsEvent = new SMSEvent();
                    smsEvent.setSendMobile(msg.getDisplayOriginatingAddress());
                    smsEvent.setContent(msg.getDisplayMessageBody());

                    EventBus.getDefault().post(smsEvent);
                    Toast.makeText(context, "发件人：" + smsEvent.getSendMobile() + "\n内容：" + smsEvent.getContent(), Toast.LENGTH_SHORT).show();

                }
            }
        }

    }

    public static SmsMessage[] getMessageFromIntent(Intent intent) {
        SmsMessage retmeMessage[] = null;
        Bundle bundle = intent.getExtras();
        Object pdus[] = (Object[]) bundle.get("pdus");
        retmeMessage = new SmsMessage[pdus.length];
        for (int i = 0; i < pdus.length; i++) {
            byte[] bytedata = (byte[]) pdus[i];
            retmeMessage[i]  = SmsMessage.createFromPdu(bytedata);
        }
        return retmeMessage;
    }
}
