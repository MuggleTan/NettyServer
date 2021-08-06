package com.hunau.netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author TanXY
 * @date 2021/4/5 - 10:13
 */
@Slf4j
public class UdpServer extends Thread {

    private int port = 10998;

    public UdpServer(int port) {
        this.port = port;
    }

    public UdpServer() {

    }

    @Override
    public void start() {
        EventLoopGroup workGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap()
                .group(workGroup)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new UdpServerHandler());
        try {
            //启动服务
            ChannelFuture channelFuture = bootstrap.bind(10998).sync();
            log.info("UDP server port: " + port);
            log.info("UDP server started...");

            channelFuture.channel().closeFuture().sync();
            log.info("UDP server stopped...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                workGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new UdpServer(10998).start();
    }
}
