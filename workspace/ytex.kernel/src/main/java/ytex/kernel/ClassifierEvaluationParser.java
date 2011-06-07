package ytex.kernel;

import java.io.File;
import java.io.IOException;

import ytex.kernel.model.ClassifierEvaluation;

public interface ClassifierEvaluationParser {
	public static final String YES = "yes";
	public static final String NO = "no";

	/**
	 * Property keys for various parse options
	 * 
	 * @author vhacongarlav
	 */
	public enum ParseOption {
		/**
		 * key <tt>kernel.StoreInstanceEval</tt>.
		 * 
		 */
		STORE_INSTANCE_EVAL("kernel.StoreInstanceEval", NO),
		/**
		 * key <tt>kernel.StoreProbabilities</tt>.
		 */
		STORE_PROBABILITIES("kernel.StoreProbabilities", NO),
		/**
		 * key <tt>kernel.StoreUnlabeled</tt>
		 */
		STORE_UNLABELED("kernel.StoreUnlabeled", NO),
		/**
		 * key <tt>kernel.StoreIRStats</tt>
		 */
		STORE_IRSTATS("kernel.StoreIRStats", YES),
		/**
		 * key <tt>cv.fold.base</tt>
		 * base name of file; other file names constructed relative to this.
		 * fold/run taken from this file name. 
		 */
		FOLD_BASE("cv.fold.base", ""),
		/**
		 * key <tt>kernel.distance</tt>
		 * distance measure for semiL. 1 - euclidean, 2 - cosine.
		 */
		DISTANCE("kernel.distance", "1"),
		/**
		 * key <tt>cv.eval.line</tt>
		 * options used to train model.
		 */
		EVAL_LINE("cv.eval.line", null);
		String optionKey;
		public String getOptionKey() {
			return optionKey;
		}

		String defaultValue;

		public String getDefaultValue() {
			return defaultValue;
		}

		ParseOption(String optionKey, String defaultValue) {
			this.optionKey = optionKey;
			this.defaultValue = defaultValue;
		}
	}

	public void parseDirectory(File dataDir, File outputDir) throws IOException;

}