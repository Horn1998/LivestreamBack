package com.horn.energy_blockchain.Utils;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author DELL
 * @date 2020/5/20 18:14
 *
 */
@Component
public class WebSocketServer {

    @Value("${netty.port}")
    private Integer port;

    @Value("${netty.context-path}")
    private String contentPath;
    /*
    * 1. Bootstrap通常使用connect()方法连接到远程的主机和端口，作为一个Netty TCP协议通信中的客户端。另外，Bootstrap也可以通过bind()方法绑定本地的一个端口，作为UDP协议通信中的一端。
      2. ServerBootstrap通常使用bind()方法绑定本地的端口上，然后等待客户端的连接。
      3. Bootstrap只需要配置一个线程组一EventLoopGroup ,而ServerBootstrap需要配置两个线程组一EventLoopGroup，一个用于接收连接，一个用于具体的IO处理。
    * */
    public void run() throws Exception{
        //多线程模型，满足绝大部分应用场景
        //1.bossGroup用于接受链接，workerGroup用于具体的处理
        // 处理客户端的TCP连接请求
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //默认构造CPU核心数*2
        //负责每一条连接的具体读写数据处理逻辑，整整负责I/O读写操作，交友对应Handler处理。
        EventLoopGroup workerGroup = new NioEventLoopGroup();  //8个

        //服务端启动
        try {
            //创建服务端启动引导/辅助类：ServerBootStrap
            ServerBootstrap bootstrap = new ServerBootstrap();
            //给引导类配置两大线程组，确定了线程模型
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //1个Channel分配一个ChannelPipeline，一个ChannelPipeline包含多个ChannelHandler
                            //ChannelHandler之间是一条链条，顺序进行数据处理操作
                            //获取到pipeline
                            ChannelPipeline pipeline = socketChannel.pipeline();

                            //因为是基于http协议，使用http的编码和解码器
                            pipeline.addLast(new HttpServerCodec());

                            //是以块方式写，添加 ChunkedWriteHandler 处理器
                            pipeline.addLast(new ChunkedWriteHandler());

                            /*
                            说明：
                            1. http 数据在传输过程中是分段，HttpObjectAggregator，就是可以将多个报文段聚合
                            2. 这就是为什么，当浏览器发送大量数据时，就会发出多次http请求。
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));

                            /*
                             * 说明：
                             *  1.对应websocket ，它的数据是以  帧（frame） 形式传播
                             *  2. 可以看到WebSocketFrame ，下面有六个子类
                             *  3. 浏览器请求时，  ws://localhost:7000/hello 表示请求的uri
                             *  4. WebSocketServerProtocolHandler 核心功能是将 http协议升级为 ws 协议，保持长连接
                             *  5. 是通过一个状态码 101
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler(contentPath));

                            //自定义的handler，处理业务逻辑
                            pipeline.addLast(new WebSocketServerHandler());
                        }
                    });

            System.out.println("netty 服务器启动");
            //绑定端口，等待连接结束
            // bind()是异步的，但是可以通过sync()方法将其变为同步。
            ChannelFuture channelFuture = bootstrap.bind(port).sync();

            //监听关闭
            channelFuture.channel().closeFuture().sync();

        }finally {
            //优雅关闭相关线程组资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
