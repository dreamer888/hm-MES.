package com.dream.iot.test.plc.omron;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
/**

 *
 */
public class UDPClient {
	
	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		DatagramSocket clientSocket = null;
		try {
			//��������


			clientSocket =new DatagramSocket();//���조�ʾ֡�
			InetAddress address = InetAddress.getByName("localhost");
			byte[] receive = new byte[1024];
			byte[] sendData = new byte[1024];

			while (true) {
				String data = br.readLine();
				sendData = data.getBytes();
				//����һ�����ݰ�[һ����װ�õ��ŷ�]�����͵�address��ָ���˿�
				DatagramPacket dp = new DatagramPacket(sendData, sendData.length, address, 9600);
				clientSocket.send(dp);

				//�������ݣ���ǰ׼��һ�����ŷ�DatagramPacket
				DatagramPacket dp1 = new DatagramPacket(receive, receive.length);

				clientSocket.receive(dp1);//�������ݣ���װ�ŵ�dp1���ݰ�
				String data1 = new String(dp1.getData());
				System.out.println("Client's receive:" + data1);
			}
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{clientSocket.close();}
		
	}
	
}
