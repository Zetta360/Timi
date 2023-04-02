package com.cicada.sisi.bot;

import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;
import java.util.List;

public class OpenAI {

    static private final String token = "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";
    static private final String model = "text-davinci-003";

    public List<CompletionChoice> searchFor(String prompt) {

        Duration timeout = Duration.ofSeconds(15);
        OpenAiService service = new OpenAiService(token, timeout);

        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt("Best sustainable / eco places to visit in " +
                        "Timisoara, Romania \n" +
                        "- regarding this topic: " + prompt + " \n" +
                        "- provide me exactly 6 results with this format: the name and coordinates(lat and long) \n" +
                        "- without any type of description \n" +
                        "- add a new line between every result \n")
                .model(model)
                .echo(true)
                .temperature(0.85)
                .maxTokens(256)
                .bestOf(2)
                .build();

        List<CompletionChoice> choices = service.createCompletion(completionRequest).getChoices();
        choices.forEach(System.out::println);

        return choices;
    }
}
