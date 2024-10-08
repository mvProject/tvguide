# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
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

-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn org.slf4j.impl.StaticMDCBinder
-dontwarn org.slf4j.impl.StaticMarkerBinder
-dontwarn org.graalvm.nativeimage.hosted.Feature
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE
-dontwarn edu.umd.cs.findbugs.annotations.SuppressFBWarnings
-dontwarn reactor.blockhound.integration.BlockHoundIntegration
-dontwarn com.oracle.svm.core.annotate.AutomaticFeature
-dontwarn com.oracle.svm.core.annotate.Delete
-dontwarn com.oracle.svm.core.annotate.TargetClass
-dontwarn java.awt.BorderLayout
-dontwarn java.awt.Color
-dontwarn java.awt.Component
-dontwarn java.awt.Container
-dontwarn java.awt.Dimension
-dontwarn java.awt.GraphicsEnvironment
-dontwarn java.awt.HeadlessException
-dontwarn java.awt.LayoutManager
-dontwarn java.awt.Rectangle
-dontwarn java.awt.Shape
-dontwarn java.awt.Window
-dontwarn java.awt.datatransfer.Transferable
-dontwarn java.awt.dnd.DragGestureListener
-dontwarn java.awt.dnd.DragSourceListener
-dontwarn java.awt.dnd.DragSourceMotionListener
-dontwarn java.awt.dnd.DropTargetListener
-dontwarn java.awt.event.AWTEventListener
-dontwarn java.awt.event.ActionListener
-dontwarn java.awt.event.ComponentListener
-dontwarn java.awt.event.HierarchyListener
-dontwarn java.awt.event.WindowAdapter
-dontwarn java.awt.geom.Area
-dontwarn java.awt.image.DataBuffer
-dontwarn java.awt.image.DataBufferByte
-dontwarn java.awt.image.DataBufferInt
-dontwarn java.awt.image.DirectColorModel
-dontwarn java.awt.image.MultiPixelPackedSampleModel
-dontwarn java.awt.image.Raster
-dontwarn java.awt.image.SampleModel
-dontwarn java.awt.image.SinglePixelPackedSampleModel
-dontwarn java.lang.instrument.ClassDefinition
-dontwarn java.lang.instrument.IllegalClassFormatException
-dontwarn java.lang.instrument.UnmodifiableClassException
-dontwarn java.lang.management.ManagementFactory
-dontwarn java.lang.management.RuntimeMXBean
-dontwarn javax.swing.Icon
-dontwarn javax.swing.JComponent
-dontwarn javax.swing.JLayeredPane
-dontwarn javax.swing.JPanel
-dontwarn javax.swing.JRootPane
-dontwarn javax.swing.RootPaneContainer
-dontwarn javax.swing.SwingUtilities
-dontwarn kotlinx.serialization.KSerializer
-dontwarn kotlinx.serialization.Serializable
-dontwarn kotlinx.serialization.SealedClassSerializer
-dontwarn kotlinx.serialization.descriptors.ClassSerialDescriptorBuilder
-dontwarn kotlinx.serialization.descriptors.PrimitiveKind$STRING
-dontwarn kotlinx.serialization.descriptors.PrimitiveKind
-dontwarn kotlinx.serialization.descriptors.SerialDescriptor
-dontwarn kotlinx.serialization.descriptors.SerialDescriptorsKt
-dontwarn kotlinx.serialization.internal.AbstractPolymorphicSerializer
-dontwarn kotlinx.serialization.internal.EnumSerializer
-dontwarn kotlinx.serialization.internal.IntSerializer
-dontwarn kotlinx.serialization.internal.LongSerializer
-dontwarn kotlinx.serialization.internal.ShortSerializer

# Retain service method parameters when optimizing.
-keep,allowobfuscation,allowoptimization interface * {
    @retrofit2.http.* <methods>;
}

-keep class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-keepattributes Signature
-keep class com.google.gson.reflect.TypeToken

-keepclassmembernames class com.mvproject.tvprogramguide.data.** {
    public ** component1();
    <fields>;
}

-keep,allowobfuscation,allowshrinking @dagger.hilt.EntryPoint class *