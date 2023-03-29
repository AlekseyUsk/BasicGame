package com.bignerdranch.android.basicgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bignerdranch.android.basicgame.databinding.FragmentGameBinding
import com.bignerdranch.android.basicgame.viewmodel.GameViewModel

class GameFragment : Fragment() {

    lateinit var viewModel: GameViewModel
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        updateScreen()

        binding.guessButton.setOnClickListener {
            viewModel.makeGuess(binding.guess.text.toString().uppercase())  // пользователь сделал догадку чтобы разобраться с предположением
            binding.guess.text = null                             // сбросить текст редактирования
            updateScreen()                                        // обновить экран
            if (viewModel.isWon() || viewModel.isLost()) {       // если пользователь выиграл или проиграл перейдите к ResultFragment передав значение сообщения
                val action = GameFragmentDirections.actionGameFragmentToResultFragment(viewModel.wonLostMessage())
                view.findNavController().navigate(action)
            }
        }
        return view
    }

    fun updateScreen() {                                         // установите текстовые представления макета
        binding.word.text = viewModel.secretWordDisplay
        binding.lives.text = "у вас ${viewModel.livesLeft} осталось жизней"
        binding.incorrectGuesses.text = "неверные догадки : ${viewModel.incorrectGuesses}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}