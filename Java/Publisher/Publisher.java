import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;


public class Publisher {
	public static final String BROKER_URL = "tcp://broker.mqttdashboard.com:1883";
	
	public static final String TOPIC_BRIGHTNESS = "pb_kyou_home/brightness";
	public static final String TOPIC_TEMPERATURE = "pb_kyou_home/temperature";
	
	private MqttClient client;
	
	public Publisher(){
		String clientId = this.getMacAddress() + "-pub";
		try{
			client= new MqttClient(BROKER_URL, clientId);
			
			
		}catch(MqttException e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void start(){
		System.out.println("Start...");
		try{
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(false);
			options.setWill(client.getTopic("pb_kyou_home/LWT"), "Aaand .. down".getBytes(), 2, true);
			
			client.connect(options);
			System.out.println("Client connect");
			
			while(true){
				publishBrightness();
				Thread.sleep(500);
				publishTemperature();
				Thread.sleep(500);
			}
			
		}catch(MqttException e){
			e.printStackTrace();
			System.exit(1);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		
		
	}
	
	private void publishTemperature() throws MqttException{
		final MqttTopic temperatureTopic = client.getTopic(TOPIC_TEMPERATURE);
		Random rand = new Random();
		
		final int temperatureNumber = rand.nextInt((30-18)+1)+18;
		final String temperature = temperatureNumber+"";
		
		temperatureTopic.publish(new MqttMessage(temperature.getBytes()));
		System.out.println("Data published " +temperature);
		
	}
	private void publishBrightness() throws MqttException{
		final MqttTopic brightnessTopic = client.getTopic(TOPIC_BRIGHTNESS);
		Random rand = new Random();
		
		final int brightnessNumber = rand.nextInt((100-0)+1)+0;
		final String brightness = brightnessNumber+"";
		
		brightnessTopic.publish(new MqttMessage(brightness.getBytes()));
		System.out.println("Data published " +brightness);
		
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
	
	
	public static void main(String[] args) {
		final Publisher publisher = new Publisher();
		publisher.start();
	}
}
