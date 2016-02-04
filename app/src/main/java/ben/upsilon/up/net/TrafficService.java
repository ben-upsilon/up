package ben.upsilon.up.net;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import ben.upsilon.up.R;


/**
 * Created by ben on 1/10/16
 */
public class TrafficService extends Service {


    // 系统流量文件
    public final String DEV_FILE = "/proc/self/net/dev";
    // wifi
    final String WIFILINE = "wlan0";
    // 流量数据
    String[] wifiData = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
            "0", "0", "0", "0", "0", "0"};
    // 用来存储前一个时间点的数据
    String[] data = {"0", "0", "0", "0",};
    private Handler mHandler;
    /**
     * 定义线程周期性地获取网速
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            refresh();
            mHandler.postDelayed(mRunnable, 1000);
        }
    };
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private LayoutInflater mLayoutInflater;
    private TextView mFloatView;
    private int mCurrentX;
    private int mCurrentY;

    /**
     * 在服务结束时删除消息队列
     */
    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }

    private void createView() {
        // TODO Auto-generated method stub
        //加载布局文件
        mFloatView = (TextView) mLayoutInflater.inflate(R.layout.float_view, null);
//        mFloatView = new TextView(this);


//        mFloatView.setBackgroundColor(getResources().getColor(android.R.color.transparent, getTheme()));
        //为View设置监听，以便处理用户的点击和拖动
        mFloatView.setOnTouchListener(new OnFloatViewTouchListener());
       /*为View设置参数*/
        mLayoutParams = new WindowManager.LayoutParams();
        //设置View默认的摆放位置
        mLayoutParams.gravity = Gravity.START | Gravity.TOP;
        //设置window type
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //设置背景为透明
        mLayoutParams.format = PixelFormat.RGBA_8888;
        //注意该属性的设置很重要，FLAG_NOT_FOCUSABLE使浮动窗口不获取焦点,若不设置该属性，屏幕的其它位置点击无效，应为它们无法获取焦点
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //设置视图的显示位置，通过WindowManager更新视图的位置其实就是改变(x,y)的值
        mCurrentX = mLayoutParams.x = 50;
        mCurrentY = mLayoutParams.y = 50;
        //设置视图的宽、高
        mLayoutParams.width = 180;
        mLayoutParams.height = 50;
        //将视图添加到Window中
        mWindowManager.addView(mFloatView, mLayoutParams);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化WindowManager对象和LayoutInflater对象
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mLayoutInflater = LayoutInflater.from(this);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    int data = msg.arg1;

                    if (data / 1024 > 0)
//                        System.out.println((float) (msg.arg1 / 1024) + "kb/s");
                        mFloatView.setText((float) (msg.arg1 / 1024) + "kb/s\n" + (float) (msg.arg2 / 1024) + "kb/s");
                    else
//                        System.out.println((float) (msg.arg1) + "b/s");
//                    view.tv_show.setText((float) (msg.arg1 / (1024 * 3))
//                            + "k/s");
                        mFloatView.setText((msg.arg1) + "b/s\n" + msg.arg2 + "b/s");

                }
            }
        };
        mHandler.postDelayed(mRunnable, 0);
        createView();
    }

    public void readDev() {
        FileReader fr = null;
        try {
            fr = new FileReader(DEV_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        BufferedReader bufr = new BufferedReader(fr, 500);
        String line;
        String[] data_temp;
        String[] netData;
        int k;
        int j;
        // 读取文件，并对读取到的文件进行操作
        try {
            while ((line = bufr.readLine()) != null) {
                Log.d("ben.upsilon", "line > " + line);
                data_temp = line.trim().split(":");

                if (line.contains(WIFILINE)) {
//                    Log.d("ben.upsilon", "data_temp > " + Arrays.toString(data_temp));
//                    Log.d("ben.upsilon", "line > " + line);
                    netData = data_temp[1].trim().replaceAll("\\s+", " ").split(" ");

//                    Log.d("ben.upsilon", "netData > " + Arrays.toString(netData));
//                    Log.d("ben.upsilon", "netData > " + netData.length);
                    for (k = 0, j = 0; k < wifiData.length; k++) {
                        wifiData[j] = netData[k];
                        j++;
                    }
                }
            }
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 实时读取系统流量文件，更新
     */
    public void refresh() {
        // 读取系统流量文件

        readDev();

        // 计算增量
        int[] delta = new int[4];
        delta[0] = Integer.parseInt(wifiData[0]) - Integer.parseInt(data[0]);
        delta[1] = Integer.parseInt(wifiData[1]) - Integer.parseInt(data[1]);
        delta[2] = Integer.parseInt(wifiData[8]) - Integer.parseInt(data[2]);
        delta[3] = Integer.parseInt(wifiData[9]) - Integer.parseInt(data[3]);

        data[0] = wifiData[0];
        data[1] = wifiData[1];
        data[2] = wifiData[8];
        data[3] = wifiData[9];

        // 每秒下载的字节数
        int Receive_data = delta[0];
        int Transmit_data = delta[2];
        Message msg = mHandler.obtainMessage();
        msg.what = 1;
        msg.arg1 = Receive_data;
        msg.arg2 = Transmit_data;
        mHandler.sendMessage(msg);
    }

    /*该方法用来更新视图的位置，其实就是改变(LayoutParams.x,LayoutParams.y)的值*/
    private void updateFloatView() {
        mLayoutParams.x = mCurrentX;
        mLayoutParams.y = mCurrentY;
        mWindowManager.updateViewLayout(mFloatView, mLayoutParams);
    }

    private class OnFloatViewTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            int mFloatViewHeight = 80;
            int mFloatViewWidth = 50;
            Log.i("ben.upsilon", "mCurrentX: " + mCurrentX + ",mCurrentY: "
                    + mCurrentY + ",mFloatViewWidth: " + mFloatViewWidth
                    + ",mFloatViewHeight: " + mFloatViewHeight);
           /*
            * getRawX(),getRawY()这两个方法很重要。通常情况下，我们使用的是getX(),getY()来获得事件的触发点坐标，
            * 但getX(),getY()获得的是事件触发点相对与视图左上角的坐标；而getRawX(),getRawY()获得的是事件触发点
            * 相对与屏幕左上角的坐标。由于LayoutParams中的x,y是相对与屏幕的，所以需要使用getRawX(),getRawY()。
            */
            mCurrentX = (int) event.getRawX() - mFloatViewWidth;
            mCurrentY = (int) event.getRawY() - mFloatViewHeight;
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    updateFloatView();
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return true;
        }

    }
}
