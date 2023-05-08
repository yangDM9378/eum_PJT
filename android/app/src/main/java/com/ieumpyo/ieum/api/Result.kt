data class Pin(
    val result: List<Result>,
    val resultCode: String,
    val resultMsg: String
)
data class PinDetail(
    val result: Detail,
    val resultCode: String,
    val resultMsg: String
)

data class Result(
    val latitude: Double,
    val longitude: Double,
    val pinId: Int
)

data class Detail(
    val title : String,
    val userName : String,
    val role : String
)