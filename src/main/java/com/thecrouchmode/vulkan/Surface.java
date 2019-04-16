package com.thecrouchmode.vulkan;

import com.thecrouchmode.glfw.Window;
import org.lwjgl.glfw.GLFWVulkan;
import org.lwjgl.vulkan.KHRSurface;

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
}
