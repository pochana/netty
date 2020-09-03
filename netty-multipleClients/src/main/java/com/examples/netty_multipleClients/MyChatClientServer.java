package com.examples.netty_multipleClients;

import java.io.BufferedReader;
import java.io.InputStreamReader;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class MyChatClientServer {
	
	public static void main(String[] args) throws Exception {
		
		EventLoopGroup eventGroup = new NioEventLoopGroup();
		try{
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(eventGroup).channel(NioSocketChannel.class)
			.handler(new MyChatClientInitializer());
			Channel channel = bootstrap.connect("localhost",8899).sync().channel();
			 // Read the contents of the console input
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			 //Infinite loop, waiting for keyboard input
			for(;;){
				channel.writeAndFlush(br.readLine()+"\r\n");
			}
		}finally{
			eventGroup.shutdownGracefully(); 
		}
	}
}