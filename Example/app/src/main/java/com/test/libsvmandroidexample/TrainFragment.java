package com.test.libsvmandroidexample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aditya.filebrowser.Constants;
import com.aditya.filebrowser.FileChooser;

import umich.cse.yctung.androidlibsvm.LibSVM;


public class TrainFragment extends Fragment {

    ProgressDialog progressDialog;
    public static int PICK_DATAFILE = 2000;
    Button trainButton;
    Button dataFilePicker;
    EditText outputFileInput;
    EditText commandInput;


    public TrainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_train, container, false);
        progressDialog = new ProgressDialog(getContext());
        trainButton = (Button) view.findViewById(R.id.train_btn);
        dataFilePicker = (Button) view.findViewById(R.id.datafilepicker);
        outputFileInput = (EditText) view.findViewById(R.id.output_filename);
        commandInput = (EditText) view.findViewById(R.id.train_options);
        trainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isEmptyOrWhitespace(outputFileInput.getText().toString()) || Utility.isEmptyOrWhitespace(commandInput.getText().toString())) {
                    Toast.makeText(getContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String outputModelPath = ContainerActivity.appFolderPath+outputFileInput.getText().toString();
                String commandString = commandInput.getText().toString();
                String dataFilePath = dataFilePicker.getText().toString();
                new AsyncTrainTask().execute(new String[]{commandString, dataFilePath, outputModelPath});
            }
        });
        dataFilePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(getContext(), FileChooser.class);
                i2.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODES.SINGLE_SELECTION.ordinal());
                startActivityForResult(i2, PICK_DATAFILE);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_DATAFILE && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                Uri file = data.getData();
                dataFilePicker.setText(file.getPath());
            }
        }
    }

    private class AsyncTrainTask extends AsyncTask<String, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("SVM Train");
            progressDialog.setMessage("Executing svm-train, please wait...");
            progressDialog.show();
            Log.d(ContainerActivity.TAG, "==================\nStart of SVM TRAIN\n==================");
        }

        @Override
        protected Void doInBackground(String... params) {
            LibSVM.getInstance().train(TextUtils.join(" ", params));
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "SVM Train has executed successfully!", Toast.LENGTH_LONG).show();
            Log.d(ContainerActivity.TAG, "==================\nEnd of SVM TRAIN\n==================");
            Utility.readLogcat(getContext(), "SVM-Train Results");
        }
    }

}
