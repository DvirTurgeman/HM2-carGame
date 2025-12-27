package com.guy.class26a_and_3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.guy.class26a_and_3.databinding.FragmentScoresListBinding

class ScoresListFragment : Fragment() {

    private var _binding: FragmentScoresListBinding? = null
    private val binding get() = _binding!!
    private lateinit var scoresManager: ScoresManager
    private lateinit var scoresAdapter: ScoresAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScoresListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scoresManager = ScoresManager(requireContext())
        val scores = scoresManager.getScores()

        scoresAdapter = ScoresAdapter(scores) { score ->
            (activity as? ScoresActivity)?.onScoreClicked(score)
        }

        binding.scoresRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = scoresAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}