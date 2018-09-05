package cafe.adriel.greenhell.view.main.crafting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cafe.adriel.greenhell.model.CraftItem
import cafe.adriel.greenhell.repository.CraftingRepository
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class CraftingViewModel(private val craftingRepo: CraftingRepository) : ViewModel(){

    private val craftItems = MutableLiveData<List<CraftItem>>()

    fun getCraftItems(): LiveData<List<CraftItem>> {
        launch(UI) {
            craftItems.value = craftingRepo.getCraftItems()
        }
        return craftItems
    }

}