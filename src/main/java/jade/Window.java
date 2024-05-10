package jade;


import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private int width;
    private int height;
    private final String title;
    private long glfwWindow;
    private static Triangle currentForm;

    private static Window instance;

    private static int currentFormIndex = -1;
    private static Window window = null;

    public Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Jade";
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }

        return Window.window;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and the free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE); // the window will be resizable

        // Create the window
        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
           Window.setWidth(newWidth);
           Window.setHeight(newHeight);
        });

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync
        glfwSwapInterval(1);
        // Make the window visible
        glfwShowWindow(glfwWindow);
        GL.createCapabilities();
    }

    public void loop() {
        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        currentForm = new Triangle();
        currentForm.init();
//        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        while (!glfwWindowShouldClose(glfwWindow)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the frame buffer

            processInput(glfwWindow);
            currentForm.update(0.5F);

            glfwSwapBuffers(glfwWindow);
            glfwPollEvents();
        }
    }

    private void processInput(long window)
    {
        if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS)
            glfwSetWindowShouldClose(window, true);

        if (glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS)
        {
            currentForm.sumControl();
        }
        if (glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS)
        {
           // mixValue -= 0.001f; // change this value accordingly (might be too slow or too fast based on system hardware)
        }
    }

    public static void setWidth(int newWidth) {
        get().width = newWidth;
    }

    public static void setHeight(int newHeight) {
        get().height = newHeight;
    }

    public long getWindow() {
        return glfwWindow;
    }

}
