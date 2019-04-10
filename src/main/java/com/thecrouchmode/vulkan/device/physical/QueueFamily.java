package com.thecrouchmode.vulkan.device.physical;

import org.lwjgl.vulkan.VkQueueFamilyProperties;

import static org.lwjgl.vulkan.VK10.VK_QUEUE_COMPUTE_BIT;
import static org.lwjgl.vulkan.VK10.VK_QUEUE_GRAPHICS_BIT;

public class QueueFamily {

    public final int index;
    public final boolean graphicsBit;
    public final boolean computeBit;

    QueueFamily(int index, VkQueueFamilyProperties properties) {
        this.index = index;
        graphicsBit = (properties.queueFlags() & VK_QUEUE_GRAPHICS_BIT) != 0;
        computeBit = (properties.queueFlags() & VK_QUEUE_COMPUTE_BIT) != 0;

    }
}
