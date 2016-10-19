package legal.documentassembly;

import java.text.SimpleDateFormat;

import javax.lang.model.util.SimpleAnnotationValueVisitor6;

import legal.documentassembly.bean.Exercise;
import legal.documentassembly.bean.Step;

public class ExerciseEngine {

	private Exercise exercise = null;
	private String exercise_filename;
	private int currentStepIndex;

	
	public ExerciseEngine(String exercise_filename) {
		this.exercise_filename = exercise_filename;
		exercise = Exercise.load(exercise_filename);
		currentStepIndex = 0;
	}
	
	public void startReasoner() {
		ReasonerUtil.getConclusion(exercise);
	}
	
	public void exportArgumentGraph() {
		ArgumentGraphUtil.buildArgumentGraph();
	}

	public void exportDocument() {
		TemplateUtil.buildDocument(exercise);
	}
	
	public boolean hasNextStep() {
		return currentStepIndex + 1 < exercise.getSteps().size();
	}

	public Step getCurrentStep() {
		return exercise.getSteps().get(currentStepIndex);
	}
/*
	// next 3 methods requires -1 as currentStepIndex initial value
	public Step getNextStep() {
		if (hasNextStep())
			return exercise.getSteps().get(++currentStepIndex);
		return null;
	}
	
	public Step getPreviousStep() {
		if (currentStepIndex > 0)
			return exercise.getSteps().get(--currentStepIndex);
		return null;
	}

	public Step getPreviousStep(int index) {
		if (index >= 0 && index < currentStepIndex) {
			currentStepIndex = index;
			return exercise.getSteps().get(currentStepIndex);
		}
		return null;
	}
*/
	public boolean stepForward() {
		currentStepIndex++;
		if (endOfExercise())
			return false;
		return true;
	}

	public boolean stepBackward() {
		if (currentStepIndex > 0) {
			currentStepIndex--;
			return true;
		}
		return false;
	}
	
	public boolean endOfExercise() {
		return currentStepIndex >= exercise.getSteps().size();
	}
	
	public void assignAnswer(String answer) throws Exception {
		Step currentStep = exercise.getSteps().get(currentStepIndex);
		String answerType = currentStep.getAnswerType();
		if ("int".equals(answerType)) {
			try {
				Integer.parseInt(answer);
			} catch (Exception e) {
				throw new Exception("Wrong format, expected integer");
			}
		} else if ("float".equals(answerType)) {
			try {
				Float.parseFloat(answer);
			} catch (Exception e) {
				throw new Exception("Wrong format, expected float");
			}
		} else if ("date".equals(answerType)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.");
				sdf.parse(answer);
			} catch (Exception e) {
				throw new Exception("Wrong format, expected dd.mm.yyyy.");
			}
		} else if ("time".equals(answerType)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				sdf.parse(answer);
			} catch (Exception e) {
				throw new Exception("Wrong format, expected hh:mm");
			}
		}
		currentStep.setAnswer(answer);
	}
	
}
