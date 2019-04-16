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

import static org.lwjgl.vulkan.EXTDebugReport.*;
import static org.lwjgl.vulkan.VK10.*;
import static org.lwjgl.vulkan.VK10.VK_SUCCESS;

public class Instance {

    public List<String> extensions = new ArrayList<>();
    public VkInstance instance;

    public Instance() {
        createInstance();
        setupDebugging();
    }

    public List<PhysicalDevice> physicalDevices() {
        try(var stack = MemoryStack.stackPush()) {
            var db = Util.vulkanGetCount(instance, VK10::vkEnumeratePhysicalDevices, stack::mallocPointer);
            return Util.mapPointer(db, this::getDevice);
        }
    }

    public static List<VkLayerProperties> avaliableLayers() {
        try(var stack = MemoryStack.stackPush()) {
            VkLayerProperties.Buffer layerProperties = Util.vulkanGetCount(VK10::vkEnumerateInstanceLayerProperties, VkLayerProperties::calloc);

            var layers = new ArrayList<VkLayerProperties>(layerProperties.capacity());
            layerProperties.forEach(layers::add);
            return layers;
        }
    }

    public static List<VkExtensionProperties> avaliableExtensions() {
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

    private void createInstance() {
        try(var stack = MemoryStack.stackPush()) {

            PointerBuffer glfwRequiredExtensions = GLFWVulkan.glfwGetRequiredInstanceExtensions();

            var enabledExtensions = stack.mallocPointer(glfwRequiredExtensions.capacity()+1);
            enabledExtensions.put(stack.UTF8(VK_EXT_DEBUG_REPORT_EXTENSION_NAME));
            enabledExtensions.put(glfwRequiredExtensions);
            Util.forEachPointer(glfwRequiredExtensions, e->extensions.add(MemoryUtil.memUTF8(e)));
            extensions.add(VK_EXT_DEBUG_REPORT_EXTENSION_NAME);
            enabledExtensions.flip();

            var appInfo = VkApplicationInfo.mallocStack(stack);
            appInfo
                    .sType(VK_STRUCTURE_TYPE_APPLICATION_INFO)
                    .pNext(0)
                    .pApplicationName(stack.UTF8("Hello Triangle"))
                    .applicationVersion(VK_MAKE_VERSION(0, 1, 0))
                    .pEngineName(stack.UTF8("Sethlans"))
                    .engineVersion(VK_MAKE_VERSION(0, 1,0))
                    .apiVersion(VK_API_VERSION_1_0);

            var createInfo = VkInstanceCreateInfo.calloc();

            PointerBuffer layers = stack.callocPointer(1);
            layers.put(stack.UTF8("VK_LAYER_LUNARG_standard_validation"));
            createInfo.sType(VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO);
            createInfo.pNext(0);
            createInfo.pApplicationInfo(appInfo);
            createInfo.ppEnabledExtensionNames(enabledExtensions);
            createInfo.ppEnabledLayerNames(layers.flip());

            PointerBuffer handle = stack.pointers(1);

            var result = vkCreateInstance(createInfo, null, handle);


            System.out.println(result == VK_SUCCESS);

            instance = new VkInstance(handle.get(0), createInfo);
        }
    }

    private void setupDebugging() {
        final VkDebugReportCallbackEXT debugCallback = new VkDebugReportCallbackEXT() {
            public int invoke(int flags, int objectType, long object, long location, int messageCode, long pLayerPrefix, long pMessage, long pUserData) {
                System.err.println("ERROR: "+VkDebugReportCallbackEXT.getString(pLayerPrefix));
                System.err.println('\t'+VkDebugReportCallbackEXT.getString(pMessage));
                return 0;
            }
        };

        try(var stack = MemoryStack.stackPush()) {


            var debugCreateInfo = VkDebugReportCallbackCreateInfoEXT.mallocStack()
                    .sType(VK_STRUCTURE_TYPE_DEBUG_REPORT_CALLBACK_CREATE_INFO_EXT)
                    .pfnCallback(debugCallback)
                    //.pUserData(0)
                    //.pNext(0)
                    .flags(VK_DEBUG_REPORT_ERROR_BIT_EXT | VK_DEBUG_REPORT_WARNING_BIT_EXT);

            long[] callback = {0};
            vkCreateDebugReportCallbackEXT(instance, debugCreateInfo, null, callback);
        }
    }

    public void destroy() {
        vkDestroyInstance(instance, null);
    }

    public PhysicalDevice getDevice(long devicePointer) {
        return new PhysicalDevice(new VkPhysicalDevice(devicePointer, instance));
    }
}
