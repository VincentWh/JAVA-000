package io.kimmking.rpcfx.netty;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.util.CharsetUtil;
import io.netty.util.Constant;

import java.net.URI;

public class NettyHttpClient {

    public String post(String req, String backendUrl) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            NettyHttpClientOutboundHandler handler = new NettyHttpClientOutboundHandler();
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                    ch.pipeline().addLast("RpcEncoder", new RpcEncoder());
                    // 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
                    ch.pipeline().addLast("RpcDecoder", new RpcDecoder());
//                    ch.pipeline().addLast(new HttpClientCodec());
                    ch.pipeline().addLast("NettyHttpClientOutboundHandler", handler);
                }
            });
            URI uri = new URI(backendUrl);
            // Start the client.
            ChannelFuture f = b.connect(uri.getHost(), uri.getPort()).sync();
//            FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, String.valueOf(uri)
//                    , Unpooled.copiedBuffer(req.getBytes(CharsetUtil.UTF_8)));
//            request.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/json; charset=utf-8");
//            f.channel().writeAndFlush(request).sync();

            f.channel().writeAndFlush(Unpooled.copiedBuffer(req.getBytes(CharsetUtil.UTF_8))).sync();

            f.channel().closeFuture().sync();
            return handler.getRespJson();
        } finally {
            workerGroup.shutdownGracefully();
        }

    }

}