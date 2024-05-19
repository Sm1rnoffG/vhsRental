package com.example.vhsrental.data


data class Query<T>(
    val list: List<T>,
    val searchValue: String = ""
) {
    fun filter(request: (T) -> Boolean) : Query<T> {
        return Query(list.filter(request))
    }

    fun sort(comparator: Comparator<T> , flipped: Boolean) : Query<T> {
        val newList = list.sortedWith(comparator)
        return Query(if (flipped) newList.reversed() else newList)
    }

    fun search(selector: (T) -> String) = Query(list.filter { selector(it).contains(searchValue) })

    fun updateSearch(update: String) : Query<T> = this.copy(searchValue = update)
}
