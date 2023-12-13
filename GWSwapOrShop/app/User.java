public class User {
    public String username;
    public String email;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User() {
        // Default constructor is required for Firebase
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }


}
