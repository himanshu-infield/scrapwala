package com.scrapwala.screens.pickups.category.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.scrapwala.databinding.ActivityCategoryBinding
import com.scrapwala.screens.pickups.category.adapter.CategoryAdapter
import com.scrapwala.screens.pickups.category.adapter.ClickedItemCallback
import com.scrapwala.screens.pickups.category.model.CategoryData
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CategoryActivity: AppCompatActivity() {

    private var data: ArrayList<CategoryData>?=null
    private var adapter: CategoryAdapter?=null
    private lateinit var binding: ActivityCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCategoryBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setRecyclerAdapter()

        setUpToolbar()

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

                if(s.toString().isNullOrEmpty().not()&&s.toString().length>2){

                    if(data.isNullOrEmpty().not()){
                     var filterData=   data?.filter {
                            it.itemName!!.contains(s.toString(),true)
                        }



                        if(filterData.isNullOrEmpty().not()){
                            if(adapter!=null){
                                adapter?.filterList(filterData as ArrayList<CategoryData>,true)
                            }
                        }
                        else{
                            if(adapter!=null){
                                adapter?.filterList(ArrayList<CategoryData>(),false)
                            }
                        }
                    }

                }

                else{
                    if(adapter!=null){
                        adapter?.filterList(data as ArrayList<CategoryData>,false)
                    }
                }
            }
        })
    }

    private fun setRecyclerAdapter() {
        var layoutManager = LinearLayoutManager(this)



         data= arrayListOf<CategoryData>()


        for(i in 0 until 10){
            var categoryData=CategoryData()

            categoryData.categoryName="Paper"
            categoryData.itemName="White Paper"
            categoryData.itemPrice="₹12/Kg"
            data?.add(categoryData)
        }


        for(i in 0 until 10){
            var categoryData=CategoryData()

            categoryData.categoryName="Glass"
            categoryData.itemName="Glass"
            categoryData.itemPrice="₹16/Kg"
            data?.add(categoryData)
        }



        adapter=CategoryAdapter(this,data!!,object:ClickedItemCallback{
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