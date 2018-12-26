import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.management.InstanceNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class GraphTest {
    private Graph dependencyGraph = new Graph();

    @Test
    void addNode() {
        Map<String, String> detailsMap = new HashMap<>();
        detailsMap.put("type", "testNode");
        this.dependencyGraph.addNode("Node1", detailsMap);
        Graph.Node node = this.dependencyGraph.getNodes().get(0);
        assertAll(() -> assertEquals(node.getName(), "Node1"),
                () -> assertEquals(node.getDetails().get("type"), "testNode"));
    }

    @Test
    void deleteNode() {
        long id = this.dependencyGraph.addNode("Node2", null);
        Executable nodeDeletion = () -> this.dependencyGraph.deleteNode(id);
        assertDoesNotThrow(nodeDeletion);
        // try deleting a second time
        assertThrows(InstanceNotFoundException.class, nodeDeletion);
    }

    @Test
    void addDependency() {
        long parentId = this.dependencyGraph.addNode("parentNode", null);
        long childId = this.dependencyGraph.addNode("childNone", null);
        Executable nonExistentParentId = () -> this.dependencyGraph.addDependency(-1, childId);
        assertThrows(InstanceNotFoundException.class, nonExistentParentId);
        Executable nonExistentChildId = () -> this.dependencyGraph.addDependency(parentId, -1);
        assertThrows(InstanceNotFoundException.class, nonExistentChildId);
        Executable dependencyAddition = () -> this.dependencyGraph.addDependency(parentId, childId);
        assertDoesNotThrow(dependencyAddition);
        List<LinkedList<Long>> adjList = this.dependencyGraph.getAdjList();
        for (LinkedList<Long> nodeList : adjList) {
            if (nodeList.getFirst() == parentId)
                assertTrue(nodeList.contains(childId));
        }
    }

    @Test
    void deleteDependency() throws Exception {
        long parentId = this.dependencyGraph.addNode("parentNode", null);
        long childId = this.dependencyGraph.addNode("childNone", null);
        this.dependencyGraph.addDependency(parentId, childId);
        Executable nonExistentParentId = () -> this.dependencyGraph.deleteDependency(-1, childId);
        assertThrows(InstanceNotFoundException.class, nonExistentParentId);
        Executable nonExistentChildId = () -> this.dependencyGraph.deleteDependency(parentId, -1);
        assertThrows(InstanceNotFoundException.class, nonExistentChildId);
        Executable dependencyDeletion = () -> this.dependencyGraph.deleteDependency(parentId, childId);
        assertDoesNotThrow(dependencyDeletion);
        List<LinkedList<Long>> adjList = this.dependencyGraph.getAdjList();
        for (LinkedList<Long> nodeList : adjList) {
            if (nodeList.getFirst() == parentId)
                assertFalse(nodeList.contains(childId));
        }
    }

    @Test
    void getParents() throws Exception {
        long parentId = this.dependencyGraph.addNode("parentNode", null);
        long childId = this.dependencyGraph.addNode("childNone", null);
        this.dependencyGraph.addDependency(parentId, childId);
        Executable nonExistentId = () -> this.dependencyGraph.getParents(-1);
        assertThrows(InstanceNotFoundException.class, nonExistentId);
        Long[] parentIds = this.dependencyGraph.getParents(childId);
        assertEquals(parentIds[0], parentId);
    }

    @Test
    void getChildren() throws Exception {
        long parentId = this.dependencyGraph.addNode("parentNode", null);
        long childId = this.dependencyGraph.addNode("childNone", null);
        this.dependencyGraph.addDependency(parentId, childId);
        Executable nonExistentId = () -> this.dependencyGraph.getChildren(-1);
        assertThrows(InstanceNotFoundException.class, nonExistentId);
        Long[] childIds = this.dependencyGraph.getChildren(parentId);
        assertEquals(childIds[0], childId);
    }

    @Test
    void getAncestors() throws Exception {
        long grandParentId = this.dependencyGraph.addNode("grandParentNode", null);
        long parentId = this.dependencyGraph.addNode("parentNode", null);
        long childId = this.dependencyGraph.addNode("childNone", null);
        this.dependencyGraph.addDependency(grandParentId, parentId);
        this.dependencyGraph.addDependency(parentId, childId);
        Executable nonExistentId = () -> this.dependencyGraph.getAncestors(-1);
        assertThrows(InstanceNotFoundException.class, nonExistentId);
        Long[] ancestorIds = this.dependencyGraph.getAncestors(childId);
        assertAll(() -> assertEquals(ancestorIds[0], parentId),
                () -> assertEquals(ancestorIds[1], grandParentId));
    }

    @Test
    void getDescendents() throws Exception {
        long grandParentId = this.dependencyGraph.addNode("grandParentNode", null);
        long parentId = this.dependencyGraph.addNode("parentNode", null);
        long childId = this.dependencyGraph.addNode("childNone", null);
        this.dependencyGraph.addDependency(grandParentId, parentId);
        this.dependencyGraph.addDependency(parentId, childId);
        Executable nonExistentId = () -> this.dependencyGraph.getDescendents(-1);
        assertThrows(InstanceNotFoundException.class, nonExistentId);
        Long[] descendentIds = this.dependencyGraph.getDescendents(grandParentId);
        assertAll(() -> assertEquals(descendentIds[0], parentId),
                () -> assertEquals(descendentIds[1], childId));
    }
}