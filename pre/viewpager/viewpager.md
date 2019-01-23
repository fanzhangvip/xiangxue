https://blog.csdn.net/sinat_34383316/article/details/75332415

### ViewPager的使用
* 一、ViewPager简介：
android.support.v4.view.ViewPager中的一个常用的控件。允许数据页从左到右或者从右到左翻页，因此这种交互也备受设计师的青睐。在APP中的很多场景都用得到，比如第一次安装APP时的用户引导页、图片浏览时左右翻页、广告Banner页等等都会用到ViewPager。

* 二、ViewPager显示的对象
ViewPager显示的对象是View及其子类，自然包括各种布局及ImageView对象等。
```
LayoutInflater inflater=getLayoutInflater();
view = inflater.inflate(R.layout.pager1, null);

ImageView image=new ImageView(this);
image.setImageResource(imageIds[i]);
```
ViewPager可以显示Activity，首先将Activity转换成View对象：
```
LocalActivityManager lam = new LocalActivityManager(this, true);
lam.dispatchCreate(savedInstanceState);//必须被调用
Intent intent = new Intent(this, MainActivity.class);
    View mainView = lam.startActivity("MainActivity", intent).getDecorView();
```
ViewPager可以显示Fragment，会用到FragmentPagerAdapter，其实FragmentPagerAdapter就是PagerAdapter的子类。
```
    List<Fragment> fragments=new ArrayList<>();
    fragments.add(new BlankFragment());
    fragments.add(new PlusOneFragment());
    fragments.add(new ItemFragment());

    FragAdapter adapter=new FragAdapter(getSupportFragmentManager(),fragments);

    ViewPager viewPager= (ViewPager) findViewById(R.id.viewpager);
    viewPager.setAdapter(adapter);
```
* 三、XML布局中添加
1.使用系统自带的标题栏的ViewPager
下面先介绍一下系统自带的两种标题栏：

ViewPager用来实现标题栏的两个子控件
```
android.support.v4.view.PagerTitleStrip
android.support.v4.view.PagerTabStrip
```
注：PagerTabStrip是继承PagerTitleStrip实现的

共同点：
都实现了标题栏的效果
通过android:layout_gravity 属性设置为TOP或BOTTOM来将它显示在ViewPager的顶部或底部
标题的获取，是重写适配器的getPageTitle(int)函数来获取的
不同点：
PagerTitleStrip无下划线，PagerTabStrip有下划线
PagerTitleStrip无交互，PagerTabStrip有交互，点击标题可实现页面跳转
添加使用：
```
<android.support.v4.view.ViewPager
    android:id="@+id/vp_main"
    android:layout_width="match_parent"
    android:layout_height="200dp"
    android:layout_gravity="center">

    <!--PagerTitleStrip和PagerTabStrip只能存在一个，否则会影响效果-->
    <!--<android.support.v4.view.PagerTitleStrip-->
        <!--android:layout_width="match_parent"-->
        <!--android:layout_height="wrap_content"-->
        <!--android:layout_gravity="top"/>-->

    <android.support.v4.view.PagerTabStrip
        android:id="@+id/pager_tab_strip"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="top"/>

</android.support.v4.view.ViewPager>
```
PagerTabStrip的相关属性设置
```
PagerTabStrip tabStrip = (PagerTabStrip) findViewById(R.id.pager_tab_strip);
tabStrip.setTabIndicatorColorResource(R.color.colorAccent);//设置PagerTabStrip下标颜色
tabStrip.setBackgroundResource(android.R.color.darker_gray);//设置PagerTabStrip背景色
...
```

* 、ViewPager的适配器PagerAdapter
PageAdapter必须重写的四个函数：
```
//ViewPager要显示的view数目
int getCount()

//判断view和object的对应关系
boolean isViewFromObject(View arg0, Object arg1)

//将当前视图添加到container中，并获取当前位置的view
Object instantiateItem(ViewGroup container, int position)

//从当前container中销毁对应位置的object
void destroyItem(ViewGroup container, int position,Object object)
```
### ViewPager源码分析
1. 问题
由于Android Framework源码很庞大，所以读源码必须带着问题来读！没有问题，创造问题再来读！否则很容易迷失在无数的方法与属性之中，最后无功而返。
那么，关于ViewPager有什么问题呢？
    1. setOffsreenPageLimit()方法是如何实现页面缓存的？
    2. 在布局文件中，ViewPager布局内部能否添加其他View？
    3. 为什么ViewPager初始化时，显示了一个页面却不会触发onPageSelected回调？

