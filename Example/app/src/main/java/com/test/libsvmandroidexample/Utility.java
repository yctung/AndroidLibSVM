package com.test.libsvmandroidexample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utility {

    public static boolean isEmptyOrWhitespace(String str){
        if (str.isEmpty() || str.trim().isEmpty())
            return true;
        else
            return false;
    }

    public static void readLogcat(Context context, String title){
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder log = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(ContainerActivity.processId) && line.contains("LibSvm")) {
                    if (line.contains("=======")){
                        log.append("==================\n");
                    } else if (line.contains("Start of SVM")){
                        log.append(line.substring(line.indexOf("Start"))+"\n");
                    } else if (line.contains("End of SVM")) {
                        log.append(line.substring(line.indexOf("End"))+"\n");
                    } else {
                        int indexOfProcessId = line.lastIndexOf(ContainerActivity.processId);
                        String newLine = line.substring(indexOfProcessId);
                        log.append(newLine+"\n\n");
                    }
                }
            }
            showResult(context, log.toString(), title);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(ContainerActivity.TAG, "readLogcat: failed to read from logcat logger.");
        }
    }

    public static void showResult(Context context, String resultText, String title){
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog_result, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(promptsView)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        final TextView resultTextView = (TextView) promptsView.findViewById(R.id.resulttextview);
        resultTextView.setText(resultText);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
