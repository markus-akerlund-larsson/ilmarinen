package com.thecrouchmode.lwjgl;

import org.lwjgl.PointerBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;

public class Util {

    public static <T> List<T> mapPointer(PointerBuffer pb, LongFunction<T> map) {
        var res = new ArrayList<T>(pb.capacity());

        for(int i = 0; i < pb.capacity(); ++i) {
            res.add(map.apply(pb.get(i)));
        }

        return res;
    }

    public static void forEachPointer(PointerBuffer pb, LongConsumer consumer) {

        for(int i = 0; i < pb.capacity(); ++i) {
            consumer.accept(pb.get(i));
        }
    }

    public interface VulkanCountedQuery <P, B> {

        public void get(P pointer, int[] count, B buffer);
    }



    public static <P, B> B vulkanGetCount(P pointer, VulkanCountedQuery<P, B> q, IntFunction<B> allocator) {
        int[] count = {0};
        q.get(pointer, count, null);
        var buffer = allocator.apply(count[0]);
        q.get(pointer, count, buffer);

        return buffer;
    }
}
