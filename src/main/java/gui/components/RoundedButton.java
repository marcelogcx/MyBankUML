package gui.components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class RoundedButton extends JButton {
    // Config
    private final boolean transparentBorder;
    private final float borderAlpha; // 0.0f … 1.0f
    private final int arc;

    // New: explicit border colors (null falls back to getForeground())
    private Color borderColor;
    private Color borderColorHover;
    private Color borderColorPressed;

    public RoundedButton(String label) {
        this(label, null, false, 1.0f, 15);
    }

    public RoundedButton(String label, ImageIcon icon) {
        this(label, icon, false, 1.0f, 15);
    }

    public RoundedButton(String label, boolean borderTransparent) {
        this(label, null, borderTransparent, borderTransparent ? 0.0f : 1.0f, 15);
    }

    public RoundedButton(String label, ImageIcon icon, boolean borderTransparent) {
        this(label, icon, borderTransparent, borderTransparent ? 0.0f : 1.0f, 15);
    }

    public RoundedButton(String label, ImageIcon icon, boolean borderTransparent, float borderAlpha, int arc) {
        super(label, icon);
        this.transparentBorder = borderTransparent;
        this.borderAlpha = Math.max(0f, Math.min(1f, borderAlpha));
        this.arc = arc;

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false); // custom paint handles border [web:27][web:9]
        setRolloverEnabled(true); // enable hover state [web:30]
    }

    // New setters/getters
    public void setBorderColor(Color c) {
        this.borderColor = c;
        repaint();
    } // normal [web:9]

    public void setBorderColorHover(Color c) {
        this.borderColorHover = c;
        repaint();
    } // hover [web:9]

    public void setBorderColorPressed(Color c) {
        this.borderColorPressed = c;
        repaint();
    } // pressed [web:9]

    public Color getBorderColor() {
        return borderColor;
    }

    public Color getBorderColorHover() {
        return borderColorHover;
    }

    public Color getBorderColorPressed() {
        return borderColorPressed;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        boolean pressed = getModel().isPressed();
        Color fill = pressed ? Color.LIGHT_GRAY : getBackground();
        g2.setColor(fill);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, arc, arc));

        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        if (transparentBorder && borderAlpha <= 0f)
            return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        float a = transparentBorder ? borderAlpha : 1.0f;
        Composite old = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a)); // alpha border [web:9]

        // Resolve stateful border color
        boolean pressed = getModel().isPressed();
        boolean rollover = getModel().isRollover();
        Color c = pressed && borderColorPressed != null ? borderColorPressed
                : rollover && borderColorHover != null ? borderColorHover
                        : borderColor != null ? borderColor : getForeground();

        g2.setColor(c);
        g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, arc, arc));

        g2.setComposite(old);
        g2.dispose();
    }
}
