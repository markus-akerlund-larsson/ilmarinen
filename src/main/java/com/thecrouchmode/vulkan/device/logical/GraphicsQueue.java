package com.thecrouchmode.vulkan.device.logical;

import org.lwjgl.vulkan.VkQueue;

import static org.lwjgl.vulkan.VK10.vkGetDeviceQueue;

public class GraphicsQueue {

    VkQueue graphicsQueue;


    public GraphicsQueue(VkQueue graphicsQueue) {
        this.graphicsQueue = graphicsQueue;
    }
}
