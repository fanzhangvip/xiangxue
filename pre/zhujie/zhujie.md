#### 什么是注解？
* 对于很多初次接触的开发者来说应该都有这个疑问？Annontation是Java5开始引入的新特征，中文名称叫注解。它提供了一种安全的类似注释的机制，用来将任何的信息或元数据（metadata）与程序元素（类、方法、成员变量等）进行关联。为程序的元素（类、方法、成员变量）加上更直观更明了的说明，这些说明信息是与程序的业务逻辑无关，并且供指定的工具或框架使用。Annontation像一种修饰符一样，应用于包、类型、构造方法、方法、成员变量、参数及本地变量的声明语句中。
* Java注解是附加在代码中的一些元信息，用于一些工具在编译、运行时进行解析和使用，起到说明、配置的功能。注解不会也不能影响代码的实际逻辑，仅仅起到辅助性的作用。包含在 java.lang.annotation 包中。
#### 注解的用处
1. 生成文档。这是最常见的，也是java 最早提供的注解。常用的有@param @return 等
2. 跟踪代码依赖性，实现替代配置文件功能。比如Dagger 2依赖注入，未来java开发，将大量注解配置，具有很大用处;
3. 在编译时进行格式检查。如@override 放在方法前，如果你这个方法并不是覆盖了超类方法，则编译时就能检查出。
#### 注解的原理
注解本质是一个继承了Annotation的特殊接口，其具体实现类是Java运行时生成的动态代理类。而我们通过反射获取注解时，返回的是Java运行时生成的动态代理对象$Proxy1。通过代理对象调用自定义注解（接口）的方法，会最终调用AnnotationInvocationHandler的invoke方法。该方法会从memberValues这个Map中索引出对应的值。而memberValues的来源是Java常量池
#### 元注解
* java.lang.annotation提供了四种元注解，专门注解其他的注解（在自定义注解的时候，需要使用到元注解）：
   1. @Documented –注解是否将包含在JavaDoc中
   2. @Retention –什么时候使用该注解
   3. @Target –注解用于什么地方
   4. @Inherited – 是否允许子类继承该注解

##### @Retention– 定义该注解的生命周期
     ●   RetentionPolicy.SOURCE : 在编译阶段丢弃。这些注解在编译结束之后就不再有任何意义，所以它们不会写入字节码。@Override, @SuppressWarnings都属于这类注解。
     ●   RetentionPolicy.CLASS : 在类加载的时候丢弃。在字节码文件的处理中有用。注解默认使用这种方式
     ●   RetentionPolicy.RUNTIME : 始终不会丢弃，运行期也保留该注解，因此可以使用反射机制读取该注解的信息。我们自定义的注解通常使用这种方式。

##### @Target – 表示该注解用于什么地方。默认值为任何元素，表示该注解用于什么地方。可用的ElementType参数包括
     ● ElementType.CONSTRUCTOR:用于描述构造器
     ● ElementType.FIELD:成员变量、对象、属性（包括enum实例）
     ● ElementType.LOCAL_VARIABLE:用于描述局部变量
     ● ElementType.METHOD:用于描述方法
     ● ElementType.PACKAGE:用于描述包
     ● ElementType.PARAMETER:用于描述参数
     ● ElementType.TYPE:用于描述类、接口(包括注解类型) 或enum声明

##### @Documented–一个简单的Annotations标记注解，表示是否将注解信息添加在java文档中。

##### @Inherited – 定义该注释和子类的关系
        @Inherited 元注解是一个标记注解，@Inherited阐述了某个被标注的类型是被继承的。如果一个使用了@Inherited修饰的annotation类型被用于一个class，则这个annotation将被用于该class的子类。
