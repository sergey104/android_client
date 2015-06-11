#Android_app#

#SEP client for GCM system


1.The main project folders:

	java: contains the source code written in java ordered by packet. In our example, the package com.beanslegacy.sample.helloworld contains one activity called HelloWorldActivity.

	res: contains all the resources of our application ie images, colors, text in any languages, themes… and most GUIs.
    
	Res contains some sub-directories:

    drawable- *: they are many sub-directories because there is one per type of screen (or type of screen density). You put such images in your application adapted to each type of device. We will see this later.
    layout: the default directory for graphical interfaces. We will see later that we can create others layout* sub-directories, such as when you want to do a different interface between portrait and landscape mode.
    menu: it allows you to define the menus that are offered by the different activities.
    values: this sub-directory contains the default dimens.xml files to centralize sizing information such as lines and spaces between other elements of the graphical interface. strings.xml contains the strings of your application (text menu, text buttons…) and styles.xml that defines the style of your application (Android offers styles to easyly start your application, we will see this later).
    

##What you’ll need

    JDK 7 or later
    
    Gradle

    Android SDK

    An Android device or Emulator
    

##Install Gradle

Now you can install Gradle.

    1.  Download Gradle 2.3 from the Gradle Downloads page.
	Only the binaries are required, so look for the link to gradle-2.3-bin.zip. Alternatively, you can choose gradle-2.3-all.zip to download the sources and documentation as well as the binaries.

    2. Unzip the archive and place it in a location of your choosing. For example, on Linux or Mac, you may want to place it in the root of your user directory. See the Installing Gradle page for additional details.

    3. Configure the GRADLE_HOME environment variable based on the location where you installed Gradle.

*Mac/Linux*

`export GRADLE_HOME=/<installation location>/gradle-2.3`
`export PATH=${PATH}:$GRADLE_HOME/bin`

*Windows*

`set GRADLE_HOME=C:\<installation location>\gradle-2.3`
`set PATH=%PATH%;%GRADLE_HOME%\bin`

4. Test the Gradle installation with following command:

`$ gradle`

If the installation is correct, you see a welcome message:

    :help

    Welcome to Gradle 2.3.

    To run a build, run gradle <task> ...

    To see a list of available tasks, run gradle tasks

    To see a list of command-line options, run gradle --help

    To see more detail about a task, run gradle help --task <task>

    BUILD SUCCESSFUL

    Total time: 1.435 secs

You now have Gradle installed.
Find out what Gradle can do

Before you even create a build.gradle file for the project, you can ask Gradle what tasks are available:

`gradle tasks`


You will use the gradle build task frequently. This task compiles, tests, and packages the code into an APK file. You can run it like this:

`gradle build`

###Build output

The build generates an APK for each build variant in the app/build folder: the app/build/outputs/apk/ directory contains packages named app-<flavor>-<buildtype>.apk; for example, 
app-full-release.apk and app-demo-debug.apk.
The APK file here is ready to be deployed to a device or emulator.
For information about gradle builds configuration read https://developer.android.com/intl/ru/tools/building/configuring-gradle.html

##Signing apk file

to be developed

#Deploying and Running Your App

You should also read

    Using Hardware Devices - http://developer.android.com/intl/ru/tools/device.html
   
How you run your app depends on two things: whether you have a real device running Android and whether you're using Android Studio. This doc shows you how to install and run your app on a real device and on the Android emulator, and in both cases with either Android Studio or the command line tools.

##Run on a Real Device

If you have a device running Android, here's how to install and run SEPclient app.
'Set up your device'

    Plug in your device to your development machine with a USB cable. If you're developing on Windows, you might need to install the appropriate USB driver for your device. For help installing drivers, see the OEM USB Drivers document.
    Enable USB debugging on your device.
        On most devices running Android 3.2 or older, you can find the option under Settings > Applications > Development.
        On Android 4.0 and newer, it's in Settings > Developer options.

        Note: On Android 4.2 and newer, Developer options is hidden by default. To make it available, go to Settings > About phone and tap Build number seven times. Return to the previous screen to find Developer options.

Run the app from Android Studio

    Select one of SEPclient project's files and click Run from the toolbar.
    In the Choose Device window that appears, select the Choose a running device radio button, select your device, and click OK .

Android Studio installs the app on your connected device and starts it.
Run the app from a command line

Open a command-line and navigate to the root of your project directory. Use Gradle to build your project in debug mode, invoke the assembleDebug build task using the Gradle wrapper script (gradlew assembleRelease).

This creates your debug .apk file inside the module build/ directory, named MyFirstApp-debug.apk.

On Windows platforms, type this command:

`> gradlew.bat assembleDebug`

On Mac OS and Linux platforms, type these commands:

`$ chmod +x gradlew`
`$ ./gradlew assembleDebug`

After you build the project, the output APK for the app module is located in app/build/outputs/apk/

Note: The first command (chmod) adds the execution permission to the Gradle wrapper script and is only necessary the first time you build this project from the command line.

Make sure the Android SDK platform-tools/ directory is included in your PATH environment variable, then execute:

`adb install app/build/outputs/SEPclient-debug.apk`

On your device, locate SEPclient and open it.

That's how you build and run your Android app on a device!
Run on the Emulator

Whether you're using Android Studio or the command line, to run your app on the emulator you need to first create an Android Virtual Device (AVD). An AVD is a device configuration for the Android emulator that allows you to model a specific device.
Create an AVD

    Launch the Android Virtual Device Manager:
        In Android Studio, select Tools > Android > AVD Manager, or click the AVD Manager icon in the toolbar.
        Or, from the command line, change directories to sdk/ and execute:

        tools/android avd

    On the AVD Manager main screen, click Create Virtual Device.
    In the Select Hardware window, select a device configuration, such as Nexus 6, then click Next.
    Select the desired system version for the AVD and click Next.
    Verify the configuration settings, then click Finish.

For more information about using AVDs, see firmware documentation about Managing AVDs with AVD Manager.
Run the app from Android Studio

    In Android Studio, select your project and click Run from the toolbar.
    In the Choose Device window, click the Launch emulator radio button.
    From the Android virtual device pull-down menu, select the emulator you created, and click OK.

It can take a few minutes for the emulator to load itself. You may have to unlock the screen. When you do, My First App appears on the emulator screen.
Run your app from the command line

    Build the project from the command line. The output APK for the app module is located in app/build/outputs/apk/.
    Make sure the Android SDK platform-tools/ directory is included in your PATH environment variable.
    Execute this command:

    adb install app/build/outputs/MyFirstApp-debug.apk

    On the emulator, locate MyFirstApp and open it.

