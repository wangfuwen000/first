package applicationmanager.com.example.a123;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by 123 on 2018/11/29.
 */

public class BActivity extends Activity implements IHandler{
    private Thread thread = null;
    private WeakHandler handler = new WeakHandler(this); // 让handler持有activity的弱引用
    private volatile boolean shouldEnd = false; // 通过修改标志位终止一个无限循环的线程
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Logger.logger("myHandler got Message");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_b);
        Logger.logger("B - onCreate()");
        myHandler.sendEmptyMessageDelayed(0, 50 * 1000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.logger("B - onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.logger("B - onResume");
        handler.sendEmptyMessage(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.logger("B - onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.logger("B - onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.logger("B - onDestroy");
        if (thread != null) {
            shouldEnd = true;
        }
        myHandler.removeCallbacksAndMessages(null); // 或者在销毁时清空所有消息
    }

    @Override
    public void handleMessage(Message msg) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!shouldEnd);
            }
        });
        thread.start();
    }
}
