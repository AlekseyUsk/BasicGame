package com.bignerdranch.android.basicgame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bignerdranch.android.basicgame.databinding.FragmentResultBinding
import com.bignerdranch.android.basicgame.viewmodel.ResultViewModel
import com.bignerdranch.android.basicgame.viewmodel.factory.ResultViewModelFactory

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: ResultViewModel
    lateinit var viewModelFactory: ResultViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultBinding.inflate(inflater, container, false).apply {
            //ДОБАВИЛ ComposeView (предварительно в xml внес его)
            composeView.setContent {
                MaterialTheme {
                    Surface {
                        view?.let { ResultFragmentContent(view = it, viewModel = viewModel) }
                    }
                }
            }
        }

        val view = binding.root
        val result = ResultFragmentArgs.fromBundle(requireArguments()).result
        viewModelFactory = ResultViewModelFactory(result)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ResultViewModel::class.java)
        binding.resultViewModel = viewModel
        click(binding, view)
        return view
    }

    @Composable
    fun ResultFragmentContent(view: View, viewModel: ResultViewModel) {
//Todo место для кода составных элементов своего рода контейнер перед отправкой в onCreateView compose.setContent{}
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ResultViewModel(viewModel.result)
            NewGameButton {
                view.findNavController().navigate(R.id.action_resultFragment_to_gameFragment)
            }
        }
    }

    @Composable
    fun NewGameButton(clicked: () -> Unit) {
        Button(onClick = clicked) {
            Text("Start new game")
        }
    }

    @Composable
    fun ResultText(result: String) {
        Text(text = result, fontSize = 28.sp, textAlign = TextAlign.Center)
    }

    fun click(binding: FragmentResultBinding, view: View) {
        binding.newGameButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_resultFragment_to_gameFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}