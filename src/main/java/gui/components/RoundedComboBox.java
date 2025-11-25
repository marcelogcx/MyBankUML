package gui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class RoundedComboBox<E> extends JComboBox<E> {
    private int arc = 12;

    // Border colors
    private Color borderColor = Color.LIGHT_GRAY;
    private Color hoverBorderColor = new Color(0, 153, 255);
    private Color selectedBorderColor = new Color(255, 102, 0);

    // Selection/hover color for dropdown
    private Color selectionHoverColor = new Color(0, 200, 255);

    private boolean hovered = false;

    public RoundedComboBox() {
        super();
        init();
    }

    public RoundedComboBox(E[] items) {
        super(items);
        init();
    }

    private void init() {
        setOpaque(false);
        setBackground(Color.WHITE); // Always white
        setEditable(false);
        setBorder(new RoundedCornerBorder());

        setUI(new NoFocusComboBoxUI());

        setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                if (index == -1) { // Main display area (selected value)
                    lbl.setOpaque(true);
                    lbl.setBackground(Color.WHITE); // Always white, no gray/focus overlay
                    lbl.setForeground(Color.BLACK);
                } else if (isSelected) {
                    lbl.setOpaque(true);
                    lbl.setBackground(selectionHoverColor); // Dropdown highlight
                    lbl.setForeground(Color.BLACK);
                } else {
                    lbl.setOpaque(true);
                    lbl.setBackground(list.getBackground());
                    lbl.setForeground(list.getForeground());
                }
                return lbl;
            }
        });

        // Track mouse hover for the border
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                hovered = false;
                repaint();
            }
        });
    }

    // Setters for border and dropdown colors
    public void setBorderColor(Color c) {
        borderColor = c;
        repaint();
    }

    public void setHoverBorderColor(Color c) {
        hoverBorderColor = c;
        repaint();
    }

    public void setSelectedBorderColor(Color c) {
        selectedBorderColor = c;
        repaint();
    }

    public void setSelectionHoverColor(Color c) {
        selectionHoverColor = c;
        repaint();
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public Color getHoverBorderColor() {
        return hoverBorderColor;
    }

    public Color getSelectedBorderColor() {
        return selectedBorderColor;
    }

    public Color getSelectionHoverColor() {
        return selectionHoverColor;
    }

    public int getArc() {
        return arc;
    }

    public void setArc(int a) {
        arc = a;
        repaint();
    }

    // Prevent gray focus background
    public static class NoFocusComboBoxUI extends BasicComboBoxUI {
        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            /* nothing */ }

        @Override
        protected JButton createArrowButton() {
            JButton b = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    int w = getWidth(), h = getHeight(), size = Math.min(w, h) / 3;
                    int x = (w - size) / 2, y = (h - size) / 2;
                    Polygon p = new Polygon();
                    p.addPoint(x, y);
                    p.addPoint(x + size, y);
                    p.addPoint(x + size / 2, y + size);
                    g2.setColor(getForeground());
                    g2.fill(p);
                    g2.dispose();
                }
            };
            b.setContentAreaFilled(false);
            b.setBorderPainted(false);
            b.setOpaque(false);
            b.setForeground(Color.BLACK);
            return b;
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Always fill background with white
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        // Border color
        Color bColor = borderColor;
        if (hasFocus())
            bColor = selectedBorderColor;
        else if (hovered)
            bColor = hoverBorderColor;
        g2.setColor(bColor);
        g2.setStroke(new BasicStroke(1.4f));
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, arc, arc);

        // Get selected value and paint it
        Object selected = getSelectedItem();
        if (selected != null) {
            g2.setFont(getFont());
            g2.setColor(Color.BLACK);
            FontMetrics fm = g2.getFontMetrics();
            int textX = 10;
            int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(selected.toString(), textX, textY);
        }

        g2.dispose();

        // Still paint children for drop arrow etc.
        paintChildren(g);
    }

    // Only used for layout - painting is handled above
    private class RoundedCornerBorder extends AbstractBorder {
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(4, 8, 4, 8);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.set(4, 8, 4, 8);
            return insets;
        }
    }

}
