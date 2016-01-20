import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;


public class Subscriber {

	public static final String BROKER_URL = "tcp://broker.mqttdashboard.com:1883";
	
	public static final String TOPIC_BRIGHTNESS = "pb_kyou_home/brightness";
	public static final String TOPIC_TEMPERATURE = "pb_kyou_home/temperature";
	
	private MqttClient client;
	private String clientId = this.getMacAddress()+"-sub";
	
	public Subscriber(){
		try{
			client = new MqttClient(BROKER_URL, clientId);
			
		}catch(MqttException e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void start(){
		try{
			client.setCallback(new SubscribeCallback());
			client.connect();
			
			final String topic = "pb_kyou_home/#";
			client.subscribe(topic);
			
			System.out.println("Subscriber is now listening to "+topic);
		}catch(MqttException e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static void main(String[] args) {
		final Subscriber subscriber = new Subscriber();
		subscriber.start();
	}
	
	
	private String getMacAddress(){
		InetAddress ip;
		StringBuilder sb = new StringBuilder();
		try{
			ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();
			
			
			for(int i = 0;i<mac.length;i++){
				sb.append(String.format("%02X%s", mac[i], (i<mac.length-1) ? "-": ""));
			}
			
			
		}catch(UnknownHostException e){
			e.printStackTrace();
		}catch(SocketException e){
			e.printStackTrace();
		}
		System.out.println("Mac address retrieved : "+sb.toString());
		return sb.toString();
	}
	
}
