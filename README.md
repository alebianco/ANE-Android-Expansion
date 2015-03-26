# ANE-Android-Expansion

Allow AIR applications for Android to break the 50mb limit and use expansion files.

## Introduction
Google Play currently requires that your APK file be no more than 50MB. For most applications, this is plenty of space for all the application's code and assets. However, some apps need more space for high-fidelity graphics, media files, or other large assets. Previously, if your app exceeded 50MB, you had to host and download the additional resources yourself when the user opens the app. Hosting and serving the extra files can be costly, and the user experience is often less than ideal. To make this process easier for you and more pleasant for users, Google Play allows you to attach two large expansion files that supplement your APK. One main expansion file and one 'patch' expansion file.

Google Play hosts the expansion files for your application and serves them to the device at no cost to you. The expansion files are saved to the device's shared storage location (the SD card or USB-mountable partition; also known as the "external" storage) where your app can access them. On most devices, Google Play downloads the expansion file(s) at the same time it downloads the APK, so your application has everything it needs when the user opens it for the first time. In some cases, however, your application must download the files from Google Play when your application starts.

This Adobe Air Expansion enables you to the use expansion files in ActionScript 3 projects.

## Usage

Declare (extra) permissions in your AIR application descriptor:

<manifest ...>
	<uses-sdk android:minSdkVersion="11" 
		android:targetSdkVersion="13" />
			    
	<uses-permission android:name="com.android.vending.CHECK_LICENSE" />
	<uses-permission android:name="android.permission.INTERNET" />
	<uses-permission android:name="android.permission.WAKE_LOCK" />
	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
	<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
			    
	<application>
		<receiver android:name="eu.alebianco.air.extensions.expansion.receivers.XAPKAlarmReceiver"></receiver>
		<service android:name="eu.alebianco.air.extensions.expansion.services.XAPKDownloaderService"></service>
	</application>

</manifest>


ActionScript initial setup:

```actionscript
// get your API key from your Google store publisher account
var PUBLIC_KEY:String = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAl7ARg/syoLy9O9RQgiYiHQXE/gj4YxkGDeGxBmPZuE/XNAHHZt7Ur1QHXK9skFJs0wwTnBF0jKYaspHLp42SlifT5zoTd/+4csFS8sSO8HO/AECirVYzT6X5cqhUU4ftGFErzvuJfNXn7xm5zeVCnAji7+nT8Q36PH7JopYfQkBAzxJvvyoPapTdV8ta0Nr9bjW6/T87pe7TKECXVbKPAU6/BME+YBfdLzMLV3AEEndBJNUOTdOdbwcRdk+JoJgnWqMNll6AKHn/1dph45zfyDNYQUrY1yRrZRiz9C0sSJcpmQsKzRZUrrKk+qNqgCAKeeGgCLGiie7ASBdLnn0lFQIDAQAB";
// randomly generate 20 bytes. You can use a generator: https://www.random.org/integers/?num=20&min=-255&max=255&col=5&base=10&format=html&rnd=new
var SALT:Vector.<int> = Vector.<int>([1, 42, -12, -1, 32, 98, -100, -12, 43, 8, -8, -4, 9, 5, -106, -107, -33, 5, -1, 84]);

var manager:ExpansionManager = ExpansionManager.getInstance();
if (ExpansionManager.isSupported()) {

	// initialise security
    manager.setupMarketSecurity(PUBLIC_KEY, SALT);
    // define which files you'll need. this is mandatory and you need to specify version and filesize (this is the filesize on disk)
    manager.addRequiredExpansion(new ExpansionFile(ExpansionType.MAIN, 1, 203871335));
	
	// register events to track download and/or ZIP extraction completion
	manager.addEventListener(DownloadCompleteEvent.COMPLETE, onDownloadComplete);
    manager.addEventListener(manager, ExtractionCompleteEvent.COMPLETE, onExtractionComplete);
	
	// check if the expansion files have been downloaded
    var hasExpansions:Boolean = manager.hasExpansionFiles();
    if (hasExpansions) {
    	// files are there. Unzip them
		unzip();
    } else {
        manager.download();
    }
}

function unzip() : void {
    manager.unzip(File.applicationStorageDirectory.nativePath, false);
}

function onDownloadComplete(event:DownloadCompleteEvent):void {
	// when downloaded, start unzipping
    unzip();
}

function onExtractionComplete(event:ExtractionCompleteEvent):void {
    trace("all done");
}
```

## Building

Requirements:
* Apache Flex SDK 4.12 or later
* Google Android SDK 2.2

Add the _FlashRuntimeExtensions.jar_ file from the Adobe Air SDK to the _source/android/air_extension/libs_ folder
In the **build** folder, make a copy of the _user.properties.eg_ file and call it _user.properties_  
Edit that file to provide values specific to your system  
Use the `build.ant` ant script you'll find in the **build** folder, to build the project

## Possible problems

Keep in mind that:
- AIR adds 'air.' to your package name.
- The AIR versionNumber is translated to the Android integer versionCode using the formula: a*1000000 + b*1000 + c, where a, b, and c are the components of the AIR version number: a.b.c.

If your package name is
com.yourdomain.newapp

And your VersionCode is 1.0.1

Your .obb file will be:
main.1000001.air.com.yourdomain.newapp.obb

## Further reading

Android expansion files: http://developer.android.com/google/play/expansion-files.html

## Contributing

If you want to contribute to the project refer to the [CONTRIBUTING.md](CONTRIBUTING.md) document for guidelines.

## Roadmap

You can follow the project planning on [Trello](https://trello.com/b/5e1dI2Fn), you can even vote for the tasks that are more important for you and you'll like to see implemented.