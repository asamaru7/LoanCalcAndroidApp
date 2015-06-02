# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/yyj/Library/Android/sdk/tools/proguard/proguard-android.txt
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

-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

-libraryjars libs

# support v7
-keep class android.support.v7.** { *; }

# android annotation
-dontwarn org.androidannotations.api.rest.**
-dontwarn org.springframework.http.**

# Keep all the ACRA classes
-keep class org.acra.** { *; }

# realm
-keepnames public class * extends io.realm.RealmObject
-keep class io.realm.** { *; }
-dontwarn javax.**
-dontwarn io.realm.**

# chunk
-dontwarn  com.x5.**
-keep class org.cheffo.** {*;}
-keep class sun.misc.** {*;}
-keep class com.madrobot.** {*;}
-keep class net.minidev.json.** {*;}
-keep class com.x5.template.** { *; }
-keep class net.minidev.** { *; }
-keep class com.x5.util.** { *; }

# admob
-keep public class com.google.android.gms.ads.** {
   public *;
}

-keep public class com.google.ads.** {
   public *;
}

# -------------------------------------------------------
# bootstrap

# android-iconify
-keep class com.joanzapata.** { *; }

# otto
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}