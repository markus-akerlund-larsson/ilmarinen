package com.thecrouchmode.vulkan;

import com.thecrouchmode.glfw.Window;
import org.lwjgl.glfw.GLFW;

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
