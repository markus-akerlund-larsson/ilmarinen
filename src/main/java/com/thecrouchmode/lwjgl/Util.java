package com.thecrouchmode.lwjgl;

import org.lwjgl.PointerBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import static org.lwjgl.vulkan.VK10.*;

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

    public interface VulkanCountedPQuery<P, B> {

        public void get(P pointer, int[] count, B buffer);
    }

    public interface VulkanCountedQuery <B> {

        public void get(int[] count, B buffer);
    }



    public static <P, B> B vulkanGetCount(P pointer, VulkanCountedPQuery<P, B> q, IntFunction<B> allocator) {
        int[] count = {0};
        q.get(pointer, count, null);
        var buffer = allocator.apply(count[0]);
        q.get(pointer, count, buffer);

        return buffer;
    }

    public static <B> B vulkanGetCount(VulkanCountedQuery<B> q, IntFunction<B> allocator) {
        int[] count = {0};
        q.get(count, null);
        var buffer = allocator.apply(count[0]);
        q.get(count, buffer);

        return buffer;
    }

    public static String translateVulkanResult(int result) {
        switch (result) {
            //Success codes
            case VK_SUCCESS:
                return "Command successfully completed.";
            case VK_NOT_READY:
                return "A fence or query has not yet completed.";
            case VK_TIMEOUT:
                return "A wait operation has not completed in the specified time.";
            case VK_EVENT_SET:
                return "An event is signaled.";
            case VK_EVENT_RESET:
                return "An event is unsignaled.";
            case VK_INCOMPLETE:
                return "A return array was too small for the result.";
            // Error codes
            case VK_ERROR_OUT_OF_HOST_MEMORY:
                return "A host memory allocation has failed.";
            case VK_ERROR_OUT_OF_DEVICE_MEMORY:
                return "A device memory allocation has failed.";
            case VK_ERROR_INITIALIZATION_FAILED:
                return "Initialization of an object could not be completed for implementation-specific reasons.";
            case VK_ERROR_DEVICE_LOST:
                return "The logical or physical device has been lost.";
            case VK_ERROR_MEMORY_MAP_FAILED:
                return "Mapping of a memory object has failed.";
            case VK_ERROR_LAYER_NOT_PRESENT:
                return "A requested layer is not present or could not be loaded.";
            case VK_ERROR_EXTENSION_NOT_PRESENT:
                return "A requested extension is not supported.";
            case VK_ERROR_FEATURE_NOT_PRESENT:
                return "A requested feature is not supported.";
            case VK_ERROR_INCOMPATIBLE_DRIVER:
                return "The requested version of Vulkan is not supported by the driver or is otherwise incompatible for implementation-specific reasons.";
            case VK_ERROR_TOO_MANY_OBJECTS:
                return "Too many objects of the type have already been created.";
            case VK_ERROR_FORMAT_NOT_SUPPORTED:
                return "A requested format is not supported on this device.";
            default:
                return String.format("%s [%d]", "Unknown", Integer.valueOf(result));
        }
    }
}
