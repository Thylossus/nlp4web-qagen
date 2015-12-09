function readQuestions(){
  var questions = [];
  var sheet = SpreadsheetApp.openById("1sYiEtQxNLkCNJ335soV0gBS5KGhHYHo_Fpd-t72k7Ck");
  var lastRow = sheet.getLastRow();
  var rowsPerQuestion = 6;
  for(var i = 1; i < 100; i+=6) {
    var values = sheet.getSheetValues(i, 1, rowsPerQuestion,1);
    if(values.length == 6){
      var question = values[0];
      var answers = [values[1],values[2],values[3],values[4]];
      var correctAnswerId = values[5];
      questions.push({
        "question": question,
        "answers": answers,
        "correctAnswerId": correctAnswerId
      });
    }
  }
  return questions;
}

function readAnswers(){
  var answers = [];

  var sheet = SpreadsheetApp.openById("1bS0CEYrY6dRHWjD3ENeexJ8bgeXpMZ7cuTOdTKa4nnM");
  var lastColumn = sheet.getLastColumn();
  for(var i = 2; i < lastColumn; i++) {
    var values = sheet.getSheetValues(1, i, 2,1);
    if(values.length == 2){
      var question = values[0];
      var answer = values[1];
      answers.push({
        "question": question,
        "answer": answer
      });
    }
  }
  return answers;
}

function createForm(){
  var form = FormApp.create("Who wants to be a milionaire?");

  var questions = readQuestions();

  for (question in questions) {
     form.addMultipleChoiceItem()
         .setTitle(question.question)
         .setChoiceValues(question.answers)
         .setRequired(true)
  }

  Logger.log(form.getPublishedUrl())
}

function checkAnswers(){
  var questions = readQuestions();
  var answers = readAnswers();
  var correctAnswersCounter = 0;
  for (var i = 0; i < answers.length; i++){
    var question = questions[i];
    var answer = answers[i];
    var chosenAnswerId = -1;
    for(var j = 0; j < question.answers.length; j++){
      if(question.answers[j] == answer.answer){
        chosenAnswerId = j;
        break;
      } else {
        Logger.log(question.answers[j]);
        Logger.log(answer.answer);
        Logger.log("Could not find the answer!");
      }
    }
    Logger.log(chosenAnswerId);
    Logger.log(question.correctAnswerId);
    if(chosenAnswerId == question.correctAnswerId){
      correctAnswersCounter++;
    }
  }
  Logger.log(correctAnswersCounter);
}
