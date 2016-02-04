package candidate.extraction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import types.CandidateAnswer;

public class BlacklistFilter extends JCasAnnotator_ImplBase {

	private List<Pattern> blacklistedPhrases;
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		
		InputStream is = BlacklistFilter.class.getClassLoader().getResourceAsStream("config/candidate-blacklist.txt");
		
		if (is == null) {
			throw new ResourceInitializationException("Could not read candidate blacklist from resources", null);
		}
		
		this.blacklistedPhrases = new LinkedList<>();
		try {
			this.parseBlacklist(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		Collection<CandidateAnswer> candidates = JCasUtil.select(jcas, CandidateAnswer.class);
		List<CandidateAnswer> remove = new LinkedList<>();
		String title;
		Matcher m;
		
		for (CandidateAnswer candidate : candidates) {
			title = candidate.getTitle();
			
			for (Pattern phrase : this.blacklistedPhrases) {
				m = phrase.matcher(title);
				if (m.find()) {
					this.getContext().getLogger().log(Level.INFO, "Removing candidate " + 
							title + 
							"(" + 
							candidate.getWikipediaPageId() + 
							") from index because it contains the blacklisted phrase \"" +
							phrase +
							"\".");
					remove.add(candidate);
				}
			}
		}
		
		for (CandidateAnswer rc : remove) {
			rc.removeFromIndexes();
		}
		
	}
	
	private void parseBlacklist(InputStream is) throws IOException {
		BufferedReader blacklistReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		boolean finishedReading = false;
		String phrase;
		
		while(!finishedReading) {
			phrase = blacklistReader.readLine();
			
			if (phrase == null) {
				finishedReading = true;
			} else {
				phrase = phrase.trim();
				
				if (!phrase.isEmpty()) {
					this.blacklistedPhrases.add(Pattern.compile(phrase));
				}
			}
			
		}
		
		blacklistReader.close();
		
		StringBuilder sb = new StringBuilder();
		final String LF = System.getProperty("line.separator");
		sb.append("List of blacklist phrases: ");
		sb.append(LF);
		for (int i = 0; i < this.blacklistedPhrases.size(); i++) {
			sb.append("\t");
			sb.append(i);
			sb.append(". ");
			sb.append(this.blacklistedPhrases.get(i));
			sb.append(LF);
		}
		
		this.getContext().getLogger().log(Level.INFO, sb.toString());
	}

}
