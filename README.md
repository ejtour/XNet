##使用介绍
step1 创建类实现 NetInfo 
step2 Application 中初始化设置 RetrofitApi
step3 创建类ErrorHandler 实现 IGlobalError（设置自己的业务异常逻辑）
```
 RetrofitApi.initNetInfo(HttpBaseInfo(),ErrorHandler())

```
step4 创建 Service 并继承 RetrofitApi

```
WanAndroidService : RetrofitApi("https://www.wanandroid.com/")

```

1.0.0
完成项目的基础引用
网络模块采用Retrofit+okHttp+moshi 
1.0.1
完成网络请求库的基本封装
支持多域名,自定义错误,拦截