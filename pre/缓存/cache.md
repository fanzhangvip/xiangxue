#### 什么是缓存
* 所有保存“中间的，额外的”数据的机制
* 缓存就是数据交换的缓冲区（称作Cache），这个概念最初是来自于内存和CPU
##### android中的缓存
* 缓存特点：一块有大小限制的可以临时存储数据的区域，当它的数据存储满了的时候，可以根据某种缓存策略来清除部分旧数据以便可以存入新的数据
* 内存缓存 临时存储网络数据的内存区域，杀死app即没了
* 磁盘缓存 临时存储数据的磁盘空间，杀死app可重新加载

#### 为什么需要缓存
* 提高响应速度，减少响应延迟
* 减少资源消耗（服务器、带宽）
* 提升用户体验

#### 缓存相关概念
* 缓存介质
    1. 内存：将缓存存储于内存中是最快的选择，无需额外的I/O开销，但是内存的缺点是没有持久化落地物理磁盘，一旦应用异常break down而重新启动，数据很难或者无法复原。
    2. 硬盘：一般来说，很多缓存框架会结合使用内存和硬盘，在内存分配空间满了或是在异常的情况下，可以被动或主动的将内存空间数据持久化到硬盘中，达到释放空间或备份数据的目的。
    3. 数据库：前面有提到，增加缓存的策略的目的之一就是为了减少数据库的I/O压力。现在使用数据库做缓存介质是不是又回到了老问题上了？其实，数据库也有很多种类型，像那些不支持SQL，只是简单的key-value存储结构的特殊数据库（如BerkeleyDB和Redis），响应速度和吞吐量都远远高于我们常用的关系型数据库等
* 命中
  * 如果在缓存中，一个条目通过一个标记被找到了，这个条目就会被使用、我们就叫它缓存命中
* Cache Miss
    1. 如果还有缓存的空间，那么，没有命中的对象会被存储到缓存中来
    2. 如果缓存满了，而又没有命中缓存，那么就会按照某一种策略，把缓存中的旧对象踢出，而把新的对象加入缓存池。而这些策略统称为替代策略（缓存算法），这些策略会决定到底应该提出哪些对象。
* 命中率
    * 命中率=返回正确结果数/请求缓存次数，命中率问题是缓存中的一个非常重要的问题，它是衡量缓存有效性的重要指标。命中率越高，表明缓存的使用率越高
* 最大元素 or 最大空间
    * 缓存中可以存放的最大元素的数量，一旦缓存中元素数量超过这个值（或者缓存数据所占空间超过其最大支持空间），那么将会触发缓存启动清空策略根据不同的场景合理的设置最大元素值往往可以一定程度上提高缓存的命中率，从而更有效的时候缓存
* 存储成本：
    * 当没有命中时，我们会从数据库取出数据，然后放入缓存。而把这个数据放入缓存所需要的时间和空间，就是存储成本。
* 索引成本：和存储成本相仿。
* 失效：当存在缓存中的数据需要更新时，就意味着缓存中的这个数据失效了

#### 缓存算法
* 概论 缓存算法应该考虑哪些点？
    * 成本 如果缓存对象有不同的成本，应该把那些难以获得的对象保存下来
    * 容量 如果缓存对象有不同的大小，应该把那些大的缓存对象清除，这样就可以让更多的小缓存对象进来了
    * 时间 一些缓存还保存着缓存的过期时间。电脑会失效他们，因为他们已经过期了
