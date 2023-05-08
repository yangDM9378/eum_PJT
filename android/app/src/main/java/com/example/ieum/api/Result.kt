data class Pin(
    val result: List<Result>,
    val resultCode: String,
    val resultMsg: String
)

data class Result(
    val latitude: Double,
    val longitude: Double,
    val pinId: Int
)