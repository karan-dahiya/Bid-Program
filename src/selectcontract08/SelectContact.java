package selectcontract08;
/**
 * @author Karan Dahiya
 */
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author Karan Dahiya
 */
public class SelectContact {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ContractView theView = new ContractView();
        ContractModel theModel = new ContractModel();
        ContractController theController;
        theController = new ContractController(theView, theModel);
        theView.setVisible(true);
        theView.setLocationRelativeTo(null);
    }
}
