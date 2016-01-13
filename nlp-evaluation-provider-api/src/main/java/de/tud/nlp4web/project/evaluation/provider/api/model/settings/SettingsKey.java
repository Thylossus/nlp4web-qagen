package de.tud.nlp4web.project.evaluation.provider.api.model.settings;

public enum SettingsKey {

	/** Amount of question sets within an evaluation session */
	EVALUATION_SESSION_SETS_AMOUNT,
	
	/** Amount of evaluations per difficulty baseline that should be done before the real evaluation starts */
	EVALUATION_EVALS_PER_DB,

	/** Time before a evaluation session is regarded as timeout session, in seconds */
	EVALUATION_TIMEOUT_LIMIT;
}
