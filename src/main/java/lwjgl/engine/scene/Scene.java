package lwjgl.engine.scene;

import java.util.HashMap;
import java.util.Map;
import lwjgl.engine.graph.Mesh;

public class Scene {

    private Map<String, Mesh> meshMap;
    private Projection projection;

    public Scene(int width, int height) {
        meshMap = new HashMap<>();
        projection = new Projection(width, height);
    }

    public void addMesh(String meshId, Mesh mesh) {
        meshMap.put(meshId, mesh);
    }

    public void resize(int width, int height) {
        projection.updateProjMatrix(width, height);
    }

    public void cleanup() {
        meshMap.values().forEach(Mesh::cleanup);
    }

    public Projection getProjection() {
        return projection;
    }
    public Map<String, Mesh> getMeshMap() {
        return meshMap;
    }
}
