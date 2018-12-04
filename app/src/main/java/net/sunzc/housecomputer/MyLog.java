package net.sunzc.housecomputer;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 自定义日志输出类
 */
public class MyLog {
    //    private static ILog LOG = BuildConfig.DEBUG ? new AndroidLog() : new AndroidLog();
    private static ILog LOG = BuildConfig.DEBUG ? new AndroidLog() : new AndroidLog();

    /**
     * 绑定自定义的log类
     *
     * @param log log接口的实现类
     */
    public static void setLog(ILog log) {
        if (log == null) return;
        if (BuildConfig.DEBUG) {
            LOG = log;
        }
    }

    public static void v(Object c, String msg) {
        LOG.v(c, msg);
    }

    public static void v(String tag, String msg) {
        LOG.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        LOG.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        LOG.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        LOG.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable e) {
        LOG.e(tag, msg, e);
    }

    public static void w(String tag, String msg) {
        LOG.w(tag, msg);
    }

    /**
     * 公共Log接口
     */
    public interface ILog {
        void v(String tag, String msg);

        void v(Object tag, String msg);

        void d(String tag, String msg);

        void i(String tag, String msg);

        void e(String tag, String msg);

        void e(String tag, String msg, Throwable e);

        void w(String tag, String msg);
    }

    public static class FileLog extends AndroidLog {
        private BufferedWriter writer;
        private File logFile;

        public FileLog(File dir, String fileName) {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            logFile = new File(dir, fileName);
            try {
                if (!logFile.exists()) {
                    logFile.createNewFile();
                }
                writer = new BufferedWriter(new FileWriter(logFile, true));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public FileLog() {
            this(new File("/sdcard/log"), "log.txt");
        }

        @Override
        public void i(String tag, String msg) {
            super.i(tag, msg);
            try {
                if (writer != null)
                    writer.append("I:").append(tag).append("---").append(msg).append("\n").flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void e(String tag, String msg, Throwable e) {
            super.e(tag, msg, e);
            try {
                if (writer != null)
                    writer.append("E:").append(tag).append("---").append(msg).append("\n").flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void e(String tag, String msg) {
            super.e(tag, msg);
            try {
                if (writer != null)
                    writer.append("E:").append(tag).append("---").append(msg).append("\n").flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String read() {
            BufferedReader br;
            StringBuilder log = new StringBuilder();
            try {
                br = new BufferedReader(new FileReader(logFile));
                String line;
                while (!TextUtils.isEmpty(line = br.readLine())) {
                    log.append(line).append('\n');
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return log.toString();
        }

        public void close() {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 默认的log输出方式
     */
    public static class DefaultLog implements ILog {

        @Override
        public void v(String tag, String msg) {

        }

        @Override
        public void v(Object tag, String msg) {

        }

        @Override
        public void d(String tag, String msg) {

        }

        @Override
        public void i(String tag, String msg) {

        }

        @Override
        public void e(String tag, String msg) {
        }

        @Override
        public void e(String tag, String msg, Throwable e) {
        }

        @Override
        public void w(String tag, String msg) {

        }
    }

    /**
     * 使用android sdk自带的log输出
     */
    private static class AndroidLog extends DefaultLog {

        @Override
        public void v(String tag, String msg) {
            Log.v(tag, Thread.currentThread().getName() + "--" + msg);
        }

        @Override
        public void v(Object tag, String msg) {
            Log.v(tag.getClass().getSimpleName(), Thread.currentThread().getName() + "--" + msg);
        }

        @Override
        public void d(String tag, String msg) {
            Log.d(tag, Thread.currentThread().getName() + "--" + msg);
        }

        @Override
        public void i(String tag, String msg) {
            Log.i(tag, Thread.currentThread().getName() + "--" + msg);
        }

        @Override
        public void e(String tag, String msg) {
            Log.e(tag, Thread.currentThread().getName() + "--" + msg);
        }

        @Override
        public void e(String tag, String msg, Throwable e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            Log.e(tag, Thread.currentThread().getName() + "--" + msg + ":" + sw.toString());
        }

        @Override
        public void w(String tag, String msg) {
            Log.w(tag, Thread.currentThread().getName() + "--" + msg);
        }
    }
}
