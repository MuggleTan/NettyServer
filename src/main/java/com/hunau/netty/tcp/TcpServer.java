package com.hunau.netty.tcp;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author TanXY
 * @since 2021/4/4 - 9:47
 */
@Slf4j
public class TcpServer extends Thread {

    private int port = 10999;

    /**
     * 在线设备Map
     */
    static HashMap<String, Channel> onlineEquipmentMap = new HashMap<>(32);


    public TcpServer(int port) {
        this.port = port;
    }

    public TcpServer() {
    }

    public synchronized static void setOnlineEquipmentMap(String ip, Channel channel) {
        onlineEquipmentMap.put(ip, channel);
    }

    public synchronized static void removeInactiveChannel(Channel channel) {
        onlineEquipmentMap.remove(((InetSocketAddress) channel.remoteAddress())
                .getAddress().getHostAddress());
    }

    @Override
    public void start() {

        /* new 一个主线程组*/
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        /* new 一个工作线程组*/
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            //创建和配置ServerBootstrap
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    // 两个线程组
                    .group(bossGroup, workGroup)
                    // 服务器的通道
                    .channel(NioServerSocketChannel.class)
//                    .localAddress(new InetSocketAddress(port))
                    // 线程队列等待连接个数
                    .option(ChannelOption.SO_BACKLOG, 64)
                    // 保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            //创建ChannelInitializer
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
                    socketChannel.pipeline()
                            .addLast("decoder", new StringDecoder(CharsetUtil.UTF_8))//解码
                            .addLast("encoder", new StringEncoder(CharsetUtil.UTF_8))//编码
                            .addLast(new IdleStateHandler(3, 0, 0, TimeUnit.MINUTES))
                            .addLast(new TcpServerHandler());
                }
            });
            //启动服务
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            log.info("TCP server port: {}", port);
            log.info("TCP server started...");

            channelFuture.channel().closeFuture().sync();
            log.info("TCP server stopped...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bossGroup.shutdownGracefully().sync();
                workGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new TcpServer(10999).start();
    }
}
