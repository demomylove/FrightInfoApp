package com.flightinfo.app.data.model

data class FlightBooking(
    val flightId: String,
    val passengerName: String,
    val passengerEmail: String,
    val passengerPhone: String,
    val seat: String,
    val passengerId: String = "", // 乘客身份证号
    val specialRequests: String = "", // 特殊需求
    val baggageInfo: BaggageInfo = BaggageInfo(), // 行李信息
    val mealPreference: MealPreference = MealPreference.NONE, // 餐食选择
    val insuranceOption: InsuranceOption = InsuranceOption.NONE, // 保险选项
)

// 行李信息数据类
data class BaggageInfo(
    val checkedBaggage: Int = 0, // 托运行李数量
    val cabinBaggage: Int = 1, // 随身行李数量
    val specialBaggage: String = "", // 特殊行李描述
)

// 餐食选择枚举
enum class MealPreference {
    NONE, // 无餐食
    STANDARD, // 标准餐
    VEGETARIAN, // 素食
    VEGAN, // 纯素食
    GLUTEN_FREE, // 无麸质
    CHILD, // 儿童餐
    DIABETIC, // 糖尿病餐
}

// 保险选项枚举
enum class InsuranceOption {
    NONE, // 无保险
    BASIC, // 基础保险
    COMPREHENSIVE, // 综合保险
    PREMIUM, // 高级保险
}
