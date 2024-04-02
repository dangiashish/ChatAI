package com.codebyashish.chatai.utility

import java.util.regex.Matcher
import java.util.regex.Pattern


class validator {

    private val CODE_SYNTAX_PATTERN = ".*\\{.*\\}.*|.*\\(.*\\).*|.*\\[.*\\].*|.*\\;.*|.*\\=.*"

    fun containsCodeSyntax(inputText: String?): Boolean {
        // Compile the regular expression pattern
        val pattern: Pattern = Pattern.compile(CODE_SYNTAX_PATTERN)

        // Create a matcher for the input text
        val matcher: Matcher? = inputText?.let { pattern.matcher(it) }

        // Check if any matches are found

            return matcher!!.find()

    }
}