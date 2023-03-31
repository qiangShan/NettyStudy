package com.mashibing.nettyStudy.ts02;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

public class Server {

    public static ChannelGroup clents=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

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
                    .bind(8889)
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


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Server.clents.add(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext context,Object msg) throws Exception{
        ByteBuf buf=null;
        try{
            buf=(ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(),bytes);
            System.out.println(new String(bytes));

            Server.clents.writeAndFlush(msg);

        }finally {

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        Server.clents.remove(ctx.channel());
        ctx.close();
    }
}
