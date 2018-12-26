import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class UserStoreTest {
    private UserStore userStore = new UserStore();
    private UserStore.Courses[] courses = new UserStore.Courses[]{UserStore.Courses.A, UserStore.Courses.D};

    @Test
    void addUserTest() {
        Executable userAddition = () -> this.userStore.addUser("testUser", (short) 18, "221B Baker St", 35, this.courses);
        assertDoesNotThrow(userAddition);
        // try adding the a user with the same roll no again, it should throw an exception
        Executable duplicateUserAddition = () -> this.userStore.addUser("testUser1", (short) 21, "221B Chef St", 35, this.courses);
        assertThrows(InstanceAlreadyExistsException.class, duplicateUserAddition);
    }

    @Test
    void deleteUserTest() throws InstanceAlreadyExistsException {
        this.userStore.addUser("testUser", (short) 18, "221B Baker St", 35, this.courses);
        Executable userDeletion = () -> this.userStore.deleteUser(35);
        assertDoesNotThrow(userDeletion);
        // try deleting a second time
        assertThrows(InstanceNotFoundException.class, userDeletion);
    }

    @Test
    void saveAndRestoreTest() throws InstanceAlreadyExistsException, IOException, ClassNotFoundException {
        this.userStore.addUser("testUser", (short) 25, "705 Bellandur", 15, this.courses);
        this.userStore.saveToDisk();
        UserStore newUserStore = new UserStore();
        newUserStore.restoreFromDisk();
        UserStore.User user1 = newUserStore.getUsers().get(0);
        UserStore.User user2 = this.userStore.getUsers().get(0);
        assertAll(() -> assertEquals(user1.getName(), user2.getName()),
                () -> assertEquals(user1.getAge(), user2.getAge()),
                () -> assertEquals(user1.getAddress(), user2.getAddress()),
                () -> assertEquals(user1.getRollNo(), user2.getRollNo()),
                () -> assertEquals(user1.getCourses()[0], user2.getCourses()[0]),
                () -> assertEquals(user1.getCourses()[1], user2.getCourses()[1]));
    }
}