##### Least Frequently Used（LFU）
* 大家好，我是 LFU，我会计算为每个缓存对象计算他们被使用的频率。我会把最不常用的缓存对象踢走
##### Least Recently User（LRU）
* 我是 LRU 缓存算法，我把最近最少使用的缓存对象给踢走。
我总是需要去了解在什么时候，用了哪个缓存对象。如果有人想要了解我为什么总能把最近最少使用的对象踢掉，是非常困难的。
浏览器就是使用了我（LRU）作为缓存算法。新的对象会被放在缓存的顶部，当缓存达到了容量极限，我会把底部的对象踢走，而技巧就是：我会把最新被访问的缓存对象，放到缓存池的顶部。
所以，经常被读取的缓存对象就会一直呆在缓存池中。有两种方法可以实现我，array 或者是 linked list。
我的速度很快，我也可以被数据访问模式适配。我有一个大家庭，他们都可以完善我，甚至做的比我更好（我确实有时会嫉妒，但是没关系）。我家庭的一些成员包括 LRU2 和 2Q，他们就是为了完善 LRU 而存在的。
##### Least Recently Used 2（LRU2）
* 我是 Least Recently Used 2，有人叫我最近最少使用 twice，我更喜欢这个叫法。我会把被两次访问过的对象放入缓存池，当缓存池满了之后，我会把有两次最少使用的缓存对象踢走。因为需要跟踪对象2次，访问负载就会随着缓存池的增加而增加。如果把我用在大容量的缓存池中，就会有问题。另外，我还需要跟踪那么不在缓存的对象，因为他们还没有被第二次读取。我比LRU好，而且是 adoptive to access 模式 。
##### Two Queues（2Q）
* 我是 Two Queues；我把被访问的数据放到 LRU 的缓存中，如果这个对象再一次被访问，我就把他转移到第二个、更大的 LRU 缓存。
我踢走缓存对象是为了保持第一个缓存池是第二个缓存池的1/3。当缓存的访问负载是固定的时候，把 LRU 换成 LRU2，就比增加缓存的容量更好。这种机制使得我比 LRU2 更好，我也是 LRU 家族中的一员，而且是 adoptive to access 模式
##### Adaptive Replacement Cache（ARC）
* 我是 ARC，有人说我是介于 LRU 和 LFU 之间，为了提高效果，我是由2个 LRU 组成，第一个，也就是 L1，包含的条目是最近只被使用过一次的，而第二个 LRU，也就是 L2，包含的是最近被使用过两次的条目。因此， L1 放的是新的对象，而 L2 放的是常用的对象。所以，别人才会认为我是介于 LRU 和 LFU 之间的，不过没关系，我不介意。
  我被认为是性能最好的缓存算法之一，能够自调，并且是低负载的。我也保存着历史对象，这样，我就可以记住那些被移除的对象，同时，也让我可以看到被移除的对象是否可以留下，取而代之的是踢走别的对象。我的记忆力很差，但是我很快，适用性也强
##### Most Recently Used（MRU）
* 我是 MRU，和 LRU 是对应的。我会移除最近最多被使用的对象，你一定会问我为什么。好吧，让我告诉你，当一次访问过来的时候，有些事情是无法预测的，并且在缓存系统中找出最少最近使用的对象是一项时间复杂度非常高的运算，这就是为什么我是最好的选择。
  我是数据库内存缓存中是多么的常见！每当一次缓存记录的使用，我会把它放到栈的顶端。当栈满了的时候，你猜怎么着？我会把栈顶的对象给换成新进来的对象
