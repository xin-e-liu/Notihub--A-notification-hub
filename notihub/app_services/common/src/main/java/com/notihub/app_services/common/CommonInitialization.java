/*
Copyright 2011 codeoedoc

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package com.notihub.app_services.common;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 * @author codeoedoc
 */
public class CommonInitialization implements BundleActivator {
    private static BundleContext context;
    private static Logger logger=Logger.getLogger(CommonInitialization.class);
    private static final String LOG4J_PROPERTIES="~/glassfish3/glassfish/domains/domain1/config/log4j.properties";
    
    
    public CommonInitialization(){
    	System.out.println("Initializing log4j");
    	PropertyConfigurator.configure(LOG4J_PROPERTIES);
    	System.out.println("Initializing log4j complete");
    }
    
    public void start(final BundleContext context) throws Exception {

    	StringBuffer logSB=new StringBuffer();
    	logSB.append("notihub common initialization complete");
    	logger.debug(logSB);
	}

    public void stop(BundleContext context) throws Exception {
        System.out.println("Queue Dispatcher stopped");
    }
}

