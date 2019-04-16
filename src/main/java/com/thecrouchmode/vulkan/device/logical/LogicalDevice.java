package com.thecrouchmode.vulkan.device.logical;

import com.thecrouchmode.vulkan.device.physical.PhysicalDevice;
import com.thecrouchmode.vulkan.device.physical.QueueFamily;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.vulkan.VK10.*;

public class LogicalDevice {
    private final List<GraphicsQueue> queues;
    VkDevice device;

    public LogicalDevice(PhysicalDevice pd, List<QueueFamily> queueFamilies, List<String> extensions) {
        try(var stack = MemoryStack.stackPush()) {
            VkDeviceCreateInfo deviceCreateInfo = VkDeviceCreateInfo.callocStack(stack);

            VkDeviceQueueCreateInfo.Buffer queueInfos = VkDeviceQueueCreateInfo.callocStack(queueFamilies.size(), stack);

            for(QueueFamily qf : queueFamilies) {{
                queueInfos.put(qf.createInfo(stack));
            }}
            queueInfos.flip();

            var enabledExtensions = stack.mallocPointer(extensions.size());
            extensions.forEach(s->enabledExtensions.put(stack.UTF8(s)));
            enabledExtensions.flip();

            PointerBuffer layers = stack.callocPointer(1);
            //layers.put(stack.UTF8("VK_LAYER_LUNARG_standard_validation"));
            deviceCreateInfo
                    .sType(VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO)
                    .pQueueCreateInfos(queueInfos)
                    .pEnabledFeatures(VkPhysicalDeviceFeatures.mallocStack(stack))
                    //.ppEnabledLayerNames(layers.flip())
                    .ppEnabledExtensionNames(enabledExtensions);

            PointerBuffer pointer = stack.pointers(1);
            int res = vkCreateDevice(pd.device, deviceCreateInfo, null, pointer);

            device = new VkDevice(pointer.get(0), pd.device, deviceCreateInfo);


            queues = new ArrayList<>();
            for(var qf : queueFamilies) {
                queues.addAll(qf.createQueues(device));
            }

        }

    }

    public void destroy() {
        vkDestroyDevice(device, null);
    }
}
