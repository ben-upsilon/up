package ben.upsilon.analyzers;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by ben on 1/10/16
 */
public class TrafficService extends Service {


    private Handler mHandler;

    // 系统流量文件
    public final String DEV_FILE = "/proc/self/net/dev";

    // 流量数据
    String[] wifiData = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
            "0", "0", "0", "0", "0", "0"};

    // 用来存储前一个时间点的数据
    String[] data = {"0", "0", "0", "0",};

    // wifi
    final String WIFILINE = "wlan0";

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

    @Override
    public void onStart(Intent intent, int startId) {

    }

    ;

    /**
     * 在服务结束时删除消息队列
     */
    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }

    ;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        final FloatView view = new FloatView(this);
        view.show();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    System.out.println((float) (msg.arg1 / 1024) + "kb/s");
//                    view.tv_show.setText((float) (msg.arg1 / (1024 * 3))
//                            + "k/s");

                }
            }
        };
        mHandler.postDelayed(mRunnable, 0);

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
                data_temp = line.trim().split(":");
                if (line.contains(WIFILINE)) {
                    Log.d("ben.upsilon", "data_temp > " + Arrays.toString(data_temp));
                    Log.d("ben.upsilon", "line > " + line);
                    netData = data_temp[1].trim().replaceAll("\\s+", " ").split(" ");

                    Log.d("ben.upsilon", "netData > " + Arrays.toString(netData));
                    Log.d("ben.upsilon", "netData > " + netData.length);
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
        int traffic_data =  delta[0];
        Message msg = mHandler.obtainMessage();
        msg.what = 1;
        msg.arg1 = traffic_data;
        mHandler.sendMessage(msg);
    }
}
