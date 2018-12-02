import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class UserStore implements Serializable {

    enum Courses {
        A, B, C, D, E, F
    }

    private class User implements Serializable {
        private String name;
        private short age;
        private String address;
        private int rollNo;
        private Courses[] courses;

        User(String name, short age, String address, int rollNo, Courses[] courses) {
            this.name = name;
            this.age = age;
            this.address = address;
            this.courses = courses;
            this.rollNo = rollNo;
        }
    }

    private List<User> users = new ArrayList<>();

    void addUser(String name, short age, String address, int rollNo, Courses[] courses) throws InstanceAlreadyExistsException {
        User newUser = new User(name, age, address, rollNo, courses);
        for (User user : this.users) {
            if (user.rollNo == newUser.rollNo)
                throw new InstanceAlreadyExistsException("Given roll no has already been added.");
        }
        this.users.add(newUser);
        // sorting based on ascending order of name, and if names are equal, the roll numbers instead
        this.users.sort(new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                int nameSort = user1.name.compareToIgnoreCase(user2.name);
                if (nameSort == 0)
                    return user1.rollNo - user2.rollNo;
                return nameSort;
            }
        });
    }

    void deleteUser(int rollNo) throws InstanceNotFoundException {
        for (User user : this.users) {
            if (user.rollNo == rollNo) {
                this.users.remove(user);
                return;
            }
        }
        // user with the given roll number doesn't exist
        throw new InstanceNotFoundException("User with given roll no does not exist");
    }

    private static void sortList(List<User> usersList, int sortOption) {
        switch (sortOption) {
            case 1:
                usersList.sort(new Comparator<User>() {
                    @Override
                    public int compare(User user1, User user2) {
                        return user1.name.compareToIgnoreCase(user2.name);
                    }
                });
                break;
            case 2:
                usersList.sort(new Comparator<User>() {
                    @Override
                    public int compare(User user1, User user2) {
                        return user1.rollNo - user2.rollNo;
                    }
                });
                break;
            case 3:
                usersList.sort(new Comparator<User>() {
                    @Override
                    public int compare(User user1, User user2) {
                        return user1.age - user2.age;
                    }
                });
                break;
            case 4:
                usersList.sort(new Comparator<User>() {
                    @Override
                    public int compare(User user1, User user2) {
                        return user1.address.compareToIgnoreCase(user2.address);
                    }
                });
                break;
            default:
                System.out.println("Valid sort option not provided. Displaying with the default option...");
        }
    }

    void display(int sortOption) {
        List<User> usersList = new ArrayList<>(this.users);
        if (sortOption > 0) {
            sortList(usersList, sortOption);
        }
        System.out.println(new String(new char[150]).replace("\0", "-"));
        System.out.printf("%30s%30s%30s%30s%30s\n", "Name", "Roll Number", "Age", "Address", "Courses");
        System.out.println(new String(new char[150]).replace("\0", "-"));
        for (User user : usersList) {
            String[] courses = new String[user.courses.length];
            for (int i = 0; i < courses.length; i++) courses[i] = user.courses[i].toString();
            System.out.printf("%30s%30d%30d%30s%30s\n", user.name, user.rollNo, user.age, user.address, String.join(",", courses));
        }
        System.out.println(new String(new char[150]).replace("\0", "-"));
    }

    void saveToDisk() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Users.serialized"));
        oos.writeObject(this.users);
        oos.flush();
    }

    void restoreFromDisk() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Users.serialized"));
        this.users = (List<User>) ois.readObject();
    }
}
