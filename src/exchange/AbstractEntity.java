package exchange;


public class AbstractEntity implements Comparable<AbstractEntity> {

    private int price=0;
    private int size=0;

    public AbstractEntity(){}

    public AbstractEntity(int price, int size){
        this.price=price;
        this.size=size;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractEntity)) return false;

        AbstractEntity that = (AbstractEntity) o;

        if (price != that.price) return false;
        return size == that.size;
    }

    @Override
    public int hashCode() {
        int result = price;
        result = 31 * result + size;
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "price=" + price +
                ", size=" + size +
                '}';
    }

    @Override
    public int compareTo(AbstractEntity o) {
        return o.getPrice() - this.getPrice();
    }
}
