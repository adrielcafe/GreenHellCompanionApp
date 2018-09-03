package cafe.adriel.greenhell.view.main.crafting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cafe.adriel.greenhell.model.CraftItem
import cafe.adriel.greenhell.repository.CraftingRepository

class CraftingViewModel(private val craftingRepo: CraftingRepository) : ViewModel(){

    private val craftItems = MutableLiveData<List<CraftItem>>()

    fun getCraftItems() = craftItems.apply {
        value = craftingRepo.getCraftItems()
    }

}