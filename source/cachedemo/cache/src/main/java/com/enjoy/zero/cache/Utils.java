package com.enjoy.zero.cache;

import java.util.HashMap;
import java.util.Random;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询安生老师QQ 669100976
 * 类说明:
 */
public class Utils {
    HashMap<Object, Object> table = new HashMap<>();

    public final synchronized void addElement(Object key, Object value) {


        int index;
        Object obj;
        // get the entry from the table
        obj = table.get(key);
        // If we have the entry already in our table
        // then get it and replace only its value.
        obj = table.get(key);

        if (obj != null) {
            CacheElement element;
            element = (CacheElement) obj;
            element.setObjectValue(value);
            element.setObjectKey(key);
            return;
        }
    }


    public CacheElement[] cache = new CacheElement[8];

    public boolean isFull() {
        return cache.length == numEntries;
    }

    //缓存中元素的个数
    public int numEntries = 0;

    Random random = new Random();

    public final synchronized void addElementRandom(Object key, Object value) {
        int index;
        Object obj;
        obj = table.get(key);
        if (obj != null) {
            CacheElement element;// Just replace the value.
            element = (CacheElement) obj;
            element.setObjectValue(value);
            element.setObjectKey(key);
            return;
        }// If we haven't filled the cache yet, put it at the end.
        if (!isFull()) {
            index = numEntries;
            ++numEntries;
        } else { // Otherwise, replace a random entry.
            index = (int) (cache.length * random.nextFloat());
            table.remove(cache[index].getObjectKey());
        }
        cache[index].setObjectValue(value);
        cache[index].setObjectKey(key);
        table.put(key, cache[index]);
    }

    public int current = 0;
    public final synchronized void addElementFIFO(Object key, Object value)
    {
        int index;
        Object obj;
        obj = table.get(key);
        if (obj != null)
        {
            CacheElement element; // Just replace the value.
            element = (CacheElement) obj;
            element.setObjectValue(value);
            element.setObjectKey(key);
            return;
        }
        // If we haven't filled the cache yet, put it at the end.
        if (!isFull())
        {
            index = numEntries;
            ++numEntries;
        }
        else { // Otherwise, replace the current pointer,
            // entry with the new one.
            index = current;
            // in order to make Circular FIFO
            if (++current >= cache.length)
                current = 0;
            table.remove(cache[index].getObjectKey());
        }
        cache[index].setObjectValue(value);
        cache[index].setObjectKey(key);
        table.put(key, cache[index]);
    }

    //LFU 计算为每个缓存对象计算他们被使用的频率。我会把最不常用的缓存对象踢走 数组or LinkedHashMap
    public int index = 0;
    public synchronized Object getElementLFU(Object key)
    {
        Object obj;
        obj = table.get(key);
        if (obj != null)
        {
            CacheElement element = (CacheElement) obj;
            element.setHitCount(element.getHitCount() + 1);
            return element.getObjectValue();
        }
        return null;
    }
    public final synchronized void addElementLFU(Object key, Object value)
    {
        Object obj;
        obj = table.get(key);
        if (obj != null)
        {
            CacheElement element; // Just replace the value.
            element = (CacheElement) obj;
            element.setObjectValue(value);
            element.setObjectKey(key);
            return;
        }
        if (!isFull())
        {
            index = numEntries;
            ++numEntries;
        }
        else
        {
            CacheElement element = removeLfuElement();
            index = element.getIndex();
            table.remove(element.getObjectKey());
        }
        cache[index].setObjectValue(value);
        cache[index].setObjectKey(key);
        cache[index].setIndex(index);
        table.put(key, cache[index]);
    }

    public CacheElement[] getElementsFromTable(){
        return new CacheElement[8];
    }

    public CacheElement removeLfuElement()
    {
        CacheElement[] elements = getElementsFromTable();
        CacheElement leastElement = leastHit(elements);
        return leastElement;
    }
    public static CacheElement leastHit(CacheElement[] elements)
    {
        CacheElement lowestElement = null;
        for (int i = 0; i < elements.length; i++)
        {
            CacheElement element = elements[i];
            if (lowestElement == null)
            {
                lowestElement = element;
            }
            else {
                if (element.getHitCount() < lowestElement.getHitCount())
                {
                    lowestElement = element;
                }
            }
        }
        return lowestElement;
    }

    //LRU 我把最近最少使用的缓存对象给踢走

    public int head;
    public int tail = 0;
    public int[] next = new int[]{};
    public int[] prev = new int[]{};
    private void moveToFront(int index)
    {
        int nextIndex, prevIndex;
        if(head != index)
        {
            nextIndex = next[index];
            prevIndex = prev[index];
            // Only the head has a prev entry that is an invalid index
            // so we don't check.
            next[prevIndex] = nextIndex;
            // Make sure index is valid. If it isn't, we're at the tail
            // and don't set prev[next].
            if(nextIndex >= 0)
                prev[nextIndex] = prevIndex;
            else
                tail = prevIndex;
            prev[index] = -1;
            next[index] = head;
            prev[head] = index;
            head = index;
        }
    }
    public final synchronized void addElementLRU(Object key, Object value)
    {
        int _numEntries = 0;
        int index;Object obj;
        obj = table.get(key);
        if(obj != null)
        {
            CacheElement entry;
            // Just replace the value, but move it to the front.
            entry = (CacheElement)obj;
            entry.setObjectValue(value);
            entry.setObjectKey(key);
            moveToFront(entry.getIndex());
            return;
        }
        // If we haven't filled the cache yet, place in next available
        // spot and move to front.
        if(!isFull())
        {
            if(_numEntries > 0)
            {
                prev[_numEntries] = tail;
                next[_numEntries] = -1;
                moveToFront(numEntries);
            }
            ++numEntries;
        }
        else { // We replace the tail of the list.
            table.remove(cache[tail].getObjectKey());
            moveToFront(tail);
        }
        cache[head].setObjectValue(value);
        cache[head].setObjectKey(key);
        table.put(key, cache[head]);
    }


}
