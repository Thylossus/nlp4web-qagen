package de.tud.nlp4web.project.evaluation.web.evaluation;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import de.tud.nlp4web.project.evaluation.provider.api.model.Answer;
import de.tud.nlp4web.project.evaluation.provider.api.model.EvaluationSession;
import de.tud.nlp4web.project.evaluation.provider.api.model.Question;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionEvaluationResult;
import lombok.Getter;
import lombok.Setter;

@ViewScoped
@ManagedBean
public class ResultsBean implements Serializable {

	private static final long serialVersionUID = 1616108182797737455L;
	
	@Getter @Setter @ManagedProperty(value = "#{evaluationBean}")
	private EvaluationBean evaluationBean;
	
	@Getter
	private int totalQuestionCount;
	
	@Getter
	private int solvedQuestionCount;
	
	@Getter
	private List<ResultLine> results;
	
	@PostConstruct
	public void postConstruct() {
		totalQuestionCount = 0;
		solvedQuestionCount = 0;
		results = new LinkedList<>();
		if (getSession()!=null) {
			for(Question q : getSession().getAnsweredQuestions().keySet()) {
				QuestionEvaluationResult qer = getSession().getAnsweredQuestions().get(q);
				if (qer.getChosenAnswer().equals(q.getCorrectAnswer())) {
					solvedQuestionCount+=1;
				}
				totalQuestionCount+=1;
				
				ResultLine rl = new ResultLine();
				rl.setQuestion(q.getText());
				rl.setAnswers(new LinkedList<ResultCell>());
				for(Answer a : q.getAnswers()) {
					ResultCell rc = new ResultCell();
					rc.setAnswer(a.getText());
					rc.setChosen(a.equals(qer.getChosenAnswer()));
					rc.setCorrect(a.equals(q.getCorrectAnswer()));
					rl.getAnswers().add(rc);
				}
				results.add(rl);
			}
		}
	}
	
	
	public EvaluationSession getSession() {
		return evaluationBean.getEvaluationSession();
	}

	@Getter @Setter
	public static class ResultCell {
		private boolean chosen;
		private boolean correct;
		private String answer;
	}
	
	@Getter @Setter
	public static class ResultLine {
		private String question;
		private List<ResultCell> answers;
	}
}
