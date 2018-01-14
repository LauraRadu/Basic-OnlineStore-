public class User {


    private int userID;
    private String username;
    private String password;
    private Cart usersCart;

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }

    public Cart getUsersCart() {
        return usersCart;
    }

    public void setUsersCart(Cart usersCart) {
        this.usersCart = usersCart;
    }


    public User() {

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
