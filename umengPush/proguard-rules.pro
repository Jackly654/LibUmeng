# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/ruichao/Tools/android-sdk-linux/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keep class com.umeng.message.* {
    public <fields>;
    public <methods>;
}

-keep class com.umeng.message.protobuffer.* {
	public <fields>;
    public <methods>;
}

-keep class com.squareup.wire.* {
	public <fields>;
    public <methods>;
}

-keep class org.android.agoo.impl.*{
	public <fields>;
    public <methods>;
}

-keep class org.android.agoo.service.* {*;}

-keep class org.android.spdy.**{*;}

-keep public class com.umeng.message.example.R$*{
   public static final int *;
}