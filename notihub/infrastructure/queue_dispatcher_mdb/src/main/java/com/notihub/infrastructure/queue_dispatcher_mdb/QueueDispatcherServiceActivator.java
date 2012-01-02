package com.notihub.infrastructure.queue_dispatcher_mdb;

import java.util.Hashtable;

import com.notihub.app_services.common.MerchantiseStockTrackerService;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.apache.log4j.Logger;

/**
 * @author codeoedoc
 */
public class QueueDispatcherServiceActivator implements BundleActivator {
    private static BundleContext context;
    private static Logger logger=Logger.getLogger(QueueDispatcherServiceActivator.class);
    
    
    private static Hashtable<String, ServiceLocator> backEndServices;
    
    public QueueDispatcherServiceActivator(){

    	System.out.println("Initializing QueueDispatcherServiceActivator");
    	backEndServices =new Hashtable<String, ServiceLocator>();
    }
    
    public void start(final BundleContext context) throws Exception {
    	QueueDispatcherServiceActivator.context = context;
    	
		new Thread() {
			@Override
			public void run() {
				
				// Load all available services
				boolean allServicesLoaded = false;
				while (allServicesLoaded == false) {
					ServiceLocator sl = new ServiceLocator(context);
					QueueDispatcherServiceActivator.backEndServices.put(MerchantiseStockTrackerService.serviceName, sl);
					StringBuffer logSB=new StringBuffer();
					logSB.append("Loading ");
					logSB.append(MerchantiseStockTrackerService.serviceName);
					System.out.println(logSB);
					allServicesLoaded = true;
				}
			}
		}.start();
	}

    public void stop(BundleContext context) throws Exception {
        System.out.println("Queue Dispatcher stopped");
    }
    
    public static BundleContext getBundleContext(){
    	return QueueDispatcherServiceActivator.context;
    }
    
    public static ServiceLocator LocateService(String serviceKey){
    	return (ServiceLocator)QueueDispatcherServiceActivator.backEndServices.get(serviceKey);
    }
}
