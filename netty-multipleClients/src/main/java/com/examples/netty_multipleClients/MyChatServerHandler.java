package com.examples.netty_multipleClients;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class MyChatServerHandler extends SimpleChannelInboundHandler<String> {

	
	private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		Channel channel = ctx.channel(); // Channel object that sends the message
	
		System.out.println("["+channel.remoteAddress() +"]--> request received from Client : "+msg);
		channelGroup.forEach(ch -> {
			if (channel != ch) { // not yourself
				ch.writeAndFlush(channel.remoteAddress() + "[not your self ] -> Message: " + msg + "\n");
			} else {
				// I am myself
				ch.writeAndFlush("[your self]-> messaage:" + msg + "\n");
			}
		});
	}

	@Override // Link establishment
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel(); // Link object
		// Broadcast server saves all linked objects
		// The writeAndFlush method in channelGroup will write the content to all
		// channels
		channelGroup.writeAndFlush("[server-]" + channel.remoteAddress() + "join\n");
		channelGroup.add(channel);
	}

	/*
	 * @Override // The link is broken public void
	 * handlerRemoved(ChannelHandlerContext ctx) throws Exception { Channel channel
	 * = ctx.channel(); // If the link is broken, then netty will automatically
	 * remove this channel out // of channelGroup
	 * channelGroup.writeAndFlush("[server]-" + channel.remoteAddress() +
	 * "Leave\n"); System.out.println(channelGroup.size()); }
	 * 
	 * @Override // Active state public void channelActive(ChannelHandlerContext
	 * ctx) throws Exception { Channel channel = ctx.channel();
	 * System.out.println(channel.remoteAddress() + "- is already online"); }
	 * 
	 * @Override public void channelInactive(ChannelHandlerContext ctx) throws
	 * Exception { Channel channel = ctx.channel();
	 * System.out.println(channel.remoteAddress() + "- has been offline"); }
	 */

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
