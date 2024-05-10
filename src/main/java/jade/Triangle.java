package jade;


import static org.lwjgl.opengl.GL30.*;

public class Triangle extends Forms {

    private int VAO, VBO, EBO;
    public Shader defaultShader;
    public Texture defaultTexture, secondTexture;
    private int[] indices;
    public float control = 0.2f;

    @Override
    public void update(float dt) {
        // Set control in our shader, previously changed
        // by KeyInput
        defaultShader.setFloat("control", control);
        defaultShader.use();

        glUniform1i(glGetUniformLocation(defaultShader.getShaderProgram(), "ourTexture"), 0); // set it manually
        glUniform1i(glGetUniformLocation(defaultShader.getShaderProgram(), "ourTxt2"), 1); // set it manually

        glActiveTexture(GL_TEXTURE0);
        defaultTexture.bind();
        glActiveTexture(GL_TEXTURE1);
        secondTexture.bind();

        glBindVertexArray(VAO);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

        // Clean
        defaultTexture.unbind();
        secondTexture.unbind();
        glBindVertexArray(0);
    }

    @Override
    public void init() {
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();

        defaultTexture = new Texture("assets/textures/container.jpg");
        defaultTexture.init();

        secondTexture = new Texture("assets/textures/awesomeface.png");
        secondTexture.init();

        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");


        float vertices[] = {
                // positions          // colors           // texture coords
                0.5f,  0.5f, 0.0f,   1.0f, 0.0f, 0.0f,   1.0f, 1.0f,   // top right
                0.5f, -0.5f, 0.0f,   0.0f, 1.0f, 0.0f,   1.0f, 0.0f,   // bottom right
                -0.5f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f,   0.0f, 0.0f,   // bottom left
                -0.5f,  0.5f, 0.0f,   1.0f, 1.0f, 0.0f,   0.0f, 1.0f    // top left
        };

        indices = new int[]{
                0, 1, 3,
                1, 2, 3
        };

        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        EBO = glGenBuffers();

        glBindVertexArray(VAO);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * 4, 0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * 4, 3 * 4);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * 4, 6 * 4);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
    }

    public void sumControl() {
        control += 0.002F;
        if (control >= 1)
            control = 1;
    }
}
