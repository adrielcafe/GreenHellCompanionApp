# Green Hell
-keepclasseswithmembers class cafe.adriel.greenhell.model.** { *; }

# Billing
-keep class com.android.vending.billing.**

# Kotlin
-dontwarn kotlin.**
-dontwarn org.jetbrains.annotations.**
-keepattributes EnclosingMethod
-keepattributes Signature
-keepattributes Annotation
-keep public class kotlin.reflect.jvm.internal.impl.builtins.* { public *; }
-keep public class kotlin.reflect.jvm.internal.impl.serialization.deserialization.builtins.* { public *; }
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# Firebase
-keep class io.grpc.** {*;}

# Crashlytics
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

# Okio
-dontwarn org.codehaus.mojo.animal_sniffer.*

# Moshi
-dontwarn javax.annotation.**
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
-keep @com.squareup.moshi.JsonQualifier interface *
-keepclassmembers @com.squareup.moshi.JsonClass class * extends java.lang.Enum {
    <fields>;
}
-keepnames @com.squareup.moshi.JsonClass class *
-if @com.squareup.moshi.JsonClass class *
-keep class <1>JsonAdapter {
    <init>(...);
    <fields>;
}
-keep class kotlin.reflect.jvm.internal.impl.builtins.BuiltInsLoaderImpl
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# EventBus
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }