-printmapping mapping.txt
-dontwarn com.airbnb.lottie.**
-keep class com.airbnb.lottie.** {*;}

-keep class android.content.pm.PackageInstaller.**
-keep class com.google.** { *;}

-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

-keep class com.facebook.android.** { *; }
-dontwarn com.facebook.android.**

-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

-keep class  allanksr.com.firebase.realtimeDatabase.** { *; }

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

#VOLLEY
-keepclassmembers,allowshrinking,allowobfuscation class com.android.volley.NetworkDispatcher {
    void processRequest();
}
-keepclassmembers,allowshrinking,allowobfuscation class com.android.volley.CacheDispatcher {
    void processRequest();
}

-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

-keepattributes EnclosingMethod
-keepattributes InnerClasses
