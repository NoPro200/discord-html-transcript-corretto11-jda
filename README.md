# Discord (JDA) HTML Transcripts
[![](https://jitpack.io/v/Ryzeon/discord-html-transcripts.svg)](https://jitpack.io/#Ryzeon/discord-html-transcripts)

Discord HTML Transcripts is a node.js module (recode on JDA) to generate nice looking HTML transcripts. Processes discord markdown like **bold**, *italics*, ~~strikethroughs~~, and more. Nicely formats attachments and embeds. Built in XSS protection, preventing users from inserting html tags. 

**This module is designed to work with [JDA](https://github.com/DV8FromTheWorld/JDA).**

HTML Template stolen from [DiscordChatExporter](https://github.com/Tyrrrz/DiscordChatExporter) and this is a Fork from [Ryzeon/discord-html-transcripts](https://github.com/Ryzeon/discord-html-transcripts) with the difference that it's compiled for the corretto11 Version and an older Version of JDA [5.0.0-alpha.20](https://mvnrepository.com/artifact/net.dv8tion/JDA/5.0.0-alpha.20).

## Installation

```xml
<repositories>
    <repository>
	<id>jitpack.io</id>
	<url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
<dependency>
    <groupId>com.github.NoPro200</groupId>
    <artifactId>discord-html-transcript-corretto11-jda</artifactId>
    <version>1.0</version>
</dependency>
```

## Example Output
![output](https://img.derock.dev/5f5q0a.png)

## Usage
### Example usage using the built-in message fetcher.
```java
DiscordHtmlTranscripts transcript = DiscordHtmlTranscripts.getInstance();
textChannel.sendFiles(transcript.createTranscript(textChannel)).queue()
```

### Or if you prefer, you can pass in your own messages.
```java
DiscordHtmlTranscripts transcript = DiscordHtmlTranscripts.getInstance();
transcript.generateFromMessages(messages); // return to InputStream
```

### You can also put the transcript into a variable
```java
DiscordHtmlTranscripts transcripts = new DiscordHtmlTranscripts();
try {
	testChannel.sendFiles(transcripts.getTranscript(testChannel, "test.html")).queue();
} catch (IOException e) {
	throw new RuntimeException(e);
}
```


