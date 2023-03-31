package com.mashibing.nettyStudy.ts01;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

    public static void main(String[] args) {
        //线程池
        EventLoopGroup group=new NioEventLoopGroup(2);

        Bootstrap b=new Bootstrap();

        try {

            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientChannelInitializer())
                    .connect("localhost",8888)
                    .addListener(null)
                    .sync();
            /**
           ChannelFuture f= b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientChannelInitializer())
                    .connect("localhost",8888);

           f.addListener(new ChannelFutureListener() {
               @Override
               public void operationComplete(ChannelFuture channelFuture) throws Exception {
                   if(!channelFuture.isSuccess()){
                       System.out.println("not connected");
                   }else{
                       System.out.println("connected");
                   }
               }
           });

           f.sync();

           System.out.println("........");

        /**            .sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
         */
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }
}

class ClientChannelInitializer extends ChannelInitializer<SocketChannel>{

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        System.out.println(socketChannel);
        //socketChannel.pipeline().addLast();
    }

}
