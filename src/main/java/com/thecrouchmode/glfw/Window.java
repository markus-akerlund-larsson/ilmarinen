package com.thecrouchmode.glfw;

import org.lwjgl.glfw.GLFW;

public class Window {

    private final long id;

    public Window(int width, int height, String title) {
        GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, GLFW.GLFW_NO_API);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

        id = GLFW.glfwCreateWindow(width, height, title, 0, 0);
    }

    public void pollEvents() {
        GLFW.glfwPollEvents();
    }

    public boolean closed() {
        return GLFW.glfwWindowShouldClose(id);
    }

    public void destroy() {
        GLFW.glfwDestroyWindow(id);
    }
}
