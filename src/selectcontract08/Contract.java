package selectcontract08;

/**
 *
 * @author Karan Dahiya
 */
public class Contract {

    private String contractID;
    private String originCity;
    private String destCity;
    private String orderItem;

    public static final int NUMBER_OF_CONTACT_ATTRIBUTES = 4;
    public static final int INDEX_OF_CONTACT_ID = 0;
    public static final int INDEX_OF_ORIGIN_CITY = 1;
    public static final int INDEX_OF_DEST_CITY = 2;
    public static final int INDEX_OF_ORDER_ITEM = 3;

    // Constructor
    public Contract(String contractID, String originCity, String destCity, String orderItem) {
        this.contractID = contractID;
        this.originCity = originCity;
        this.destCity = destCity;
        this.orderItem = orderItem;
    }

    // Getters
    public String getContractID() {
        return contractID;
    }

    public String getOriginCity() {
        return originCity;
    }

    public String getDestCity() {
        return destCity;
    }

    public String getOrderItem() {
        return orderItem;
    }

    boolean contains(String city) {
        return this.originCity.equals(city);
    }
}