##### First in First out（FIFO）
* 我是先进先出，我是一个低负载的算法，并且对缓存对象的管理要求不高。我通过一个队列去跟踪所有的缓存对象，最近最常用的缓存对象放在后面，而更早的缓存对象放在前面，当缓存容量满时，排在前面的缓存对象会被踢走，然后把新的缓存对象加进去。我很快，但是我并不适用
##### Second Chance：
大家好，我是 second chance，我是通过 FIFO 修改而来的，被大家叫做 second chance 缓存算法，我比 FIFO 好的地方是我改善了 FIFO 的成本。我是 FIFO 一样也是在观察队列的前端，但是很FIFO的立刻踢出不同，我会检查即将要被踢出的对象有没有之前被使用过的标志（1一个 bit 表示），没有没有被使用过，我就把他踢出；否则，我会把这个标志位清除，然后把这个缓存对象当做新增缓存对象加入队列。你可以想象就这就像一个环队列。当我再一次在队头碰到这个对象时，由于他已经没有这个标志位了，所以我立刻就把他踢开了。我在速度上比 FIFO 快。
##### CLock：
* 我是 Clock，一个更好的 FIFO，也比 second chance 更好。因为我不会像 second chance 那样把有标志的缓存对象放到队列的尾部，但是也可以达到 second chance 的效果。
我持有一个装有缓存对象的环形列表，头指针指向列表中最老的缓存对象。当缓存 miss 发生并且没有新的缓存空间时，我会问问指针指向的缓存对象的标志位去决定我应该怎么做。如果标志是0，我会直接用新的缓存对象替代这个缓存对象；如果标志位是1，我会把头指针递增，然后重复这个过程，知道新的缓存对象能够被放入。我比 second chance 更快。
##### Simple time-based：
* 我是 simple time-based 缓存算法，我通过绝对的时间周期去失效那些缓存对象。对于新增的对象，我会保存特定的时间。我很快，但是我并不适用。
##### Extended time-based expiration：
* 我是 extended time-based expiration 缓存算法，我是通过相对时间去失效缓存对象的；对于新增的缓存对象，我会保存特定的时间，比如是每5分钟，每天的12点。
##### Sliding time-based expiration：
* 我是 sliding time-based expiration，与前面不同的是，被我管理的缓存对象的生命起点是在这个缓存的最后被访问时间算起的。我很快，但是我也不太适用
##### Random Cache
* 我是随机缓存，我随意的替换缓存实体，没人敢抱怨。你可以说那个被替换的实体很倒霉。通过这些行为，我随意的去处缓存实体。我比 FIFO 机制好，在某些情况下，我甚至比 LRU 好，但是，通常LRU都会比我好


#### 题目
* Design and implement a data structure for Least Recently Used (LRU) cache. It should support the following operations: get and put.
get(key) - Get the value (will always be positive) of the key if the key exists in the cache, otherwise return -1.
put(key, value) - Set or insert the value if the key is not already present. When the cache reached its capacity, it should invalidate the least recently used item before inserting a new item.
1. Follow up:
2. Could you do both operations in O(1) time complexity?

