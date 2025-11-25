package gui.components;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

public class RoundedPanel extends JPanel {
    private final int arc;
    private float borderWidth = 1.5f;
    private Color borderColor = new Color(107, 124, 147, 95);

    public RoundedPanel(int arc) {
        this.arc = arc;
        setOpaque(false);
    }

    public void setBorderWidth(float width) {
        this.borderWidth = Math.max(0f, width);
        repaint();
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Paint background with rounded corners
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Inset by half the stroke so the border stays fully inside bounds
            float inset = borderWidth / 2f;
            float w = getWidth() - 1f;
            float h = getHeight() - 1f;

            Shape rr = new RoundRectangle2D.Float(
                    inset, inset,
                    w - 2f * inset,
                    h - 2f * inset,
                    arc, arc);

            // Fill background first
            g2.setColor(getBackground());
            g2.fill(rr);

            // Draw border on top if width > 0
            if (borderWidth > 0f && borderColor != null) {
                g2.setStroke(new BasicStroke(borderWidth));
                g2.setColor(borderColor);
                g2.draw(rr);
            }
        } finally {
            g2.dispose();
        }

        // Let Swing paint children afterward
        super.paintComponent(g);
    }
}