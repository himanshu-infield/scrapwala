package com.scrapwala.screens.pickups.category.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.scrapwala.databinding.ActivityCategoryBinding
import com.scrapwala.screens.pickups.category.adapter.CategoryAdapter
import com.scrapwala.screens.pickups.category.adapter.ClickedItemCallback
import com.scrapwala.screens.pickups.category.model.CategoryData
import com.scrapwala.screens.pickups.category.model.CategoryResponse
import com.scrapwala.screens.pickups.viewmodel.PickupViewModel
import com.scrapwala.utils.ErrorResponse
import com.scrapwala.utils.extensionclass.hideSpinner
import com.scrapwala.utils.extensionclass.showCustomToast
import com.scrapwala.utils.extensionclass.showSpinner
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CategoryActivity: AppCompatActivity() {

    private var data: ArrayList<CategoryResponse.Data?>?=null
    private var adapter: CategoryAdapter?=null
    private lateinit var binding: ActivityCategoryBinding


    private val viewModel: PickupViewModel by viewModels()
    private lateinit var categoryResponse: CategoryResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setUpToolbar()

        observeApiCategory()
        callApiCategoryList()
    }


    private fun observeApiCategory() {
        viewModel.responseCategory.observe(this as AppCompatActivity, Observer {
            when (it) {
                is CategoryResponse -> {
                  hideSpinner()
                    categoryResponse = it
                    if (categoryResponse.success==1 && categoryResponse.data.isNullOrEmpty().not()){
                        renderData(categoryResponse)
                    }
                }

                is ErrorResponse -> {
                   hideSpinner()
                    if (it.message.isNullOrEmpty().not()) {
                        showCustomToast(findViewById(android.R.id.content),this,it.message)
                    }
                }

                is String -> {
                    hideSpinner()
                    showCustomToast(findViewById(android.R.id.content),this,it)
                }
            }

        })
    }

    private fun renderData(it: CategoryResponse) {

        val newExpert: CategoryResponse.Data =
            CategoryResponse.Data("Metal","",101,"aa",1,"s","ss",101,111,"22",11)
        val mutableExperts = categoryResponse.data?.toMutableList()
        mutableExperts?.add(newExpert)
        categoryResponse.data = mutableExperts?.toList()


        val categories = categoryResponse.data
        val sortedCategories = categories?.sortedBy { it?.category }

        data = ArrayList(sortedCategories)

        setRecyclerAdapter(sortedCategories)
    }

    private fun callApiCategoryList() {
        showSpinner(this@CategoryActivity)
        viewModel.getCategoryList()
    }




    private fun setUpToolbar() {
        binding.toolbar.toolbarTitle.setText("Waste Category")


        binding.toolbar.imgBack.setOnClickListener {
            this.finish()
        }


        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Called to notify you that the characters within s[start, start+count] are about to be replaced.
                // Prepare for changes if needed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Called to notify you that somewhere within s, the text has been replaced with new text.
                // React to the changes immediately.
            }

            override fun afterTextChanged(s: Editable?) {

                if(s.toString().isNullOrEmpty().not()&&s.toString().length>1){

                    if(data.isNullOrEmpty().not()){
                     var filterData=   data?.filter {
                            it?.name!!.contains(s.toString(),true)
                        }



                        if(filterData.isNullOrEmpty().not()){
                            if(adapter!=null){
                                adapter?.filterList(filterData as ArrayList<CategoryResponse.Data>,true)
                            }
                        }
                        else{
                            if(adapter!=null){
                                adapter?.filterList(ArrayList<CategoryResponse.Data>(),false)
                            }
                        }
                    }

                }

                else{
                    if(adapter!=null){
                        adapter?.filterList(data as ArrayList<CategoryResponse.Data>,false)
                    }
                }
            }
        })
    }

    private fun setRecyclerAdapter(sortedCategories: List<CategoryResponse.Data?>?) {
        var layoutManager = LinearLayoutManager(this)

        adapter=CategoryAdapter(this,sortedCategories!!,object:ClickedItemCallback{
            override fun clickedItem(position: Int, item: String) {

                var intent=Intent()

                intent.putExtra("clickedItem",item)

                setResult(100,intent)
                finish()

            }

        })

        binding.recyclerVw.layoutManager=layoutManager

        binding.recyclerVw.adapter=adapter

    }


}