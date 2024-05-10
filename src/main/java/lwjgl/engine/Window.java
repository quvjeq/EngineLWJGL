package lwjgl.engine;

import java.util.concurrent.Callable;
import org.lwjgl.system.MemoryUtil;
import org.tinylog.Logger;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private final long windowHandle;
    private int height;
    private Callable<Void> resizeFunc;
    private int width;

    public Window(String title, WindowOptions opts, Callable<Void> resizeFunc) {
        this.resizeFunc = resizeFunc;

        // Init GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to init GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
//        // Configure version
//        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
//        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        // Set profile
        if (opts.compatibleProfile) {
//            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE);
        } else {
//            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
//            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_TRUE);
        }

        // Set width & height (If not setted before just do fullscreen)
        if (opts.width > 0 && opts.height > 0) {
            this.width = opts.width;
            this.height = opts.height;
        } else {
            glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            width = vidMode.width();
            height = vidMode.height();
        }

        System.out.println("Width: " + width + " Height: " + height + " Title: " + title);
        // Create Window
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create window");
        }

        glfwSetFramebufferSizeCallback(windowHandle, (window, w, h) -> resized(w, h));
        // Log error with TinyLog (see Docs)
        glfwSetErrorCallback((int errorCode, long msgPtr) ->
                Logger.error("Error code [{}], msg [{}]", errorCode, MemoryUtil.memUTF8(msgPtr))
        );

        // Setup callbacks
        glfwSetKeyCallback(windowHandle, (w, key, scancode, action, mods) -> {
            keyCallBack(key, action);
        });

//        // Get the thread stack and push a new frame
//        try (MemoryStack stack = stackPush()){
//            IntBuffer pWidth = stack.mallocInt(1);
//            IntBuffer pHeight = stack.mallocInt(1);
//
//            // Get the window size passed to glfwCreateWindow
//            glfwGetWindowSize(windowHandle, pWidth, pHeight); // Store in width and height values
//            // like a C pointer by MemoryStack
//
//            // Get the resolution of the primary monitor
//            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
//
//            // Center the window
//            glfwSetWindowPos(
//                    windowHandle,
//                    (vidMode.width() - pWidth.get(0))/ 2,
//                    (vidMode.height() - pHeight.get(0)) / 2
//            );
//        } // the stack frame is popped automatically
        glfwMakeContextCurrent(windowHandle);

        if (opts.fps > 0) {
            glfwSwapInterval(0);
        } else {
            glfwSwapInterval(1);
        }

        glfwShowWindow(windowHandle);

        int[] arrWidth = new int[1];
        int[] arrHeight = new int[1];
        glfwGetFramebufferSize(windowHandle, arrWidth, arrHeight);
        width = arrWidth[0];
        height = arrHeight[0];
    }

    public void keyCallBack(int key, int action) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
            glfwSetWindowShouldClose(windowHandle, true);
        }
    }

    public void cleanup() {
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);
        glfwTerminate();
        GLFWErrorCallback callback = glfwSetErrorCallback(null);
        if (callback != null) {
            callback.free();
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public long getWindowHandle() {
        return windowHandle;
    }

    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
    }

    public void pollEvents() {
        glfwPollEvents();
    }

    protected void resized(int width, int height) {
        this.width = width;
        this.height = height;
        try {
            resizeFunc.call();
        } catch (Exception excp) {
            Logger.error("Error calling resize callback", excp);
        }
    }

    public void update() {
        glfwSwapBuffers(windowHandle);
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    public static class WindowOptions {
        public boolean compatibleProfile;
        public int fps;
        public int height;
        public int ups = Engine.TARGET_UPS;
        public int width;
    }
}
