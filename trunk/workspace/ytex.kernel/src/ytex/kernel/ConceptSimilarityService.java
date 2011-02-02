package ytex.kernel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ytex.kernel.model.ConcRel;
import ytex.kernel.model.Corpus;
import ytex.kernel.model.CorpusTerm;
import ytex.kernel.model.InfoContent;
import ytex.kernel.model.ObjPair;

public interface ConceptSimilarityService {

	public abstract double lch(String concept1, String concept2);

	public abstract double lin(String corpus, String concept1, String concept2);

	public void updateInformationContent(String corpusName);

	public Object[] lcs(String concept1, String concept2);


}