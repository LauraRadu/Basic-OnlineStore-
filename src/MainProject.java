import java.sql.SQLException;
import java.util.*;

public class MainProject {

    public static void main(String[] args) {

        MainProject obj = new MainProject();
        DBOperations dbObj = new DBOperations();

        String input = obj.input("Press A for administrator DB or U for user.");

        if (input.equalsIgnoreCase("a")) {
            String password = obj.input("Insert admin password: ");
            if (password.equals("admin")) {
                System.out.println("Hello, admin!");
                int option = -1;
                do {
                    obj.meniuAdmin();
                    option = obj.inputNumber("Select option: ");

                    switch (option) {
                        case 1: {
                            User u = obj.giveAUser();
                            obj.checkDuplicateUser(u);
                            try {
                                dbObj.addUser(u);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }  catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        }

                        case 2: {
                            System.out.println("Who do you want to modify?");
                            User u = obj.giveAUser();
                            int userID = 0;
                            try {
                                userID = dbObj.verifyUser(u);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            if(userID == 0){
                                System.out.println("User not existent in DB");
                            }
                            else {
                                System.out.println("New credentials: ");
                                User newUser = obj.giveAUser();
                                User validNewUser = obj.checkDuplicateUser(newUser);
                                try {
                                    dbObj.modifyUser(u, validNewUser.getUsername(), validNewUser.getPassword());
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        }

                        case 3: {
                            User u = obj.giveAUser();
                            try {
                                dbObj.deleteUser(u);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        }

                        case 4: {
                            Product p = obj.giveAProduct();

                            try {

                                dbObj.addProductInDB(p);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        }

                        case 5: {
                            System.out.println("What product do you want to modify?");
                            Product p = obj.giveAProduct();
                            System.out.println("Insert modifications: ");
                            Product newP = obj.giveAProduct();
                            try {
                                dbObj.modifyProductInDB(p,newP.getProductName(),newP.getProductPrice());
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        }

                        case 6: {
                            Product p = obj.giveAProduct();
                            try {
                                dbObj.deleteProductInDB(p);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        }

                        case 7: {
                            try {
                                int userId = dbObj.cartWithMostProducts();
                                System.out.println("Userul cu ID "+ userId + " are cele mai multe produse in cos");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                            break;
                        }

                        case 8: {
                            Map<Integer,String> map = null;
                            try {
                                map = dbObj.listOfUsersWithProducts();
                                for (Integer i : map.keySet()) {
                                    String key = i.toString();
                                    String value = map.get(i).toString();
                                    System.out.println(value + " " + key + " products in cart");
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                            break;
                        }

                        case 9: {
                            try {
                                int[] result = dbObj.mostExpensiveCartAndUser();
                                int sum = result[0];
                                int user_id = result[1];
                                System.out.println("The most expesive cart is " + sum + " lei and belongs to user with id " + user_id);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }

                            break;
                        }

                        case 10: {
                            int product = obj.inputNumber("product ID: ");
                            try {
                                List carts = dbObj.cartsWithSpecificProduct(product);
                                for (int i=0; i<carts.size();i++) {
                                    System.out.println(carts.get(i));
                                }
                                if(carts.size() == 0) {
                                    System.out.println("No carts contain this product.");
                                }
                                }
                            catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        }

                        case 11: {
                            int product = obj.inputNumber("product ID: ");
                            try {
                                List users = dbObj.usersWithSpecificProduct(product);
                                for (int i=0; i<users.size();i++) {
                                    System.out.println(users.get(i));
                                }
                                if(users.size() == 0) {
                                    System.out.println("No users bought this product.");
                                }
                            }
                            catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        }

                        case 12: {
                            int userID = obj.inputNumber("User ID: ");
                            try {
                                dbObj.deleteCart(userID);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
                while (option != 0);


            } else {
                System.out.println("Please try again!");
            }


        } else if (input.equalsIgnoreCase("u")) {
            int loginUserID = obj.askForCredentials();
            if (loginUserID != 0) {
                System.out.println("Succesfully logged in!");

                int option = -1;
                do {
                    obj.meniuUser();

                    try {
                        option = obj.inputNumber("Choose option: ");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    switch (option) {
                        case 1: {
                            List<Product> products = new ArrayList<>();
                            try {
                                products = dbObj.getAllProductsByUser(loginUserID);
                                for (int i = 0; i < products.size(); i++) {
                                    System.out.println(products.get(i).getProductName() + products.get(i).getProductPrice() + " lei");
                                }
                                if (products.size() == 0) {
                                    System.out.println("Your cart is empty!");
                                }
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        }

                        case 2: {
                            String productWanted = obj.input("What are you looking for?");
                            List<Product> products = new ArrayList<>();
                            try {
                                products = dbObj.searchProduct(productWanted);
                                if (products.size() == 0) {
                                    System.out.println("We do not have this product!");
                                } else if (products.size() == 1) {

                                    dbObj.addProductToCart(products.get(0), loginUserID);
                                    System.out.println("Added to cart.");
                                }
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        }

                        case 3: {
                            String productToDelete = obj.input("What product do you want to delete? Insert name: ");
                            List<Product> products = new ArrayList<>();
                            try {
                                products = dbObj.searchProduct(productToDelete);
                                if (products.size() == 0) {
                                    System.out.println("You do not have this product in cart!");
                                } else if (products.size() == 1) {

                                    dbObj.deleteProductInCart(products.get(0), loginUserID);
                                    System.out.println("Deleted from cart.");
                                }
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        }

                        case 4: {
                            try {
                                List<Product> products = dbObj.getAllProducts();
                                for (Product p : products
                                        ) {
                                    System.out.println(p.getProductName() + p.getProductPrice());
                                }
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
                while (option != 0);

            } else {
                System.out.println("Inexistent user!");
            }
        }
    }

    private String input(String label) {
        System.out.print(label);
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        return input;
    }

    private int inputNumber(String label) {
        System.out.print(label);
        Scanner scan = new Scanner(System.in);
        int input = scan.nextInt();
        return input;
    }

    private void meniuAdmin() {
        System.out.println("1> Add user");
        System.out.println("2> Modify user");
        System.out.println("3> Delete user");
        System.out.println("4> Add product");
        System.out.println("5> Modify product");
        System.out.println("6> Delete product");
        System.out.println("7> Find the cart with the most products");
        System.out.println("8> List of users ordered by number of products in cart");
        System.out.println("9> Get the most expensive cart and its user");
        System.out.println("10> Get carts that contain a specific product");
        System.out.println("11> Get users that bought a specific product");
        System.out.println("12> Delete all products from a specific cart of a user");
        System.out.println("0> Exit");
    }

    private User giveAUser() {
        String name = input("Write a name: ");
        String password = input("Write a password: ");
        User u = new User(name, password);
        return u;
    }

    private User checkDuplicateUser(User u) {
        boolean duplicate = true;
        do {
            try {
                duplicate = new DBOperations().verifyUserByNameOnly(u.getUsername());
                if (duplicate == true) {
                    System.out.println(u.getUsername() + " already exists in the DB!");
                    u = giveAUser();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } while (duplicate == true);

        return u;
    }

    private Product giveAProduct(){
        String name = input("Give the name: ");
        int price = inputNumber("Give the price: ");
        Product p = new Product(name, price);
        return p;
    }

    private int askForCredentials() {
        String username = input("Insert Username: ");
        String password = input("Insert password: ");
        User u = new User(username, password);

        int loginID = 0;
        try {
            DBOperations db = new DBOperations();
            loginID = db.verifyUser(u);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return loginID;
    }

    private void meniuUser() {
        System.out.println("1> See cart");
        System.out.println("2> Add products");
        System.out.println("3> Delete products");
        System.out.println("4> See all product in shop");
        System.out.println("0> Exit");
    }

}
