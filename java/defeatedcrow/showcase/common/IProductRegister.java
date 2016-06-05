package defeatedcrow.showcase.common;

/**
 * Created by Belgabor on 30.05.2016.
 */
public interface IProductRegister {
    void logProductRegisterSuccess(String logItem);
    void logProductRegisterFail(String logItem);
    void doProductRegister(Object item, int price);
}
