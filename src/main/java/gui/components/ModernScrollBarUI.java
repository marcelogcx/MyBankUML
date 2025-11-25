package gui.components;

import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class ModernScrollBarUI extends BasicScrollBarUI {
    private static final int THICKNESS = 10;
    private static final Color TRACK = new Color(0, 0, 0, 20);
    private static final Color THUMB = new Color(120, 120, 120, 180);
    private static final Color THUMB_HOVER = new Color(120, 120, 120, 220);
    private static final Color THUMB_DRAG = new Color(120, 120, 120, 255);

    @Override
    protected void configureScrollBarColors() {
        trackColor = new Color(0, 0, 0, 0); // handled in paintTrack
        thumbColor = THUMB;
    }

    @Override
    protected JButton createDecreaseButton(int o) {
        return zeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int o) {
        return zeroButton();
    }

    private JButton zeroButton() {
        JButton b = new JButton();
        b.setPreferredSize(new Dimension(0, 0));
        b.setMinimumSize(new Dimension(0, 0));
        b.setMaximumSize(new Dimension(0, 0));
        b.setOpaque(false);
        b.setFocusable(false);
        b.setBorder(BorderFactory.createEmptyBorder());
        return b;
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        Dimension d = super.getPreferredSize(c);
        return (scrollbar.getOrientation() == Adjustable.VERTICAL)
                ? new Dimension(THICKNESS, d.height)
                : new Dimension(d.width, THICKNESS);
    }

    @Override
    protected Dimension getMinimumThumbSize() {
        return new Dimension(THICKNESS, 40);
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(TRACK);
        if (scrollbar.getOrientation() == Adjustable.VERTICAL) {
            int x = r.x + (r.width - THICKNESS) / 2;
            g2.fillRect(x, r.y, THICKNESS, r.height); // flat top/bottom
        } else {
            int y = r.y + (r.height - THICKNESS) / 2;
            g2.fillRect(r.x, y, r.width, THICKNESS); // flat left/right
        }
        g2.dispose();
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
        if (!c.isEnabled() || r.isEmpty())
            return;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color color = isDragging ? THUMB_DRAG : (isThumbRollover() ? THUMB_HOVER : THUMB);
        g2.setColor(color);
        if (scrollbar.getOrientation() == Adjustable.VERTICAL) {
            int x = r.x + (r.width - THICKNESS) / 2;
            g2.fillRect(x, r.y, THICKNESS, r.height); // no rounding on ends
        } else {
            int y = r.y + (r.height - THICKNESS) / 2;
            g2.fillRect(r.x, y, r.width, THICKNESS); // no rounding on ends
        }
        g2.dispose();
    }
}
