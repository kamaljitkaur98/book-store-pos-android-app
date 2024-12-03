package com.example.bookstoreposapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.content.Intent
import com.example.bookstoreposapp.API.add.AddBookActivity
import com.example.bookstoreposapp.MainActivity
import com.example.bookstoreposapp.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NavFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NavFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_nav, container, false)
        val addButton: LinearLayout = view.findViewById(R.id.addButton)
        addButton.setOnClickListener(this)
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NavFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NavFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.addButton -> {
                val intent = Intent(requireContext(), AddBookActivity::class.java)
                startActivity(intent)
            }
            R.id.homeButton -> {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}