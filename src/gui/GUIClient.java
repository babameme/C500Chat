package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import connection.ServerConnect;
import dataframe.Packet;

public class GUIClient extends JFrame implements ActionListener, MouseListener, KeyListener {

    private static final long serialVersionUID = -2253155641100317203L;

    private Vector<Packet> listData = new Vector<Packet>();
    private final JList log;
    private JTextField message = new JTextField();
    private JButton send = new JButton("Send");
    private JScrollPane scrollPane = new JScrollPane();
    private JTextField serverName = new JTextField(10);
    private JTextField nickName = new JTextField(10);
    private JTextField port = new JTextField(10);
    private JButton connect = new JButton("Connect");
    private JPanel panelControl = new JPanel(new GridLayout(4, 2));
    private JPanel panelTop = new JPanel(new BorderLayout());
    private JPanel panelAvatar = new JPanel(new FlowLayout());
    private String urlAvatar = new String("avatar0.png");
    private Avatar avatar = new Avatar(urlAvatar, 80, 80);
    private Packet packetSend = new Packet("You:", urlAvatar, "");
    private Packet packetRevice = new Packet("", urlAvatar, "");
    private ServerConnect server;
    private boolean mySend = false;

    public GUIClient() {
        serverName.setText("localhost");
        String name = "nickName" + (int)(Math.random()*100);
        nickName.setText(name);
        port.setText("8697");
        panelAvatar.add(avatar);
        panelAvatar.addMouseListener(this);

        panelControl.add(new JLabel("Server name:"));
        panelControl.add(serverName);
        panelControl.add(new JLabel("Port:"));
        panelControl.add(port);
        panelControl.add(new JLabel("Nick name:"));
        panelControl.add(nickName);
        panelControl.add(new JLabel());
        panelControl.add(connect);

        panelTop.add(panelAvatar, BorderLayout.WEST);
        panelTop.add(panelControl, BorderLayout.EAST);
        log = new JList(listData);
        log.setCellRenderer(new CustomCellRenderer());
        scrollPane.setViewportView(log);
        scrollPane.setPreferredSize(getPreferredSize());
        scrollPane.createVerticalScrollBar();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());
            }
        });
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(message, BorderLayout.CENTER);
        panel.add(send, BorderLayout.EAST);

        this.setSize(400, 600);
        this.setLayout(new BorderLayout());
        this.add(panelTop, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(panel, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle(name);
        send.addActionListener(this);
        connect.addActionListener(this);
        message.addKeyListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        // nếu ấn nút Connect
        if (ae.getSource() == connect
                && !serverName.getText().equals("")
                && !port.getText().equals("")
                && !nickName.getText().equals("")) {
            // Kết nối tới server
            server = new ServerConnect(serverName.getText(),
                    Integer.parseInt(port.getText()));

            new Thread(new Runnable() {

                @Override
                public void run() {
                    // liên tục lắng nghe dữ liệu gửi từ server
                    while (true) {
                        Packet msg = server.receiveMessage();
                        if(msg == null) continue;
                        if(mySend || !msg.getName().equalsIgnoreCase(packetRevice.getName())){
                            System.out.print("'" + msg.getUrl() + "'");
                            packetRevice = new Packet(msg.getName(), msg.getUrl(), msg.getMessage());
                            listData.add(packetRevice);
                        }else{
                            packetRevice.setMessage(packetRevice.getMessage() + "\n" + msg.getMessage());
                        }
                        log.updateUI();
                        mySend = false;
                    }
                }
            }).start();
        }
        if (ae.getSource() == send) {
            Packet packet = new Packet(nickName.getText(), urlAvatar, message.getText());
            server.sendMessage( packet);

            if(!mySend){
                packetSend = new Packet("You", urlAvatar, message.getText());
                listData.add(packetSend);
            }else{
                packetSend.setMessage(packetSend.getMessage() + "\n" + message.getText() );
            }
            log.updateUI();
            message.setText("");
            mySend = true;
        }
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setName("Chooser avatar...");
        chooser.setSize(500, 400);
        chooser.setVisible(true);
        int value = chooser.showOpenDialog(this);
        if (value == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            urlAvatar = file.getPath();
            avatar.loadAvatar(urlAvatar, 80, 80);
            repaint();
        }
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyReleased(KeyEvent e) {

        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            Packet packet = new Packet(nickName.getText(), urlAvatar, message.getText());
            server.sendMessage( packet);

            if(!mySend){
                packetSend = new Packet("Me", urlAvatar, message.getText());
                listData.add(packetSend);
            }else{
                packetSend.setMessage(packetSend.getMessage() + "\n" + message.getText() );
            }

            log.updateUI();
            message.setText("");
            mySend = true;

        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}

class CustomCellRenderer implements ListCellRenderer {

    @Override
    public JPanel getListCellRendererComponent(JList list, Object value,
                                               int index, boolean isSelected, boolean cellHasFocus) {

        JPanel panel = new GUIPacket((Packet) value);
        return panel;
    }
}