#### 常见标准的Annotation：
  1. Override
      * java.lang.Override是一个标记类型注解，它被用作标注方法。它说明了被标注的方法重载了父类的方法，起到了断言的作用。如果我们使用了这种注解在一个没有覆盖父类方法的方法时，java编译器将以一个编译错误来警示。
  2. Deprecated
      * Deprecated也是一种标记类型注解。当一个类型或者类型成员使用@Deprecated修饰的话，编译器将不鼓励使用这个被标注的程序元素。所以使用这种修饰具有一定的“延续性”：如果我们在代码中通过继承或者覆盖的方式使用了这个过时的类型或者成员，虽然继承或者覆盖后的类型或者成员并不是被声明为@Deprecated，但编译器仍然要报警。
  3. SuppressWarnings
      * SuppressWarning不是一个标记类型注解。它有一个类型为String[]的成员，这个成员的值为被禁止的警告名。对于javac编译器来讲，被-Xlint选项有效的警告名也同样对@SuppressWarings有效，同时编译器忽略掉无法识别的警告名。
　　@SuppressWarnings("unchecked")

#### 自定义注解
  1. Annotation型定义为@interface, 所有的Annotation会自动继承java.lang.Annotation这一接口,并且不能再去继承别的类或是接口.
  2. 参数成员只能用public或默认(default)这两个访问权修饰
  3. 参数成员只能用基本类型byte,short,char,int,long,float,double,boolean八种基本数据类型和String、Enum、Class、annotations等数据类型,以及这一些类型的数组.
  4. 要获取类方法和字段的注解信息，必须通过Java的反射技术来获取 Annotation对象,因为你除此之外没有别的获取注解对象的方法
  5. 注解也可以没有定义成员, 不过这样注解就没啥用了
* PS:自定义注解需要使用到元注解
#### 高级用法
* 增加数组类型的属性：int[] arrayAttr() default {1,2,4};
* 应用数组类型的属性：@MyAnnotation(arrayAttr={2,4,5})
* 如果数组属性只有一个值，这时候属性值部分可以省略大括号，如：@MyAnnotation(arrayAttr=2)，这就表示数组属性只有一个值，值为2
#### 注解的处理
* Java在java.lang.reflect 包下新增了AnnotatedElement接口，该接口主要有如下几个实现类：
```
    //如果存在这样的注解，则返回该元素的指定类型的注解，否则为空。
    <T extends Annotation> T getAnnotation(Class<T> annotationClass)
    //返回该程序元素上存在的所有注解。
    Annotation[] getAnnotations()
    //判断该程序元素上是否包含指定类型的注解，存在则返回true，否则返回false.
    boolean is AnnotationPresent(Class<?extends Annotation> annotationClass)
    //返回直接存在于此元素上的所有注释。与此接口中的其他方法不同，该方法将忽略继承的注释。（如果没有注释直接存在于此元素上，则返回长度为零的一个数组。）该方法的调用者可以随意修改返回的数组；这不会对其他调用者返回的数组产生任何影响。
    Annotation[] getDeclaredAnnotations()
```
---
#### 代理模式
* jdk为我们的生成了一个叫$Proxy0（这个名字后面的0是编号，有多个代理类会一次递增）的代理类，这个类文件时放在内存中的，我们在创建代理对象时，就是通过反射获得这个类的构造方法，然后创建的代理实例。通过对这个生成的代理类源码的查看，我们很容易能看出，动态代理实现的具体过程。
我们可以对InvocationHandler看做一个中介类，中介类持有一个被代理对象，在invoke方法中调用了被代理对象的相应方法。通过聚合方式持有被代理对象的引用，把外部对invoke的调用最终都转为对被代理对象的调用。
代理类调用自己方法时，通过自身持有的中介类对象来调用中介类对象的invoke方法，从而达到代理执行被代理对象的方法。也就是说，动态代理通过中介类实现了具体的代理功能。

* 生成的代理类：$Proxy0 extends Proxy implements ILawsuit，我们看到代理类继承了ILawsuit类，所以也就决定了java动态代理只能对接口进行代理，Java的继承机制注定了这些动态代理类们无法实现对class的动态代理。
上面的动态代理的例子，其实就是AOP的一个简单实现了，在目标对象的方法执行之前和执行之后进行了处理，对方法耗时统计。Spring的AOP实现其实也是用了Proxy和InvocationHandler这两个东西的。






































































































































