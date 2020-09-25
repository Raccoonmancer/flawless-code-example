# flawless-code-example
Repository for the FlawlessCode example

## Implementation high level description

In this example I created a JSON to HTML converter with a main class being `HtmlConverter`. In case of more time I would've used the design pattern Strategy on `EnumMap` base instead of the switch case implementation for the appending logic. 

In the current implementation I created a public method `getHtmlString` which receives the stringified json data which is read from a json file with the help of `retrieveJsonFromFile` method and calls a private method `convertJsonToHtml` which takes care of the convertion itself by looping through the object and appending the values with the help of the `StringBuilder`.

I realise that there are some design "hiccups" such as hard coding some specific aspects, some code smells mainly due to my newness to Java. I would be happy to learn and improve my knowledge.
