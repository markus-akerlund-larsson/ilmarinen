package com.thecrouchmode.vulkan;

import com.thecrouchmode.glfw.Window;
import com.thecrouchmode.vulkan.device.logical.LogicalDevice;
import com.thecrouchmode.vulkan.device.physical.PhysicalDevice;
import com.thecrouchmode.vulkan.device.physical.QueueFamily;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.stream.Collectors;

public class HelloTriangleApp {


    private Instance instance;
    private Window window;

    public void run() {
        initWindow();
        initVulkan();
        mainLoop();
        cleanup();
    }

    private void initWindow() {
        GLFW.glfwInit();
        window = new Window(800, 600, "Vulkan");

    }


    private void initVulkan() {
        instance = new Instance();

        PhysicalDevice physicalDevice = instance.physicalDevices().get(0);

        List<QueueFamily> qfs = physicalDevice.queueFamilies()
                .stream()
                .peek(qf-> System.out.println("Queue family: "+qf))
                .filter(q->q.graphicsBit)
                .limit(1)
                .collect(Collectors.toList());
        LogicalDevice logicalDevice = new LogicalDevice(physicalDevice, qfs, instance.enabledExtensions);

    }

    private void mainLoop() {

        while(!window.closed()) {
            window.pollEvents();
        }

    }

    private void cleanup() {


        window.destroy();
        instance.destroy();
        GLFW.glfwTerminate();

    }
}
