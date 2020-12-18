package io.kimmking.rpcfx.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyHttpClientOutboundHandler extends ChannelInboundHandlerAdapter {

    private String respJson;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        System.out.println("Msg we receive:" + msg);
        this.respJson = ((ByteBuf) msg).toString(CharsetUtil.UTF_8);
    }

    public String getRespJson() {
        return respJson;
    }

}