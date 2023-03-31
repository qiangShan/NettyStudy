package com.mashibing.nettyStudy.ts01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;

public class NioServer {

    public static void main(String[] args) {
        EventLoopGroup boosGroup=new NioEventLoopGroup(1);
        EventLoopGroup workGroup=new NioEventLoopGroup(2);

        ServerBootstrap b=new ServerBootstrap();
        try {
            ChannelFuture f=b.group(boosGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            //System.out.println(Thread.currentThread().getId());
                            ChannelPipeline pl = channel.pipeline();
                            pl.addLast(new ServerChildHandler());
                        }
                    })
                    .bind(8888)
                    .sync();
            System.out.println("server started");

            f.channel().closeFuture().sync();   //close() ->ChannelFuture

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            workGroup.shutdownGracefully();
            boosGroup.shutdownGracefully();
        }
    }
}

class ServerChildHandler extends ChannelInboundHandlerAdapter{  //SimpleChannelInboundHandler

    /**
    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception{
        System.out.println(Thread.currentThread().getId());
    }
     */

    @Override
    public void channelRead(ChannelHandlerContext context,Object msg) throws Exception{
        ByteBuf buf=null;
        try{
            buf=(ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(),bytes);
            System.out.println(new String(bytes));

            context.writeAndFlush(msg);
        }finally {
            if(buf !=null){
                ReferenceCountUtil.release(buf);
            }
        }
    }
}
