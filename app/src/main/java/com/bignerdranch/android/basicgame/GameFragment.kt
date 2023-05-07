package com.bignerdranch.android.basicgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bignerdranch.android.basicgame.databinding.FragmentGameBinding
import com.bignerdranch.android.basicgame.viewmodel.GameViewModel
import com.bignerdranch.android.basicgame.viewmodel.ResultViewModel

class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameBinding.inflate(inflater, container, false).apply {
            composeView.setContent {
                MaterialTheme {
                    Surface {
                        GameFragmentContent(viewModel)
                    }
                }
            }
        }
        val view = binding.root
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        binding.gameViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        gameOver()
        click()
        return view
    }

    @Composable
    fun GameFragmentContent(viewModel : GameViewModel) {

        val guess = remember { mutableStateOf("") }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            EnterGuess(guess.value) { guess.value = it }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GuessButton {
                viewModel.makeGuess(guess.value.uppercase())
                guess.value = ""
            }
            FinishGameButton {
                viewModel.finishGame()
            }
        }
    }


    @Composable
    fun FinishGameButton(clicked: () -> Unit) {
        Button(onClick = clicked) {
            Text("Finish game")
        }
    }

    @Composable
    fun EnterGuess(guess: String, change: (String) -> Unit) {
        TextField(
            value = guess,
            label = { Text("Угадай букву") },
            onValueChange = change
        )
    }

    @Composable
    fun GuessButton(clicked: () -> Unit) {
        Button(onClick = clicked) {
            Text("Предпологаю!")
        }
    }


    private fun gameOver() {
        viewModel.gameOver.observe(viewLifecycleOwner, Observer { newValue ->
            if (newValue) {
                val action =
                    GameFragmentDirections.actionGameFragmentToResultFragment(viewModel.wonLostMessage())
                view?.findNavController()?.navigate(action)
            }
        })
    }

    private fun click() {
        binding.guessButton.setOnClickListener {
            viewModel.makeGuess(
                binding.guess.text.toString().uppercase()
            )
            binding.guess.text = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}