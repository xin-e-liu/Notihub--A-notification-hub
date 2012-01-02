package com.notihub.infrastructure.queue_dispatcher_mdb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.notihub.app_services.common.MerchantiseStockTrackerService;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Message-Driven Bean implementation class for: QueueDispatcherMDB
 *
 */
@MessageDriven(
		activationConfig = { @ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Queue"
		) }, 
		mappedName = "amqmsg")
public class QueueDispatcherMDB implements MessageListener {
	private static Logger logger=Logger.getLogger(QueueDispatcherMDB.class);
	private static final String LOG4J_PROPERTIES="/home/xiliu3/glassfish3/glassfish/domains/domain1/config/log4j.properties";
    /**
     * Default constructor. 
     */
    public QueueDispatcherMDB() {
        // TODO Auto-generated constructor stub
    	// The configuration should be performed in a initialization module.
		// For now, keep it here for simplicity
		PropertyConfigurator.configure(LOG4J_PROPERTIES);
    }
    
    private ServiceLocator LocateService(Message message){
    	return QueueDispatcherServiceActivator.LocateService(MerchantiseStockTrackerService.serviceName);
    }
	
	/**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message message) {
        // TODO Auto-generated method stub
    	StringBuffer logSB=new StringBuffer();
    	logSB.append("Incoming message is: ");
    	logSB.append((Message)message);
    	logger.debug(logSB);
    	
    	ServiceLocator sl = LocateService(message);
        try {
            boolean ss=sl.getMerchantiseStockTrackerService(0).checkStockStatus("Touchpad");
            if(ss){
            	logger.debug("Touchpad is in stock!");
            }else{
            	logger.debug("Touchpad is out of stock");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sl.close();
       
    }

}
