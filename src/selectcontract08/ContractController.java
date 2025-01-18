package selectcontract08;

/**
 * @author Karan Dahiya
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import javax.swing.Action;
import javax.swing.JFrame;
import selectcontract08.ContractModel;
import selectcontract08.ContractView;

/**
 *
 * @author Karan Dahiya
 */
public class ContractController {

    private ContractView theView;
    private ContractModel theModel;
    private ContractController theController = this;

    // Constructor
    public ContractController(ContractView theView, ContractModel theModel) {
        this.theView = theView;
        this.theModel = theModel;

        // Add ActionListeners to the view's buttons
        this.theView.addPrevListener(new PrevButtonListener());
        this.theView.addNextListener(new NextButtonListener());
        this.theView.addBidListener(new BidButtonListener());
        this.theView.addComboListener(new ComboListener());
        this.theView.addContractListener(new AddContractListner());
        this.theView.viewBiddingsListener(new ViewBiddingsListener());  // Lab8 Addition

        setUpComboBox();
        // Set up initial display
        setUpDisplay();
    }

    public void makeNewContract(String contractID, String origin, String destination, String orderItem) throws IOException {
        theModel.addContract(contractID, origin, destination, orderItem);
    }

    private void setUpComboBox() {
        String[] originCities = theModel.getOriginCityList();
        theView.setComboBoxModel(originCities);
    }

    // Method to set up initial display of contract details
    private void setUpDisplay() {
        try {
            if (theModel.foundContracts()) {
                Contract c = theModel.getTheContract();
                theView.setContactID(c.getContractID());
                theView.setDestCity(c.getDestCity());
                theView.setOriginCity(c.getOriginCity());
                theView.setOrderItem(c.getOrderItem());
                theView.updateContactViewPanel(theModel.getCurrentContractNum(), theModel.getContractCount());
            } else {
                theView.setContactID("???");
                theView.setDestCity("???");
                theView.setOriginCity("???");
                theView.setOrderItem("???");
            }
        } catch (Error ex) {
            System.out.println(ex);
            theView.displayErrorMessage(
                    "Error: There was a problem setting the contract.\n" + "Contact Number: " + theModel.getCurrentContractNum());
        }
    }

    // Inner class for handling Previous button clicks
    class PrevButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (theModel.getCurrentContractNum() == 0) {
                return;
            }
            try {
                theModel.prevContract();
                setUpDisplay();
            } catch (Exception ex) {
                System.out.print(ex);
                theView.displayErrorMessage(
                        "Error: There was a problem setting the contract.\n");
            }
        }
    }

    // Inner class for handling Next button clicks
    class NextButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (theModel.getCurrentContractNum() >= theModel.getContractCount() - 1) {
                return;
            }
            try {
                theModel.nextContract();
                setUpDisplay();
            } catch (Exception ex) {
                System.out.print(ex);
                theView.displayErrorMessage(
                        "Error: There was a problem setting the contract.\n");
            }
        }
    }

    // Inner class for handling Bid button clicks
    class BidButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                ConfirmBid cb;
                cb = new ConfirmBid(theView, true,
                        (theModel.getTheContract()));
                cb.setLocationRelativeTo(null);
                cb.setVisible(true);
            } catch (Exception ex) {
                System.out.println(ex);
                theView.displayErrorMessage(
                        "Error The numbers entered must be integer.");
            }
        }
    }

    class ComboListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
//            System.out.println(e.getItem().toString());
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedCity = e.getItem().toString();
                System.out.println(selectedCity);
                theModel.updateContactList(selectedCity);
                setUpDisplay();
            }

        }
    }

    class AddContractListner implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                AddContact ad = new AddContact(theView, true, theController);
                ad.setLocationRelativeTo(null); // Center the dialog relative to the parent frame
                ad.setVisible(true); // Show the modal dialog
                setUpDisplay(); // Refresh all the details
            } catch (Exception ex) {
                ex.printStackTrace(); // Print stack trace for any exceptions
            }
        }

    }

    // Lab8 Addition, connects the bidding dialog form to the bidding menu button
    class ViewBiddingsListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                BiddingView bid = new BiddingView(theView, true);
                bid.setLocationRelativeTo(null);
                bid.setVisible(true);
//                setUpDisplay();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
