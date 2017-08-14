DeviceControl演示程序

生成的apk需要使用系统签名

系统签名方法：

java -jar signapk.jar platform.x509.pem platform.pk8 app-release.apk ControlSigned.apk

