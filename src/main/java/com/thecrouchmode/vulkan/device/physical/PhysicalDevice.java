package com.thecrouchmode.vulkan.device.physical;

import com.thecrouchmode.vulkan.Surface;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;
import com.thecrouchmode.lwjgl.Util;

import java.util.ArrayList;

import static org.lwjgl.vulkan.VK10.*;

public class PhysicalDevice {

    private final VkPhysicalDeviceProperties properties;
    private final VkPhysicalDeviceFeatures features;
    public VkPhysicalDevice device;

    public PhysicalDevice(VkPhysicalDevice device) {
        this.device = device;

        properties = VkPhysicalDeviceProperties.malloc();
        features = VkPhysicalDeviceFeatures.malloc();

        vkGetPhysicalDeviceFeatures(device, features);
        vkGetPhysicalDeviceProperties(device, properties);

    }

    public ArrayList<QueueFamily> queueFamilies(Surface surface) {
        return initQueueFamilies(surface);
    }

    private ArrayList<QueueFamily> initQueueFamilies(Surface surface) {

        try(var stack = MemoryStack.stackPush()) {
            var properties = Util.vulkanGetCount(
                    device,
                    VK10::vkGetPhysicalDeviceQueueFamilyProperties,
                    VkQueueFamilyProperties::calloc);

            ArrayList<QueueFamily> queueFamilies = new ArrayList<>(properties.capacity());
            for(int i = 0; i < properties.capacity(); ++i) {

                queueFamilies.add(new QueueFamily(i, properties.get(i), device, surface));
            }
            return queueFamilies;
        }
    }

    @Override
    public String toString() {
        return properties.deviceID()+"; "+properties.deviceNameString()+"; "+properties.apiVersion();
    }

    public void destroy() {
        properties.free();
        features.free();
    }
}
