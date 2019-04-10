package com.thecrouchmode.vulkan;

import com.thecrouchmode.vulkan.device.physical.PhysicalDevice;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWVulkan;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.*;
import com.thecrouchmode.lwjgl.Util;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.vulkan.VK10.*;
import static org.lwjgl.vulkan.VK10.VK_SUCCESS;

public class Instance {

    private VkInstance instance;

    public Instance() {
        instance = createInstance();
    }

    private List<PhysicalDevice> physicalDevices() {
        int[] count = {0};
        vkEnumeratePhysicalDevices(instance,  count, null);
        try(var stack = MemoryStack.stackPush()) {
            var db = Util.vulkanGetCount(instance, VK10::vkEnumeratePhysicalDevices, stack::mallocPointer);
            return Util.mapPointer(db, this::getDevice);
        }
    }


    private static List<VkExtensionProperties> extensions() {
        try(var stack = MemoryStack.stackPush()) {
            var extensions = Util.vulkanGetCount(
                    (String)null,
                    VK10::vkEnumerateInstanceExtensionProperties,
                    c->VkExtensionProperties.mallocStack(c, stack));
            var props = new ArrayList<VkExtensionProperties>();
            extensions.forEach(props::add);
            return props;
        }
    }

    private static VkInstance createInstance() {
        try(var stack = MemoryStack.stackPush()) {

            var glfwRequiredExtensions = GLFWVulkan.glfwGetRequiredInstanceExtensions();
            extensions().forEach(e-> System.out.println("Existing "+e.extensionNameString()));


            Util.forEachPointer(glfwRequiredExtensions,
                    e->System.out.println("glfw required Extensions: "+MemoryUtil.memUTF8(e)));


            var appInfo = VkApplicationInfo.callocStack(stack);
            appInfo
                    .sType(VK_STRUCTURE_TYPE_APPLICATION_INFO)
                    .pNext(0)
                    .pApplicationName(stack.UTF8("Hello Triangle"))
                    .apiVersion(0)
                    .engineVersion(0)
                    .apiVersion(VK.getInstanceVersionSupported());
            var createInfo = VkInstanceCreateInfo.callocStack(stack);
            createInfo
                    .sType(VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO)
                    .pApplicationInfo(appInfo)
                    .ppEnabledExtensionNames(glfwRequiredExtensions);

            System.out.println("enabled extensions count: "+createInfo.enabledExtensionCount());

            PointerBuffer pp = MemoryUtil.memAllocPointer(1);

            var result = vkCreateInstance(createInfo, null, pp);


            System.out.println(result == VK_SUCCESS);

            return new VkInstance(pp.get(0), createInfo);
        }




    }

    public void destroy() {
        vkDestroyInstance(instance, null);
    }

    public PhysicalDevice getDevice(long devicePointer) {
        return new PhysicalDevice(new VkPhysicalDevice(devicePointer, instance));
    }
}
