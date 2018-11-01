import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache<K, V> {
    private final LinkedHashMap<K, V> map;//使用LinkedHashMap的主要原因在于：它的put的顺序与迭代器的取的顺序一致。

    /** 最大的缓存数*/
    private int size;
    private int maxSize;

    private int putCount;
    private int createCount;
    private int evictionCount;
    private int hitCount;
    private int missCount;

    public LruCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = maxSize;
        this.map = new LinkedHashMap<K, V>(0, 0.75f, true);
    }

    /**
     * 重新定义最大的缓存大小
     */
    public void resize(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }

        synchronized (this) {
            this.maxSize = maxSize;
        }
        trimToSize(maxSize);
    }

    public final V get(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        V mapValue;
        synchronized (this) {
            mapValue = map.get(key);
            if (mapValue != null) {
                hitCount++;
                return mapValue;
            }
            missCount++;
        }

        /*
         * 通过key值获取value值。如果该key值对应的有value了，则放弃create()返回的值。
         * 因为create()可能耗费的时间比较长，所以create()执行完毕后，key可能对应的有value。
         */
        V createdValue = create(key);
        if (createdValue == null) {
            return null;
        }

        synchronized (this) {
            createCount++;
            mapValue = map.put(key, createdValue);
            if (mapValue != null) {
                // key值有冲突，放弃create()生成的value
                map.put(key, mapValue);
            } else {
                size += safeSizeOf(key, createdValue);
            }
        }

        if (mapValue != null) {
            entryRemoved(false, key, createdValue, mapValue);
            return mapValue;
        } else {
            trimToSize(maxSize);
            return createdValue;
        }
    }
    /**
     * 这里是先将数据加入缓存中，再判断缓存的大小是否超过了限制。
     * 这会导致在临界状态下依旧会OOM，可以先移除多余的部分，再将新的加入到缓存中。
     */
    public final V put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }

        V previous;
        synchronized (this) {
            putCount++;
            size += safeSizeOf(key, value);
            previous = map.put(key, value);//返回key对应的value。
            if (previous != null) {
                size -= safeSizeOf(key, previous);//previous是要被删除的，所以size应减除previous的大小
            }
        }

        if (previous != null) {
            entryRemoved(false, key, previous, value);
        }

        trimToSize(maxSize);
        return previous;
    }

    /**
     * 移除存在时间最久的对象，直到缓存的大小不大于最大缓存范围
     * 整理整个缓存池，防止超出最大的范围
     */
    public void trimToSize(int maxSize) {
        while (true) {
            K key;
            V value;
            synchronized (this) {
                if (size < 0 || (map.isEmpty() && size != 0)) {
                    throw new IllegalStateException(getClass().getName()
                            + ".sizeOf() is reporting inconsistent results!");
                }

                if (size <= maxSize || map.isEmpty()) {
                    break;
                }
                //因为获取的顺序与存储的顺序一致，所以该句得到的就是存在时间最久的缓存对象。
                Map.Entry<K, V> toEvict = map.entrySet().iterator().next();
                key = toEvict.getKey();
                value = toEvict.getValue();
                map.remove(key);
                size -= safeSizeOf(key, value);
                evictionCount++;
            }

            entryRemoved(true, key, value, null);
        }
    }

    public final V remove(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        V previous;
        synchronized (this) {
            previous = map.remove(key);
            if (previous != null) {
                size -= safeSizeOf(key, previous);
            }
        }

        if (previous != null) {
            entryRemoved(false, key, previous, null);
        }

        return previous;
    }

    /**
     * oldValue从缓存池中移除时的回调
     */
    protected void entryRemoved(boolean evicted, K key, V oldValue, V newValue) {}

    /**
     * 根据key值生成value。
     */
    protected V create(K key) {
        return null;
    }
    /**
     * 对sizeOf()的大小加一层判断而已
     */
    private int safeSizeOf(K key, V value) {
        int result = sizeOf(key, value);
        if (result < 0) {
            throw new IllegalStateException("Negative size: " + key + "=" + value);
        }
        return result;
    }

    /**
     * 获取对应value的大小。一般都需要重写该方法
     */
    protected int sizeOf(K key, V value) {
        return 1;
    }

    /**
     * trimToSize()传入-1，则所有的缓存对象都会被清除
     */
    public final void evictAll() {
        trimToSize(-1); // -1 will evict 0-sized elements
    }
    //剩余代码无意义，略
}
