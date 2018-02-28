package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import connection.ListenServer;
import dataframe.Packet;


public class GUIServer extends JFrame implements ActionListener{

    private static final long serialVersionUID = 1L;
    private JTextField portName = new JTextField(20);
    private JButton start = new JButton("Start server C500");
    private ListenServer listenServer;
    private Thread thread = null;

    public GUIServer(){
        portName.setText("8697");
        JPanel panel = new JPanel();
        JPanel panelControl = new JPanel(new BorderLayout());
        panel.add(new JLabel("Port"));
        panel.add(portName);
        panelControl.add(start, BorderLayout.EAST);
        this.setLayout(new GridLayout(2,1));
        this.add(panel);
        this.add(panelControl);
        this.pack();
        this.setTitle("Server C500 chat");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        start.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if(!portName.getText().equals("")){
            listenServer = new ListenServer(Integer.parseInt(portName.getText()));

            thread = new Thread( new Runnable() {

                @Override
                public void run() {
                    final ArrayList<Socket> list = new ArrayList<Socket>();

                    while(true){
                        final Socket socket = listenServer.accept();
                        list.add(socket);

                        new Thread( new Runnable() {

                            @Override
                            public void run() {

                                while(true){
                                    Packet packet = listenServer.receiveMessage(socket);
                                    if(packet == null) continue;
                                    for (Socket soc : list) {
                                        if (soc == null) {
                                            list.remove(soc);
                                            continue;
                                        }
                                        if (!soc.equals(socket)) {
                                            listenServer.sendMessage(packet , soc);
                                        }
                                    }
                                }
                            }
                        }).start();
                    }
                }
            });
            thread.start();
        }
    }
}
