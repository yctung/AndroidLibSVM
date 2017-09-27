package umich.cse.yctung.androidlibsvm;

import android.util.Log;

/**
 * Created by yctung on 9/26/17.
 * This is a java wrapper of LibSVM
 */

public class LibSVM {
    String LOG_TAG = "LibSVM";

    static {
        System.loadLibrary("jnilibsvm");
    }

    // connect the native functions
    private native void testLog(String log);
    private native void jniSvmTrain(String cmd);
    private native void jniSvmPredict(String cmd);
    private native void jniSvmScale(String cmd, String fileOutPath);

    // public interfaces
    public void train(String cmd) {
        jniSvmTrain(cmd);
    }
    public void predict(String cmd) {
        jniSvmPredict(cmd);
    }
    public void scale(String cmd, String fileOutPath) {
        jniSvmScale(cmd, fileOutPath);
    }

    // singleton for the easy access
    private static LibSVM svm;
    public static LibSVM getInstance() {
        if (svm == null) {
            svm = new LibSVM();
        }
        return svm;
    }

    public LibSVM() {
        Log.d(LOG_TAG, "LibSVM init");
    }
}
