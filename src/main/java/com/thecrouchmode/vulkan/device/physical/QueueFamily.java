package com.thecrouchmode.vulkan.device.physical;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkDeviceQueueCreateInfo;
import org.lwjgl.vulkan.VkQueueFamilyProperties;

import static org.lwjgl.vulkan.VK10.VK_QUEUE_COMPUTE_BIT;
import static org.lwjgl.vulkan.VK10.VK_QUEUE_GRAPHICS_BIT;

public class QueueFamily {

    public final int index;
    public final boolean graphicsBit;
    public final boolean computeBit;
    public float priority = 1;

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

    @Override
    public String toString() {
        return "index: "+index+" graphics: "+graphicsBit+", compute: "+computeBit;
    }
}
