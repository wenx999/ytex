package ytex.uima.annotators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import com.google.common.base.Strings;

import edu.mayo.bmi.uima.core.type.textspan.Segment;

import ytex.uima.ApplicationContextHolder;
import ytex.uima.dao.SegmentRegexDao;
import ytex.uima.model.SegmentRegex;

/**
 * Annotate segments (i.e. sections). Use regexs to find segments. Read the
 * regex-segment id map from the db.
 * 
 * @author vhacongarlav
 * 
 */
public class SegmentRegexAnnotator extends JCasAnnotator_ImplBase {
	private static final Log log = LogFactory
			.getLog(SegmentRegexAnnotator.class);
	private SegmentRegexDao segmentRegexDao;
	private Map<SegmentRegex, Pattern> regexMap;
	private String defaultSegmentId = null;

	/**
	 * Load the regex-segment map from the database using the segmentRegexDao.
	 * Compile all the patterns.
	 */
	public void initialize(UimaContext aContext)
			throws ResourceInitializationException {
		segmentRegexDao = (SegmentRegexDao) ApplicationContextHolder
				.getApplicationContext().getBean("segmentRegexDao");
		regexMap = new HashMap<SegmentRegex, Pattern>();
		for (SegmentRegex regex : segmentRegexDao.getSegmentRegexs()) {
			if (log.isDebugEnabled())
				log.debug(regex);
			Pattern pat = Pattern.compile(regex.getRegex());
			regexMap.put(regex, pat);
		}
		defaultSegmentId = (String) aContext
				.getConfigParameterValue("SegmentID");
		if (Strings.isNullOrEmpty(defaultSegmentId)) {
			defaultSegmentId = "DEFAULT";
		}
	}

	/**
	 * Add Segment annotations to the cas. First create a list of segments. Then
	 * sort the list according to segment start. For each segment that has no
	 * end, set the end to the [beginning of next segment - 1], or the eof.
	 */
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		String strDocText = aJCas.getDocumentText();
		if (strDocText == null)
			return;
		List<Segment> segmentsAdded = new ArrayList<Segment>();
		// find all the segments, set begin and id, add to list
		for (Map.Entry<SegmentRegex, Pattern> entry : regexMap.entrySet()) {
			if (log.isDebugEnabled()) {
				log.debug("applying regex:" + entry.getKey().getRegex());
			}
			Matcher matcher = entry.getValue().matcher(strDocText);
			while (matcher.find()) {
				Segment seg = new Segment(aJCas);
				if (entry.getKey().isLimitToRegex()
						&& matcher.groupCount() == 1) {
					seg.setBegin(matcher.start(1));
					seg.setEnd(matcher.end(1));
				} else {
					seg.setBegin(matcher.start());
					if (entry.getKey().isLimitToRegex()) {
						seg.setEnd(matcher.end());
					}
				}
				seg.setId(entry.getKey().getSegmentID());
				if (log.isDebugEnabled()) {
					log.debug("found match: id=" + seg.getId() + ", begin="
							+ seg.getBegin());
				}
				segmentsAdded.add(seg);
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("segmentsAdded: " + segmentsAdded.size());
		}
		if (segmentsAdded.size() > 0) {
			// sort the segments by begin
			Collections.sort(segmentsAdded, new Comparator<Segment>() {

				// @Override
				public int compare(Segment o1, Segment o2) {
					return o1.getBegin() < o2.getBegin() ? -1
							: o1.getBegin() > o2.getBegin() ? 1 : 0;
				}

			});
			// set the end for each segment
			for (int i = 0; i < segmentsAdded.size(); i++) {
				Segment seg = segmentsAdded.get(i);
				Segment segNext = (i + 1) < segmentsAdded.size() ? segmentsAdded
						.get(i + 1) : null;
				if (seg.getEnd() <= 0) {
					if (segNext != null) {
						// set end to beginning of next segment
						seg.setEnd(segNext.getBegin() - 1);
					} else {
						// set end to doc end
						seg.setEnd(strDocText.length());
					}
				} else {
					// segments shouldn't overlap
					if (segNext != null && segNext.getBegin() < seg.getEnd()) {
						seg.setEnd(segNext.getBegin() - 1);
					}
				}
				if (log.isDebugEnabled()) {
					log.debug("Adding Segment: segment id=" + seg.getId()
							+ ", begin=" + seg.getBegin() + ", end="
							+ seg.getEnd());
				}
				seg.addToIndexes();
			}
		}
		// ctakes 1.3.2 - anything not in a segment will not be annotated - add
		// text outside segments to the 'default' segment
		int end = 0;
		for (Segment seg : segmentsAdded) {
			if ((seg.getBegin() - 1) > end) {
				addGapSegment(aJCas, end, seg.getBegin() - 1);
			}
			end = seg.getEnd();
		}
		if (end < strDocText.length()) {
			addGapSegment(aJCas, end, strDocText.length());
		}
	}

	private void addGapSegment(JCas aJCas, int begin, int end) {
		Segment segGap = new Segment(aJCas);
		segGap.setBegin(begin);
		segGap.setEnd(end);
		segGap.addToIndexes();
		segGap.setId(defaultSegmentId);
	}
}
