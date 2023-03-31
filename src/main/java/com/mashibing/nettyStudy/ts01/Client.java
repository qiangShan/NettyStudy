package com.mashibing.nettyStudy.ts01;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.StandardCharsets;

public class Client {

    public static void main(String[] args) {
        //线程池
        EventLoopGroup group=new NioEventLoopGroup(2);

        Bootstrap b=new Bootstrap();

        try {

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

           f.channel().closeFuture().sync();

        /**
             b.group(group)
             .channel(NioSocketChannel.class)
             .handler(new ClientChannelInitializer())
             .connect("localhost",8888)
             .addListener(null)
             .sync();
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
        socketChannel.pipeline().addLast(new ClientHandler());
    }

}

class ClientHandler extends ChannelInboundHandlerAdapter{

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf=null;
        try{
            buf=(ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(),bytes);
            System.out.println(new String(bytes));
        }finally {
            if(buf !=null){
                ReferenceCountUtil.release(buf);
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //channle 第一次连上可用，写出一个字符串Direct Memory
        ByteBuf buf= Unpooled.copiedBuffer("hello".getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(buf);
    }
}
