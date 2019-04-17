package com.thecrouchmode.vulkan;

import com.thecrouchmode.glfw.Window;
import com.thecrouchmode.vulkan.device.physical.QueueFamily;
import org.lwjgl.glfw.GLFWVulkan;
import org.lwjgl.vulkan.KHRSurface;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkPhysicalDevice;

public class Surface {

    private final Instance instance;
    private final long surface;

    public Surface(Window window, Instance instance) {
        this.instance = instance;

        long[] surface = {0};
        GLFWVulkan.glfwCreateWindowSurface(instance.instance, window.id, null, surface);
        this.surface = surface[0];
    }

    public void destroy() {
        KHRSurface.vkDestroySurfaceKHR(instance.instance, surface, null);
    }

    public boolean supported(QueueFamily queueFamily, VkPhysicalDevice device) {
        int[] support = {0};
        KHRSurface.vkGetPhysicalDeviceSurfaceSupportKHR(device, queueFamily.index, surface, support);

        return VK10.VK_FALSE != support[0];
    }
}
