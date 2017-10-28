package camel;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class CamelActivator implements BundleActivator
{
    public void start(BundleContext context) throws Exception
    {
        System.out.println("-----------start----------");
    }

    public void stop(BundleContext context) throws Exception
    {
        System.out.println("-----------stop----------");
    }
}
