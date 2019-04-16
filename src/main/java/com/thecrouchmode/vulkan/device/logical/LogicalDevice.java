package com.thecrouchmode.vulkan.device.logical;

import com.thecrouchmode.vulkan.device.physical.PhysicalDevice;
import com.thecrouchmode.vulkan.device.physical.QueueFamily;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.util.List;

public class LogicalDevice {
    VkDevice device;

    public LogicalDevice(PhysicalDevice pd, List<QueueFamily> queueFamilies, List<String> extensions) {
        try(var stack = MemoryStack.stackPush()) {
            VkDeviceCreateInfo deviceCreateInfo = VkDeviceCreateInfo.callocStack(stack);

            VkDeviceQueueCreateInfo.Buffer queues = VkDeviceQueueCreateInfo.callocStack(queueFamilies.size(), stack);

            for(QueueFamily qf : queueFamilies) {{
                queues.put(qf.createInfo(stack));
            }}

            var enabledExtensions = stack.mallocPointer(extensions.size());
            extensions.forEach(s->enabledExtensions.put(stack.UTF8(s)));
            enabledExtensions.flip();

            PointerBuffer layers = stack.callocPointer(1);
            //layers.put(stack.UTF8("VK_LAYER_LUNARG_standard_validation"));
            deviceCreateInfo
                    .sType(VK10.VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO)
                    .pQueueCreateInfos(queues)
                    .pEnabledFeatures(VkPhysicalDeviceFeatures.mallocStack(stack))
                    //.ppEnabledLayerNames(layers.flip())
                    .ppEnabledExtensionNames(enabledExtensions);

            PointerBuffer pointer = stack.pointers(1);
            int res = VK10.vkCreateDevice(pd.device, deviceCreateInfo, null, pointer);

            device = new VkDevice(pointer.get(0), pd.device, deviceCreateInfo);
        }

    }
}
