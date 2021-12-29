package com.thecrouchmode.vulkan;

import com.thecrouchmode.glfw.Window;
import com.thecrouchmode.vulkan.device.logical.LogicalDevice;
import com.thecrouchmode.vulkan.device.physical.PhysicalDevice;
import com.thecrouchmode.vulkan.device.physical.QueueFamily;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.vulkan.VkLayerProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HelloTriangleApp {


    private Instance instance;
    private Window window;
    private LogicalDevice logicalDevice;
    private Surface surface;

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
        instance = new Instance(new String[]{"VK_LAYER_KHRONOS_validation"});

        System.out.println("Physical devices:");
        instance.physicalDevices(surface).forEach(System.out::println);
        System.out.println();

        surface = new Surface(window, instance);

        PhysicalDevice physicalDevice = instance.physicalDevices(surface).get(0);
        System.out.println("Avaliable extensions:");
        Instance.avaliableExtensions().forEach(e->System.out.println(e.extensionNameString()));
        System.out.println();

        System.out.println("Avaliable layers:");
        Instance.avaliableLayers().stream()
                .map(VkLayerProperties::layerNameString)
                .forEach(System.out::println);
        System.out.println();

        System.out.println("Avaliable queues:");
        physicalDevice.queueFamilies(surface).forEach(System.out::println);
        System.out.println();

        List<QueueFamily> qfs = physicalDevice.queueFamilies(surface)
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
        //logicalDevice.destroy();
        instance.destroy();
        window.destroy();
        GLFW.glfwTerminate();

    }
}
