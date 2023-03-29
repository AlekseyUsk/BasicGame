package com.bignerdranch.android.basicgame.viewmodel

import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    val words = listOf("Android", "Activity", "Fragment")
    val secretWord = words.random().uppercase()                    // слово которое пользователь должен угадать
    var secretWordDisplay = ""                                     // как отображается это слово
    var correctGuesses = ""                                        // правильно
    var incorrectGuesses = ""                                      // не правильно
    var livesLeft = 2                                              // жизни

    init {
        secretWordDisplay = deriveSecretWorldDisplay()            // определите, как должно отобрадаться секретное слово и обновите экран
    }

    fun deriveSecretWorldDisplay(): String {                     // это создает строку для того, как секретное слово должно отображаться на экране
        var display = ""
        secretWord.forEach {
            display += checkLetter(it.toString())                //Вызовите check Letter для каждой буквы в секретном слове
        }                                                        // и добавьте ее возвращаемое значение в конец отображаемой переменной
        return display
    }

    fun checkLetter(str: String) = when (correctGuesses.contains(str)) { //Это проверяет, содержит ли секретное слово букву, которую угадал пользователь,
        true -> str                                                      // если да, то возвращает букву. Если нет, то он возвращает "_"
        false -> ""
    }

    fun makeGuess(guess: String) {                              //Это вызывается каждый раз, когда пользователь делает предположение
        if (guess.length == 1) {
            if (secretWord.contains(guess)) {      //Для каждого правильного предположения обновляйте правильные догадки и отображайте секретное слово
                correctGuesses += guess
                secretWordDisplay = deriveSecretWorldDisplay()
            } else {                                //За каждое неверное предположение обновляйте неверные догадки и оставляйте их в живых
                incorrectGuesses += "$guess"
                livesLeft--
            }
        }
    }

    fun isWon() = secretWord.equals(secretWordDisplay, true) //Игра считается выигранной, если секретный мир соответствует отображаемому секретному слову
    fun isLost() = livesLeft <= 0                    //Игра проигрывается, когда у пользователя заканчиваются жизни

    fun wonLostMessage() : String{
        var message = ""
        if (isWon()) message = "Winner Congratulates! "
        else if (isLost()) message = "Game over! "
        message += "Слово было $secretWord"
        return message         //wonLostMessage() возвращает строку о том, выиграл ли пользователь и каким было секретное слово
    }

    override fun onCleared() {
        super.onCleared()
    }
}