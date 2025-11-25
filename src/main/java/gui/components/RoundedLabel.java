package gui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class RoundedLabel extends JLabel {
    private final int arc;
    private Color borderColor = new Color(0, 0, 0, 60);
    private float borderWidth = 1f;
    private Insets padding = new Insets(4, 8, 4, 8);

    public RoundedLabel(String text, int arc) {
        super(text);
        this.arc = arc;
        setOpaque(false);
        setForeground(Color.BLACK);
        setBackground(new Color(230, 230, 230));
        setBorder(BorderFactory.createEmptyBorder(
                padding.top, padding.left, padding.bottom, padding.right));
    }

    public void setBorderColor(Color c) {
        borderColor = c;
        repaint();
    }

    public void setBorderWidth(float w) {
        borderWidth = w;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            float inset = Math.max(0f, borderWidth) / 2f;
            float w = getWidth() - 1f;
            float h = getHeight() - 1f;

            Shape rr = new RoundRectangle2D.Float(inset, inset, w - 2 * inset, h - 2 * inset, arc, arc);

            // Fill rounded background
            g2.setColor(getBackground());
            g2.fill(rr);

            // Draw rounded border
            if (borderWidth > 0f && borderColor != null) {
                g2.setStroke(new BasicStroke(borderWidth));
                g2.setColor(borderColor);
                g2.draw(rr);
            }

            g2.setClip(rr);
            super.paintComponent(g2);
        } finally {
            g2.dispose();
        }
    }
}
