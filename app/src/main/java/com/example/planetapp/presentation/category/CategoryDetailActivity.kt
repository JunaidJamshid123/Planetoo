package com.example.planetapp.presentation.category

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.planetapp.databinding.ActivityCategoryDetailBinding

class CategoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryDetailBinding

    companion object {
        const val EXTRA_CATEGORY_ID = "extra_category_id"
        const val EXTRA_CATEGORY_TITLE = "extra_category_title"
        const val EXTRA_CATEGORY_IMAGE = "extra_category_image"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoryId = intent.getIntExtra(EXTRA_CATEGORY_ID, 0)
        val categoryTitle = intent.getStringExtra(EXTRA_CATEGORY_TITLE) ?: "Category"
        val categoryImage = intent.getIntExtra(EXTRA_CATEGORY_IMAGE, 0)

        setupUI(categoryTitle, categoryImage)
    }

    private fun setupUI(title: String, imageRes: Int) {
        binding.apply {
            categoryTitle.text = title
            if (imageRes != 0) {
                categoryImage.setImageResource(imageRes)
            }
            
            // Back button
            backButton.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }
}
