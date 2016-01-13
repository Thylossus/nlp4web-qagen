package de.tud.nlp4web.project.evaluation.web.evaluation;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import de.tud.nlp4web.project.evaluation.provider.api.model.Answer;
import de.tud.nlp4web.project.evaluation.provider.api.model.Question;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionEvaluationResult;
import de.tud.nlp4web.project.evaluation.web.Outcome;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@ViewScoped
@ManagedBean
@Log4j
public class QuestionBean implements Serializable {

	private static final long serialVersionUID = -6693590194084896938L;
                                        
	@Getter @Setter @ManagedProperty(value = "#{evaluationBean}")
	private EvaluationBean evaluationBean;
	
	@Getter @Setter
	private Question currentQuestion;
	
	/** The time when this question has been shown */
	private long startingTime;
	
	@PostConstruct
	public void postConstruct() {
		currentQuestion = evaluationBean.getNextQuestion();
		startingTime = System.currentTimeMillis();
	}
	
	public String answer(String answerIdStr) {
		try {
			int answerId = Integer.parseInt(answerIdStr);
			Answer chosenAnswer = null;
			for(Answer answer : currentQuestion.getAnswers()) {
				if (answer.getId() == answerId) chosenAnswer = answer;
			}
			if (chosenAnswer == null) {
				log.warn("Answer " + answerId + " does not belong to question " + currentQuestion.getId());
				return "";
			} else {
				QuestionEvaluationResult evalRes = new QuestionEvaluationResult();
				evalRes.setChosenAnswer(chosenAnswer);
				evalRes.setTimeToAnswer(((int)(System.currentTimeMillis() - startingTime)));
				
				evaluationBean.storeAnswer(currentQuestion, evalRes);
				
				if (evaluationBean.hasNextQuestion()) {
					return Outcome.NEXT_QUESTION;
				} else {
					return Outcome.SHOW_RESULT;
				}
			}
		} catch (NumberFormatException ex) {
			log.warn("Invalid answer ID: " + answerIdStr, ex);
			return "";
		}
	}
	
	
}
