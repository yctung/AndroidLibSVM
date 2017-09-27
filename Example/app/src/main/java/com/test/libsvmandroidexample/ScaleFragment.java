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


public class ScaleFragment extends Fragment {

    ProgressDialog progressDialog;
    public static int PICK_DATAFILE = 1000;
    public static int PICK_RESTOREFILE = 1001;
    Button dataFilePicker;
    Button restoreFilePicker;
    Button scaleButton;
    CheckBox saveFileNameCheckBox;
    CheckBox restoreFileNameCheckbox;
    CheckBox xLowerCheckBox;
    CheckBox xUpperCheckBox;
    CheckBox yLimitsCheckBox;
    EditText saveFileNameInput;
    EditText xLowerLimitInput;
    EditText xUpperLimitInput;
    EditText yLimitsInput;
    EditText outputFileNameInput;


    public ScaleFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_scale, container, false);
        progressDialog = new ProgressDialog(getContext());

        // Buttons
        dataFilePicker = (Button) view.findViewById(R.id.datafilepicker);
        restoreFilePicker = (Button) view.findViewById(R.id.restorefilepicker);
        scaleButton = (Button) view.findViewById(R.id.scale_btn);

        // EditTexts
        saveFileNameInput = (EditText) view.findViewById(R.id.save_filename);
        outputFileNameInput = (EditText) view.findViewById(R.id.output_filename);
        xLowerLimitInput = (EditText) view.findViewById(R.id.lowerlimit);
        xUpperLimitInput = (EditText) view.findViewById(R.id.upperlimit);
        yLimitsInput = (EditText) view.findViewById(R.id.ylimit);

        // CheckBoxes
        saveFileNameCheckBox = (CheckBox) view.findViewById(R.id.savecheckBox);
        restoreFileNameCheckbox = (CheckBox) view.findViewById(R.id.restorecheckBox);
        xLowerCheckBox = (CheckBox) view.findViewById(R.id.lowerlimitcheckbox);
        xUpperCheckBox = (CheckBox) view.findViewById(R.id.upperlimitcheckbox);
        yLimitsCheckBox = (CheckBox) view.findViewById(R.id.ylimitcheckbox);

        // Listeners
        dataFilePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(getContext(), FileChooser.class);
                i2.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODES.SINGLE_SELECTION.ordinal());
                startActivityForResult(i2, PICK_DATAFILE);
            }
        });
        restoreFilePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(getContext(), FileChooser.class);
                i2.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODES.SINGLE_SELECTION.ordinal());
                startActivityForResult(i2, PICK_RESTOREFILE);
            }
        });
        saveFileNameCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    saveFileNameInput.setEnabled(true);
                    restoreFileNameCheckbox.setChecked(false);
                } else {
                    saveFileNameInput.setEnabled(false);
                }
            }
        });
        restoreFileNameCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    restoreFilePicker.setEnabled(true);
                    saveFileNameCheckBox.setChecked(false);
                } else {
                    restoreFilePicker.setEnabled(false);
                }
            }
        });
        xLowerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    xLowerLimitInput.setEnabled(true);
                    yLimitsCheckBox.setChecked(false);
                } else {
                    xLowerLimitInput.setEnabled(false);
                }
            }
        });
        xUpperCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    xUpperLimitInput.setEnabled(true);
                    yLimitsCheckBox.setChecked(false);
                } else {
                    xUpperLimitInput.setEnabled(false);
                }
            }
        });
        yLimitsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    yLimitsInput.setEnabled(true);
                    xLowerCheckBox.setChecked(false);
                    xUpperCheckBox.setChecked(false);
                } else {
                    yLimitsInput.setEnabled(false);
                }
            }
        });
        scaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> options = new ArrayList<>();
                String dataFilePath = dataFilePicker.getText().toString();
                String outputFileName = outputFileNameInput.getText().toString();
                if (xLowerCheckBox.isChecked() || xUpperCheckBox.isChecked()){
                    if (xLowerCheckBox.isChecked()){
                        if (!Utility.isEmptyOrWhitespace(xLowerLimitInput.getText().toString())){
                            options.add("-l");
                            options.add(xLowerLimitInput.getText().toString());
                        }
                    }
                    if (xUpperCheckBox.isChecked()){
                        if (!Utility.isEmptyOrWhitespace(xUpperLimitInput.getText().toString())){
                            options.add("-u");
                            options.add(xUpperLimitInput.getText().toString());
                        }
                    }
                } else if (yLimitsCheckBox.isChecked()){
                    if (!Utility.isEmptyOrWhitespace(yLimitsInput.getText().toString())){
                        options.add("-y");
                        options.add(yLimitsInput.getText().toString());
                    }
                }
                if (saveFileNameCheckBox.isChecked()) {
                  String saveFileName = saveFileNameInput.getText().toString();
                  if (!Utility.isEmptyOrWhitespace(saveFileName)){
                      options.add("-s");
                      options.add(ContainerActivity.appFolderPath+saveFileName);
                  }
                } else if (restoreFileNameCheckbox.isChecked()){
                    options.add("-r");
                    options.add(restoreFilePicker.getText().toString());
                }
                options.add(dataFilePath);
                String optionsString = TextUtils.join(" ", options);
                String outputFilePath = null;
                if (Utility.isEmptyOrWhitespace(outputFileName)) {
                    Toast.makeText(getContext(), "Output file name is required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                outputFilePath = ContainerActivity.appFolderPath+outputFileName;
                new AsyncScaleTask().execute(new String[]{optionsString, outputFilePath});
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
        if (requestCode == PICK_RESTOREFILE && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                Uri file = data.getData();
                restoreFilePicker.setText(file.getPath());
            }
        }
    }


    private class AsyncScaleTask extends AsyncTask<String, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("SVM Scale");
            progressDialog.setMessage("Executing svm-scale, please wait...");
            progressDialog.show();
            Log.d(ContainerActivity.TAG, "==================\nStart of SVM SCALE\n==================");
        }

        @Override
        protected Void doInBackground(String... params) {
            LibSVM.getInstance().scale(params[0], params[1]);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "SVM Scale has executed successfully!", Toast.LENGTH_LONG).show();
            Log.d(ContainerActivity.TAG, "==================\nEnd of SVM SCALE\n==================");
            Utility.readLogcat(getContext(), "SVM-Scale Results");
        }
    }
}