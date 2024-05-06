# ENGLISH -> Discord (JDA) HTML Transcripts
[![](https://jitpack.io/v/NoPro200/discord-html-transcript-corretto11-jda.svg)](https://jitpack.io/#NoPro200/discord-html-transcript-corretto11-jda)

Discord HTML Transcripts is a node.js module (recode on JDA) to generate nice looking HTML transcripts. Processes discord markdown like **bold**, *italics*, ~~strikethroughs~~, and more. Nicely formats attachments and embeds. Built in XSS protection, preventing users from inserting html tags. 

**This module is designed to work with [JDA](https://github.com/DV8FromTheWorld/JDA).**

HTML Template stolen from [DiscordChatExporter](https://github.com/Tyrrrz/DiscordChatExporter) and this is a Fork from [Ryzeon/discord-html-transcripts](https://github.com/Ryzeon/discord-html-transcripts) with the difference that it's compiled for the corretto11 Version and an older Version of JDA ([5.0.0-alpha.20](https://mvnrepository.com/artifact/net.dv8tion/JDA/5.0.0-alpha.20)).

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
    <version>TAG</version>
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

### You Also can save the HTML File by simply write
```java
DiscordHtmlTranscripts transcripts = new DiscordHtmlTranscripts();
try {
	transcripts.saveHtmlFile(textchannel, "test.html");
} catch (IOException e) {
	throw new RuntimeException(e);
}
```
It also returns the FileUpload so you can easely can send it
```java
DiscordHtmlTranscripts transcripts = new DiscordHtmlTranscripts();
try {
	FileUpload testFile = transcripts.saveHtmlFile(textchannel, "test.html");
        textchannel.sendFiles(testFile).queue();
} catch (IOException e) {
	throw new RuntimeException(e);
}
```
### You Also can convert the FileUpload in a String by
```java
DiscordHtmlTranscripts transcripts = new DiscordHtmlTranscripts();
try {
	FileUpload testFile = testChannel.saveHtmlFile(textchannel, "test.html");
        String htmlString = transcripts.fileUploadToFileContent(testFile);
} catch (IOException e) {
	throw new RuntimeException(e);
}
```





# DEUTSCH -> Discord (JDA) HTML Transcripts
[![](https://jitpack.io/v/NoPro200/discord-html-transcript-corretto11-jda.svg)](https://jitpack.io/#NoPro200/discord-html-transcript-corretto11-jda)

Discord HTML Transcripts ist ein node.js-Modul (recode on JDA), um schön aussehende HTML-Transkripte zu erzeugen. Verarbeitet Discord Markdown wie **fett**, *Kursivschrift*, ~~Durchstreichen~~ und mehr. Schöne Formatierung von Anhängen und Einbettungen. Eingebauter XSS-Schutz, der verhindert, dass Benutzer HTML-Tags einfügen. 

**Dieses Modul ist für die Arbeit mit [JDA](https://github.com/DV8FromTheWorld/JDA) konzipiert.**

HTML-Vorlage gestohlen von [DiscordChatExporter](https://github.com/Tyrrrz/DiscordChatExporter) und dies ist ein Fork von [Ryzeon/discord-html-transcripts](https://github.com/Ryzeon/discord-html-transcripts) mit dem Unterschied, dass es für die corretto11 Version und eine ältere Version von JDA ([5.0.0-alpha.20](https://mvnrepository.com/artifact/net.dv8tion/JDA/5.0.0-alpha.20)) kompiliert wurde.

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
    <version>TAG</version>
</dependency>
```

## Beispielergebnis
![output](https://img.derock.dev/5f5q0a.png)

## Benutzung
### Beispiel für die Verwendung des integrierten Nachrichtenabrufers
```java
DiscordHtmlTranscripts transcript = DiscordHtmlTranscripts.getInstance();
textChannel.sendFiles(transcript.createTranscript(textChannel)).queue()
```

### Wenn Sie möchten, können Sie auch Ihre eigenen Nachrichten eingeben
```java
DiscordHtmlTranscripts transcript = DiscordHtmlTranscripts.getInstance();
transcript.generateFromMessages(messages); // return to InputStream
```

### Sie können das Transkript auch in eine Variable einfügen
```java
DiscordHtmlTranscripts transcripts = new DiscordHtmlTranscripts();
try {
	testChannel.sendFiles(transcripts.getTranscript(testChannel, "test.html")).queue();
} catch (IOException e) {
	throw new RuntimeException(e);
}
```

### Sie können die HTML-Datei auch speichern, indem Sie einfach schreiben
```java
DiscordHtmlTranscripts transcripts = new DiscordHtmlTranscripts();
try {
	transcripts.saveHtmlFile(textchannel, "test.html");
} catch (IOException e) {
	throw new RuntimeException(e);
}
```
Es gibt auch den FileUpload zurück, so dass Sie es einfach senden können
```java
DiscordHtmlTranscripts transcripts = new DiscordHtmlTranscripts();
try {
	FileUpload testFile = transcripts.saveHtmlFile(textchannel, "test.html");
        textchannel.sendFiles(testFile).queue();
} catch (IOException e) {
	throw new RuntimeException(e);
}
```
### Sie können den FileUpload auch in einen String umwandeln, indem Sie
```java
DiscordHtmlTranscripts transcripts = new DiscordHtmlTranscripts();
try {
	FileUpload testFile = testChannel.saveHtmlFile(textchannel, "test.html");
        String htmlString = transcripts.fileUploadToFileContent(testFile);
} catch (IOException e) {
	throw new RuntimeException(e);
}
```




