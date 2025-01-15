/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProjectINSY.java.model;

/**
 *
 * @author admin
 */
public abstract class ItemPanel extends javax.swing.JPanel {

    protected String filterWHERE = "", filterHAVING = "";
    protected String currentSearchQuery = "";
    protected boolean isUpdatingComboBoxes = false;

    protected final String PLACEHOLDER_CATEGORY = "Enter Category";
    protected final String PLACEHOLDER_NAME = "Enter Name";
    protected final String PLACEHOLDER_DESC = "Enter Description";
    protected final String PLACEHOLDER_QTY = "1";
    protected final String PLACEHOLDER_CODE = "Enter Code (XXXX)";
    protected final String PLACEHOLDER_PRICE = "Enter Price";
    protected final String PLACEHOLDER_DOD = "Enter Delivery Date";
    protected final String PLACEHOLDER_BENEFACTOR = "Enter Benefactor";

    public ItemPanel() {
    }

    public abstract void refreshItemTable();

    public void refreshItemTable_2() {
    }

    public abstract void repopulateComboBox();

    public abstract void repopulateFilterComboBox();

    public void enableUpdatingComboBoxes() {
        isUpdatingComboBoxes = false;
    }

    public void disableUpdatingComboBoxes() {
        isUpdatingComboBoxes = true;
    }
}
