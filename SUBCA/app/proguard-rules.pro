# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#-keep class org.apache.** { *; }
#
## Keep Firebase-specific classes and methods
#-keep class com.google.firebase.** { *; }
#-keep class com.google.android.gms.** { *; }
#-keep class com.google.firebase.messaging.** { *; }
#
## Keep GoogleCredentials and related classes
#-keep class com.google.auth.oauth2.** { *; }
#
## Keep classes annotated with @Keep or @IgnoreExtraProperties
#-keep @com.google.firebase.database.IgnoreExtraProperties class * { *; }
#-keep @com.google.firebase.messaging.FirebaseMessagingService class * { *; }
#-keep @com.google.firebase.messaging.RemoteMessage class * { *; }
#
#-keep class com.example.arjun.su_bca.R$raw { *; }
#
## Keep Volley related classes
#-keep class com.android.volley.** { *; }
#-keep class com.android.volley.toolbox.** { *; }
## Keep classes using reflection or specific method signatures
#-keep class com.example.arjun.su_bca.Utils.VolleyRequestQueue { *; }
#
## Keep data model classes (add your package name)
#-keepclassmembers class com.example.arjun.su_bca.** {
#    <fields>;
#    <methods>;
#}