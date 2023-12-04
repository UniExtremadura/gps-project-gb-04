package es.unex.giss.asee.ghiblitrunk.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.unex.giss.asee.ghiblitrunk.databinding.ActivityFilterBinding

class FilterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //view binding and set content view
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load previous state of buttons
        loadFilterState()
        // Views initialization and listeners
        setUpListeners()
    }

    private fun setUpListeners() {
        with(binding) {
            btApply.setOnClickListener { SaveFilters() } // guardar la selección del usuario
            ibClose.setOnClickListener { finish() } // cierre del filtro
        }
    }

    private fun SaveFilters(){
        with(binding){
            /*
            val sharedPrefs = getSharedPreferences("Filters", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()

            val selectedCategories = mutableListOf<String>()

            // Almacenando la selección del usuario
            if (tbFilterTechnology.isChecked) {
                selectedCategories.add("technology")
            }
            if (tbFilterHealth.isChecked) {
                selectedCategories.add("health")
            }
            if (tbFilterScience.isChecked) {
                selectedCategories.add("science")
            }
            if (tbFilterGeneral.isChecked) {
                selectedCategories.add("general")
            }
            if (tbFilterEntertainment.isChecked) {
                selectedCategories.add("entertainment")
            }
            if (tbFilterSports.isChecked) {
                selectedCategories.add("sports")
            }
            if (tbFilterBusiness.isChecked) {
                selectedCategories.add("business")
            }

            // Guardado de la selección del usuario
            editor.putStringSet("selectedCategories", selectedCategories.toSet())

            editor.apply()

             */
        }
    }

    // Cargar la selección del usuario
    private fun loadFilterState() {
        /*
        val sharedPrefs = getSharedPreferences("Filters", Context.MODE_PRIVATE)
        val selectedCategories = sharedPrefs.getStringSet("selectedCategories", emptySet()) ?: emptySet()

        with(binding) {
            tbFilterTechnology.isChecked = "technology" in selectedCategories
            tbFilterHealth.isChecked = "health" in selectedCategories
            tbFilterScience.isChecked = "science" in selectedCategories
            tbFilterGeneral.isChecked = "general" in selectedCategories
            tbFilterEntertainment.isChecked = "entertainment" in selectedCategories
            tbFilterSports.isChecked = "sports" in selectedCategories
            tbFilterBusiness.isChecked = "business" in selectedCategories
        }

         */
    }
}