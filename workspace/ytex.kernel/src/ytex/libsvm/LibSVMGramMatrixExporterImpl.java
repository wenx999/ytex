package ytex.libsvm;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import ytex.kernel.dao.KernelEvaluationDao;

public class LibSVMGramMatrixExporterImpl {
	private KernelEvaluationDao kernelEvaluationDao = null;
	private JdbcTemplate jdbcTemplate = null;

	public void exportGramMatrix(Properties props) throws IOException {
		String name = props.getProperty("kernel.name");
		String testInstanceQuery = props.getProperty("test.instance.query");
		String trainInstanceQuery = props.getProperty("train.instance.query");
		String outdir = props.getProperty("outdir");
		exportGramMatrices(name, testInstanceQuery, trainInstanceQuery, outdir);
	}

	private SortedMap<Integer, Map<String, Integer>> loadClassLabels(
			String strQuery, final Set<String> labels) {
		final SortedMap<Integer, Map<String, Integer>> instanceLabelsMap = new TreeMap<Integer, Map<String, Integer>>();
		jdbcTemplate.query(strQuery, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				int instanceId = rs.getInt(1);
				String label = rs.getString(2);
				int classID = rs.getInt(3);
				Map<String, Integer> instanceLabels = instanceLabelsMap
						.get(instanceId);
				if (instanceLabels == null) {
					instanceLabels = new HashMap<String, Integer>(1);
					instanceLabelsMap.put(instanceId, instanceLabels);
				}
				labels.add(label);
				instanceLabels.put(label, classID);
			}
		});
		return instanceLabelsMap;
	}

	private void exportGramMatrices(String name, String testInstanceQuery,
			String trainInstanceQuery, String outdir) {
		Set<String> labels = new HashSet<String>();
		SortedMap<Integer, Map<String, Integer>> trainInstanceLabelMap = loadClassLabels(
				trainInstanceQuery, labels);
		double[][] trainGramMatrix = new double[trainInstanceLabelMap.size()][trainInstanceLabelMap
				.size()];
		SortedMap<Integer, Map<String, Integer>> testInstanceLabelMap = null;
		double[][] testGramMatrix = null;
		if (testInstanceQuery != null) {
			testInstanceLabelMap = loadClassLabels(testInstanceQuery, labels);
			testGramMatrix = new double[trainInstanceLabelMap.size()][testInstanceLabelMap
					.size()];
		}
		fillGramMatrix(name, trainInstanceLabelMap, trainGramMatrix,
				testInstanceLabelMap, testGramMatrix);
		for (String label : labels) {
			outputGramMatrix(name, outdir, label, trainInstanceLabelMap,
					trainGramMatrix);
			if (testGramMatrix != null) {
				outputGramMatrix(name, outdir, label, testInstanceLabelMap,
						testGramMatrix);
			}
		}
		outputInstanceIds(name, outdir, trainInstanceLabelMap, "train");
		if (testInstanceLabelMap != null)
			outputInstanceIds(name, outdir, testInstanceLabelMap, "test");
	}

	private void outputInstanceIds(String name, String outdir,
			SortedMap<Integer, Map<String, Integer>> trainInstanceLabelMap,
			String string) {
	}

	private void outputGramMatrix(String name, String outdir, String label,
			SortedMap<Integer, Map<String, Integer>> trainInstanceLabelMap,
			double[][] trainGramMatrix) {
	}

	private void fillGramMatrix(String name,
			SortedMap<Integer, Map<String, Integer>> trainInstanceLabelMap,
			double[][] trainGramMatrix,
			SortedMap<Integer, Map<String, Integer>> testInstanceLabelMap,
			double[][] testGramMatrix) {
	}

}
