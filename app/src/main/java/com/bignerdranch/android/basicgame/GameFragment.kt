package com.bignerdranch.android.basicgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
    observationOfTheChange()
    click()
        return view
    }
//наблюдение и обновление value у своих View
    private fun observationOfTheChange(){
        viewModel.incorrectGuesses.observe(viewLifecycleOwner, Observer { newValue ->
            binding.incorrectGuesses.text = "incorrectGuesses - неверные догадки $newValue"
        })
        viewModel.livesLeft.observe(viewLifecycleOwner, Observer { newValue ->
            binding.lives.text = "У вас есть $newValue жизни осталось"
        })
        viewModel.secretWordDisplay.observe(viewLifecycleOwner, Observer { newValue ->
            binding.word.text = newValue
        })
        viewModel.gameOver.observe(viewLifecycleOwner, Observer { newValue ->
            if (newValue){
                val action = GameFragmentDirections.actionGameFragmentToResultFragment(viewModel.wonLostMessage())
                view?.findNavController()?.navigate(action)
            }
        })
    }

    private fun click(){
        binding.guessButton.setOnClickListener {
            viewModel.makeGuess(
                binding.guess.text.toString().uppercase())
            binding.guess.text = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}