package exchange;

public class Ask extends AbstractEntity {

    public Ask(int price, int size){
        super(price, size);
    }

    @Override
    public int compareTo(AbstractEntity o) {
        return this.getPrice() - o.getPrice();
    }
}
