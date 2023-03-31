package com.mashibing.nettyStudy.ts02;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientFrame extends Frame {

    TextArea ta=new TextArea();
    TextField tf=new TextField();

    public ClientFrame(){
        this.setSize(600,400);
        this.setLocation(100,20);
        this.setTitle("聊天室");
        this.add(ta,BorderLayout.CENTER);
        this.add(tf,BorderLayout.SOUTH);
        tf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //把字符串发送到服务器
                ta.setText(ta.getText()+tf.getText());
                tf.setText("");
            }
        });

        this.setVisible(true);
        new Client().connect();
    }

    public static void main(String[] args) {
        new ClientFrame();
    }
}
