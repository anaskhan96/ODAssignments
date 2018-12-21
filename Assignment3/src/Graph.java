import javax.management.InstanceNotFoundException;
import java.util.*;
import java.util.stream.Stream;

public class Graph {

    class Node {
        private long id;
        private String name;
        private Map<String, String> details;

        Node(long id, String name, Map<String, String> details) {
            this.id = id;
            this.name = name;
            this.details = details;
        }

        long getId() {
            return this.id;
        }

        void displayDetails() {
            System.out.printf("Node ID: %d\n", this.id);
            System.out.printf("Node Name: %s\n", this.name);
            System.out.println("Additional Info :-");
            for (Map.Entry<String, String> entry : this.details.entrySet())
                System.out.printf("%s: %s\n", entry.getKey(), entry.getValue());
            System.out.println();
        }
    }

    private static Long nodeIds = (long) 0;
    private List<Node> nodes = new ArrayList<>(); // list of all the nodes
    private List<LinkedList<Long>> adjList = new ArrayList<>(); // adjacency list representing the graph

    /* Display a node details, given the name */
    void displayNodeDetails(String name) {
        for (Node node : this.nodes) {
            if (node.name.equals(name)) {
                node.displayDetails();
                break;
            }
        }
    }

    /* Add a node to the graph, without any implicit dependencies, and return its id */
    Long addNode(String name, Map<String, String> details) {
        // long id = new Date().getTime(); // ensures that the id is always unique
        Node node = new Node(nodeIds++, name, details);
        this.nodes.add(node);
        LinkedList<Long> nodeList = new LinkedList<>(); // new linked list for tracking directed edges from this node
        nodeList.add(node.id);
        this.adjList.add(nodeList); // adding the newly formed linked list to the adjacency list representation
        return node.id;
    }

    /* Delete a node, and all its dependencies too */
    void deleteNode(long id) throws InstanceNotFoundException {
        if (isNonExistent(id))
            throw new InstanceNotFoundException("Given node doesn't exist");
        // remove node dependencies
        for (int i = 0; i < this.adjList.size(); i++) {
            LinkedList<Long> nodeList = this.adjList.get(i);
            if (nodeList.getFirst() == id) {
                this.adjList.remove(nodeList);
                i--;
            } else if (nodeList.contains(id))
                nodeList.remove(id);
        }
        // remove node data
        for (Node node : this.nodes) {
            if (node.id == id) {
                this.nodes.remove(node);
                break;
            }
        }
    }

    /* Add a dependency between a parent node and a child node */
    void addDependency(long parentId, long childId) throws Exception {
        if (isNonExistent(parentId))
            throw new InstanceNotFoundException("Parent node doesn't exist.");
        if (isNonExistent(childId))
            throw new InstanceNotFoundException("Child node doesn't exist.");
        // a cyclic dependency exists if the child is an ancestor of the parent
        Long[] ancestors = this.getAncestors(parentId);
        for (Long ancestorId : ancestors) {
            if (ancestorId == childId)
                throw new Exception("Cyclic dependency error: Parent already depends on child.");
        }
        for (LinkedList<Long> nodeList : this.adjList) {
            if (nodeList.getFirst() == parentId && !nodeList.contains(childId)) {
                nodeList.add(childId);
                break;
            }
        }
    }

    /* Delete a dependency between a parent node and a child node */
    void deleteDependency(long parentId, long childId) throws InstanceNotFoundException {
        if (isNonExistent(parentId))
            throw new InstanceNotFoundException("Parent node doesn't exist");
        if (isNonExistent(childId))
            throw new InstanceNotFoundException("Child node doesn't exist");
        for (LinkedList<Long> nodeList : this.adjList) {
            if (nodeList.getFirst() == parentId && nodeList.contains(childId)) {
                nodeList.remove(childId);
                break;
            }
        }
    }

    /* Get parents of a given node */
    Long[] getParents(long id) throws InstanceNotFoundException {
        if (isNonExistent(id))
            throw new InstanceNotFoundException("Given node doesn't exist");
        List<Long> parents = new ArrayList<>();
        for (LinkedList<Long> nodeList : this.adjList) {
            if (nodeList.contains(id) && nodeList.getFirst() != id)
                parents.add(nodeList.getFirst());
        }
        return parents.toArray(new Long[parents.size()]);
    }

    /* Get children of a given node */
    Long[] getChildren(long id) throws InstanceNotFoundException {
        if (isNonExistent(id))
            throw new InstanceNotFoundException("Given node doesn't exist");
        Long[] children = new Long[1];
        for (LinkedList<Long> nodeList : this.adjList) {
            if (nodeList.getFirst() == id) {
                children = nodeList.toArray(new Long[nodeList.size()]);
                break;
            }
        }
        // removing the first element of the array which is the given node itself
        return Arrays.copyOfRange(children, 1, children.length);
    }

    /* Get ancestors of a given node */
    Long[] getAncestors(long id) throws InstanceNotFoundException {
        if (isNonExistent(id))
            throw new InstanceNotFoundException("Given node doesn't exist.");
        List<Long> ancestors = new ArrayList<>();
        Queue<Long> queue = new LinkedList<>();
        // instantiate queue with all immediate parents
        Long[] nodeParents = this.getParents(id);
        for (Long parentNodeId : nodeParents)
            queue.add(parentNodeId);
        // loop over queue to find ancestors
        while (!queue.isEmpty()) {
            Long currNodeId = queue.remove();
            if (!ancestors.contains(currNodeId))
                ancestors.add(currNodeId);
            Long[] currNodeParents = this.getParents(currNodeId);
            for (Long parentNodeId : currNodeParents) {
                if (!queue.contains(parentNodeId))
                    queue.add(parentNodeId);
            }
        }
        return ancestors.toArray(new Long[ancestors.size()]);
    }

    /* Get descendents of a given node */
    Long[] getDescendents(long id) throws InstanceNotFoundException {
        if (isNonExistent(id))
            throw new InstanceNotFoundException("Given node doesn't exist.");
        List<Long> descendents = new ArrayList<>();
        Queue<Long> queue = new LinkedList<>();
        // instantiate queue with all immediate children
        Long[] nodeChildren = this.getChildren(id);
        for (Long childNodeId : nodeChildren)
            queue.add(childNodeId);
        // loop over the queue to find descendents
        while (!queue.isEmpty()) {
            Long currNodeId = queue.remove();
            descendents.add(currNodeId);
            Long[] currNodeChildren = this.getChildren(currNodeId);
            for (Long childNodeId : currNodeChildren) {
                if (!queue.contains(childNodeId))
                    queue.add(childNodeId);
            }
        }
        return descendents.toArray(new Long[descendents.size()]);
    }

    /* Helper function to check if a node id is invalid */
    private boolean isNonExistent(long id) {
        Stream<Node> nodeStream = this.nodes.stream();
        return nodeStream.noneMatch(node -> id == node.getId());
    }
}
