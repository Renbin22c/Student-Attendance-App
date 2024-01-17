package com.renbin.student_attendance_app.ui.screens.quizPage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.data.model.Quiz
import com.renbin.student_attendance_app.data.model.QuizQuestions
import com.renbin.student_attendance_app.databinding.FragmentQuizPageBinding
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.quizPage.viewModel.QuizPageViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuizPageFragment : BaseFragment<FragmentQuizPageBinding>() {

    override val viewModel: QuizPageViewModelImpl by viewModels()
    private val args: QuizPageFragmentArgs by navArgs()

    private var result = 0
    private var currentIndex = 0

    private val myQuestions = mutableListOf<QuizQuestions>()
    private var selectedAnswer = ""
    private var correctAnswer = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuizPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Disable the back button
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Handle the back button press as needed (e.g., do nothing)
             fun onBackPressed() {
                // Clear the back stack
                navController.popBackStack(R.id.studentJoinQuizPageFragment, false)

                // Do nothing or handle it based on your requirements
                // To completely disable the back button, you can leave this block empty
            }
        }
    }


    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)

        viewModel.getQuiz(args.quizId)


        binding.btnNext.setOnClickListener {
            handleAnswerSelection()
            handleQuizProgress()
        }

        binding.btnFinish.setOnClickListener {
            val action = QuizPageFragmentDirections.actionQuizPageFragmentToStudentTabContainerFragment()
            navController.navigate(action)
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.remainingTime.collect { remainingTime ->
                if (remainingTime == "00:00") {
                    handleTimerEnd()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.quiz.collect { quiz ->
                if (quiz.timer > 0L) {
                    viewModel.startCountdownTimer(quiz.timer)
                }
                myQuestions.clear()
                myQuestions.addAll(createQuizQuestions(quiz))
                if (currentIndex < myQuestions.size) {
                    nextPage(myQuestions[currentIndex])
                }
            }
        }

        lifecycleScope.launch {
            viewModel.remainingTime.collect { remainingTime ->
                binding.tvTimer.text = remainingTime
            }
        }
    }

    private fun handleAnswerSelection() {
        val selectedRadioButton = binding.rgOptions.findViewById<RadioButton>(
            binding.rgOptions.checkedRadioButtonId
        )
        selectedAnswer = selectedRadioButton?.text?.toString() ?: ""
        correctAnswer = myQuestions[currentIndex].correctAnswer
        if (selectedAnswer == correctAnswer) {
            result += 1
        }
    }

    private fun resetRadioButtons() {
        binding.rgOptions.clearCheck()
    }

    private fun handleQuizProgress() {
        currentIndex++
        if (currentIndex < myQuestions.size) {
            nextPage(myQuestions[currentIndex])
            resetRadioButtons()
        } else {
            showResult()
        }
    }

    private fun updateResultView(message: String) {
        binding.run {
            llQuestions.visibility = View.GONE
            constraintLayout2.visibility = View.GONE
            llResult.visibility = View.VISIBLE
            btnNext.visibility = View.GONE
            val resultText = "$message$result/${myQuestions.size}"
            tvResult.text = resultText
            viewModel.addResult(result.toString(), args.quizId)

        }
    }

    private fun showResult() {
        val resultText = getString(R.string.score_message)
        updateResultView(resultText)
    }


    private fun handleTimerEnd() {
        val timeoutText = getString(R.string.timeout_message)
        updateResultView(timeoutText)
        binding.tvResult.setTextColor(ContextCompat.getColor(requireContext(), R.color.error))
    }

    private fun createQuizQuestions(quiz: Quiz): List<QuizQuestions> {
        return quiz.titles.indices.map { i ->
            QuizQuestions(
                question = quiz.titles[i],
                option1 = quiz.options[4 * i],
                option2 = quiz.options[4 * i + 1],
                option3 = quiz.options[4 * i + 2],
                option4 = quiz.options[4 * i + 3],
                correctAnswer = quiz.answers.getOrNull(i) ?: ""
            )
        }
    }

    private fun nextPage(questions: QuizQuestions) {
        binding.run {
            val questionTitle = questions.question
            tvQuestion.text = questionTitle
            option1.text = questions.option1
            option2.text = questions.option2
            option3.text = questions.option3
            option4.text = questions.option4
        }
    }
}
