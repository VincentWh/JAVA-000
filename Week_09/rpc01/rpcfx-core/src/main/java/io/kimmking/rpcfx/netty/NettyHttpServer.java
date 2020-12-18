package io.kimmking.rpcfx.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class NettyHttpServer {
    private final ApplicationContext context;

    private EventLoopGroup boss;
    private EventLoopGroup worker;

    public NettyHttpServer(ApplicationContext context) {
        this.context = context;
    }

    public void destroy() {
        worker.shutdownGracefully();
        boss.shutdownGracefully();
    }

    public void run() throws Exception {
        boss = new NioEventLoopGroup(1);
        worker = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast("RpcEncoder", new RpcEncoder());
                        pipeline.addLast("RpcDecoder", new RpcDecoder());
                        pipeline.addLast("NettyHttpServerInboundHandler", new NettyHttpServerInboundHandler(context));
                    }
                });

        int port = 8080;
        Channel channel = serverBootstrap.bind(port).sync().channel();
        channel.closeFuture().sync();
    }
}
