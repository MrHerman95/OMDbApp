package com.example.sdk.ads.nativead

/**
 * Where native ads go in a list.
 *
 * @param positions indexes in the final list (content and ads together) that hold an ad.
 * @param minContentCount the list gets no ads at all while it has fewer content items than this.
 */
data class NativeAdPlacement(
    val positions: List<Int>,
    val minContentCount: Int = 0
) {
    init {
        require(positions.all { it >= 0 }) { "Ad positions can't be negative" }
        require(positions.distinct().size == positions.size) { "Ad positions must be unique" }
    }

    private val sortedPositions = positions.sorted()

    /** Number of ads this placement needs; slot indexes run from 0 to `slotCount - 1`. */
    val slotCount: Int get() = positions.size

    /**
     * Interleaves ad slots into [items]. Slots are numbered by their position in the list, and
     * a slot past the end of the content is left out instead of being appended.
     */
    fun <T> insertInto(items: List<T>): List<AdListItem<T>> {
        val result: MutableList<AdListItem<T>> = items.mapTo(ArrayList()) { AdListItem.Content(it) }
        if (items.size < minContentCount) return result

        // Ascending order, so each position already accounts for the ads inserted before it
        sortedPositions.forEachIndexed { slot, position ->
            // `<` rather than `<=` keeps an ad from ending the list
            if (position < result.size) result.add(position, AdListItem.Ad(slot))
        }
        return result
    }

    companion object {
        val None = NativeAdPlacement(positions = emptyList())
    }
}

sealed interface AdListItem<out T> {
    data class Content<out T>(val value: T) : AdListItem<T>
    data class Ad(val slot: Int) : AdListItem<Nothing>
}
