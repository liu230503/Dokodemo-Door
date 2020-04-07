# Dokomedo Door 使用手册
![](https://img.shields.io/badge/license-Apache--2.0-blue.svg)
![](https://img.shields.io/badge/minSDK-16+-orange.svg)
![](https://img.shields.io/badge/aarSize-100KB-blue.svg)

项目地址： https://github.com/liu230503/Dokodemo-Door.git 

本框架是以**AOP**思想实现的一款Android端用于管理`Fragment`栈的框架。无需继承任何基类即可使用。

annotation-v7|dokodemoDoor-v7|annotation-x|dokodemoDoor-x
--|:--:|--:|--:|
1.2.0|1.2.0|1.2.0|1.2.0

## 解决痛点

 1. 使`Fragment` 的入栈出栈、显示隐藏变得更容易.
 2. 解决`ViewPager`嵌套`Fragment`时造成的初始化慢问题，本框架支持懒加载.
 3. 解决`Fragment` 嵌套`Fragment` 时栈管理困难问题.
 4. 让`Fragment` 使用起来更加简单.

 ## 使用

 #### 1.使用须知

   1. Dokomedo Door 支持` Support-V7 `与`AndroidX` 包下的`Fragment `与`FragmentActivity`.但不支持`android.app`包下的`Fragment`.
   2. 本框架是基于HuJian适配的可用于`Android`平台的`AspectJX`开发，在使用时如想缩短编译时间可参考 [AspectJX-HuJian]("https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx") 进行配置.


 #### 2.添加依赖

1. 在工程的`build.gradle`文件中添加

```gradle
buildscript {
    repositories {
    	maven { url "https://jitpack.io" }
    }
    dependencies {
        ...
        classpath 'org.aspectj:aspectjtools:1.8.13'
        classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.1'
    }
    allprojects {
    	maven { url "https://jitpack.io" }
    }
}
```

2. 在项目`app`的`build.gradle`文件中添加

```gradle
apply plugin: 'android-aspectjx'
android {
	...
}
```

3. 在需要支持的`Module`的`build.gradle`文件中添加

```gradle
dependencies {
    implementation 'com.github.liu230503:dokodemoDoor-annotation-v7:TAG'
    implementation 'com.github.liu230503:dokodemoDoor-v7:TAG'
    ...
}
```

#### 3.Dokomedo Door的注解

1. `@Node` 用于标记一个可被管理的节点，该注解可被继承，只可作用于类上且只支持`FragmentActivity`与`Fragment`.
> `containerViewId` 可选参数.
>
> >此处需要传入`Fragment`在添加时候占用的容器的`id`，作为`Fragment`在添加的时候代替的位置.如果是小于等于0，`Fragment`将不会代替任何位置.默认值是0.

> `stickyStack` 可选参数
>
> > 如果值为`true`: 当被操纵的节点(`Activity`/`Fragment`)中的栈底`Fragment`和栈顶`Fragment`是同一个元素并且该`Fragment`被关闭的时候，这个宿主类也会被关闭，并且栈顶的`Fragment`不会执行退出转场动画.
> > 如果值为`false`: 当寄主类内部的最后一个`Fragment`出栈的时候，宿主类不会做任何动作.并且所有的转场动画都是正常执行的。

```java
@Node
public abstract class BaseFragmentActivity extends FragmentActivity {
	...
}

@Node(containerViewId = R.id.contentFrame)
public class MainActivity extends FragmentActivity {
    ...
}

@Node
public abstract class BaseFragment extends Fragment {
    ...
}

@Node(containerViewId = R.id.contentFrame,stickyStack = true)
public class MainFragment extends Fragment {
    ...
}
```


2. `@Animation` 用于标记一个添加了`@Node`注解的节点，该注解可被继承.
	
	>`enterAnim` 入场的动画，主要是对于新加进来的`Fragment`执行动画.
	>`exitAnim` 出场动画，主要是对于关闭`Fragment`执行动画。需要注意的是当调用`replace`的时候，前一个`Fragment`也会执行退出动画.
	>`popEnterAnim `当调用`addToBackStack()`方法后，当前的`Fragment`退出返回上一个`Fragment`，上一个`Fragment`就是`popEnter`，这时候它就会执行这个动画。这个动画对于新创建的`Fragment`无效.
	>`popExitAnim` 当前`Fragment`在`back stack`中，并且出栈的时候会执行`popExit`动画.

```java
@Animator(enter = R.anim.push_left_in_no_alpha, exit = R.anim.push_right_out_no_alpha,
    popEnter = R.anim.push_right_in_no_alpha, popExit = R.anim.push_left_out_no_alpha)
@Node
public abstract class BaseFragment extends Fragment{
	...
}
```

3. `LoadModel` 用于标记一个添加了`@Node`注解的节点，该注解不可被继承。
	
	> `loadModel `必须使用`EnumLoadModel`中定义的元素.
	>
	> > `EnumLoadModel.NORMAL_LOAD `表示普通加载.
	> > `EnumLoadModel.LAZY_LOAD` 表示懒加载.
	
	```java
	@LoadModel(loadModel = EnumLoadModel.LAZY_LOAD)
	public class ItemFragment extends BaseFragment {
	    ...
	}
	```
#### 4.管理Fragment

 	1. add 方法：
	
	> `addFragment(@IdRes int containerViewId, Fragment... fragments)`用于在一个容器中同一添加`Fragment`

```java
Fragment fragments[] = new Fragment[4];
DokodemoDoor.getNodeProxy(this).addFragment(R.id.contentFrame, fragments);
```

2. show 方法：
	> `showFragment(@NonNull Fragment fragment, @IdRes int containerViewId)`  用于对指定容器中的一个`Fragment `进行显示，同时隐藏该容器中其他`Fragment`.如果容器中不包含当前`Fragment`则会先调用`add`方法进行添加.
	

```java
DokodemoDoor.getNodeProxy(this).showFragment(new TestFragment(), R.id.contentFrame);
```
>  `void showFragment(@NonNull Fragment fragment, @IdRes int containerViewId, boolean showRepeatAnim) `用于对指定容器中的一个`Fragment `进行显示，同时隐藏该容器中其他`Fragment`.如果容器中不包含当前`Fragment`则会先调用`add`方法进行添加. `showRepeatAnim` 如果为`true`,则如果当前要显示的`Fragment` 已经显示，则触发该`Fragment` 的动画。

```java
DokodemoDoor.getNodeProxy(this).showFragment(new TestFragment(),R.id.contentFrame, true);
```
> `void showFragment(@NonNull String tag) `显示一个已经被添加的`Fragment`同时隐藏其所属宿主容器中包含的其他`Fragment`，如果该`Fragment`已经显示，则什么也不做。如果该Tag对应的`Fragment`还未添加会抛出一个`NotExistException`.

```java
Fragment fragments[] = new Fragment[4];
DokodemoDoor.getNodeProxy(this).addFragment(R.id.contentFrame, fragments);
DokodemoDoor.getNodeProxy(this).show(DokodemoDoor.getNodeProxy(fragments[0]).getFragmentTag());
```
> `void showFragment(@NonNull String tag, boolean showRepeatAnim) `同上.

3. hide 方法：

  > `hide`方法和`show`方法对应，用法也比较类似，不同的是，`hide`的`fragment`必须已经存在.

>>  `void hideFragment(@NonNull Fragment fragment)`

```java
DokodemoDoor.getNodeProxy(this).hideFragment(fragments[0]);
```

>>  `void hideFragment(@NonNull String tag)`

```java
DokodemoDoor.getNodeProxy(this).hideFragment(DokodemoDoor.getNodeProxy(fragments[0]).getFragmentTag());
```

4. start  方法：

> `void startFragment(@NonNull Fragment fragment) `方法是对`add`与`show`方法的整合。其作用为添加并显示一个`Fragment`，同时隐藏起宿主容器内的其他`Fragment`.
>
> > 需要注意的是如果调用`startFragment`的节点在`@Noed`注解中提供的`containerViewId`大于0，那么`start`的`Fragment`将被添加到此类的栈中。如果提供的`containerViewId`小于等于0，那么将会添加至当前节点所处容器的持有者的栈中.

```java
@Node(containerViewId = R.id.contentFrame)
public class MainActivity extends FragmentActivity {
    DokodemoDoor.getNodeProxy(this).startFragment(new TestFragment1());
}
```

```
@Node()
public class TestFragment1 extends Fragment {
     DokodemoDoor.getNodeProxy(this).startFragment(new TestFragment2());
}
```

```java
@Node()
public class TestFragment2 extends Fragment {
    ...
}
```

> > 此时`TestFragment1`与`TestFragment2`都在`MainActivity`所持有的栈中,并且所使用的容器都是 `R.id.contentFrame`

```java
@Node(containerViewId = R.id.contentFrame1)
public class MainActivity extends FragmentActivity {
    DokodemoDoor.getNodeProxy(this).startFragment(new TestFragment1());
}
```

```java
@Node(containerViewId = R.id.contentFrame2)
public class TestFragment1 extends Fragment {
     DokodemoDoor.getNodeProxy(this).startFragment(new TestFragment2());
}
```

```java
@Node()
public class TestFragment2 extends Fragment {
    ...
}
```

> > 此时`TestFragment1` 在 `MainActivity `所持有的栈中,并且所使用的容器是 `R.id.contentFrame1`
> > `TestFragment2 `在 `TestFragment1 `所持有的栈中，并且所使用的容器是 `R.id.contentFrame2`



5.  startForResult 方法：

	> `void startFragmentForResult(Object receive, @NonNull Fragment fragment, int requestCode)`
	>
	> > 效果同`startFragment`，但可以将一个`requestCode`传给将要显示的`Fragment`，将要显示的`Fragment`可以在退栈时调用`setResult(int resultCode, Bundle bundle)`函数将数据返回给上一页面.类似于`Activity`的`startActivityForResult`函数

```java
@Node()
public class TestFragment1 extends Fragment {
     DokodemoDoor.getNodeProxy(this).startFragmentForResult(this,new TestFragment2(),0x1001);
     // 用于接收回调数据的函数
     public void onFragmentResult(int requestCode, int resultCode, Bundle args) {
     		
     }
}
```

```java
@Node()
public class TestFragment2 extends Fragment {
	Bundle bundle = new Bundle();
	DokodemoDoor.getNodeProxy(this).setResult(DokodemoDoor.FRAGMENT_RESULT_OK, bundle);
  	DokodemoDoor.getNodeProxy(this).close();
}
```



6. replace  方法：

	> 需要注意`replace`方法实际上是`add`和`show`方法的合并，可以保证在使用的时候，一个容器中只存在一个`Fragment`.
	> > `void replaceFragment(@NonNull Fragment fragment)` 显示一个`Fragment `并移除宿主容器中其他`Fragment`.如果`Fragment` 已经显示了什么也不会做.该方法添加的`Fragment`不会入栈，所以调用`onBackPressed`时该`Fragment `没有任何操作.
	> > `void replaceFragment(@NonNull Fragment fragment, @IdRes int containerViewId)` 同上，可以指定一个容器.
	

```java
@Node(containerViewId = R.id.contentFrame)
public class MainActivity extends FragmentActivity {
    DokodemoDoor.getNodeProxy(this).replaceFragment(new TestFragment1());
}
```

```java
@Node
public class TestFragment1 extends Fragment {
     DokodemoDoor.getNodeProxy(this).replaceFragment(new TestFragment2(),DokodemoDoor.getNodeProxy(DokodemoDoor.getNodeProxy(this).getHost()).getContainerViewId());
}
```



7. close  方法：
	
	> 本框架默认是监听`onBackPressed`函数来处理`Fragment`退栈。调用`close`函数也可以实现退栈.
	> 退栈默认的操作是移除当前的栈顶节点，并对宿主的下一个栈顶节点进行显示.
	>
	> > 当持有容器的节点所提供的`stickyStack = true`,退栈时，当栈内节点为空或者只剩一个，宿主会跟着出栈或者`finish`，此时最后一个栈顶节点不执行转场动画.
	> > 当持有容器的节点所提供的`stickyStack = false`,退栈时，只有在栈内节点为空，宿主自己才会出栈或者`finish`，所有的栈内节点都会执行转场动画.


```java
@Node
public class TestFragment1 extends Fragment {
     DokodemoDoor.getNodeProxy(this).close();
}
```

```java
@Node(containerViewId = R.id.contentFrame)
public class MainActivity extends FragmentActivity {
    DokodemoDoor.getNodeProxy(this).close(fragments[0]);
}
```

#### 5.Fragment Tag

 添加了`@Node` 注解的`Fragment` 在构造函数执行时，Dokodemo Door会为期分配一个默认的Tag,如果想要自定义Tag 本框架提供了两种方式.

 > `void setFragmentTag(@NonNull String tag)`
 >
 > > 此方法需要在`Fragment`创建完成之后,并且在使用Dokodemo Door操纵`Fragment`之前才会生效.

```java
@Node(containerViewId = R.id.contentFrame)
public class MainActivity extends FragmentActivity {
	TestFragment1 fragment = new TestFragment1();
	DokodemoDoor.getNodeProxy(fragment).setFragmentTag("123");
    DokodemoDoor.getNodeProxy(this).addFragment(R.id.contentFrame,fragment);
}
```

> `public String getFragmentTag() `
>
> > 需要在自定义Tag的`Fragment` 中添加此函数,并返回一个不为空的`String`对象。此方法优先级低于方法1.

```java
@Node
public class TestFragment1 extends Fragment {
     public String getFragmentTag(){
     	return "123";
     }
}
```

#### 5.LoadModel

​	在使用`ViewPager`+`Fragment` 时会出现同时将多个`Fragment`同时初始化导致页面加载时间过长，对用户不友好。部分数据只有在显示时才需要加载。本框架提供的**懒加载**模式即可解决这一问题.

1. 在需要使用懒加载的`Fragment`中添加`@LoadModel`注解,并赋值`loadModel = EnumLoadModel.LAZY_LOAD`

```java
@LoadModel(loadModel = EnumLoadModel.LAZY_LOAD)
public class TestFragment extends Fragment {
    ...
}
```

2. 在需要实现**懒加载**的`Fragment`中添加 `public void onLazyLoadViewCreated(Bundle savedInstanceState)`函数.

```java
@LoadModel(loadModel = EnumLoadModel.LAZY_LOAD)
public class TestFragment extends Fragment {
 
    public void onLazyLoadViewCreated(Bundle savedInstanceState) {
       // TODO 该函数只会在页面首次显示时被调用，可以在此处处理要显示的内容
    }
}
```

#### 6.Fragment 退栈拦截

> 本框架对`Fragment`的出栈操作是通过`onBackPressed`方法，但是在某些时候，我们需要拦截这个方法并进行一些额外的处理，本框架对这种场景提供了支持.
>
> > `boolean onInterruptBackPressed()` 此方法用于标识是否拦截继续向子节点传递`onInterruptBackPressed()`方法.

```java
@Node
public class TestFragment1 extends Fragment {
	public Boolean onInterruptBackPressed() {
      	// 需要拦截，则返回true，否则返回false
        return false;
    }
}
```

> > `boolean onNodeBackPressed()` 此方法用于拦截退栈事件,返回值标识了是否继续向上层传递`onNodeBackPressed()`方法.

```java
@Node
public class TestFragment1 extends Fragment {
	public Boolean onNodeBackPressed() {
        // 需要拦截，则返回true，否则返回false
        return false;
    }
}
```

#### 7.注解扩展

> 在`application`的`Module`中，我们可以使用注解来声明一些变量，但是在`library`的`Module`中，`R`中的数据并不是最终常量，所以本框架提供的一些注解就不可以传入值了。为了解决此问题，本框架提供了`Api`来扩展注解.

1. `@Node `中的 `containerViewId` 可以通过在类中添加 `public Integer getContainerViewId()` 函数来实现相同效果.

```java
@Node
public class MainActivity extends FragmentActivity {
	public Integer getContainerViewId(){
        return R.id.contentFrame;
    }
}
```

2. `@Animation` 中的参数可以通过在节点中添加 `public int[] getNodeAnimations()` 函数来实现相同效果.

```java
@Node
public class MainActivity extends FragmentActivity {
	public int[] getNodeAnimations(){
        // 此函数返回值必须为一个不为null 的整形数组，且数组长度不得小于4，如不需要对应动画，请传0.
		return new int[]{ R.anim.enter,R.anim.exit,R.anim.popEnter,R.anim.popExit};
	}
}
```

#### 8.代码混淆注意事项

> 需要用到代码混淆时，请在混淆规则文件中添加以下规则

```pro
keep class org.alee.dokodemo.door.core.DokodemoDoor.** {*;}
-keep interface org.alee.dokodemo.door.core.IProxy.** {*;}

-keep public class * extends android.app.Activity
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends androidx.app.Activity
-keep public class * extends androidx.app.Fragment
-keepclassmembers class * extends android.app.Activity {
   public Integer getContainerViewId();
   public Boolean onNodeBackPressed();
   public void onFragmentResult(int,int,android.os.Bundle);
   public Boolean onInterceptBackPressed();
   public void onLazyLoadViewCreated(android.os.Bundle);
   public int[] getNodeAnimations();
   public String getFragmentTag();
}
-keepclassmembers class * extends android.support.v4.app.Fragment {
   public Integer getContainerViewId();
   public Boolean onNodeBackPressed();
   public void onFragmentResult(int,int,android.os.Bundle);
   public Boolean onInterceptBackPressed();
   public void onLazyLoadViewCreated(android.os.Bundle);
   public int[] getNodeAnimations();
   public String getFragmentTag();
}

-keepclassmembers class * extends androidx.app.Activity {
   public Integer getContainerViewId();
   public Boolean onNodeBackPressed();
   public void onFragmentResult(int,int,android.os.Bundle);
   public Boolean onInterceptBackPressed();
   public void onLazyLoadViewCreated(android.os.Bundle);
   public int[] getNodeAnimations();
   public String getFragmentTag();
}

-keepclassmembers class * extends androidx.app.Fragment {
   public Integer getContainerViewId();
   public Boolean onNodeBackPressed();
   public void onFragmentResult(int,int,android.os.Bundle);
   public Boolean onInterceptBackPressed();
   public void onLazyLoadViewCreated(android.os.Bundle);
   public int[] getNodeAnimations();
   public String getFragmentTag();
}
```

#### 9.其他问题

1. 遇到`Gradle`编译不过的问题可以尝试`clean Build` 后再试.
2. 遇到其他未知问题请联系作者 l15040565660@gmail.com .
