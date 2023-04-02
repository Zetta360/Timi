package com.cicada.sisi.bot

import com.theokanning.openai.completion.CompletionChoice
import kotlinx.coroutines.*


fun search(prompt : String, callback: (List<String>) -> Unit) {

    GlobalScope.launch {

        // Long running background task
        val searchFor = OpenAI().searchFor(prompt)
        val result = extractCompletionChoiceValues(searchFor)
        callback(result)
    }
}


private fun extractCompletionChoiceValues(choices: List<CompletionChoice>): List<String> {

    val element = choices[0].text.split("\n")
    val startIndex = element.size

    return element.subList(startIndex - 6, startIndex)
}