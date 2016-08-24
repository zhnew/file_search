package index;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

public class ZHAnalyzer extends Analyzer {

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
	    return new ZHTokenStream(reader);
	  }

}
