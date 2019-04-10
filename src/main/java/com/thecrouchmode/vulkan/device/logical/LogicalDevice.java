package com.thecrouchmode.vulkan.device.logical;

import com.thecrouchmode.vulkan.device.physical.PhysicalDevice;
import com.thecrouchmode.vulkan.device.physical.QueueFamily;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkDeviceCreateInfo;

import java.util.List;

public class LogicalDevice {
    VkDevice device;

    public LogicalDevice(PhysicalDevice pd, List<QueueFamily> queueFamilies) {
        try(var stack = MemoryStack.stackPush()) {
            VkDeviceCreateInfo deviceCreateInfo = VkDeviceCreateInfo.callocStack(stack);

            deviceCreateInfo
                    .sType(VK10.VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO)
                    .pQueueCreateInfos(null)
                    .pEnabledFeatures(null);

            PointerBuffer pointer = stack.pointers(1);
            VK10.vkCreateDevice(pd.device, deviceCreateInfo, null, pointer);
        }

    }
}
