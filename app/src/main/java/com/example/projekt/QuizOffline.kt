package com.example.projekt
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class QuizOffline : AppCompatActivity() {
    private lateinit var quizWeiterOnline: Button
    private lateinit var buttonAnswer1Online: ToggleButton
    private lateinit var buttonAnswer2Online: ToggleButton
    private lateinit var buttonAnswer3Online: ToggleButton
    private lateinit var buttonAnswer4Online: ToggleButton
    private lateinit var textViewFrageQuiz: TextView
    private lateinit var playerName: TextView
    private var questionNumber = 0
    private var questionID: String = ""
    private var clickedAnswerID = "0"
    private lateinit var questionText: String
    private var answer1Text: String? = null
    private lateinit var answer2Text: String
    private lateinit var answer3Text: String
    private lateinit var answer4Text: String
    private var questionsChosen = mutableListOf<String>()
    private var matchedAnswers = DataStore.answers.filter { it._QuestionID == questionID }
    private var matchAnswersText = matchedAnswers.map { it.text }
    private var matchAnswersId = matchedAnswers.map { it.ID }
    private var choosenanswer = matchedAnswers.filter { clickedAnswerID == it.ID }
    private var correctAnswer = matchedAnswers.filter { it.correct == "true" }
    private var toggleButtonClicked = 0
    private val buttonTintMap = mapOf(
        1 to R.color.highlightedGrey,
        2 to R.color.grey,
        3 to R.color.grey,
        4 to R.color.grey
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz)
        init()
    }

    private fun init() {
        DataStore.stage = 3
        quizWeiterOnline = findViewById(R.id.quizWeiterOnline)
        buttonAnswer1Online = findViewById(R.id.buttonAnswer1Online)
        buttonAnswer2Online = findViewById(R.id.buttonAnswer2Online)
        buttonAnswer3Online = findViewById(R.id.buttonAnswer3Online)
        buttonAnswer4Online = findViewById(R.id.buttonAnswer4Online)
        textViewFrageQuiz = findViewById(R.id.textViewFrageQuiz)
        playerName = findViewById(R.id.playerName)
        playerName.text = DataStore.playerName1

        nextQuestion()
        quizWeiterOnline.setOnClickListener {
            if (toggleButtonClicked == 0) {
                DataStore.chosenPopout = mutableListOf(getString(R.string.no_answer), getString(R.string.no_answer_explained))
               DataStore.popout(this)
            } else {
                nextQuestion()
            }
        }
        buttonAnswer1Online.setOnClickListener {
            buttonClicked(1)
        }
        buttonAnswer2Online.setOnClickListener {
            buttonClicked(2)
        }
        buttonAnswer3Online.setOnClickListener {
            buttonClicked(3)
        }
        buttonAnswer4Online.setOnClickListener {
            buttonClicked(4)
        }
    }

    private fun nextQuestion() {
        toggleButtonClicked = 0
        changeToggleButtonStyle()
        if (questionNumber == DataStore.questionCount * 2) {
            addPoints()
            DataStore.player1OR2 = true
            startActivity(Intent(this, BoardOffline::class.java))
        } else if (questionNumber != 0) {
            addPoints()
            selectQuestion()
            toggleButtonClicked = 0
        } else {
            selectQuestion()
        }
    }

    private fun addPoints() {
        if (choosenanswer == correctAnswer) {
            if (questionNumber == DataStore.questionCount + 1) {
                DataStore.player1OR2 = false
            } else if (questionNumber == DataStore.questionCount) {
                playerName.text = DataStore.playerName2
            }
            if (DataStore.player1OR2) {
                DataStore.currentPoints1++
            } else {
                DataStore.currentPoints2++
            }
        }
    }

    private fun selectQuestion() {
        setNewRandomQuestion()
        setTextEtcToChosenQuestio()
        questionNumber++
    }

    private fun setNewRandomQuestion() {
        questionID = DataStore.questions[DataStore.questionsPicked[questionNumber]].ID
        questionText = DataStore.questions[DataStore.questionsPicked[questionNumber]].text
        textViewFrageQuiz.text = questionText
    }

    private fun setTextEtcToChosenQuestio() {
        matchedAnswers = DataStore.answers.filter { it._QuestionID == questionID }
        matchAnswersText = matchedAnswers.map { it.text }
        matchAnswersId = matchedAnswers.map { it.ID }
        choosenanswer = matchedAnswers.filter { clickedAnswerID == it.ID }
        correctAnswer = matchedAnswers.filter { it.correct == "true" }
        questionsChosen.add(questionID)

        answer1Text = matchAnswersText[0]
        answer2Text = matchAnswersText[1]
        answer3Text = matchAnswersText[2]
        answer4Text = matchAnswersText[3]

        buttonAnswer1Online.text = answer1Text
        buttonAnswer2Online.text = answer2Text
        buttonAnswer3Online.text = answer3Text
        buttonAnswer4Online.text = answer4Text
    }

    private fun buttonClicked(buttonNumber: Int) {
        toggleButtonClicked = buttonNumber
        changeToggleButtonStyle(toggleButtonClicked)
        clickedAnswerID = matchAnswersId[buttonNumber - 1]
        choosenanswer = matchedAnswers.filter { clickedAnswerID == it.ID && it._QuestionID == questionID }
    }

    private fun changeToggleButtonStyle(toggleButtonClicked: Int? = null) {
        buttonTintMap.forEach { (buttonNumber, colorResId) ->
            val button = when (buttonNumber) {
                1 -> buttonAnswer1Online
                2 -> buttonAnswer2Online
                3 -> buttonAnswer3Online
                4 -> buttonAnswer4Online
                else -> null
            }
            val color = if (buttonNumber == toggleButtonClicked) {
                colorResId
            } else {
                R.color.grey
            }
            button?.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(this, color)
            )
        }
    }

}
