package com.jahanbabu.deskerademo.ui.fruitView.fruitDetail

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jahanbabu.deskerademo.R
import com.jahanbabu.deskerademo.data.Fruit

class FruitDetailsFragment : Fragment(), View.OnClickListener, FruitDetailsContract.View {

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.primaryTextView -> activity!!.finish()
            R.id.secondaryTextView -> {
                if (isEditing){
                    presenter.onSaveClick(fruitId, etTableItem!!.text.toString())
                    activity!!.finish()
                } else {
                    etTableItem!!.requestFocus()
                    etTableItem!!.setSelection(etTableItem!!.text.length)
                }

            }
        }
    }

    override val isActive: Boolean
        get() = isAdded


    override fun setDataToView(fruits: Fruit) {

    }

    override lateinit var presenter: FruitDetailsContract.Presenter
    private var toolbarTitleTextView: TextView? = null
    private var mtvPrimary: TextView? = null
    private var mtvSecondary: TextView? = null
    private var etTableItem: EditText? = null
    var fruitId = 0
    var name = ""
    var isEditing = false

    companion object {
        private val TAG = FruitDetailsFragment::class.java.name

        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        fun newInstance(param1: Int, param2: String): FruitDetailsFragment {
            val fragment = FruitDetailsFragment()
            val args = Bundle()
            args.putInt(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fruitId = arguments!!.getInt(ARG_PARAM1)
        name = arguments!!.getString(ARG_PARAM2)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_table_item_details, container, false)

        initViews(view)

        setupToolbar()

        return view
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    private fun initViews(view: View) {
        toolbarTitleTextView = view.findViewById(R.id.toolbar_title)
        toolbarTitleTextView!!.setText("")

        mtvPrimary = view.findViewById(R.id.primaryTextView)
        mtvSecondary = view.findViewById(R.id.secondaryTextView)
        etTableItem = view.findViewById(R.id.etTableItem)

        etTableItem!!.clearFocus()

        etTableItem!!.setText(name)

        mtvPrimary!!.setOnClickListener(this)
        mtvSecondary!!.setOnClickListener(this)
        etTableItem!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                isEditing = true
                mtvSecondary!!.setText(R.string.label_save)
            }

            override fun afterTextChanged(s: Editable) {}
        })

    }

    private fun setupToolbar() {
        mtvSecondary!!.setVisibility(View.VISIBLE)
        mtvPrimary!!.setVisibility(View.VISIBLE)
        mtvPrimary!!.setText(R.string.back)
        mtvSecondary!!.setText(R.string.edit)
    }
}
