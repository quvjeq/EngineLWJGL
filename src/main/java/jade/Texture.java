package jade;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30C.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private final String filepath;
    private int texture;

    public Texture(String filepath) {
        this.filepath = filepath;
        this.texture = -1;
    }

    void init() {
        // Generate texture on GPU
        texture = glGenTextures();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);    // set texture wrapping to GL_REPEAT (default wrapping method)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        // set texture filtering parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        // load image, create texture and generate mipmaps
        stbi_set_flip_vertically_on_load(true); // tell stb_image.h to flip loaded texture's on the y-axis.

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);

        if (image != null) {
            switch (channels.get(0)) {
                case 3 -> glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(), height.get(),
                        0, GL_RGB, GL_UNSIGNED_BYTE, image);
                case 4 -> glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(),
                        0, GL_RGBA, GL_UNSIGNED_BYTE, image);
                default -> System.out.println("Error: (Texture) Unknown number of channels '" + channels.get(0) + "'");
            }
            glGenerateMipmap(GL_TEXTURE_2D);
        } else {
            assert false : "Error: (Texture) Could not load image '" + filepath + "'";
        }
        stbi_image_free(image);
    }

    public int getTexture() {
        return texture;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
