package gui.components;

import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class RoundedTextField extends JTextField {
    private Shape shape;
    private int arcWidth = 15;
    private int arcHeight = 15;

    private Color borderColor;
    private Color borderColorHover;
    private Color borderColorFocused;
    private float borderStroke = 1.5f;

    private boolean hover;

    private String placeholder;
    private Color placeholderColor = new Color(150, 150, 150);

    public RoundedTextField(boolean isOnlyNumeric) {
        super();
        setOpaque(false);
        enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });

        if (getBorder() == null) {
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        } else {
            setBorder(BorderFactory.createCompoundBorder(
                    getBorder(),
                    BorderFactory.createEmptyBorder(0, 10, 0, 0)));
        }

        if (isOnlyNumeric) {
            ((AbstractDocument) this.getDocument()).setDocumentFilter(new NumericDecimalFilter());
        }
    }

    @Override
    protected void processMouseEvent(java.awt.event.MouseEvent e) {
        switch (e.getID()) {
            case MouseEvent.MOUSE_ENTERED -> {
                hover = true;
                repaint();
            }
            case MouseEvent.MOUSE_EXITED -> {
                hover = false;
                repaint();
            }
        }
        super.processMouseEvent(e);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
        g2.dispose();

        super.paintComponent(g);

        if ((getText() == null || getText().isEmpty()) && placeholder != null && !placeholder.isEmpty()) {
            Graphics2D gph = (Graphics2D) g.create();
            gph.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            gph.setColor(placeholderColor);

            Insets in = getInsets();
            FontMetrics fm = gph.getFontMetrics(getFont());
            int x = in.left;
            int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            gph.drawString(placeholder, x, y);
            gph.dispose();
        }
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color c = getForeground();
        if (borderColor != null)
            c = borderColor;
        if (hover && borderColorHover != null)
            c = borderColorHover;
        if (isFocusOwner() && borderColorFocused != null)
            c = borderColorFocused;

        Stroke old = g2.getStroke();
        g2.setStroke(new BasicStroke(borderStroke));

        g2.setColor(c);
        float inset = borderStroke / 2f;
        g2.drawRoundRect(Math.round(inset), Math.round(inset),
                Math.round(getWidth() - 1 - borderStroke),
                Math.round(getHeight() - 1 - borderStroke), arcWidth, arcHeight);

        g2.setStroke(old);
        g2.dispose();
    }

    @Override
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0,
                    getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
        }
        return shape.contains(x, y);
    }

    public void setPlaceholder(String text) {
        this.placeholder = text;
        repaint();
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholderColor(Color c) {
        this.placeholderColor = c;
        repaint();
    }

    public Color getPlaceholderColor() {
        return placeholderColor;
    }

    public void setBorderColor(Color c) {
        borderColor = c;
        repaint();
    }

    public void setBorderColorHover(Color c) {
        borderColorHover = c;
        repaint();
    }

    public void setBorderColorFocused(Color c) {
        borderColorFocused = c;
        repaint();
    }

    public void setBorderStroke(float px) {
        borderStroke = Math.max(0.5f, px);
        repaint();
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public Color getBorderColorHover() {
        return borderColorHover;
    }

    public Color getBorderColorFocused() {
        return borderColorFocused;
    }

    public float getBorderStroke() {
        return borderStroke;
    }

    public void setArc(int w, int h) {
        this.arcWidth = w;
        this.arcHeight = h;
        repaint();
    }

    public int getArcWidth() {
        return arcWidth;
    }

    public int getArcHeight() {
        return arcHeight;
    }

    private static class NumericDecimalFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string == null)
                return;
            String newText = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()))
                    .insert(offset, string).toString();
            if (isValidInput(newText)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text == null)
                return;
            StringBuilder current = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
            current.replace(offset, offset + length, text);
            String newText = current.toString();
            if (isValidInput(newText)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        // Allow empty, digits, or digits with one decimal point and digits after
        // decimal
        private boolean isValidInput(String text) {
            if (text.isEmpty())
                return true;
            // Regex: optional digits, optional one '.', optional digits
            return text.matches("\\d*(\\.\\d*)?");
        }
    }
}
