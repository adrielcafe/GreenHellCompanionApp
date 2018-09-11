package cafe.adriel.greenhell

import cafe.adriel.greenhell.model.Location

class AddLocationEvent

data class SaveLocationEvent(val location: Location)

data class DonateEvent(val sku: String)