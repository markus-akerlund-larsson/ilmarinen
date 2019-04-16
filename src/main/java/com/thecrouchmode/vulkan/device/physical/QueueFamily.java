package com.thecrouchmode.vulkan.device.physical;

import com.thecrouchmode.vulkan.device.logical.GraphicsQueue;
import com.thecrouchmode.vulkan.device.logical.LogicalDevice;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.vulkan.VK10.VK_QUEUE_COMPUTE_BIT;
import static org.lwjgl.vulkan.VK10.VK_QUEUE_GRAPHICS_BIT;
import static org.lwjgl.vulkan.VK10.vkGetDeviceQueue;

public class QueueFamily {

    public final int index;
    public final boolean graphicsBit;
    public final boolean computeBit;
    public float priority = 1;
    private int count = 1;

    QueueFamily(int index, VkQueueFamilyProperties properties) {
        this.index = index;
        graphicsBit = (properties.queueFlags() & VK_QUEUE_GRAPHICS_BIT) != 0;
        computeBit = (properties.queueFlags() & VK_QUEUE_COMPUTE_BIT) != 0;

    }

    public VkDeviceQueueCreateInfo createInfo(MemoryStack stack) {
        VkDeviceQueueCreateInfo queueCreateInfo = VkDeviceQueueCreateInfo.mallocStack(stack);

        queueCreateInfo
                .sType(VK10.VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO)
                .queueFamilyIndex(index)
                .pQueuePriorities(stack.floats(priority));

        return queueCreateInfo;
    }

    public List<GraphicsQueue> createQueues(VkDevice device) {
        try(var stack = MemoryStack.stackPush()) {
            var res = new ArrayList<GraphicsQueue>(count);

            for(int i = 0; i < count; ++i) {
                var handle = stack.mallocPointer(1);
                vkGetDeviceQueue(device, index, i, handle);
                res.add(new GraphicsQueue(new VkQueue(handle.get(0), device)));
            }
            return res;
        }

    }

    @Override
    public String toString() {
        return "index: "+index+" graphics: "+graphicsBit+", compute: "+computeBit;
    }
}
