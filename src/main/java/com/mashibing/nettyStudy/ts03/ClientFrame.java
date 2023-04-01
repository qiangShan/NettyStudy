package com.mashibing.nettyStudy.ts03;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientFrame extends Frame {

    public static final ClientFrame INSTANCE=new ClientFrame();

    TextArea tA=new TextArea();
    TextField tF=new TextField();

    Client client=null;

    public ClientFrame(){
        this.setSize(600,400);
        this.setLocation(100,20);
        this.setTitle("QQ聊天");
        this.add(tA,BorderLayout.CENTER);
        this.add(tF,BorderLayout.SOUTH);

        tF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //tA.setText(tA.getText()+tF.getText());
                client.send(tF.getText());
                tF.setText("");
            }
        });

    }

    public void connectToServer(){
        client=new Client();
        client.connect();
    }

    public static void main(String[] args){
        ClientFrame frame=ClientFrame.INSTANCE;
        frame.setVisible(true);
        frame.connectToServer();
    }

    public void updateText(String msgAccepted) {
        this.tA.setText(tA.getText()+System.getProperty("line.separator")+msgAccepted);
    }
}
