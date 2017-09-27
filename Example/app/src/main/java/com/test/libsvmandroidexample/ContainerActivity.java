package com.test.libsvmandroidexample;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;
import net.alhazmy13.gota.Gota;
import net.alhazmy13.gota.GotaResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class ContainerActivity extends AppCompatActivity implements Gota.OnRequestPermissionsBack {

    public static final String TAG = "LibSVMExample";
    public static final String processId = Integer.toString(android.os.Process.myPid());

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static String appFolderPath;
    public static String systemPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        final boolean readStorage = canReadExternalStorage();
        final boolean writeStorage = canWriteExternalStorage();
        // request app permissions
        if (!readStorage || !writeStorage){
            new Gota.Builder(this)
                    .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_LOGS)
                    .requestId(1)
                    .setListener(this)
                    .check();
        }
        systemPath = Environment.getExternalStorageDirectory() + "/";
        appFolderPath = systemPath+"LibSVMAssets/";

        // create assets folder if it doesn't exist
        createAssetsFolder();

        // copy all data files from assets to external storage
        try {
            String[] list = getAssets().list("data");
            for (String file: list) {
                copyToExternalStorage(file, "data");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ScaleFragment(), "Scale");
        adapter.addFragment(new TrainFragment(), "Train");
        adapter.addFragment(new PredictFragment(), "Predict");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onRequestBack(int requestId, @NonNull GotaResponse gotaResponse) {
        if (gotaResponse.isDenied(Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(this, "READ_EXTERNAL_STORAGE permission is required for the app to function properly.", Toast.LENGTH_LONG).show();
        }
        if (gotaResponse.isDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(this, "WRITE_EXTERNAL_STORAGE permission is required for the app to function properly.", Toast.LENGTH_LONG).show();
        }
        if (gotaResponse.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) || gotaResponse.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(this, "Restart the app to able to use the newly granted permissions.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_copyright_btn:
                // show copyright dialog
                showDialog("copyright");
                return true;
            case R.id.menu_train_options:
                // show train options dialog
                showDialog("Train_Options");
                return true;
            case R.id.menu_scale_options:
                // show scale options dialog
                showDialog("Scale_Options");
                return true;
            case R.id.menu_predict_options:
                // show train options dialog
                showDialog("Predict_Options");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showDialog(String htmlFile){
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this)
                .setTitle((htmlFile.contains("_") ? htmlFile.replace("_", " ") : htmlFile))
                .setView(promptsView)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        final WebView webview = (WebView) promptsView.findViewById(R.id.webview);
        webview.loadUrl("file:///android_asset/html/"+htmlFile+".html");
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public boolean canReadExternalStorage(){
        int permissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    public boolean canWriteExternalStorage(){
        int permissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    private void createAssetsFolder(){
        // create app assets folder if not created
        File folder = new File(appFolderPath);

        if (!folder.exists()) {
            Log.d(TAG,"LibSVMAssets folder does not exist, creating one");
            folder.mkdirs();
        } else {
            Log.w(TAG,"INFO: LibSVMAssets folder already exists.");
        }
    }

    private void copyToExternalStorage(String assetName, String assetsDirectory){
        String from = assetName;
        String to = appFolderPath+from;

        // check if the file exists
        File file = new File(to);
        if(file.exists()){
            Log.d(TAG, "copyToExternalStorage: file already exist, no need to copy: "+from);
        } else {
            // do copy
            boolean copyResult = copyAsset(getAssets(), from, assetsDirectory, to);
            Log.d(TAG, "copyToExternalStorage: isCopied -> "+copyResult);
        }
    }

    private boolean copyAsset(AssetManager assetManager, String fromAssetPath, String assetsDirectory, String toPath) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = assetManager.open(assetsDirectory+"/"+fromAssetPath);
            new File(toPath).createNewFile();
            outputStream = new FileOutputStream(toPath);
            copyFile(inputStream, outputStream);
            inputStream.close();
            outputStream.flush();
            outputStream.close();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            Log.e(TAG, "copyAsset: unable to copy file: "+fromAssetPath);
            return false;
        }
    }

    private void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = inputStream.read(buffer)) != -1){
            outputStream.write(buffer, 0, read);
        }
    }

}