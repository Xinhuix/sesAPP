package com.jfinalshop.controller.usercenter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FilesService {
	//文件拷贝
	public void fileChannelCopy(File s,File t) {
		FileInputStream fi = null;//文件输入流
		
		FileOutputStream fo= null;//文件输出流
		
		FileChannel in = null;
		
		FileChannel out = null;
		
		try {
			fi = new FileInputStream(s);
			
			fo = new FileOutputStream(t);
			
			in = fi.getChannel();//获取读入的文件通道
			
			out = fo.getChannel();//获取写入的文件通道;
			
			in.transferTo(0, in.size(), out);// 连接两个通道，从文件输入流读取数据到文件输出流
		} catch (Exception e) {
			e.printStackTrace();
		}finally {

            try {

                fi.close();

                in.close();

                fo.close();

                out.close();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }
}