问题肯定不止这三个，但是有这三个问题基本可以找到本次分析的重点了。读者朋友也可以自己先提出一些问题，再看下面的分析，看看是否可以从分析过程中找到答案。

2. 从onMeasure()下手
ViewPager继承自ViewGroup，是Android Framework提供的一个控件，而Android系统显示控件的流程就是： Activity加载布局实例化所有控件 —> rootView遍历所以控件 —> 对需要重绘的控件执行测量,布局，绘制的操作。
而转化到某个控件来说，它的流程就是：构造方法 —> onMeasure —> onLayout —> onDraw
由于ViewPager的构造方法中只是初始化了一些与本文主题无关的属性就略过不讲，那么自然而然onMeasure方法就来到了我们眼前。
那么在onMeasure中ViewPager做了些什么呢？先把源码摆出来，我进行了一些删减。
```
@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量ViewPager自身大小
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, heightMeasureSpec));

        final int measuredWidth = getMeasuredWidth();

        // child的宽高，占满父控件
        int childWidthSize = measuredWidth - getPaddingLeft() - getPaddingRight();
        int childHeightSize = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

        //1.测量Decor
        int size = getChildCount();
        for (int i = 0; i < size; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp != null && lp.isDecor) {//仅对Decor进行测量
                    //省略若干代码，主要负责对Decor控件的测量
                    ...
                }
            }
        }

        mChildWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        mChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY);

        // 2.从Adapter中获取childView
        mInLayout = true;
        populate();
        mInLayout = false;

        // 3.测量非Decor的childView
        size = getChildCount();
        for (int i = 0; i < size; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp == null || !lp.isDecor) {
                    final int widthSpec = MeasureSpec.makeMeasureSpec(
                            (int) (childWidthSize * lp.widthFactor), MeasureSpec.EXACTLY);
                    child.measure(widthSpec, mChildHeightMeasureSpec);
                }
            }
        }
    }
```
2.1 测量Decor控件
可能很多人有些懵x了，Decor是个啥？
其实Decor是一个接口，在ViewPager内部定义的，并且该接口是没有定义任何内容的。唯一的作用就是如果你的控件实现了Decor接口，那么你的控件就属于DecorView了。
我们知道ViewPager的数据是通过Adapter管理的，但其实还有一种方式给ViewPager添加childView.
```
#layout.xml
<ViewPager>
    <DecorView />
</ViewPager>
```
上面这种直接在ViewPager布局内部添加控件也是可以的，但是要求DecorView必须实现Decor接口，否则将不予显示。
在ViewPager的addView方法中会对childView进行判断，也看一下代码吧！
```
 @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (!checkLayoutParams(params)) {
            params = generateLayoutParams(params);
        }
        final LayoutParams lp = (LayoutParams) params;
        lp.isDecor |= child instanceof Decor; //在此处给isDecor赋值

        //省略无关代码
        ...
    }
```
至于addView()方法是如何调用，可以参考本人博客 ViewGroup如何加载布局中的View？
而上面的代码我们要注意的是lp.isDecor，这是ViewPager为它的childView准备的LayoutParams，在onMeasure的第一步中就是根据lp.isDecor来挑选出Decor控件来测量的。
至于Decor的测量过程与本文主题无关，在此就不详述了，有兴趣的可以自己去查看源码。

2.2 从Adapter中创建ChildView（populate方法）
ViewPager也是采用Observable模式来设计的，数据通过PagerAdapter来管理，并且childView也是通过PagerAdapter来创建的，ViewPager主要负责界面交互相关的工作。
对PagerAdapter并不会做太详细的介绍，直接给一个示例代码吧。
```
public class AutoScrollAdapter extends PagerAdapter {

    //省略构造方法代码
    ...

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = new TextView(mContext); //通过各种方法新建一个childView
        container.addView(itemView);//将childView添加到ViewPager中
        return itemView;
    }
}
```
这四个方法是必须要重写的，方法的含义根据方法名就能看出来。这里主要要讲一下最后这个方法instantiateItem()。它负责向ViewPager提供childView，这里调用的addView方法是被ViewPager重写过的，所以会对lp.isDecor赋值，并且我们可以知道，这里的isDecor=false。

