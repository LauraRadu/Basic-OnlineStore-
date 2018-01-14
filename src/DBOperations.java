import java.sql.*;
import java.util.*;

public class DBOperations {

    private static Connection conn;

    public void connectToDB() {

        final String URL = "jdbc:postgresql://54.93.65.5:5432/laura7";
        final String USERNAME = "fasttrackit_dev";
        final String PASSWORD = "fasttrackit_dev";

        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List getListProducts(ResultSet rs) throws ClassNotFoundException, SQLException {
        List<Product> listOfProducts = new ArrayList();
        while (rs.next()) {
            Product p = new Product();
            p.setProductName(rs.getString("name"));
            p.setProductPrice(rs.getInt("price"));
            p.setId(rs.getInt("id"));

            listOfProducts.add(p);
        }
        return listOfProducts;
    }

    public List getAllProducts() throws ClassNotFoundException, SQLException {
        connectToDB();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT name,price,id FROM products");

        List<Product> listOfProducts = new ArrayList();
        listOfProducts = getListProducts(rs);

        rs.close();
        st.close();
        conn.close();

        return listOfProducts;
    }

    public List getAllProductsByUser(int userId) throws ClassNotFoundException, SQLException {

        connectToDB();

        PreparedStatement pSt = conn.prepareStatement("select p.name, p.price,p.id from products as p inner join cart on p.id = cart.id_product where cart.id_user=?");

        pSt.setInt(1, userId);
        ResultSet rs = pSt.executeQuery();

        List<Product> listOfProducts = new ArrayList();
        listOfProducts = getListProducts(rs);

        pSt.close();
        conn.close();

        return listOfProducts;
    }


    public List searchProduct(String prod) throws ClassNotFoundException, SQLException {
        connectToDB();
        PreparedStatement pSt = conn.prepareStatement("select name, price, id from products where name=?");
        pSt.setString(1, prod);
        ResultSet rs = pSt.executeQuery();

        List<Product> listOfProducts = new ArrayList();
        listOfProducts = getListProducts(rs);

        rs.close();
        pSt.close();
        conn.close();

        return listOfProducts;
    }

    //adaugare user
    public void addUser(User u) throws ClassNotFoundException, SQLException {
        connectToDB();
        PreparedStatement pSt = conn.prepareStatement("INSERT INTO users (userNAME, password) VALUES (?,?)");

        pSt.setString(1, u.getUsername());
        pSt.setString(2, u.getPassword());

        int rowsInserted = pSt.executeUpdate();

        pSt.close();
        conn.close();
    }

    //modificare user
    public void modifyUser(User u, String newName, String newPassword) throws ClassNotFoundException, SQLException {
        connectToDB();
        PreparedStatement pSt = conn.prepareStatement("update users set username=?, password=? where username=?");

        pSt.setString(1, newName);
        pSt.setString(2, newPassword);
        pSt.setString(3, u.getUsername());

        pSt.executeUpdate();
        pSt.close();
        conn.close();
    }

    //stergere user
    public void deleteUser(User u) throws ClassNotFoundException, SQLException {
        connectToDB();
        PreparedStatement pSt = conn.prepareStatement("DELETE FROM users WHERE username=?");
        pSt.setString(1, u.getUsername());

        int rowsDeleted = pSt.executeUpdate();
        System.out.println(rowsDeleted + " users were deleted.");

        pSt.close();
        conn.close();
    }


    //adaugare produs
    public void addProductInDB(Product p) throws ClassNotFoundException, SQLException {
        connectToDB();
        PreparedStatement pSt = conn.prepareStatement("INSERT INTO products (name, price) VALUES (?,?)");

        pSt.setString(1, p.getProductName());
        pSt.setInt(2, p.getProductPrice());

        pSt.executeUpdate();
        pSt.close();
        conn.close();
    }


    public void addProductToCart(Product p, int userID) throws ClassNotFoundException, SQLException {
        connectToDB();

        PreparedStatement pSt = conn.prepareStatement("INSERT INTO cart (id_user, id_product) values (?,?)");

        pSt.setInt(1, userID);
        pSt.setInt(2, p.getId());

        pSt.executeUpdate();
        pSt.close();
        conn.close();
    }


    //modificare produs
    public void modifyProductInDB(Product p, String newName, int newPrice) throws ClassNotFoundException, SQLException {
        connectToDB();
        PreparedStatement pSt = conn.prepareStatement("update products set name=?, price=? where name=?");

        pSt.setString(1, newName);
        pSt.setInt(2, newPrice);
        pSt.setString(3, p.getProductName());

        pSt.executeUpdate();
        pSt.close();
        conn.close();
    }

    //stergere product
    public void deleteProductInDB(Product p) throws ClassNotFoundException, SQLException {
        connectToDB();
        PreparedStatement pSt = conn.prepareStatement("DELETE FROM products WHERE name=?");
        pSt.setString(1, p.getProductName());

        int rowsDeleted = pSt.executeUpdate();
        System.out.println(rowsDeleted + " products were deleted.");

        pSt.close();
        conn.close();
    }

    public void deleteProductInCart(Product p, int userID) throws ClassNotFoundException, SQLException {
        connectToDB();
        PreparedStatement pSt = conn.prepareStatement("DELETE FROM cart WHERE id_user=? AND id_product=?");
        pSt.setInt(1, userID);
        pSt.setInt(2, p.getId());

        int rowsDeleted = pSt.executeUpdate();

        pSt.close();
        conn.close();
    }


    public int verifyUser(User u) throws ClassNotFoundException, SQLException {

        connectToDB();
        PreparedStatement pSt = conn.prepareStatement("SELECT id, username, password FROM users where username=? and password=?");

        pSt.setString(1, u.getUsername());
        pSt.setString(2, u.getPassword());

        ResultSet rs = pSt.executeQuery();

        int foundID = 0;
        int id = 0;
        int counter = 0;
        while (rs.next()) {
            counter++;
            id = rs.getInt("id");
        }
        if (counter == 1) {
            foundID = id;
        }

        pSt.close();
        conn.close();

        return foundID;
    }

    public boolean verifyUserByNameOnly(String u) throws ClassNotFoundException, SQLException {

        connectToDB();
        PreparedStatement pSt = conn.prepareStatement("SELECT username FROM users where username=?");
        pSt.setString(1, u);

        ResultSet rs = pSt.executeQuery();

        boolean duplicate = false;
        if (rs.next()) {
            duplicate = true;
        }

        pSt.close();
        conn.close();

        return duplicate;
    }

    public int cartWithMostProducts() throws SQLException, ClassNotFoundException {
        connectToDB();
        PreparedStatement pSt = conn.prepareStatement("select c.id_user from cart as c group by c.id_user order by count(c.id_product) desc limit 1");

        ResultSet rs = pSt.executeQuery();

        int cartid = 0;

        while (rs.next()) {
            cartid = rs.getInt("id_user");
        }
        pSt.close();
        conn.close();

        return cartid;
    }


    public Map listOfUsersWithProducts() throws SQLException, ClassNotFoundException {
        connectToDB();

        PreparedStatement pSt = conn.prepareStatement("select c.count, u.username from (select count(c.id_product),c.id_user from cart as c group by c.id_user) as c inner join users as u on c.id_user=u.id");

        ResultSet rs = pSt.executeQuery();

        Map<Integer, String> map = new HashMap<>();

        while (rs.next()) {
            map.put(rs.getInt("count"), rs.getString("username"));
        }


        pSt.close();
        conn.close();

        return map;
    }

    public int[] mostExpensiveCartAndUser() throws SQLException, ClassNotFoundException {
        connectToDB();

        PreparedStatement pSt = conn.prepareStatement("select sum(p.price),c.id_user from products as p inner join cart as c on p.id=c.id_product group by c.id_user order by sum desc limit 1");

        ResultSet rs = pSt.executeQuery();
        int[] result = null;
        if (rs.next()) {
            int sum = rs.getInt("sum");
            int user_id = rs.getInt("id_user");
            result = new int[2];
            result[0] = sum;
            result[1] = user_id;
        }

        pSt.close();
        conn.close();

        return result;
    }

    public List cartsWithSpecificProduct(int productID) throws ClassNotFoundException,SQLException {
        connectToDB();

        PreparedStatement pSt = conn.prepareStatement("select id from cart where id_product=?");
        pSt.setInt(1, productID);

        ResultSet rs = pSt.executeQuery();

        List<Integer> cartsIDs = new ArrayList<>();

        while(rs.next()) {
            cartsIDs.add(rs.getInt("id"));
        }

        pSt.close();
        conn.close();

        return cartsIDs;
    }

    public List usersWithSpecificProduct(int productID) throws ClassNotFoundException,SQLException {
        connectToDB();

        PreparedStatement pSt = conn.prepareStatement("select id_user from cart where id_product=?");
        pSt.setInt(1, productID);

        ResultSet rs = pSt.executeQuery();

        List<Integer> usersIDs = new ArrayList<>();

        while(rs.next()) {
            usersIDs.add(rs.getInt("id_user"));
        }

        pSt.close();
        conn.close();

        return usersIDs;
    }

    public void deleteCart(int userID) throws ClassNotFoundException,SQLException {
        connectToDB();

        PreparedStatement pSt = conn.prepareStatement("delete from cart where id_user=?");
        pSt.setInt(1, userID);

        int rowsDeleted =  pSt.executeUpdate();
        System.out.println(rowsDeleted + " rows were deleted.");

        pSt.close();
        conn.close();
    }

}
