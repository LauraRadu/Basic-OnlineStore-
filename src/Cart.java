import java.util.List;

public class Cart {
    private int cartID;
    private User cartOwner;
    private List<Product> product;

    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public User getCartOwner() {
        return cartOwner;
    }

    public void setCartOwner(User cartOwner) {
        this.cartOwner = cartOwner;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }
}
