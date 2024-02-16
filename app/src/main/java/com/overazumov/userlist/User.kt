package com.overazumov.userlist

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
data class User(
    var name: String,
    var surname: String,
    var phoneNumber: PhoneNumber,
    @DrawableRes val imageRes: Int,
    val id: Int
) : Parcelable {
    @Parcelize
    data class PhoneNumber(val number: String) : Parcelable {
        override fun toString(): String {
            return "+7 " + if (number.length == 10) convertNumber() else number
        }

        private fun convertNumber(): String {
            with(number) {
                return "(${substring(0, 3)}) ${substring(3, 6)}-${substring(6, 8)}-${substring(8)}"
            }
        }
    }

    private object IdGenerator {
        private var currentId = 0

        fun generateNextId() = currentId++
    }

    object Fabric {
        fun createUsers(fullNames: List<String>): List<User> {
            val result = mutableListOf<User>()
            for (i in 0 until minOf(fullNames.size, imageResources.size)) {
                val (name, surname) = fullNames[i].split("\\s".toRegex())
                result.add(User(
                    name,
                    surname,
                    PhoneNumber(generateNumber()),
                    imageResources[i],
                    IdGenerator.generateNextId()
                ))
            }
            return result
        }

        private fun generateNumber() = Random.nextLong(1000000000, 9999999999).toString()
    }
}

val fullNames = listOf(
    "Александро Рамирес",
    "Диего Гутьеррес",
    "Анхель Фернандес",
    "Марио Альварес"
)

val imageResources = listOf(
    R.drawable.image1,
    R.drawable.image2,
    R.drawable.image3,
    R.drawable.image4
)
