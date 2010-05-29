package ytex.weka;

import java.io.IOException;

import ytex.uima.ApplicationContextHolder;


public class ExportBagOfWords {

	public static void main(String args[]) throws IOException {
		BagOfWordsExporter exporter = (BagOfWordsExporter)ApplicationContextHolder
				.getApplicationContext().getBean("bagOfWordsExporter");
		exporter.exportBagOfWords(args[0]);
	}
}
