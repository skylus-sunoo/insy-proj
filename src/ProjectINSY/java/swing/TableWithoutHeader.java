package ProjectINSY.java.swing;

import java.awt.BorderLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.Dimension;
import javax.swing.JScrollBar;

public final class TableWithoutHeader extends JPanel {

    private final String[] columnNames = {"String", "Integer", "Boolean"};
    private final Object[][] data = {
        {"Tutorials Point", 100, true},
        {"Tutorix", 200, false},
        {"Tutorials Point", 300, true},
        {"Tutorix", 400, false}
    };
    private final TableModel model = new DefaultTableModel(data, columnNames) {
        @Override
        public Class getColumnClass(int column) {
            return getValueAt(0, column).getClass();
        }
    };
    private final JTable table = new JTable(model);
    private final JScrollPane scrollPane = createCustomScrollPane(table);

    public TableWithoutHeader() {
        super(new BorderLayout());
        add(scrollPane);
        JCheckBox check = new JCheckBox("JTableHeader visible: ", true);
        check.addActionListener(ae -> {
            JCheckBox cb = (JCheckBox) ae.getSource();
            scrollPane.getColumnHeader().setVisible(cb.isSelected());
            scrollPane.revalidate();
        });
        add(check, BorderLayout.NORTH);
    }

    private JScrollPane createCustomScrollPane(JTable table) {
        JScrollPane customScrollPane = new JScrollPane(table);

        ScrollbarCustom verticalScrollBar = new ScrollbarCustom();
        verticalScrollBar.setPreferredSize(new Dimension(12, 70));
        customScrollPane.setVerticalScrollBar(verticalScrollBar);

        ScrollbarCustom horizontalScrollBar = new ScrollbarCustom();
        horizontalScrollBar.setOrientation(JScrollBar.HORIZONTAL);
        customScrollPane.setHorizontalScrollBar(horizontalScrollBar);
        customScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        return customScrollPane;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("JTableHeaderHide Test");
        frame.setSize(375, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new TableWithoutHeader());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