有些人可能要问，这一步的主角不应该是populate()方法吗？的确应该是populate方法，但是由于这个方法比较复杂，为了阅读的连贯性考虑，博主决定单独提出来，一会儿再讲它。
在这里主要告诉大家，populate()方法内部会调用Adapter.instantiateItem()方法，也就是将Adapter中的childView添加到ViewPager中来，为下一步做准备。

2.3 测量ChildView
有了上面的分析，这一步的内容就很好理解了。
简单来说就是，遍历所有的childView，挑选出lp.isDecor==false的childView，然后调用view.measure()方法让childView自己去完成测量。
还有一点需要注意，就是childView的宽度 width= childWidthSize * lp.widthFactor。
childWidthSize就是ViewPager的宽度，lp.widthFactor代表这个childView占几个页面。
lp.widthFactor默认情况下是1.0，可以重写PagerAdapter.getPageWidth(pos)方法来修改这个值。
到此，ViewPager的测量过程就完成了。

3.populate()方法
可以说这是ViewPager最核心的一个方法，所以单独作为一个小节来分析。
在分析源码之前，必须先介绍一个类——ItemInfo

3.1 ItemInfo是什么？
```
static class ItemInfo {
        Object object;  //childView
        int position;   //childView在Adapter中的位置
        boolean scrolling;  //是否在滚动
        float widthFactor;  //宽度的倍数，默认情况下是1
        float offset;       //页面的偏移参数，粗暴的理解就是第几个页面
    }
```
这是ViewPager内部定义的一个静态类，将childView相关的属性进行了包装，主要是为了方便对childView的管理。
并且在ViewPager内部还维护了一个ArrayList,由ItemInfo对象组成，属性名是mItems。
这个list的长度就是由mOffscreenPageLimit来决定的，这个在后面的代码分析中会看到。
好了，了解了基本对象之后，就可以开始分析populate方法了。
注意：由于代码比较长，为了方便阅读博主打算将populate()方法的代码分段讲解，如过代码中没有方法声明，则表示该段代码属于populate()方法。
3.2 获取当前的ItemInfo对象
从这里开始，对populate()方法的源码进行分析，分析内容主要在代码的注释中编写。
```
    void populate(int newCurrentItem) {
        ItemInfo oldCurInfo = null;
        int focusDirection = View.FOCUS_FORWARD;
        if (mCurItem != newCurrentItem) {
            focusDirection = mCurItem < newCurrentItem ? View.FOCUS_RIGHT : View.FOCUS_LEFT;
            oldCurInfo = infoForPosition(mCurItem);  //获取旧的ItemInfo对象
            mCurItem = newCurrentItem;   //更新mCurItem的值，就是在Adapter中的position
        }
        //省略无关代码
        ...
        //mOffscreenPageLimit就是setOffscreenPageLimit方法设置的值
        final int pageLimit = mOffscreenPageLimit;

        //根据下面三行代码可知：mItems的长度就是 2 * pageLimit + 1
        //这里声明的startPos和endPos在后面会起作用，大家注意一下
        final int startPos = Math.max(0, mCurItem - pageLimit);
        final int N = mAdapter.getCount();
        final int endPos = Math.min(N-1, mCurItem + pageLimit);

        // 遍历mItems列表，找出mCurItem对应的ItemInfo对象，是根据position来判断的
        int curIndex = -1;
        ItemInfo curItem = null;
        for (curIndex = 0; curIndex < mItems.size(); curIndex++) {
            final ItemInfo ii = mItems.get(curIndex);
            if (ii.position >= mCurItem) {
                if (ii.position == mCurItem) curItem = ii;
                break;
            }
        }
        // 如果mItems中还未保存该ItemInfo，则创建一个IntemInfo对象
        if (curItem == null && N > 0) {
            curItem = addNewItem(mCurItem, curIndex);
        }
        ...
```
这里要注意的一点是，在新建ItemInfo对象时，我们是调用的addNewItem方法，它的代码如下所示。
```
ItemInfo addNewItem(int position, int index) {
        ItemInfo ii = new ItemInfo(); //新建一个ItemInfo对象
        ii.position = position;
        ii.object = mAdapter.instantiateItem(this, position);//用Adapter创建一个childView
        ii.widthFactor = mAdapter.getPageWidth(position);//默认返回1.0f
        if (index < 0 || index >= mItems.size()) { //添加到mItems中
            mItems.add(ii);
        } else {
            mItems.add(index, ii);
        }
        return ii;
    }
```
不管是从mItems中提取还是新建一个ItemInfo对象，总之我们已经得到了curItem，即当前的IntemInfo对象。

