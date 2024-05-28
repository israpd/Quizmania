package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionRepo {
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private Connection connection;
    private Quiz quiz;
    public static final String UPDATE_QUESTION_LIST = "update_question_list";

    public QuestionRepo(Quiz quiz, Connection connection) {
        this.connection = connection;
        this.quiz = quiz;


    }
    public void addNewQuestions(int quizId, String questionsText /*List<Options> option*/) {
        String insertQuery = "INSERT INTO public.question(questions, quiz_id) VALUES (?,?)"; // add to database
        try {
            //vad är return genereated keys
            PreparedStatement statement= connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, questionsText);
            statement.setInt(2, quizId);
            int rowCount = statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int questionsId = generatedKeys.getInt(1);
                String insertOptionQuery = "INSERT INTO public.options (questions id, options text, is_correct) VALUES (?, ?, ?)"; // add to database
                /*for (Options options : option) {
                    statement = connection.prepareStatement(insertOptionQuery);
                    statement.setInt(1, questionsId);
                    statement.setString(2, options.getText());
                    statement.setBoolean(3, options.isCorrect());
                    rowCount = statement.executeUpdate();
                }*/
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addNewAnswers(int questionId){} //for loop? or just seperate for each answer?


    public ArrayList<Questions> getQuestions(int selectedQuizId) {
        ArrayList<Questions> questions = new ArrayList<>();
        String selectQuizData = "SELECT * FROM public.question\n" + "WHERE quiz_id = " + selectedQuizId;
        try {
            PreparedStatement statement = connection.prepareStatement(selectQuizData);
            //statement.setInt(1, selectedQuizId);
            ResultSet resultSet= statement.executeQuery();
            Questions currentQuestion = null;
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                if (currentQuestion == null || currentQuestion.getId() != id) {
                    currentQuestion = new Questions(id, resultSet.getString("question_text"), selectedQuizId); // name in database
                    questions.add(currentQuestion);
                }
                int optionsId = resultSet.getInt("options_id"); // name in database
                String optionsText = resultSet.getString("options_text"); // name in database
                boolean isCorrect = resultSet.getBoolean("is_correct");
                Options options = new Options(optionsId, optionsText,isCorrect);
                currentQuestion.getOptions().add(options);

                /*String question = resultSet.getString("question");
                int quizId = resultSet.getInt("quiz_set_id");
                Quiz quiz = new Quiz(id, question, quizId);
                questions.add(quiz);*/
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return questions;
    }

    public void subscribeListener (PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void unsubscribeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }


}