```
    LRUCache cache = new LRUCache( 2 /* capacity */ );

    cache.put(1, 1);
    cache.put(2, 2);
    cache.get(1);       // returns 1
    cache.put(3, 3);    // evicts key 2
    cache.get(2);       // returns -1 (not found)
    cache.put(4, 4);    // evicts key 1
    cache.get(1);       // returns -1 (not found)
    cache.get(3);       // returns 3
    cache.get(4);       // returns 4
```
#### 审题
1. 设计一个简单版的最近使用缓存模型。缓存空间有容量限制，时间复杂度要求是O(1)。
2. 其中“最近使用”是指最近被访问过(被cache.get调用过）。
#### 解题思路
* 以上对cache的操作有：添加(put)、查找(get)、替换(put)，因有容量限制，还需有删除，每次当容量满时，将最久未使用的节点删除。
* 为快速添加和删除，我们可以用双向链表来设计cache，链表中从头到尾的数据顺序依次是，(最近访问)->...(最旧访问)：
    1. 添加节点：新节点插入到表头即可，时间复杂度O(1)；
    2. 查找节点：每次节点被查询到时，将节点移动到链表头部，时间复杂度O(n)
    3. 替换节点：查找到后替换(更新节点value)，将节点移动到链表头部；
* 可见在查找节点时，因对链表需遍历，时间复杂度O(n)，为达到O(1)，可以考虑数据结构中加入哈希(hash)
* 我们需要用两种数据结构来解题：双向链表、哈希表

```

import java.util.*;

class Node{
	int key;
	int value;
	Node next;
	Node pre;
	public Node(int key,int value,Node pre, Node next){
		this.key = key;
		this.value = value;
		this.pre = pre;
		this.next = next;
	}
}

public class LRUCache {
	int capacity;
	int count;//cache size
	Node head;
	Node tail;
	HashMap<Integer,Node>hm;
    public LRUCache(int capacity) { //only initial 2 Node is enough, head/tail
    	this.capacity = capacity;
    	this.count = 2;
    	this.head = new Node(-1,-1,null,null);
    	this.tail = new Node(-2,-2,this.head,null);
    	this.head.next = this.tail;
        hm = new HashMap<Integer,Node>();
        hm.put(this.head.key, this.head);
        hm.put(this.tail.key, this.tail);
    }

    public int get(int key) {
    	int value = -1;
    	if(hm.containsKey(key)){
    		Node nd = hm.get(key);
    		value = nd.value;
    		detachNode(nd); //detach nd from current place
    		insertToHead(nd); //insert nd into head
    	}
		return value;
    }

    public void put(int key, int value) {
    	if(hm.containsKey(key)){ //update
    		Node nd = hm.get(key);
    		nd.value = value;
    		//move to head
    		detachNode(nd); //detach nd from current place
    		insertToHead(nd); //insert nd into head
    	}else{ //add
    		Node newNd = new Node(key,value,null,this.head);
    		this.head.pre = newNd; //insert into head
    		this.head = newNd;
    		hm.put(key, newNd); //add into hashMap
    		this.count ++;
    		if(this.count > capacity){ //need delete node
    			removeNode();
    		}
    	}
    }
    //common func
    public void insertToHead(Node nd){
    	this.head.pre = nd;
    	nd.next = this.head;
    	nd.pre = null;
    	this.head = nd;
    }
    public void detachNode(Node nd){
    	nd.pre.next = nd.next;
    	if(nd.next!=null){
    		nd.next.pre = nd.pre;
    	}else{
    		this.tail = nd.pre;
    	}
    }
    public void removeNode(){ //remove from tail
		int tailKey = this.tail.key;
		this.tail = this.tail.pre;
		this.tail.next = null;
		hm.remove(tailKey);
		this.count --;
    }
    public void printCache(){
    	System.out.println("\nPRINT CACHE ------ ");
    	System.out.println("count: "+count);
    	System.out.println("From head:");
    	Node p = this.head;
    	while(p!=null){
    		System.out.println("key: "+p.key+" value: "+p.value);
    		p = p.next;
    	}
    	System.out.println("From tail:");
    	p = this.tail;
    	while(p!=null){
    		System.out.println("key: "+p.key+" value: "+p.value);
    		p = p.pre;
    	}

    }

    public static void main(String[] args){
    	LRUCache lc = new LRUCache(3);
    	lc.printCache();

    	lc.put(1, 1);
    	lc.put(2, 2);
    	lc.put(3, 3);
    	lc.printCache();

    	lc.get(2);
    	lc.printCache();

    	lc.put(4, 4);
    	lc.printCache();

    	lc.get(1);
    	lc.printCache();

    	lc.put(3, 33);
    	lc.printCache();
    }
}
```

* 假设缓存大小为4,而写入顺序为A B C D E D F.访问顺序分为写入以及读取两种操作,写入需要更新访问时间,并且当数据到达最大缓存时需要逐出数据,而读取只会更新访问时间,写入置换算法流程如上图所示.
* 当未到达缓存大小时,所有数据按写入存储,并记录写入次序.
* 写入E时缓存已经满,且E的值不存在,需要逐出最久未访问的数据A,此时缓存内容为E D C B.
* 下一个写入D, D在缓存中,直接更新D的访问次序,此时缓存内容为 D E C B
* 下一个写入F, F不在缓存中,逐出缓存中的末尾C,此时缓存内容为 F D E C

* 通过这个代码结构图，我们可以很清晰的看到DiskLruCachen内部有三个内部类，构造方法是私有的，所以必然采用了单例模式进行封装。

* 对外暴露的主要的公共方法主要有：open、get、edit、remove、flush、delete、remove等，覆盖了缓存管理的增删各个方面。

* 接下来看一下几个内部类的作用：

* Snapshot: Snapshot的中文意思是快照，它的英文备注是：A snapshot of the values for an entry.大致意思是一个缓存值，只不过使用对象封装了一下，提供了更多的信息。

* Editor:每个entry对应一个editor(代理模式)，我们可以通过这个代理来操作缓存文件

* Entity:这是一个bean类，每个entry对应一个缓存文件，保留有缓存文件的一些信息和操作。


























































