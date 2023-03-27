package com.bignerdranch.android.basicgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bignerdranch.android.basicgame.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    val words = listOf("Android", "Activity", "Fragment")
    val secretWord = words.random().uppercase()                    // слово которое пользователь должен угадать
    var secretWordDisplay = ""                                     // как отображается это слово
    var correctGuesses = ""                                        // правильно
    var incorrectGuesses = ""                                      // не правильно
    var livesLeft = 8                                              // жизни

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        val view = binding.root

        secretWordDisplay = deriveSecretWorldDisplay()            // определите, как должно отобрадаться секретное слово и обновите экран
        updateScreen()

        binding.guessButton.setOnClickListener {
            makeGuess(binding.guess.text.toString().uppercase())  // пользователь сделал догадку чтобы разобраться с предположением
            binding.guess.text = null                             // сбросить текст редактирования
            updateScreen()                                        // обновить экран
            if (isWon() || isLost()) {                            // если пользователь выиграл или проиграл перейдите к ResultFragment передав значение сообщения
                val action = GameFragmentDirections.actionGameFragmentToResultFragment(wonLostMessage())
                view.findNavController().navigate(action)
            }
        }
        return view
    }

    fun updateScreen() {                                         // установите текстовые представления макета
        binding.word.text = secretWordDisplay
        binding.lives.text = "у вас $livesLeft осталось жизней"
        binding.incorrectGuesses.text = "неверные догадки : $incorrectGuesses"

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
        if (isWon()) message = "ТЫ ВЫИГРАЛ"
        else if (isLost()) message = "ТЫ ПРОИГРАЛ"
        message += "Слово было $secretWord"
        return message         //wonLostMessage() возвращает строку о том, выиграл ли пользователь и каким было секретное слово
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}