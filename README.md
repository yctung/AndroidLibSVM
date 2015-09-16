# AndroidLibSvm

use the latest libsvm 3.2 in Android via Java-Native-Interface (JNI)

## Features
- minimal changes on the original c library (https://www.csie.ntu.edu.tw/~cjlin/libsvm/)
- use the latest libsvm 3.2
- compatible to Android Studio

## Usage
Once you import the whole project to your Android Studio and run it on your device. The svm model and prediction output file will be located at /sdcard/libsvm of your device. 
You can also check the livsvm output at your logcat output.

In java, you can call the original "./svm-train options" function as:
```sh
	jniSvmTrain(String options);
```

In a similar way, the the origianl "./svm-predict options" function is equivalent to:
```sh
	jniSvmPredict(String options);
```

An example usage in Java could be:
```sh
	jniSvmTrain("-t 2 your_train_data_path your_model_file_path");
	jniSvmPredict("your_predict_data_path your_model_file_path your_output_file_path");
```

By reading and analyze the output file, most applications can easily enjoy the powerful libsvm functionaility with this project.

## Include this project to existing Android project
To use this project in your existing Android project, you need to do the following actions:
- copy the whole AndroidLibSvm/app/src/main/jni/ to your project (with the same relative path)
- edit AndroidLibSvm/app/src/main/jni/jnilibsvm.cpp to replace any occurance of "edu_umich_eecs_androidlibsvm_MainActivity" to your package and activity name. For example, if your package name is com.happy.project and your activity name is HappyActivity, you should change the prefix to com_happy_project_HappyActivity (see this link to know the name convention in JNI: `http://journals.ecs.soton.ac.uk/java/tutorial/native1.1/implementing/declare.html`).
- copy the AndroidLibSvm/app/build.gradle to your project and ensure ndk-build is installed (see this link for further reference about compiling ndk functions in Android: `http://www.shaneenishry.com/blog/2014/08/17/ndk-with-android-studio/`).