3.3 管理mItems中的其余对象
因为我们的mItems长度是有限的，并且与pageLimit有关，所以很可能出现页面总数大于mItems长度的情况。当显示的页面改变时，我们必须将一些ItemInfo添加进来，将另一些ItemInfo移除。
以保证我们的mItems中的ItemInfo.position是这样的：
```[ startPos … mCurItem … endPos ] ```
其中：
```
mCurItem = curItem.position
startPos = mCurItem - pagLimit
endPos = mCurItem + pagLimit
```
具体如何操作，我们来看代码
```
        if (curItem != null) {
            //1.调整curItem左边的对象
            float extraWidthLeft = 0.f;

            // curIndex是curItem在mItems中的索引
            // itemIndex就是curItem左边的ItemInfo的索引
            int itemIndex = curIndex - 1;
            //获取左边的ItemInfo对象
            ItemInfo ii = itemIndex >= 0 ? mItems.get(itemIndex) : null;
            final int clientWidth = getClientWidth();
            //curItem左边需要的宽度，默认情况下为1.0f
            final float leftWidthNeeded = clientWidth <= 0 ? 0 :
                    2.f - curItem.widthFactor + (float) getPaddingLeft() / (float) clientWidth;
            //遍历mItems左半部分，即curIndex左边的对象
            //只有在pos < startPos时才能退出循环，否则会一直遍历到pos=0
            for (int pos = mCurItem - 1; pos >= 0; pos--) {
                // 建议大家先从下面的else if开始看，因为这里的逻辑是准备退出循环了
                if (extraWidthLeft >= leftWidthNeeded && pos < startPos) {
                    //当pos < startPos，说明mItems左边部分已经调整完毕了
                    //此时的ii代表的是，startPos左边的对象了
                    if (ii == null) {
                        break;
                    }
                    //如果startPos左边还有对象，需要从mItems中移除
                    if (pos == ii.position && !ii.scrolling) {
                        mItems.remove(itemIndex);
                        mAdapter.destroyItem(this, pos, ii.object);
                        itemIndex--;
                        curIndex--;
                        ii = itemIndex >= 0 ? mItems.get(itemIndex) : null;
                    }

                //如果curIndex左边的ItemInfo对象不为null
                } else if (ii != null && pos == ii.position) {
                    extraWidthLeft += ii.widthFactor; //累加curItem左边需要的宽度
                    itemIndex--;                      //再往curIndex左边移一个位置
                    ii = itemIndex >= 0 ? mItems.get(itemIndex) : null; //取出ItemInfo对象

                //如果curIndex左边的ItemInfo为null
                } else {
                    //新建一个ItemInfo对象，添加到itemIndex的右边
                    ii = addNewItem(pos, itemIndex + 1);
                    extraWidthLeft += ii.widthFactor;    //累加左边宽度
                    curIndex++;   //由于往mItems中插入了一个对象，故curIndex需要加1
                    ii = itemIndex >= 0 ? mItems.get(itemIndex) : null; //去除ItemInfo
                }
            }

            //2.调整curItem右边的对象，逻辑与上面类似
            //代码省略
            ...
            // 3.计算mItems中的偏移参数
            calculatePageOffsets(curItem, curIndex, oldCurInfo);
        }
```
代码主要是一些逻辑，需要大家静下心来读，也不知道讲清除了没有。（发现要把代码翻译成文字真是累，一句代码要用一大段文字来说明）
对于calculatePageOffsets方法，就不贴源码分析了，主要说一下它做了哪些事情吧

根据oldItem.position与curItem.position的大小关系，来确定curItem的offset值
再分别对curItem的左边和右边的Item写入offset值
mPageMargin是页面之间的间隔, marginOffset = mPageMargin / childWidth
每个页面的offset = mAdapter.getPageWidth(pos) + marginOffset
参照上面的四点提示，大家去读源码应该也没啥难度的，关键是都是一些逻辑处理很难文字化说明。

