package com.galaxy.buffer;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * @author lane
 * @date 2021年06月01日 下午3:41
 */
public class BufferCreate {

    public static void main(String[] args) {
        //1.创建指定长度的buffer
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(10);
        for (int i = 0; i <5; i++) {
            System.out.println(byteBuffer1.get());
        }
        //2.创建一个有内容的缓冲区
        ByteBuffer byteBuffer2 = ByteBuffer.wrap("galaxy".getBytes());
        for (int i = 0; i <6 ; i++) {
            System.out.println(byteBuffer2.get());
        }
    }

}
