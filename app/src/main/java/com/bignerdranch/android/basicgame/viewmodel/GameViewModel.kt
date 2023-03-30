package com.bignerdranch.android.basicgame.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private val words = listOf("Android", "Activity", "Fragment")
    private val secretWord = words.random().uppercase()

    private var correctGuesses = ""

    private val _gameOver = MutableLiveData<Boolean>()
    val gameOver : LiveData<Boolean>
        get() = _gameOver

    private val _secretWordDisplay = MutableLiveData<String>()
    val secretWordDisplay: LiveData<String>
        get() = _secretWordDisplay

    private val _incorrectGuesses = MutableLiveData<String>("")
    val incorrectGuesses: LiveData<String>
        get() = _incorrectGuesses

    private val _livesLeft = MutableLiveData<Int>(3)
    val livesLeft: LiveData<Int>
        get() = _livesLeft

    init {
        _secretWordDisplay.value =
            deriveSecretWorldDisplay()
    }

   private fun deriveSecretWorldDisplay(): String {
        var display = ""
        secretWord.forEach {
            display += checkLetter(it.toString())
        }
        return display
    }

   private fun checkLetter(str: String) =
        when (correctGuesses.contains(str)) {
            true -> str
            false -> ""
        }

    fun makeGuess(guess: String) {
        if (guess.length == 1) {
            if (secretWord.contains(guess)) {
                correctGuesses += guess
                _secretWordDisplay.value = deriveSecretWorldDisplay()
            } else {
                _incorrectGuesses.value += "$guess"
                _livesLeft.value = livesLeft.value?.minus(1)
            }
            if (isWon() || isLost()) _gameOver.value = true
        }
    }

   private fun isWon() = secretWord.equals(
        secretWordDisplay.toString(),
        true
    )

   private fun isLost() =
        livesLeft.value ?: 0 <= 0

    fun wonLostMessage(): String {
        var message = ""
        if (isWon()) message = "Winner Congratulates! "
        else if (isLost()) message = "Game over! "
        message += "Слово было $secretWord"
        return message
    }

    override fun onCleared() {
        super.onCleared()
    }
}