import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class UserManagement {

    private static void addDetails(UserStore userStore) {
        Scanner reader = new Scanner(System.in);
        System.out.print("1. Full Name: ");
        String name = reader.nextLine();
        if (name.trim().isEmpty()) {
            System.out.println("Name cannot be blank, try again.");
            return;
        }
        short age;
        System.out.print("2. Age: ");
        try {
            age = Short.parseShort(reader.nextLine());
        } catch (NumberFormatException ime) {
            System.out.println("Age should be an integer, try again.");
            return;
        }
        System.out.print("3. Address: ");
        String address = reader.nextLine();
        if (address.trim().isEmpty()) {
            System.out.println("Address cannot be empty, try again.");
            return;
        }
        int rollNo;
        System.out.print("4. Roll Number: ");
        try {
            rollNo = Integer.parseInt(reader.nextLine());
        } catch (NumberFormatException ime) {
            System.out.println("Roll number should be an integer, try again.");
            return;
        }
        System.out.print("5. Enter set of courses interested in separated by a comma (A, B, C, D, E, and F): ");
        String[] coursesEntered = reader.next().split(",");
        if (coursesEntered.length < 4) {
            System.out.printf("It is mandatory to be enrolled in at least 4 courses; you entered only %d. Try again.\n", coursesEntered.length);
            return;
        }
        UserStore.Courses[] courses = new UserStore.Courses[coursesEntered.length];
        for (int i = 0; i < coursesEntered.length; i++) {
            try {
                courses[i] = UserStore.Courses.valueOf(coursesEntered[i]);
            } catch (IllegalArgumentException iae) {
                System.out.println("Acceptable courses are A, B, C, D, E, and F. Try again.");
                return;
            } catch (ArrayIndexOutOfBoundsException aie) {
                System.out.println("Max course limit is 6 (A, B, C, D, E, and F). Try again");
                return;
            }
        }
        try {
            userStore.addUser(name, age, address, rollNo, courses);
            System.out.printf("User %s successfully added\n", name);
        } catch (InstanceAlreadyExistsException iee) {
            System.out.println(iee.getMessage());
            System.out.println("Try again.");
        }
    }

    private static void displayDetails(UserStore userStore) {
        Scanner reader = new Scanner(System.in);
        System.out.print("Do you want the display sorted in any particular order? (y/n): ");
        if (reader.next().charAt(0) == 'y') {
            System.out.print("Specify the order (name, roll number, age, address) as (1, 2, 3, 4) respectively: ");
            userStore.display(reader.nextInt());
        } else
            userStore.display(0);
    }

    private static void deleteDetails(UserStore userStore) {
        Scanner reader = new Scanner(System.in);
        System.out.print("Enter the roll no of the user: ");
        int rollNo = reader.nextInt();
        try {
            userStore.deleteUser(rollNo);
            System.out.println("User successfully deleted!");
        } catch (InstanceNotFoundException ine) {
            System.out.println(ine.getMessage());
            System.out.println("Try again.");
        }
    }

    private static void saveDetails(UserStore userStore) {
        try {
            userStore.saveToDisk();
            System.out.println("Contents successfully saved to disk!");
        } catch (IOException ioe) {
            System.out.println("There was a problem while saving to disk: " + ioe.getMessage());
        }
    }

    private static void onExitChanges(UserStore userStore) {
        Scanner reader = new Scanner(System.in);
        System.out.print("Do you want to save changes to disk before exiting? (y/n): ");
        if (reader.next().charAt(0) == 'y')
            saveDetails(userStore);
    }

    public static void main(String[] args) {
        System.out.println("1. Add User Details");
        System.out.println("2. Display User Details");
        System.out.println("3. Delete User Details");
        System.out.println("4. Save User Details");
        System.out.println("5. Exit");

        UserStore userStore = new UserStore();
        try {
            userStore.restoreFromDisk();
        } catch (Exception e) {
            System.out.println("Error loading data from disk: " + e.getMessage());
        }

        Scanner reader = new Scanner(System.in);
        while (true) {
            System.out.print("\nEnter your choice (1,2,3,4,5): ");
            switch (reader.nextShort()) {
                case 1:
                    addDetails(userStore);
                    break;
                case 2:
                    displayDetails(userStore);
                    break;
                case 3:
                    deleteDetails(userStore);
                    break;
                case 4:
                    saveDetails(userStore);
                    break;
                case 5:
                    onExitChanges(userStore);
                    System.exit(0);
                    break;
                default:
                    System.out.println("Acceptable choices are 1, 2, 3, 4, and 5. Please try again.");
            }
        }
    }
}