# AndroidLibSvm
An open-source Android AAR library of the famous LibSVM [https://www.csie.ntu.edu.tw/~cjlin/libsvm/]

## Update
2017/09/26: Build to AAR, so users don't need to worry about JNI/NDK

## Getting Started

### Prerequisites
AndroidStudio (I am using 2.3.3 w/ Gradle 3.3, but it should be ok for other versions)

### Install
Install is easy, you basically only need to import our [AAR library](AAR file (Release/androidlibsvm-release.aar) into your Android project by the following steps:

```
Right-click your module -> new -> module -> Import .JAR/.AAR Package -> select our Release/androidlibsvm-release.aar
```

After this, you should add the dependency by:

```
Right-click your app module -> open module setting -> clieck your app -> dependencies -> + -> module dependency -> androidlibsvm
```

Once you finish these two steps, you should be able to import our LibSVM class in your java code by:

```
import umich.cse.yctung.androidlibsvm.LibSVM;
```

If you get any import AAR issues, please refer this [Android official guide](https://developer.android.com/studio/projects/android-library.html).

### Usage
You can initalize our LibSVM class either by

```
LibSVM svm = new LibSVM();
```

or

```
LibSVM svm = LibSVM().getInstance();
```

Our implementation use the file as an input/output interface (just like the original LibSVM). So if you are familiar with the original LibSVM, it should be trivial to use our implementation.
In the following example, you can assume you have LibSVM's example data called **heart\_scale** and **heart\_scale\_predict** in your Andorid device. Please check our [testing app](AndroidLibSVM/app/src/main/java/edu/umich/eecs/androidlibsvm/) for further reference.

### Train/Scale/Predict
You can train/scale/predict just by the following three member functions declared in the LibSVM class:


```
public class LibSVM {
    public void train(String options); // == sh svm-train options
    public void predict(String options); // == sh svm-predict options
    public void scale(options, fileOutPath); // == sh svm-scale options > fileOutPath
}
```

For example, assume you are trying to train/predict the **heart\_scale** and **heart\_scale\_predict** datasets, you can do:

```
A
```

By reading and analyze the output file, most applications can easily enjoy the powerful LibSVM functionality with this project.

### Building from the source code
You need Android NDK to build AndroidLibSVM. I am using the NDK-r15b and the customized(deprecated) setting as shown in the [build.gradle](AndroidLibSVM/androidlibsvm/build.gradle).
Your Android Studio might complain something about "(null)/ndk-build". This is because the compiler doesn't get the path of your local NDK path
- Solution: add NDK path to your local.properties file like this:
``` ndk.dir=/Users/MyPath/Android/ndk```

## Example Demo App (with a beautiful GUI)
Thanks Isaac Freeman for building this useful demo app. Following shows some GUI of this demo app. User can easily train/predict their model with LibSVM through this GUI.

### Credit
Isaac Freeman
