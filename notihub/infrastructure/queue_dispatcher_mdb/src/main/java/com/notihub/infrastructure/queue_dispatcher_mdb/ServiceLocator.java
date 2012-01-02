package com.notihub.infrastructure.queue_dispatcher_mdb;

import com.notihub.app_services.common.MerchantiseStockTrackerService;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author codeoedoc
 */
public class ServiceLocator {
    private ServiceTracker st;

    public ServiceLocator(BundleContext context) {
        st = new ServiceTracker(context, MerchantiseStockTrackerService.class.getName(), null);
        st.open();
    }

    /**
     * Looks up a service and returns
     * @param timeout
     * @return
     */
    public MerchantiseStockTrackerService getMerchantiseStockTrackerService(long timeout) throws ServiceUnavailableException {
    	MerchantiseStockTrackerService service = null;
        try {
            service = MerchantiseStockTrackerService.class.cast(timeout == -1 ? st.getService() : st.waitForService(timeout));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (service == null) throw new ServiceUnavailableException();
        return service;
    }

    public void close() {
        st.close();
    }

    public static class ServiceUnavailableException extends Exception {
    }
}
