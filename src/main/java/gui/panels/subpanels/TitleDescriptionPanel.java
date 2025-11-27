package gui.panels.subpanels;

import javax.swing.JLabel;
import javax.swing.JPanel;

import core.ThemeManager;

public class TitleDescriptionPanel extends JPanel {
    private JLabel title;
    private JLabel description;

    public TitleDescriptionPanel(String titleText, String descriptionText) {
        setLayout(null);
        setOpaque(false);

        // Title
        title = new JLabel(titleText);
        title.setBounds(0, 0, 300, 40);
        ThemeManager.styleTitlePanel(title);

        // Description
        description = new JLabel(descriptionText);
        description.setBounds(0, 40, 500, 40);
        ThemeManager.styleProfileLabel(description);

        add(title);
        add(description);
    }

    public void setTitleDescription(String titleText, String descriptionText) {
        title.setText(titleText);
        description.setText(descriptionText);
    }
}
