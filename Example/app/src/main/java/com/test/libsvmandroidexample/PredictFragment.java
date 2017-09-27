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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.aditya.filebrowser.Constants;
import com.aditya.filebrowser.FileChooser;

import java.util.ArrayList;
import java.util.List;

import umich.cse.yctung.androidlibsvm.LibSVM;


public class PredictFragment extends Fragment {

    ProgressDialog progressDialog;
    public static int PICK_TESTFILE = 3000;
    public static int PICK_MODELFILE = 3001;
    Button predictButton;
    Button testFilePicker;
    Button modelFilePicker;
    EditText outputFileNameInput;
    EditText probabilityInput;
    CheckBox probabilityCheckbox;


    public PredictFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_predict, container, false);
        progressDialog = new ProgressDialog(getContext());
        testFilePicker = (Button) view.findViewById(R.id.testfilepicker);
        modelFilePicker = (Button) view.findViewById(R.id.modelfilepicker);
        predictButton = (Button) view.findViewById(R.id.predict_btn);
        outputFileNameInput = (EditText) view.findViewById(R.id.output_filename);
        probabilityInput = (EditText) view.findViewById(R.id.probabilityinput);
        probabilityCheckbox = (CheckBox) view.findViewById(R.id.probabilitycheckbox);
        probabilityCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    probabilityInput.setEnabled(true);
                } else {
                    probabilityInput.setEnabled(false);
                }
            }
        });
        testFilePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(getContext(), FileChooser.class);
                i2.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODES.SINGLE_SELECTION.ordinal());
                startActivityForResult(i2, PICK_TESTFILE);
            }
        });
        modelFilePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(getContext(), FileChooser.class);
                i2.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODES.SINGLE_SELECTION.ordinal());
                startActivityForResult(i2, PICK_MODELFILE);
            }
        });
        predictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> commands = new ArrayList<>();
                if (probabilityCheckbox.isChecked()){
                    if (!Utility.isEmptyOrWhitespace(probabilityInput.getText().toString())){
                        commands.add("-b");
                        commands.add(probabilityInput.getText().toString());
                    }
                }
                commands.add(testFilePicker.getText().toString());
                commands.add(modelFilePicker.getText().toString());
                if (Utility.isEmptyOrWhitespace(outputFileNameInput.getText().toString())) {
                    Toast.makeText(getContext(), "Output file name is required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                commands.add(ContainerActivity.appFolderPath+outputFileNameInput.getText().toString());
                new AsyncPredictTask().execute(commands.toArray(new String[0]));
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_TESTFILE && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                Uri file = data.getData();
                testFilePicker.setText(file.getPath());
            }
        }
        if (requestCode == PICK_MODELFILE && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                Uri file = data.getData();
                modelFilePicker.setText(file.getPath());
            }
        }
    }

    private class AsyncPredictTask extends AsyncTask<String, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("SVM Predict");
            progressDialog.setMessage("Executing svm-predict, please wait...");
            progressDialog.show();
            Log.d(ContainerActivity.TAG, "==================\nStart of SVM PREDICT\n==================");
        }

        @Override
        protected Void doInBackground(String... params) {
            LibSVM.getInstance().predict(TextUtils.join(" ", params));
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "SVM Predict has executed successfully!", Toast.LENGTH_LONG).show();
            Log.d(ContainerActivity.TAG, "==================\nEnd of SVM PREDICT\n==================");
            Utility.readLogcat(getContext(), "SVM-Predict Results");
        }
    }

}
