# 集成Flutter到现有工程

[TOC]

## 前言
相信对于绝大多数应用来说，从零开始重新做一个App的成本是相当高的，所以混合开发成了它们尝试Flutter的首选。

## 目前的混合方案

### 闲鱼模式

双Branch共存（Flutter模式 && Standalone模式）
Standalone模式：纯Native开发或者是平台打包下的模式
Flutter模式：进行flutter相关功能的开发，库生成，编译和调试走的都是Flutter定义的流程。

* 优点

在Standalone模式下，纯Native开发者和打包平台，对Flutter是无感知的。在这种情况下，Flutter相关的代码可以认为是一个常规的第三方库文件。


* 前期准备
理清Standalone模式下对Flutter的依赖并将它们提取成一个aar库。


* 开发步骤

在Flutter模式下进行flutter相关功能的开发。
将代码打包成一个aar库上传到repository中去进行版本控制。
切换分支到Standalone模式，修改相关依赖包的版本号。

当然这种方法在实际的开发过程中还会遇到很多其他问题，比如复杂流程下生成aar库脚本的编写，比如两个模式下的代码同步等。

## Google模式
对与方便的进行Flutter的混合模式开发呼声有多高，Google专门为了这个问题建立了Wiki并且进行了持续4个月42个版本的更新。
创建Flutter Module模式
### Step 1: 创建Flutter module模版
```
flutter create -t module flutter_module
```
复制代码这个时候会看到project中新增加了一个flutter_module，其中包含了.android,.ios和关键的include_flutter.groovy文件
### Step 2: 将Flutter添加到现有工程中


在android工程的根目录的settings.gradle中添加,这里有坑？
```
include ':app'                                     // assumed existing content
setBinding(new Binding([gradle: this]))                                 // new
evaluate(new File(                                                      // new
  settingsDir.parentFile,                                               // new
  'flutter_module/.android/include_flutter.groovy'                      // new
)) 
```

在app的build.gradle中添加依赖
```
dependencies {
  implementation project(':flutter')
```

简单的说，Google在兼顾维护成本和开发成本的前提下，为了Insert  flutter module ,建立了一个逻辑依赖链
```
flutter_module/.android/include_flutter.groovy ->
flutter_module/.android/Flutter/build.gradle ->
$flutterRoot/packages/flutter_tools/gradle/flutter.gradle 
```
利用``` $flutterRoot/packages/flutter_tools/lib/```中的```flutter command``` &&
```$AndroidRoot/build-tools/buildToolsVersion/```中的```android command```
完成混合开发模式下的打包操作。
