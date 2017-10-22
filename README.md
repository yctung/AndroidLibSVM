# AndroidLibSvm
An open-source Android AAR library of the famous [LibSVM](https://www.csie.ntu.edu.tw/~cjlin/libsvm/)

## Update
- 2017/09/26: Build to AAR, so users don't need to worry about JNI/NDK
- 2017/09/27: Thank [Isaac Salem](https://github.com/iamAyezak) for sharing his app using AndroidLibSVM
![Example Demo App](/Example/screenshots/demo_all.png?raw=true "Example Demo App")

## Getting Started

### Prerequisites
AndroidStudio (I am using 2.3.3 + Gradle 2.2.3, but it should be ok for other versions)

### Install
Install is easy, you just import our [AAR library](/Release/) into your Android project by the following steps:

```
Right-click your module -> new -> module -> Import .JAR/.AAR Package -> select our Release/androidlibsvm-release.aar
```

After this, you should add the app dependency by:

```
Right-click your app module -> open module setting -> clieck your app -> dependencies -> + -> module dependency -> androidlibsvm
```

Once you finish these two steps, you should be able to import our LibSVM class in your JAVA code by:

```
import umich.cse.yctung.androidlibsvm.LibSVM;
```

If you get any AAR import issue, please read this [Android official AAR guide](https://developer.android.com/studio/projects/android-library.html).

### Usage
You can initialize our LibSVM class either by

```
LibSVM svm = new LibSVM();
```

or

```
LibSVM svm = LibSVM().getInstance();
```

Our implementation uses **files** as an input/output interface (just like how the original LibSVM works on the shell). So if you are familiar with the original LibSVM, it should be trivial to use our implementation.
In the following example, you can assume you have LibSVM's **heart\_scale** and **heart\_scale\_predict** datasets in your Android storage. Please check our [testing app](AndroidLibSVM/app/src/main/java/edu/umich/eecs/androidlibsvm/) for further reference.

### Train/Scale/Predict
You can train/scale/predict just by the following three member functions declared in the LibSVM class:

```
public class LibSVM {
    public void train(String options); // equivalent to "bash svm-train options"
    public void predict(String options); // equivalent to "bash svm-predict options"
    public void scale(String options, String fileOutPath); // equivalent to "bash svm-scale options > fileOutPath"
}
```

For example, if you are trying to train/scale/predict the **heart\_scale** and **heart\_scale\_predict** datasets, you can do:

```
String systemPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
String appFolderPath = systemPath + "libsvm/"; // your datasets folder

// NOTE the space between option parameters, which is important to
// keep the options consistent to the original LibSVM format
svm.scale(appFolderPath + "heart_scale", appFolderPath + "heart_scale_scaled");
svm.train("-t 2 "/* svm kernel */ + appFolderPath + "heart_scale_scaled " + appFolderPath + "model");
svm.predict(appFolderPath + "hear_scale_predict " + appFolderPath + "model " + appFolderPath + "result");
```

By reading and analyze the output file (e.g., ```appFolderPath + "result"``` in this case), most applications can easily enjoy the powerful LibSVM functionality with this project.

### Building from the source code
You need Android NDK to build AndroidLibSVM. I am using the NDK-r15b and the customized(deprecated) c++ compile setting as shown in the [build.gradle](AndroidLibSVM/androidlibsvm/build.gradle).
Your Android Studio might complain something about "(null)/ndk-build". This is because the compiler doesn't get the path of your local NDK path
- Solution: add NDK path to your local.properties file like this:
``` ndk.dir=/Users/MyPath/Android/ndk```

### Author
- This Android library is currently maintained by [Yu-Chih Tung](https://yctung.github.io/)
- The core LibSVM is developed by [Chih-Jen Lin](https://www.csie.ntu.edu.tw/~cjlin/index.html) and his team

### Copyright
- Feel free to use this wrapper at whatever manner you want :)
- But remember to include the LibSVM's [copyright](COPYRIGHT.txt) file in your project

## Example Demo App (with a beautiful GUI)
Thank Isaac Salem for building such a useful [demo app](/Example). Following shows some GUI of this demo app. Users can easily train/scale/predict their model with LibSVM through this GUI.
![Example Demo App](/Example/screenshots/demo_all.png?raw=true "Example Demo App")

### Credit
[Isaac Salem](https://github.com/iamAyezak)

## Troubleshooting
- If LibSVM can't read/write the expected output, check if your app has the permission of ```READ_EXTERNAL_STORAGE``` and ```WRITE_EXTERNAL_STORAGE```
- If LibSVM crashes, ensure your input options follow the original LibSVM format
- We don't support input as an array (or other data types) now. Please save your dataset to train/predict as files and ensure they are accessible in the Android.
