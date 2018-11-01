import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * byte[]的缓存池，可以指定最大的缓存的byte数目。当缓存的byte数目超过指定的最大值时，以LRU策略进行回收。
 * 用一个集合记录该byte[]的使用顺序，使用另一个集合记录已经使用过的byte[]，保证对byte[]的重复利用。
 */
public class ByteArrayPool {
    /**
     * byte[]待删集合，记录了byte[]的使用顺序。当缓存的byte数目超过指定的最大值时，会回收该list集合中的第一个元素。
     * 每一次从变量mBuffersBySize中获取到合适的byte[]时，会将返回值从该集合中删除，因为这个byte[]是最近使用的。
     * 每一次回收该集合中第0个元素，保证了回收的byte[]是使用时间最久远的。
     */
    private List<byte[]> mBuffersByLastUse = new LinkedList<>();
    /**
     * byte[]的真正缓存list集合。每一次获取时都是从该集合中获取或者新生成
     */
    private List<byte[]> mBuffersBySize = new ArrayList<>(64);

    /**
     * 当前缓存池中已经有byte数
     */
    private int mCurrentSize = 0;

    /**
     * 本缓存池中最大的缓存byte数
     */
    private final int mSizeLimit;

    /**
     * 哪个byte数组元素少，哪个在前
     */
    protected static final Comparator<byte[]> BUF_COMPARATOR = new Comparator<byte[]>() {
        @Override
        public int compare(byte[] lhs, byte[] rhs) {
            return lhs.length - rhs.length;
        }
    };

    public ByteArrayPool(int sizeLimit) {
        mSizeLimit = sizeLimit;
    }

    /**
     * @param len 返回的byte[]的最小长度，有可能返回的byte[]的长度大于该len
     */
    public synchronized byte[] getBuf(int len) {
        for (int i = 0; i < mBuffersBySize.size(); i++) {
            byte[] buf = mBuffersBySize.get(i);
            System.out.println("getBuf@for buf: " + buf + " len: " + len);
            if (buf.length >= len) {
                mCurrentSize -= buf.length;//返回后，将当前缓存池中缓存的byte数目减少
                mBuffersBySize.remove(i);//删掉，保证了一个byte[]数组不会提供给两个客户端使用
                //本次使用了该缓存，所以将其从待删集合中删除，这样即使缓存的byte数量超过最大的范围，也不会被删掉。保证了最新的不会被删。
                //同时，当一个byte[]被多次使用时，则该byte[]会被存储到该集合的最后端，也不会被立即回收
                mBuffersByLastUse.remove(buf);
                System.out.println("getBuf@if buf.length: " + buf.length + " mCurrentSize: " + mCurrentSize);
                return buf;
            }
        }
        System.out.println("getBuf@new len: " + len);
        return new byte[len];//没有合适的或者第一次会直接返回
    }

    /**
     * 将使用过的byte数组返回到缓存池中
     */
    public synchronized void returnBuf(byte[] buf) {
        System.out.println("returnBuf@if buf: " + buf);
        if (buf == null || buf.length > mSizeLimit) {
            return;
        }
        mBuffersByLastUse.add(buf);//待删集合，不能对回收的byte[]进行排序
        //该二分查找的返回值有两个效果：其一知道查找的有木有，其二如果木有，插入时插入的位置。
        int pos = Collections.binarySearch(mBuffersBySize, buf, BUF_COMPARATOR);
        System.out.println("returnBuf@ pos: " + pos);
        if (pos < 0) {
            pos = -pos - 1;
        }
        mBuffersBySize.add(pos, buf);
        mCurrentSize += buf.length;
        System.out.println("returnBuf@ mCurrentSize: " + mCurrentSize);
        trim();
    }

    /**
     * 回收该回收的部分。
     */
    private synchronized void trim() {
        while (mCurrentSize > mSizeLimit) {
            System.out.println("trim@ mCurrentSize: " + mCurrentSize+ " mSizeLimit: " + mSizeLimit);
            byte[] buf = mBuffersByLastUse.remove(0);
            mBuffersBySize.remove(buf);
            mCurrentSize -= buf.length;
        }
    }

}

