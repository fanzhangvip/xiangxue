package com.mxnavi.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

/**
 * Created by Administrator on 2018/11/7.
 */

public class BufferUtil {

    public static IntBuffer iBuffer(int[] buffer) {
        // 先初始化buffer,数组的长度*4,因为一个float占4个字节
        ByteBuffer mbb = ByteBuffer.allocateDirect(buffer.length * 4);
        // 数组排列用nativeOrder
        mbb.order(ByteOrder.nativeOrder());
        IntBuffer intBuffer = mbb.asIntBuffer();
        intBuffer.put(buffer);
        intBuffer.position(0);
        return intBuffer;
    }

}
