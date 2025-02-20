package ex02;
import ex00.User;

public class UsersArrayList implements UsersList {
    private User[] users;
    private int size;
    private int maxSize;

    public UsersArrayList() {
        this.maxSize = 10;
        this.users = new User[maxSize];
        this.size = 0;
    }

    @Override
    public void addUser(User user) {
        if (size >= maxSize) {
            resizeArray();
        }
        users[size++] = user;
    }

    private void resizeArray() {
        maxSize = maxSize + (maxSize / 2);
        User[] newUsers = new User[maxSize];
        System.arraycopy(users, 0, newUsers, 0, users.length);
        users = newUsers;
    }

    @Override
    public User getUserById(int id) {
        for (int i = 0; i < size; i++) {
            if (users[i].getIdentifier() == id) {
                return users[i];
            }
        }
        throw new UserNotFoundException("User with ID " + id + " not found.");
    }

    @Override
    public User getUserByIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds.");
        }
        return users[index];
    }

    @Override
    public int getNumberOfUsers() {
        return size;
    }
}
