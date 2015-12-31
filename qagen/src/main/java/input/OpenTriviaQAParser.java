package input;

import java.io.IOException;

import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Progress;

public class OpenTriviaQAParser extends JCasCollectionReader_ImplBase {

	public Progress[] getProgress() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasNext() throws IOException, CollectionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void getNext(JCas arg0) throws IOException, CollectionException {
		// TODO Auto-generated method stub
		
	}

}
