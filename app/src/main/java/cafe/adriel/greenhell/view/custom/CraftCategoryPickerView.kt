package cafe.adriel.greenhell.view.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.HorizontalScrollView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.forEach
import cafe.adriel.greenhell.R
import cafe.adriel.greenhell.model.CraftCategory
import kotlinx.android.synthetic.main.view_craft_category_picker.view.*

class CraftCategoryPickerView(context: Context, attrs: AttributeSet) :
    HorizontalScrollView(context, attrs), View.OnClickListener {

    private lateinit var listener: (CraftCategory) -> Unit

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.view_craft_category_picker, this, true)

        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false

        with(view){
            vCategoryAll.setOnClickListener(this@CraftCategoryPickerView)
            vCategoryCampsite.setOnClickListener(this@CraftCategoryPickerView)
            vCategoryFood.setOnClickListener(this@CraftCategoryPickerView)
            vCategoryMedicine.setOnClickListener(this@CraftCategoryPickerView)
            vCategoryShelter.setOnClickListener(this@CraftCategoryPickerView)
            vCategoryTool.setOnClickListener(this@CraftCategoryPickerView)
            vCategoryTrap.setOnClickListener(this@CraftCategoryPickerView)
            vCategoryWeapon.setOnClickListener(this@CraftCategoryPickerView)
        }
    }

    override fun onClick(v: View?) {
        v?.let {
            val category = when(it.id){
                R.id.vCategoryCampsite -> CraftCategory.CAMPSITE
                R.id.vCategoryFood -> CraftCategory.FOOD
                R.id.vCategoryMedicine -> CraftCategory.MEDICINE
                R.id.vCategoryShelter -> CraftCategory.SHELTER
                R.id.vCategoryTool -> CraftCategory.TOOL
                R.id.vCategoryTrap -> CraftCategory.TRAP
                R.id.vCategoryWeapon -> CraftCategory.WEAPON
                else -> CraftCategory.ALL
            }
            selectCategory(category)
            listener(category)
        }
    }

    fun setListener(listener: (CraftCategory) -> Unit){
        this.listener = listener
    }

    fun selectCategory(category: CraftCategory){
        unSelectAllCategories()
        setCategorySelected(when(category){
            CraftCategory.CAMPSITE -> vCategoryCampsite
            CraftCategory.FOOD -> vCategoryFood
            CraftCategory.MEDICINE -> vCategoryMedicine
            CraftCategory.SHELTER -> vCategoryShelter
            CraftCategory.TOOL -> vCategoryTool
            CraftCategory.TRAP -> vCategoryTrap
            CraftCategory.WEAPON -> vCategoryWeapon
            else ->  vCategoryAll
        }, true)
    }

    private fun unSelectAllCategories(){
        vCategoriesLayout.forEach {
            setCategorySelected(it, false)
        }
    }

    private fun setCategorySelected(view: View, selected: Boolean){
        with(view as AppCompatButton) {
            if (selected) {
                setTextColor(Color.WHITE)
                setBackgroundResource(R.drawable.bg_fill_accent_ripple)
            } else {
                setTextColor(Color.BLACK)
                setBackgroundResource(R.drawable.bg_fill_white_ripple)
            }
        }
    }

}