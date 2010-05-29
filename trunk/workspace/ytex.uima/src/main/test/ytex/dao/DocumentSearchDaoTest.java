package ytex.dao;

import ytex.uima.ApplicationContextHolder;
import junit.framework.TestCase;

public class DocumentSearchDaoTest extends TestCase {
	DocumentSearchDao documentSearchDao;

	protected void setUp() throws Exception {
		super.setUp();
		documentSearchDao = (DocumentSearchDao) ApplicationContextHolder
				.getApplicationContext().getBean("documentSearchDao");
	}
	
	public void testSearch() {
		System.out.println(documentSearchDao.extendedSearch("C0003962", null, null, null, null, Boolean.TRUE));
	}

}
