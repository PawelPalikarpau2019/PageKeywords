package my.projects.components;

import my.projects.service.KeywordsCounter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainFrame extends JFrame {

    private static final String MAIN_FRAME_TITLE_TEXT = "Keyword amounts on WWW page";
    private static final String ERROR_DIALOG_TITLE_TEXT = "Error";
    private static final String WARNING_DIALOG_TITLE_TEXT = "Warning";

    private static final String WARNING_DIALOG_MESSAGE_TEXT = "None keyword was found on page";

    private static final String LABEL_TXT = "Provide URL: ";
    private static final String BUTTON_TEXT = "Apply URL";

    private static final EmptyBorder PANEL_MARGIN = new EmptyBorder(10, 10, 10, 10);

    private static final Dimension MAIN_FRAME_MIN_SIZE = new Dimension(800, 400);
    private static final Dimension TABLE_ROW_HEIGHT_30 = new Dimension(0, 30);
    private static final Dimension TABLE_CELL_LEFT_RIGHT_PADDING = new Dimension(10, 0);

    private static final int KEYWORD_COLUMN_WIDTH_PERCENT_85 = 8500;
    private static final int AMOUNT_COLUMN_WIDTH_PERCENT_15 = 1500;

    private final KeywordsCounter keywordsCounter;

    private JTable table;

    public static void main(String[] args) {
        new MainFrame();
    }

    private MainFrame() {
        super(MAIN_FRAME_TITLE_TEXT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(MAIN_FRAME_MIN_SIZE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        getContentPane().add(getMainPanel());

        setVisible(true);

        keywordsCounter = new KeywordsCounter();
    }

    private JPanel getMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(getUrlPanel(), BorderLayout.NORTH);
        mainPanel.add(getTablePanel(), BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel getUrlPanel() {
        JPanel urlPanel = new JPanel();

        urlPanel.setBorder(PANEL_MARGIN);
        urlPanel.setLayout(new BoxLayout(urlPanel, BoxLayout.X_AXIS));

        JLabel label = new JLabel(LABEL_TXT);
        JTextField texField = new JTextField();
        JButton button = new JButton(BUTTON_TEXT);

        button.addActionListener(a -> {
            try {
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                updateTable(keywordsCounter.countKeywordsOnPage(texField.getText()));
            } catch (Exception e) {
                // Show error message dialog
                JOptionPane.showMessageDialog(this, e.getMessage(), ERROR_DIALOG_TITLE_TEXT, JOptionPane.ERROR_MESSAGE);
            } finally {
                this.setCursor(Cursor.getDefaultCursor());
            }
        });

        urlPanel.add(label);
        urlPanel.add(Box.createHorizontalStrut(10));
        urlPanel.add(texField);
        urlPanel.add(Box.createHorizontalStrut(10));
        urlPanel.add(button);

        return urlPanel;
    }

    private JPanel getTablePanel() {
        JPanel tablePanel = new JPanel(new GridLayout(0, 1));
        tablePanel.setBorder(PANEL_MARGIN);

        String[] columnNames = {"The Keyword", "Amount"};

        DefaultTableModel tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        // Increasing Font of every text
        Font increasedFont = new Font(table.getFont().getName(), Font.PLAIN, 20);
        table.setFont(increasedFont);
        table.getTableHeader().setFont(increasedFont);

        tableModel.addColumn(columnNames[0]);
        tableModel.addColumn(columnNames[1]);

        table.getColumn(columnNames[0]).setPreferredWidth(KEYWORD_COLUMN_WIDTH_PERCENT_85); // 85%
        table.getColumn(columnNames[1]).setPreferredWidth(AMOUNT_COLUMN_WIDTH_PERCENT_15); // 15%

        // Increasing height of header row
        table.getTableHeader().setPreferredSize(TABLE_ROW_HEIGHT_30);

        // Centering values of Amount column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumn(columnNames[1]).setCellRenderer(centerRenderer);

        // Adding left and right padding to every cell
        table.setIntercellSpacing(TABLE_CELL_LEFT_RIGHT_PADDING);

        JScrollPane sp = new JScrollPane(table);
        tablePanel.add(sp);

        return tablePanel;
    }

    private void updateTable(java.util.Map<String, Integer> keywordAmounts) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        while (tableModel.getRowCount() > 0)
            tableModel.removeRow(0);

        if (keywordAmounts.size() > 0)
            keywordAmounts.forEach((k, v) -> {
                tableModel.addRow(new Object[]{k, v}); // Add new row to table
                table.setRowHeight(table.getRowCount() - 1, 30); // Change height of recently added row
            });
        else
            // Show "none keyword was found" dialog
            JOptionPane.showMessageDialog(this, WARNING_DIALOG_MESSAGE_TEXT, WARNING_DIALOG_TITLE_TEXT, JOptionPane.WARNING_MESSAGE);
    }
}
