package index;

import java.io.File;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

import util.Config;

public class IndexCreator {
	
	public IndexCreator() throws Exception{
		Directory dir=new SimpleFSDirectory(new File(Config.INDEXDIR));
		IndexWriter writer=new IndexWriter(dir,new ZHAnalyzer(),IndexWriter.MaxFieldLength.UNLIMITED);
		//create a list of doc, then add them to writer
		
		writer.close();
	}
	
}
