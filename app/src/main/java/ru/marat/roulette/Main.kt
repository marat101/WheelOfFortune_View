import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt
import kotlin.random.Random

const val COUNT = 100000

fun main() = runBlocking {
    val spisok = listOf(
        TestItem("0", 0.77),
        TestItem("1", 22),
        TestItem("2", 11),
        TestItem("3", 11),
        TestItem("4", 6),
        TestItem("5", 0.0001),
        TestItem("6", 1),
        TestItem("7", 12),
        TestItem("8", 3),
        TestItem("9", 22),
    )
    val totalValue = spisok.sumOf { it.value }
    var sum = .0
    val mappedList = spisok.map {
        val to = sum + it.value
        val range = sum..to
        sum = to
        it.copy(
            value = (it.value / totalValue) *100.0,
            range = range
        )
    }
    var aaaa = 0
    val listInt = mutableListOf<Int>()

    while (aaaa <= COUNT) {
        val randomDouble = Random.nextDouble(.0, sum)
        val result = mappedList.find { randomDouble in it.range!! }
        listInt.add(result!!.name.toInt())
        aaaa++
    }
    println(sum)
    mappedList.forEach {
        println("item: ${it.name}\n" +
                "chance: ${it.value.roundToInt()}\n" +
                "count: ${listInt.count { p -> it.name.toInt() == p }}\n")
    }
}

data class TestItem(
    val name: String,
    val value: Double,
    val range: ClosedFloatingPointRange<Double>? = null
) {
    constructor(name: String, value: Int) : this(name, value.toDouble())
}