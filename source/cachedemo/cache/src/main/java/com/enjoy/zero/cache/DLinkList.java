package com.enjoy.zero.cache;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询安生老师QQ 669100976
 * 类说明: 双向链表
 */
public class DLinkList<T> {

    /**
     * 双向链表的结构体
     *
     * @param <T>
     */
    class DNode<T> {
        public DNode prev;//前驱
        public DNode next;//后驱

        public T value;

        public DNode(DNode prev, DNode next, T value) {
            this.prev = prev;
            this.next = next;
            this.value = value;
        }
    }


    private DNode<T> mHead;//表头
    private int mCount;//节点个数

    public DLinkList() {
        //创建表头，表头是没存储数据的
        mHead = new DNode<T>(null, null, null);
        //初始化节点
        mHead.prev = mHead.next = mHead;
        mCount = 0;
    }

    /**
     * @return 返回节点数目
     */
    public int size() {
        return mCount;
    }

    /**
     * 获取index位置的节点
     *
     * @param index
     * @return
     */
    public DNode<T> getNode(int index) {
        //异常处理
        if (index < 0 || index >= mCount) {
            throw new IndexOutOfBoundsException();
        }

        //为了更好的查找性能 先正向查找
        if (index <= mCount / 2) {
            DNode<T> node = mHead.next;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
            return node;
        }

        //然后反向查找
        DNode<T> rnode = mHead.prev;
        int rindex = mCount - index - 1;
        for (int j = 0; j < rindex; j++) {
            rnode = rnode.prev;
        }
        return rnode;
    }

    /**
     * 获取第index位置的节点的值
     *
     * @param index
     * @return
     */
    public T get(int index) {
        return getNode(index).value;
    }

    /**
     * 获取第1个节点的值
     *
     * @return
     */
    public T getFirst() {
        return getNode(0).value;
    }

    /**
     * 获取最后一个节点的值
     *
     * @return
     */
    public T getLast() {
        return getNode(mCount - 1).value;
    }

    /**
     * 将节点插入到第index位置之前
     * @param index
     * @param t
     */
    public void insert(int index, T t){
        if(index == 0){
            // head = 10 = 20 = 30 ...
            DNode<T> node = new DNode<>(mHead,mHead.next,t);
            //现在要插入表头后面
            // head = t = 10 = 20 = 30 ...
            mHead.next.prev = node;
            mHead.next = node;
            mCount++;
            return;
        }
        //插入其他位置 index = 2 例如 10 与 20 中间
        // head = 10 = t = 20 = 30 ...
        //第一步 先获取index位置的节点  20
        DNode<T> inode = getNode(index); //20节点
        //构造插入的节点 t  断 2 这根线 20节点的前驱原来是10 现在变成t的前驱了
        DNode<T> tnode = new DNode<>(inode.prev,inode,t);
        //断 1 这根线  10节点的后驱原来是20 ，现在变成t了
        inode.prev.next = tnode;
        mCount++;
        return;
    }


}
