## 使用介绍

step1: 创建类实现 NetInfo 

step2: Application 中初始化设置 RetrofitApi

step3: 创建类ErrorHandler 实现 IGlobalError（设置自己的业务异常逻辑）
```
 RetrofitApi.initNetInfo(HttpBaseInfo(),ErrorHandler())

```
step4: 创建 Service 并继承 RetrofitApi

```
WanAndroidService : RetrofitApi("https://www.wanandroid.com/")

```
# 1.0.1
完成网络请求库的基本封装
支持多域名,自定义错误,拦截
# 1.0.0
完成项目的基础引用
网络模块采用Retrofit+okHttp+moshi 
引用第三方库

    implementation  "com.squareup.moshi:moshi:1.14.0"
    //Okhttp
    implementation  "com.squareup.okhttp3:logging-interceptor:4.9.1"
    //Permission
    api 'com.yanzhenjie:permission:2.0.2'
    //livedata 解决数据倒灌问题
    api 'com.kunminx.arch:unpeek-livedata:7.8.0'
    //第三方recyclerview
    api 'com.yanzhenjie.recyclerview:x:1.3.2'
    //BaseAdapter
    api 'com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.4'

    //dialog
    api 'com.afollestad.material-dialogs:core:3.3.0'
    api 'com.afollestad.material-dialogs:bottomsheets:3.3.0'
    api 'com.afollestad.material-dialogs:lifecycle:3.3.0'
