package com.example.projekt

data class Question(
    val text: String,
    val ID: String
)

data class Answer(
    val ID: String,
    val _QuestionID: String,
    val text: String,
    val correct: String
)