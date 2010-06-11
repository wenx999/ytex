package ytex.uima;

import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;

/**
 * 
 * @author vijay
 * @TODO load beanRefContext.xml from ytex.properties.
 */
public class ApplicationContextHolder {
	private static final BeanFactoryLocator beanFactory = ContextSingletonBeanFactoryLocator
			.getInstance();
	private static final ApplicationContext ytexApplicationContext = (ApplicationContext) beanFactory
			.useBeanFactory("ytexApplicationContext").getFactory();

	public static ApplicationContext getApplicationContext() {
		return ytexApplicationContext;
	}

}
