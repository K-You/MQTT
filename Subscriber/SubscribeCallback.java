import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class SubscribeCallback implements MqttCallback{
	
	@Override
	public void connectionLost(Throwable cause){}
	

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("Message arrived =>" +topic+":"+message);
		if("pb_kyou_home/LWT".equals(topic)){
			System.err.println("Sensor gone");
		}
		
	}

}
