# Polygon_Android_API
an Android kotlin Polygon UI View component 

iOS version API: https://github.com/xattacker/Polygon_iOS_API<br>

present a custom defined polygon view:<br>
<img src="/rm_res/cut1.png" alt="图片替换文本" width="50%" height="50%" align="bottom" /><br><br>
The API could load data from code for json file(json parsing with gson library)
<br>and supports event callback


### Setup:

minSdkVersion: 14 

``` 
allprojects {
    repositories {
        ...
		mavenCentral()
		maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.xattacker:Polygon:1.0.0'
}
``` 
