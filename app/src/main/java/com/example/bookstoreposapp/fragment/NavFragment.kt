package com.example.bookstoreposapp.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.example.bookstoreposapp.API.add.AddBookActivity
import com.example.bookstoreposapp.CartActivity
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
    private lateinit var addButton: LinearLayout
    private lateinit var cartButton: LinearLayout
    private lateinit var homeButton: LinearLayout
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
        try {
            val view : View = inflater . inflate (R.layout.fragment_nav, container, false)
            addButton= view.findViewById(R.id.addButton)
            cartButton = view.findViewById(R.id.cartButton)
            homeButton= view.findViewById(R.id.homeButton)
            addButton.setOnClickListener(this)
            cartButton.setOnClickListener(this)
            homeButton.setOnClickListener(this)
            restoreHighlight(view.context)
            return view
        } catch (e: Exception) {
            Log.e("NavFragment", "Error inflating layout: ${e.message}")
            throw e
        }
    }

    companion object {
        const val SELECTED_BUTTON_KEY = "selected_button"
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

    private fun highlightButton(selectedButton: LinearLayout) {
        // Reset backgrounds for all buttons
        homeButton.setBackgroundResource(android.R.color.transparent)
        cartButton.setBackgroundResource(android.R.color.transparent)
        addButton.setBackgroundResource(android.R.color.transparent)

        // Set the highlight background for the selected button
        selectedButton.setBackgroundResource(R.drawable.button_highlight_background)
        saveSelectedButton(selectedButton.id)
    }

    private fun saveSelectedButton(selectedButtonId: Int) {
        val sharedPref = requireContext().getSharedPreferences("NavFragmentPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt(SELECTED_BUTTON_KEY, selectedButtonId)
            apply()
        }
    }
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.addButton -> {
                highlightButton(addButton)
                if (requireActivity() !is AddBookActivity) {
                    val intent = Intent(requireContext(), AddBookActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
            R.id.homeButton -> {
                highlightButton(homeButton)
                if (requireActivity() !is MainActivity) {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
            R.id.cartButton -> {
                highlightButton(cartButton)
                if (requireActivity() !is CartActivity) {
                    val intent = Intent(requireContext(), CartActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
        }
    }

    private fun restoreHighlight(context: Context) {
        val sharedPref = context.getSharedPreferences("NavFragmentPrefs", Context.MODE_PRIVATE)
        val selectedButtonId = sharedPref.getInt(SELECTED_BUTTON_KEY, R.id.homeButton)

        when (selectedButtonId) {
            R.id.addButton -> highlightButton(addButton)
            R.id.cartButton -> highlightButton(cartButton)
            R.id.homeButton -> highlightButton(homeButton)
        }
    }




}
