package Main;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.*;

public class CurvedButton extends JButton {

    JLabel text = new JLabel();

    public CurvedButton(String label) {
        setLayout(null);
        text.setText(label);
        text.setForeground(getForeground());

        add(text);
        Dimension size = getPreferredSize();
        size.width = size.height = Math.max(size.width, size.height);
        setPreferredSize(size);

        setContentAreaFilled(false);
    }
    public void setLText(String text) {
        this.text.setText(text);
        refresh();
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        if (getModel().isArmed()) {
            g.setColor(Defaults.HOVER);
        } else {
            g.setColor(getBackground());
        }
        RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHints(qualityHints);
        g2.fillRoundRect(0, 0, getSize().width, getSize().height, 10, 10);


        super.paintComponent(g);
    }

    /*
     * protected void paintBorder(Graphics g) { g.setColor(getForeground());
     * g.drawOval(0, 0, getSize().width-1, getSize().height-1); }
     */

    private Shape shape;

    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),10,10);
        }
        return shape.contains(x, y);
    }
    public void refresh(){
        text.setForeground(getForeground());
        text.setFont(getFont());
        text.setBounds((getWidth()/2)-(text.getPreferredSize().width/2), (getHeight()/2)-(text.getPreferredSize().height/2), text.getPreferredSize().width+5, text.getPreferredSize().height+5);

    }
}
