# Discord (JDA) HTML Transcripts
[![](https://jitpack.io/v/NoPro200/discord-html-transcript-corretto11-jda.svg)](https://jitpack.io/#NoPro200/discord-html-transcript-corretto11-jda)

Discord HTML Transcripts ist ein node.js-Modul (recode on JDA), um schön aussehende HTML-Transkripte zu erzeugen. Verarbeitet Discord Markdown wie **fett**, *Kursivschrift*, ~~Durchstreichen~~ und mehr. Schöne Formatierung von Anhängen und Einbettungen. Eingebauter XSS-Schutz, der verhindert, dass Benutzer HTML-Tags einfügen. 

**Dieses Modul ist für die Arbeit mit [JDA] (https://github.com/DV8FromTheWorld/JDA) konzipiert.

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

## Beispielsergebnis
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