3.4 一些收尾工作
```
        // 将ItemInfo的内容更新到childView的LayoutParams中
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            lp.childIndex = i;
            if (!lp.isDecor && lp.widthFactor == 0.f) {
                final ItemInfo ii = infoForChild(child);
                if (ii != null) {
                    lp.widthFactor = ii.widthFactor;
                    lp.position = ii.position;
                }
            }
        }
        //根据lp.position的大小对所有childView进行排序，另外DecorView是排在其他child之前的
        sortChildDrawingOrder();
```
OK，populate方法分析到此就结束了。

4. onLayout
布局也是先布局Decor，再布局Adapter创建的childView，直接上源码吧。
```
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int width = r - l;
        int height = b - t;

        //1.布局Decor,根据lp.isDecor来筛选DecorView
        //代码略
        ...

        final int childWidth = width - paddingLeft - paddingRight;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                ItemInfo ii;
                //此处将DecorView过滤掉，并且根据view从mItems中查找ItemInfo对象
                //如果ViewPager布局中添加了未实现Decor接口的控件，将不会被布局
                //因为无法从mItems中查找到ItemInfo对象
                if (!lp.isDecor && (ii = infoForChild(child)) != null) {
                    //计算当前page的左边界偏移值，此处的offset会随着页面增加而增加
                    int loff = (int) (childWidth * ii.offset);
                    int childLeft = paddingLeft + loff;
                    int childTop = paddingTop;
                    if (lp.needsMeasure) {//如果需要重新测量，则重新测量之
                        lp.needsMeasure = false;
                        final int widthSpec = MeasureSpec.makeMeasureSpec(
                                (int) (childWidth * lp.widthFactor),
                                MeasureSpec.EXACTLY);
                        final int heightSpec = MeasureSpec.makeMeasureSpec(
                                (int) (height - paddingTop - paddingBottom),
                                MeasureSpec.EXACTLY);
                        child.measure(widthSpec, heightSpec);
                    }
                    //child调用自己的layout方法来布局自己
                    child.layout(childLeft, childTop,
                            childLeft + child.getMeasuredWidth(),
                            childTop + child.getMeasuredHeight());
                }
            }
        }
        mTopPageBounds = paddingTop;
        mBottomPageBounds = height - paddingBottom;
        mDecorChildCount = decorCount;
        //如果是首次布局，则会调用scrollToItem方法
        if (mFirstLayout) {
            scrollToItem(mCurItem, false, 0, false);
        }
        mFirstLayout = false;
    }
```
布局这一块的代码相对来说要简单一些，就是根据offset偏移量来计算出left，right, top, bottom值，然后直接调用View.layout方法进行布局。
但是，这里需要插一句，在用ViewPager实现轮播控件时，有一种方法是将Adapter.getCount返回Integer.MAX_VALUE，已达到伪循环播放的目的。从上面的代码可以看到，此时这个offset值会不断的变大，那么

               ``` int loff = (int) (childWidth * ii.offset);```

这个loff很可能会超出int的最大值边界。
所以，以后大家实现轮播控件时，还是不要采用这种方法了。

然后，回过头来再说下scrollToItem方法
注意上面调用scrollToItem时，最后一个参数传递的是false，而这个参数就是决定是否调用onPageSelected回调函数的。
看代码：
```
    private void scrollToItem(int item, boolean smoothScroll, int velocity,
            boolean dispatchSelected) {
        final ItemInfo curInfo = infoForPosition(item);
        int destX = 0;
        if (curInfo != null) {
            final int width = getClientWidth();
            destX = (int) (width * Math.max(mFirstOffset,
                    Math.min(curInfo.offset, mLastOffset)));
        }
        if (smoothScroll) {
            smoothScrollTo(destX, 0, velocity);
            if (dispatchSelected) {
                dispatchOnPageSelected(item);
            }
        } else {
            if (dispatchSelected) { //是否需要分发OnPageSelected回调
                dispatchOnPageSelected(item);
            }
            completeScroll(false);
            scrollTo(destX, 0);
            pageScrolled(destX);
        }
    }
```
也就是说，第一次布局ViewPager时虽然会显示一个页面，却不会调用onPageSelected方法。

