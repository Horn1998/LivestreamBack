package com.horn.energy_blockchain.Utils;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author DELL
 * @date 2020/5/5 20:54
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //定义一个channel组，管理所有的channel
    //GlobalEventExecutor.INSTANCE 是全局的事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器端收到消息");

        //获取到当前的channel
        Channel channel = ctx.channel();
        //这时我们遍历channelGroup，根据不同的情况，回送不同的消息
        //这里可以扩充 用户登陆提示。。。

        channelGroup.forEach(ch -> {
            if(channel != ch){ //不是当前的channel，转发消息
                ch.writeAndFlush(new TextWebSocketFrame(msg.text()));
            }
        } );
        //将消息上传到MQ服务器
        MQHelper.sendMesssage(msg.text());

        //回复消息
        //ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器事件" + LocalDateTime.now() + "  " + msg.text()));
    }

    //当web客户端连接后，触发方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //id表示唯一的值，LongText 是唯一的 ShortText 不是唯一的
        // Channel 接口是Netty对网络操作的抽象类，通过Channel可以进行I/O操作。
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其他在线的客户端
        //该方法会将 channelGroup 中所有的channel 遍历，并发送消息，我们不需要自己遍历
//        channelGroup.writeAndFlush(new TextWebSocketFrame("[客户端]" + channel.remoteAddress() + "加入聊天"
//                + sdf.format(new Date()) + "\n"));
        //该方法能将channelGroup中所有的channel遍历并且发送消息，自己不需要遍历
        channelGroup.writeAndFlush("[设备]"+channel.remoteAddress()+"加入服务器"+sdf.format(new Date()) + "\n");
        channelGroup.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 被调用" + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生" + cause.getMessage());
        ctx.close();
    }

    //表示处于活跃状态,提示xx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[设备]"+ctx.channel().remoteAddress() + " 上线了---");
    }

    //表示不再活跃状态,提示离线
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[设备]"+ctx.channel().remoteAddress() + " 离线了---");
    }

}
