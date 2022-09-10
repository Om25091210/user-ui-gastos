package com.cu.gastosmerchant1.Adapters

import java.util.regex.Matcher
import java.util.regex.Pattern

class MyRegex {

    companion object{
        fun email(email: String): Boolean {
            var pattern: Pattern = Pattern.compile(".+@.+\\.[a-z]+")
            var matcher: Matcher = pattern.matcher(email)
            return matcher.matches()
        }
        fun contact(contact:String):Boolean
        {
            val expression = "^[6-9]\\d{9}$"
            val inputStr: CharSequence = contact
            val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher: Matcher = pattern.matcher(inputStr)
            return matcher.matches()
        }

    }

}