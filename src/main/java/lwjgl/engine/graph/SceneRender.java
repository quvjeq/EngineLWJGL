package lwjgl.engine.graph;

import lwjgl.engine.scene.Scene;

import java.util.*;

import static org.lwjgl.opengl.GL30.*;

public class SceneRender {
    private ShaderProgram shaderProgram;
    private UniformsMap uniformsMap;
    private static final String uniformName = "transform";


    public SceneRender() {
        String root = String.valueOf(getClass().getResource("/assets/shaders/scene.frag"));
        System.out.println(root);
        List<ShaderProgram.ShaderModuleData> shaderModuleDataList = new ArrayList<>();
        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER));
        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER));
        shaderProgram = new ShaderProgram(shaderModuleDataList);
        createUniforms();
    }

    public void cleanup() {
        shaderProgram.cleanup();
    }

    private void createUniforms() {
        uniformsMap = new UniformsMap(shaderProgram.getProgramId());
        uniformsMap.createUniform(uniformName);
    }

    public void render(Scene scene) {
        shaderProgram.bind();


        scene.getMeshMap().values().forEach(mesh -> {
        uniformsMap.setUniform(uniformName, scene.getProjection().getProjMatrix());
                    glBindVertexArray(mesh.getVaoId());
                    glDrawElements(GL_TRIANGLES, mesh.getNumVertices(), GL_UNSIGNED_INT, 0);
//                    glDrawArrays(GL_TRIANGLES, 0, mesh.getNumVertices());
                }
        );

        glBindVertexArray(0);

        shaderProgram.unbind();
    }
}
