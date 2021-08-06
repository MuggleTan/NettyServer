package com.hunau.netty.udp;

import com.hunau.constant.Constant;
import com.hunau.entity.Info;
import com.hunau.service.InfoService;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author TanXY
 * @date 2021/4/5 - 10:17
 */
@Slf4j
@Component
public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Autowired
    InfoService infoService;

    static UdpServerHandler serverHandler;

    public UdpServerHandler() {
    }

    @PostConstruct
    public void init() {
        serverHandler = this;
        serverHandler.infoService = this.infoService;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) {
        String clientIp = msg.sender().getHostString();
        //获取发送方的udp报文数据
        String receive = msg.content().toString(CharsetUtil.UTF_8);
        log.info("received data from【" + clientIp + "】==>" + receive);

        ctx.channel().eventLoop().execute(() -> {


            String[] split = receive.split(",");
            if (split.length == Constant.FRAME_LEN && Constant.FRAME_HEAD.equals(split[0]) && Constant.FRAME_TAIL.equals(split[split.length - 1])) {
                Info info = new Info();

                info.setSno(split[1]);
                info.setSip(split[2]);
                info.setSuccess(clientIp.equals(split[2]) ? "1" : "2");
                info.setType(1);
                info.setRip(clientIp);

                serverHandler.infoService.addInfo(info);

                ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("OK", CharsetUtil.UTF_8), msg.sender()));
            } else {
                //应答,由于UDP报文头是包含了目的端口和源端口号，所以可以直接通过DatagramPacket中获得要发送方信息
                ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(receive, CharsetUtil.UTF_8), msg.sender()));
            }
        });
    }
}
