package com.thecrouchmode.vulkan;

import com.thecrouchmode.glfw.Window;
import com.thecrouchmode.vulkan.device.logical.LogicalDevice;
import com.thecrouchmode.vulkan.device.physical.PhysicalDevice;
import com.thecrouchmode.vulkan.device.physical.QueueFamily;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HelloTriangleApp {


    private Instance instance;
    private Window window;
    private LogicalDevice logicalDevice;

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

        instance.physicalDevices().forEach(System.out::println);

        PhysicalDevice physicalDevice = instance.physicalDevices().get(0);
        System.out.println("Avaliable extensions:");
        Instance.avaliableExtensions().forEach(e->System.out.println(e.extensionNameString()));

        System.out.println("Avaliable queues:");
        physicalDevice.queueFamilies().forEach(System.out::println);

        List<QueueFamily> qfs = physicalDevice.queueFamilies()
                .stream()
                .filter(q->q.graphicsBit)
                .limit(1)
                .collect(Collectors.toList());

        qfs.forEach(System.out::println);
        ArrayList<String> ext = new ArrayList<>();
        //ext.add("VK_KHR_swapchain");
        logicalDevice = new LogicalDevice(physicalDevice, qfs, ext);

    }

    private void mainLoop() {

        while(!window.closed()) {
            window.pollEvents();
        }

    }

    private void cleanup() {
        logicalDevice.destroy();
        instance.destroy();
        window.destroy();
        GLFW.glfwTerminate();

    }
}
