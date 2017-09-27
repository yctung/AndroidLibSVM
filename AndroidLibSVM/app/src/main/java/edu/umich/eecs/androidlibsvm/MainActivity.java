package edu.umich.eecs.androidlibsvm;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import umich.cse.yctung.androidlibsvm.LibSVM;

public class MainActivity extends Activity {
    LibSVM svm;
    static final String LOG_TAG = "LibSVMApp";
    String appFolderPath;
    String systemPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        systemPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        appFolderPath = systemPath+"libsvm/";

        // 1. create necessary folder to save model files
        CreateAppFolderIfNeed();
        copyAssetsDataIfNeed();

        // 2. assign model/output paths
        String dataTrainPath = appFolderPath+"heart_scale ";
        String dataScaledPath = appFolderPath+"scaled";
        String dataPredictPath = appFolderPath+"heart_scale ";
        String modelPath = appFolderPath+"model ";
        String outputPath = appFolderPath+"predict ";

        svm = new LibSVM();

        // 3. (optional) make SVM scale
        // NOTE: the jni interface is different from the original svm-scale because the it requires
        //       to redirect the output by Linux Redirecting, e.g., svm-scale input > out
        //       in this case, the jni interface changed to: jniSvmScale(input, output)
        svm.scale(dataTrainPath, dataScaledPath);

        // 4. make SVM train
        String svmTrainOptions = "-t 2 "; // note the ending space
        svm.train(svmTrainOptions + dataTrainPath + modelPath);

        // 5. make SVM predict
        svm.predict(dataPredictPath + modelPath + outputPath);
    }


    // Some utility functions
    private void CreateAppFolderIfNeed(){
        // 1. create app folder if necessary
        File folder = new File(appFolderPath);

        if (!folder.exists()) {
            folder.mkdir();
            Log.d(LOG_TAG,"Appfolder is not existed, create one");
        } else {
            Log.w(LOG_TAG,"WARN: Appfolder has not been deleted");
        }


    }

    private void copyAssetsDataIfNeed(){
        String assetsToCopy[] = {"heart_scale_predict","heart_scale_train","heart_scale"};
        //String targetPath[] = {C.systemPath+C.INPUT_FOLDER+C.INPUT_PREFIX+AudioConfigManager.inputConfigTrain+".wav", C.systemPath+C.INPUT_FOLDER+C.INPUT_PREFIX+AudioConfigManager.inputConfigPredict+".wav",C.systemPath+C.INPUT_FOLDER+"SomeoneLikeYouShort.mp3"};

        for(int i=0; i<assetsToCopy.length; i++){
            String from = assetsToCopy[i];
            String to = appFolderPath+from;

            // 1. check if file exist
            File file = new File(to);
            if(file.exists()){
                Log.d(LOG_TAG, "copyAssetsDataIfNeed: file exist, no need to copy:"+from);
            } else {
                // do copy
                boolean copyResult = copyAsset(getAssets(), from, to);
                Log.d(LOG_TAG, "copyAssetsDataIfNeed: copy result = "+copyResult+" of file = "+from);
            }
        }
    }

    private boolean copyAsset(AssetManager assetManager, String fromAssetPath, String toPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fromAssetPath);
            new File(toPath).createNewFile();
            out = new FileOutputStream(toPath);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "[ERROR]: copyAsset: unable to copy file = "+fromAssetPath);
            return false;
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}