onLayout的分析也到此结束了，至于onDraw方法ViewPager并没有做什么，只是编写了绘制Page之间间隔的代码，就不做分析了。

当然，ViewPager的代码还不止这些，此文分析的仅仅是它的骨架，还有许多其他处理如onInterceptTouchEvent方法，pageScrolled方法等等，这些就留给读者自己去分析吧。
理解了这边文章之后，对ViewPager的工作原理也有一定程度的了解了，相信再去读那些代码难度不会很大。
至于篇头提到的三个问题，相信各位也已经有了答案。

### PagerAdapter
Class Overview
提供一个适配器用于填充ViewPager页面. 你很可能想要使用一个更加具体的实现, 例如： FragmentPagerAdapter or FragmentStatePagerAdapter.

当你实现一个PagerAdapter时，至少需要覆盖以下几个方法:

instantiateItem(ViewGroup, int)
destroyItem(ViewGroup, int, Object)
getCount()
isViewFromObject(View, Object)
PagerAdapter比AdapterView的使用更加普通.ViewPager使用回调函数来表示一个更新的步骤，而不是使用一个视图回收机制。在需要的时候pageradapter也可以实现视图的回收或者使用一种更为巧妙的方法来管理视图，比如采用可以管理自身视图的fragment。

viewpager不直接处理每一个视图而是将各个视图与一个键联系起来。这个键用来跟踪且唯一代表一个页面，不仅如此，该键还独立于这个页面所在adapter的位置。当pageradapter将要改变的时候他会调用startUpdate函数，接下来会调用一次或多次的instantiateItem或者destroyItem。最后在更新的后期会调用finishUpdate。当finishUpdate返回时 instantiateItem返回的对象应该添加到父ViewGroup destroyItem返回的对象应该被ViewGroup删除。methodisViewFromObject(View, Object)代表了当前的页面是否与给定的键相关联。
 
对于非常简单的pageradapter或许你可以选择用page本身作为键，在创建并且添加到viewgroup后instantiateItem方法里返回该page本身即可
destroyItem将会将该page从viewgroup里面移除。isViewFromObject方法里面直接可以返回view == object。
 
pageradapter支持数据集合的改变，数据集合的改变必须要在主线程里面执行，然后还要调用notifyDataSetChanged方法。和baseadapter非常相似。数据集合的改变包括页面的添加删除和修改位置。viewpager要维持当前页面是活动的，所以你必须提供getItemPosition方法。

3、解析
看上面的翻译，与我们相关只有这两段话：

viewpager不直接处理每一个视图而是将各个视图与一个键联系起来。这个键用来跟踪且唯一代表一个页面，不仅如此，该键还独立于这个页面所在adapter的位置。当pageradapter将要改变的时候他会调用startUpdate函数，接下来会调用一次或多次的instantiateItem或者destroyItem。最后在更新的后期会调用finishUpdate。当finishUpdate返回时 instantiateItem返回的对象应该添加到父ViewGroup destroyItem返回的对象应该被ViewGroup删除。methodisViewFromObject(View, Object)代表了当前的页面是否与给定的键相关联。

对于非常简单的pageradapter或许你可以选择用page本身作为键，在创建并且添加到viewgroup后instantiateItem方法里返回该page本身即可destroyItem将会将该page从viewgroup里面移除。isViewFromObject方法里面直接可以返回view == object。

对于上面两段话，我这里有两点要着重讲一下：

1、第一段说明了，键（Key）的概念，首先这里要清楚的一点是，每个滑动页面都对应一个Key，而且这个Key值是用来唯一追踪这个页面的，也就是说每个滑动页面都与一个唯一的Key一一对应。大家先有这个概念就好，关于这个Key是怎么来的，下面再讲。

2、第二段简单讲了一个应用，即将当前页面本身的View作为Key。其实这个应用就是我们前一章讲的例子应用。不太理解？没关系，下面细讲。下面我们讲讲Key的问题

4、关于Key
现在我带着大家看看几个方法的官方文档：(这里结合《ViewPager 详解（一）---基本入门》最底部的例子来看)

http://hukai.me/android-training-course-in-chinese/ui/custom-view/custom-draw.html
