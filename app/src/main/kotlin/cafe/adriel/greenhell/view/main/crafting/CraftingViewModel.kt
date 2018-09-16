package cafe.adriel.greenhell.view.main.crafting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cafe.adriel.androidcoroutinescopes.viewmodel.CoroutineScopedViewModel
import cafe.adriel.greenhell.model.CraftItem
import cafe.adriel.greenhell.repository.CraftingRepository
import kotlinx.coroutines.experimental.launch

class CraftingViewModel(private val craftingRepo: CraftingRepository) : CoroutineScopedViewModel(){

    private val craftItems = MutableLiveData<List<CraftItem>>()

    fun getCraftItems(): LiveData<List<CraftItem>> {
        launch {
            craftItems.value = craftingRepo.getCraftItems()
        }
        return craftItems
    }

}