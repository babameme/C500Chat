package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import dataframe.Packet;


public class GUIPacket extends JPanel {
    private static final long serialVersionUID = 1L;

    public GUIPacket(Packet packet) {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel panelAvatar = new JPanel();
        panelAvatar.add(new Avatar(packet.getUrl(), 50, 50));
        JLabel label = new JLabel(packet.getName());
        Font font = new Font("Arial", Font.BOLD, 16);
        label.setForeground(Color.BLUE);
        label.setFont(font);
        panel.add( label, BorderLayout.NORTH);
        panel.add(new JTextArea(packet.getMessage()), BorderLayout.CENTER);
        this.setLayout(new BorderLayout());
        this.add(panelAvatar, BorderLayout.WEST);
        this.add(panel, BorderLayout.CENTER);
        this.setVisible(true);
    }

}
