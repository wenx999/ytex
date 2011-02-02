package ytex.cmc;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import ytex.kernel.dao.KernelEvaluationDao;

public class CMCEvaluatorImpl implements CMCEvaluator {
	SessionFactory cmcSessionFactory;
	KernelEvaluationDao kernelEvaluationDao;
	CMCKernel kernel;

	public KernelEvaluationDao getKernelEvaluationDao() {
		return kernelEvaluationDao;
	}

	public void setKernelEvaluationDao(KernelEvaluationDao kernelEvaluationDao) {
		this.kernelEvaluationDao = kernelEvaluationDao;
	}

	public CMCKernel getKernel() {
		return kernel;
	}

	public void setKernel(CMCKernel kernel) {
		this.kernel = kernel;
	}

	public SessionFactory getCmcSessionFactory() {
		return cmcSessionFactory;
	}

	public void setCmcSessionFactory(SessionFactory cmcSessionFactory) {
		this.cmcSessionFactory = cmcSessionFactory;
	}

	/* (non-Javadoc)
	 * @see ytex.cmc.CMCEvaluator#evaluateAllCMC()
	 */
	public void evaluateAllCMC() {
		Query q = cmcSessionFactory.getCurrentSession().createQuery(
				"select documentId from CMCDocument where documentSet = 'train' order by documentId asc");
		q.setMaxResults(100);
		List<Integer> documentIds = q.list();
		for (int i = 0; i < documentIds.size(); i++) {
			for (int j = i; j < documentIds.size(); j++) {
				if (i != j) {
					int instanceId1 = documentIds.get(i);
					int instanceId2 = documentIds.get(j);
					kernelEvaluationDao.storeKernel("cmc-ctakes", instanceId1,
							instanceId2, kernel.calculateSimilarity(instanceId1,
									instanceId2));
				}
			}
		}
	}

}
