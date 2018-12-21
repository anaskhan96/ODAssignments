import javax.management.InstanceNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DependencyGraph {

    /* Ask user for node id and fetch its parents */
    private static void fetchImmediateParents(Graph dependencyGraph) {
        Scanner reader = new Scanner(System.in);
        System.out.print("Enter node id: ");
        Long[] parents = new Long[0];
        try {
            parents = dependencyGraph.getParents(reader.nextLong());
            if (parents.length == 0)
                System.out.println("Given node has no parents.");
            else {
                System.out.println("Parent ids are as follows :-");
                for (Long parentId : parents)
                    System.out.println(parentId);
            }
        } catch (InstanceNotFoundException ine) {
            System.out.println(ine.getMessage());
            System.out.println("Try again.");
        }
    }

    /* Ask user for node id and fetch its children */
    private static void fetchImmediateChildren(Graph dependencyGraph) {
        Scanner reader = new Scanner(System.in);
        System.out.print("Enter node id: ");
        Long[] children = new Long[0];
        try {
            children = dependencyGraph.getChildren(reader.nextLong());
            if (children.length == 0)
                System.out.println("Given node has no children.");
            else {
                System.out.println("Children ids are as follows :-");
                for (Long childId : children)
                    System.out.println(childId);
            }
        } catch (InstanceNotFoundException ine) {
            System.out.println(ine.getMessage());
            System.out.println("Try again.");
        }
    }

    /* Ask user for node id and fetch its ancestors */
    private static void fetchAncestors(Graph dependencyGraph) {
        Scanner reader = new Scanner(System.in);
        System.out.print("Enter node id: ");
        Long[] ancestors = new Long[0];
        try {
            ancestors = dependencyGraph.getAncestors(reader.nextLong());
            if (ancestors.length == 0)
                System.out.println("Given node has no ancestors.");
            else {
                System.out.println("Ancestor ids are as follows :-");
                for (Long ancestorId : ancestors)
                    System.out.println(ancestorId);
            }
        } catch (InstanceNotFoundException ine) {
            System.out.println(ine.getMessage());
            System.out.println("Try again.");
        }
    }

    /* Ask user for node id and fetch its descendents */
    private static void fetchDescendents(Graph dependencyGraph) {
        Scanner reader = new Scanner(System.in);
        System.out.print("Enter node id: ");
        Long[] descendents = new Long[0];
        try {
            descendents = dependencyGraph.getDescendents(reader.nextLong());
            if (descendents.length == 0)
                System.out.println("Given node has no descendents.");
            else {
                System.out.println("Descendent ids are as follows :-");
                for (Long descentdentId : descendents)
                    System.out.println(descentdentId);
            }
        } catch (InstanceNotFoundException ine) {
            System.out.println(ine.getMessage());
            System.out.println("Try again.");
        }
    }

    /* Ask user for a parent and child node id, and delete their dependency */
    private static void deleteDependency(Graph dependencyGraph) {
        Scanner reader = new Scanner(System.in);
        System.out.print("Enter the parent id: ");
        Long parentId = reader.nextLong();
        System.out.print("Enter the child id: ");
        Long childId = reader.nextLong();
        try {
            dependencyGraph.deleteDependency(parentId, childId);
            System.out.println("Dependency successfully deleted.");
        } catch (InstanceNotFoundException ine) {
            System.out.println(ine.getMessage());
            System.out.println("Try again.");
        }
    }

    /* Ask user for a node id and delete it */
    private static void deleteNode(Graph dependencyGraph) {
        Scanner reader = new Scanner(System.in);
        System.out.print("Enter the node id: ");
        try {
            dependencyGraph.deleteNode(reader.nextLong());
            System.out.println("Node successfully deleted");
        } catch (InstanceNotFoundException ine) {
            System.out.println(ine.getMessage());
            System.out.println("Try again.");
        }
    }

    /* Ask user for a parent and child node id, and create a dependency between them */
    private static void addDependency(Graph dependencyGraph) {
        Scanner reader = new Scanner(System.in);
        System.out.print("Enter the parent id: ");
        Long parentId = reader.nextLong();
        System.out.print("Enter the child id: ");
        Long childId = reader.nextLong();
        try {
            dependencyGraph.addDependency(parentId, childId);
            System.out.println("Dependency successfully created.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Try again.");
        }
    }

    /* Ask user for new node details */
    private static void addNode(Graph dependencyGraph) {
        Scanner reader = new Scanner(System.in);
        System.out.print("Enter node name: ");
        String name = reader.nextLine();
        Map<String, String> details = new HashMap<>();
        System.out.print("Any other details to add? (y/n): ");
        while (reader.next().charAt(0) == 'y') {
            System.out.print("Key: ");
            String key = reader.nextLine();
            System.out.print("Value: ");
            String value = reader.nextLine();
            details.put(key, value);
            System.out.print("Any other details to add? (y/n): ");
        }
        Long newNodeId = dependencyGraph.addNode(name, details);
        System.out.printf("Node successfully created with id %d\n", newNodeId);
    }

    /* Ask user for node id and display its details */
    private static void printNodeDetails(Graph dependencyGraph) {
        Scanner reader = new Scanner(System.in);
        System.out.print("Enter node name: ");
        dependencyGraph.displayNodeDetails(reader.nextLine());
    }

    public static void main(String[] args) {
        Graph dependencyGraph = new Graph();

        System.out.println("1. Get immediate parents of a node");
        System.out.println("2. Get immediate children of a node");
        System.out.println("3. Get ancestors of a node");
        System.out.println("4. Get descendents of a node");
        System.out.println("5. Delete a dependency");
        System.out.println("6. Delete a node");
        System.out.println("7. Add a dependency");
        System.out.println("8. Add a node");
        System.out.println("9. Display node details");
        System.out.println("10. Exit");

        Scanner reader = new Scanner(System.in);
        while (true) {
            System.out.print("\nEnter your choice: ");
            switch (reader.nextInt()) {
                case 1:
                    fetchImmediateParents(dependencyGraph);
                    break;
                case 2:
                    fetchImmediateChildren(dependencyGraph);
                    break;
                case 3:
                    fetchAncestors(dependencyGraph);
                    break;
                case 4:
                    fetchDescendents(dependencyGraph);
                    break;
                case 5:
                    deleteDependency(dependencyGraph);
                    break;
                case 6:
                    deleteNode(dependencyGraph);
                    break;
                case 7:
                    addDependency(dependencyGraph);
                    break;
                case 8:
                    addNode(dependencyGraph);
                    break;
                case 9:
                    printNodeDetails(dependencyGraph);
                    break;
                case 10:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Acceptable choices are from 1-10. Please try again.");
            }
        }
    }
}
