package ytex.cmc;

import junit.framework.TestCase;

public class CTakesKernelTest extends TestCase {
	CMCKernel cTakesKernel;
	CMCEvaluator cmcEvaluator;

	protected void setUp() throws Exception {
		super.setUp();
		cTakesKernel = (CMCKernel)LoadCMC.cmcApplicationContext.getBean("cTakesDBKernel");
		cmcEvaluator = (CMCEvaluator)LoadCMC.cmcApplicationContext.getBean("cmcEvaluator");
	}

	public void testCalculateSimilarity() {
		System.out.println(cTakesKernel.calculateSimilarity(97634811, 97634910));
	}
	
	public void testCMCAll() {
		cmcEvaluator.evaluateAllCMC();
	}

}
