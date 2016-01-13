package de.tud.nlp4web.project.evaluation.web.admin;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import de.tud.nlp4web.project.evaluation.provider.api.AdminStatisticalServiceLocal;
import de.tud.nlp4web.project.evaluation.provider.api.ServiceException;
import de.tud.nlp4web.project.evaluation.provider.api.model.Answer;
import de.tud.nlp4web.project.evaluation.provider.api.model.EvaluationSession;
import de.tud.nlp4web.project.evaluation.provider.api.model.Question;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionEvaluationResult;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionSet;
import de.tud.nlp4web.project.evaluation.provider.api.model.QuestionSet.QuestionSetType;
import de.tud.nlp4web.project.evaluation.web.FacesContextHelper;
import lombok.extern.log4j.Log4j;

@ManagedBean
@ViewScoped
@Log4j
public class RawDataBean implements Serializable {
	
	private static final long serialVersionUID = 2603932297720818116L;

	private static final String FIELDSEP = ",", RECORDSEP = "\n", QUOTE = "\"", ESCAPE = "\\";
	
	@EJB
	private AdminStatisticalServiceLocal statService; 
	
	private List<EvaluationSession> sessions;
	
	@PostConstruct
	public void postConstruct() {
		try {
			sessions = statService.getAllSessions();
		} catch (ServiceException ex) {
			log.error("Cannot load statistics", ex);
			throw new RuntimeException(ex);
		}
	}
	
	public StreamedContent getCompleteEvaluationData() {
		
		StringWriter writer = new StringWriter();
		
		writeHeaders(writer, "SESSION_ID", "QUESTION_SET_ID", "QUESTION_SET_NAME", "QUESTION_ID", "QUESTION_TEXT", 
				"ANSWER_A", "ANSWER_A_ID", "ANSWER_B", "ANSWER_B_ID", "ANSWER_C", "ANSWER_C_ID", "ANSWER_D", "ANSWER_D_ID", 
				"CORRECT_ANSWER", "CORRECT_ANSWER_ID", "CHOSEN_ANSWER", "CHOSEN_ANSWER_ID", "TIME_TO_ANSWER");
		for(EvaluationSession session : sessions) {
			for(QuestionSet questionSet : session.getQuestionSets()) {
				for(Question question : questionSet.getQuestions()) {
					
					writeValue(writer, session.getId());
					if (questionSet.getType() == QuestionSetType.IB) {
						writeValue(writer, questionSet.getType());
					} else {
						writeValue(writer, questionSet.getType() + "[" + questionSet.getId() + "]");
					}
					writeValue(writer, questionSet.getName());
					
					writeValue(writer, question.getId());
					writeValue(writer, question.getText());
					
					for(Answer answer : question.getAnswers()) {
						writeValue(writer, answer.getText());
						writeValue(writer, answer.getId());
					}
					
					writeValue(writer, question.getCorrectAnswer().getText());
					writeValue(writer, question.getCorrectAnswer().getId());
					
					QuestionEvaluationResult evalResult = session.getAnsweredQuestions().get(question);
					if (evalResult != null) {
						writeValue(writer, evalResult.getChosenAnswer().getText());
						writeValue(writer, evalResult.getChosenAnswer().getId());
						writeValue(writer, evalResult.getTimeToAnswer());
					} else {
						writeValue(writer, null); // Answer Text
						writeValue(writer, null); // Answer ID
						writeValue(writer, null); // Answer time
					}
					
					closeRecord(writer);
				}
			}
		}
		
		return createDownload(writer);
	}
	
	private StreamedContent createDownload(StringWriter writer) {
		try {
			byte[] csv = writer.toString().getBytes("UTF-8");
			
			return new DefaultStreamedContent( new ByteArrayInputStream(csv) , "text/csv",
					"completeEvaluationData.csv");
		} catch (UnsupportedEncodingException ex) {
			log.error("Encoding error while creating csv");
			FacesContextHelper.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Encoding error during export");
			return null;
		}
	}
	
	private void writeValue(StringWriter writer, Object value) {
		if (value == null) {
			value = "";
		}
		if (value instanceof Integer) {
			writeField(writer, Integer.toString((Integer)value));
		} else {
			String strValue = value.toString();
			if (strValue.trim().length() == 0) {
				writeField(writer, "");
			} else {
				writeField(writer, quoteValue(strValue.trim()));
			}
		}
	}
	
	private void writeHeaders(StringWriter writer, String... headers) {
		for(String header : headers) {
			writeHeader(writer, header);
		}
		closeRecord(writer);
	}
	
	private void writeHeader(StringWriter writer, String header) {
		writeField(writer, quoteValue(header));
	}
	
	private void writeField(StringWriter writer, String value) {
		writer.write(value);
		writer.write(FIELDSEP);
	}
	
	private String quoteValue(String value) {
		return QUOTE + escapeValue(value) + QUOTE;
	}
	
	private String escapeValue(String value) {
		return value.replace(ESCAPE, ESCAPE + ESCAPE).replace(QUOTE, ESCAPE + QUOTE);
	}
	
	private void closeRecord(StringWriter writer) {
		StringBuffer buffer = writer.getBuffer();
		int bufferLength = buffer.length();
		if (bufferLength > 0) {
			if (FIELDSEP.equals(buffer.substring(bufferLength - FIELDSEP.length(), bufferLength))) {
				buffer.delete(bufferLength - FIELDSEP.length(), bufferLength);
			}
		}
		writer.write(RECORDSEP);
	}
}
