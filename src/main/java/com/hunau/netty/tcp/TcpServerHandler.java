package com.hunau.netty.tcp;


import com.hunau.constant.Constant;
import com.hunau.entity.Info;
import com.hunau.service.InfoService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;

/**
 * @author TanXY
 * @since 2021/4/3 23:34
 */
@Slf4j
@Component
public class TcpServerHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    InfoService infoService;

    static TcpServerHandler serverHandler;

    public TcpServerHandler() {
    }

    @PostConstruct
    public void init() {
        serverHandler = this;
        serverHandler.infoService = this.infoService;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = inetSocketAddress.getAddress().getHostAddress();
        System.err.println(ctx.channel());
        log.info("client【" + clientIp + "】connected......");

        TcpServer.setOnlineEquipmentMap(clientIp, ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        ctx.channel().eventLoop().execute(() -> {

            InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            String clientIp = inetSocketAddress.getAddress().getHostAddress();

            String received = msg.toString();
            log.info("received data from【" + clientIp + "】==>" + received);

            String[] split = received.split(",");
//            System.out.println(split.length);
//            System.out.println(Constant.FRAME_HEAD.equals(split[0]));
//            System.out.println(split[split.length - 1]);
//            System.out.println(split[0] + " " + split[1] + " " + split[2] + " " + split[3]);
            if (split.length == Constant.FRAME_LEN && Constant.FRAME_HEAD.equals(split[0]) && Constant.FRAME_TAIL.equals(split[split.length - 1])) {
                Info info = new Info();

                info.setSno(split[1]);
                info.setSip(split[2]);
                info.setSuccess(clientIp.equals(split[2]) ? "1" : "2");
                info.setType(2);
                info.setRip(clientIp);

                serverHandler.infoService.addInfo(info);

                ctx.writeAndFlush("OK");
            } else {
                ctx.writeAndFlush(received);
            }
        });

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("客户端【" + ctx.channel() + "】断开连接！");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;

            if (event.state() == IdleState.READER_IDLE) {
                TcpServer.removeInactiveChannel(ctx.channel());
                ctx.channel().close().sync();
                log.info(("关闭 " + ctx.channel() + " 这个不活跃通道！"));
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
