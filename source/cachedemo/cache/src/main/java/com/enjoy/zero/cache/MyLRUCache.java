package com.enjoy.zero.cache;

import java.util.HashMap;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询安生老师QQ 669100976
 * 类说明:
 */
public class MyLRUCache<K, V> {


    private int cacheSize;
    private HashMap<K, CacheNode> nodes;//缓存容器
    private int currentSize;
    private CacheNode first;//链表头
    private CacheNode last;//链表尾

    class CacheNode {
        CacheNode prev;//前一节点
        CacheNode next;//后一节点
        V value;//值
        K key;//键

        CacheNode() {
        }
    }

    public MyLRUCache(int i) {
        currentSize = 0;
        cacheSize = i;
        nodes = new HashMap(i);//缓存容器
    }

    @Override
    public String toString() {
        return "MyLRUCache{" +
                "cacheSize=" + cacheSize +
                ", currentSize=" + currentSize +
                ", " + getKVLinkList() +
                '}';
    }

    public String getKV() {
        StringBuffer sb = new StringBuffer();
        sb.append("{ ");
        for (K key : nodes.keySet()) {
            sb.append(key).append(": ").append(nodes.get(key).value).append(", ");
        }
        sb.deleteCharAt(sb.lastIndexOf(", "));
        sb.append("} ");
        return sb.toString();
    }

    public String getKVLinkList() {
        StringBuffer sb = new StringBuffer();
        sb.append("{ ");
        CacheNode temp = first;
        while(temp != last){
            sb.append(temp.key).append(": ").append(temp.value).append(", ");
            temp = temp.next;
        }
        sb.append(last.key).append(": ").append(last.value).append(", ");
        sb.deleteCharAt(sb.lastIndexOf(", "));
        sb.append("} ");
        return sb.toString();
    }



    /**
     * 获取缓存中对象
     *
     * @param key
     * @return
     */
    public V get(K key) {
        CacheNode node = (CacheNode) nodes.get(key);
        if (node != null) {
            moveToHead(node);
            return node.value;
        } else {
            return null;
        }
    }

    /**
     * 添加缓存
     *
     * @param key
     * @param value
     */
    public void put(K key, V value) {
        CacheNode node = (CacheNode) nodes.get(key);

        if (node == null) {
            //缓存容器是否已经超过大小.
            if (currentSize >= cacheSize) {
                if (last != null)//将最少使用的删除
                    nodes.remove(last.key);
                removeLast();
            } else {
                currentSize++;
            }

            node = new CacheNode();
        }
        node.value = value;
        node.key = key;
        //将最新使用的节点放到链表头，表示最新使用的.
        moveToHead(node);
        nodes.put(key, node);
    }

    /**
     * 将缓存删除
     *
     * @param key
     * @return
     */
    public V remove(K key) {
        CacheNode node = (CacheNode) nodes.get(key);
        if (node != null) {
            if (node.prev != null) {
                node.prev.next = node.next;
            }
            if (node.next != null) {
                node.next.prev = node.prev;
            }
            if (last == node)
                last = node.prev;
            if (first == node)
                first = node.next;
        }
        return node.value;
    }

    public void clear() {
        first = null;
        last = null;
    }

    /**
     * 删除链表尾部节点
     * 表示 删除最少使用的缓存对象
     */
    private void removeLast() {
        //链表尾不为空,则将链表尾指向null. 删除连表尾（删除最少使用的缓存对象）
        if (last != null) {
            if (last.prev != null)
                last.prev.next = null;
            else
                first = null;
            last = last.prev;
        }
    }

    /**
     * 移动到链表头，表示这个节点是最新使用过的
     *
     * @param node
     */
    private void moveToHead(CacheNode node) {
        if (node == first)
            return;
        if (node.prev != null)
            node.prev.next = node.next;
        if (node.next != null)
            node.next.prev = node.prev;
        if (last == node)
            last = node.prev;
        if (first != null) {
            node.next = first;
            first.prev = node;
        }
        first = node;
        node.prev = null;
        if (last == null)
            last = first;
    }


    public static void main(String[] args) {

        MyLRUCache<String, String> cache = new MyLRUCache(5);
        cache.put("a", "1");
        cache.put("b", "2");
        cache.put("c", "3");
        cache.put("d", "4");
        cache.put("e", "5");
        System.out.println("cache: " + cache);
        cache.put("x", "6");
        System.out.println("cache: " + cache);
        cache.put("b", "7");
        System.out.println("cache: " + cache);
        cache.put("y", "8");
        System.out.println("cache: " + cache);
        cache.put("d", "9");
        System.out.println("cache: " + cache);

    }
}
