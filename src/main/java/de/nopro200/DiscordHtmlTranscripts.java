package de.nopro200;

import de.nopro200.utils.format.IFormatHelper;
import de.nopro200.utils.format.impl.AudioFormat;
import de.nopro200.utils.format.impl.ImageFormat;
import de.nopro200.utils.format.impl.VideoFormat;
import kotlin.text.Charsets;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.utils.FileUpload;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

public class DiscordHtmlTranscripts {

    private static DiscordHtmlTranscripts instance;
    private final IFormatHelper
            imageFormats = new ImageFormat(),
            videoFormats = new VideoFormat(),
            audioFormats = new AudioFormat();

    public static DiscordHtmlTranscripts getInstance() {
        if (instance == null) {
            instance = new DiscordHtmlTranscripts();
        }
        return instance;
    }

    private void handleFooter(Document document, MessageEmbed embed, Element embedContentContainer) {
        Element embedFooter = document.createElement("div");
        embedFooter.addClass("chatlog__embed-footer");


        if (Objects.requireNonNull(embed.getFooter()).getIconUrl() != null) {
            Element embedFooterIcon = document.createElement("img");
            embedFooterIcon.addClass("chatlog__embed-footer-icon");
            embedFooterIcon.attr("src", embed.getFooter().getIconUrl());
            embedFooterIcon.attr("alt", "Footer icon");
            embedFooterIcon.attr("loading", "lazy");

            embedFooter.appendChild(embedFooterIcon);
        }

        Element embedFooterText = document.createElement("span");
        embedFooterText.addClass("chatlog__embed-footer-text");
        embedFooterText.text(embed.getTimestamp() != null
                ? embed.getFooter().getText() + " • " + embed.getTimestamp()
                .format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                : embed.getFooter().getText());

        embedFooter.appendChild(embedFooterText);

        embedContentContainer.appendChild(embedFooter);
    }

    private void handleEmbedImage(Document document, MessageEmbed embed, Element embedContentContainer) {
        Element embedImage = document.createElement("div");
        embedImage.addClass("chatlog__embed-image-container");

        Element embedImageLink = document.createElement("a");
        embedImageLink.addClass("chatlog__embed-image-link");
        embedImageLink.attr("href", embed.getImage().getUrl());

        Element embedImageImage = document.createElement("img");
        embedImageImage.addClass("chatlog__embed-image");
        embedImageImage.attr("src", embed.getImage().getUrl());
        embedImageImage.attr("alt", "Image");
        embedImageImage.attr("loading", "lazy");

        embedImageLink.appendChild(embedImageImage);
        embedImage.appendChild(embedImageLink);

        embedContentContainer.appendChild(embedImage);
    }

    private void handleEmbedThumbnail(Document document, MessageEmbed embed, Element embedContent) {
        Element embedThumbnail = document.createElement("div");
        embedThumbnail.addClass("chatlog__embed-thumbnail-container");

        Element embedThumbnailLink = document.createElement("a");
        embedThumbnailLink.addClass("chatlog__embed-thumbnail-link");
        embedThumbnailLink.attr("href", embed.getThumbnail().getUrl());

        Element embedThumbnailImage = document.createElement("img");
        embedThumbnailImage.addClass("chatlog__embed-thumbnail");
        embedThumbnailImage.attr("src", embed.getThumbnail().getUrl());
        embedThumbnailImage.attr("alt", "Thumbnail");
        embedThumbnailImage.attr("loading", "lazy");

        embedThumbnailLink.appendChild(embedThumbnailImage);
        embedThumbnail.appendChild(embedThumbnailLink);

        embedContent.appendChild(embedThumbnail);
    }

    private void handleEmbedFields(Document document, MessageEmbed embed, Element embedText) {
        Element embedFields = document.createElement("div");
        embedFields.addClass("chatlog__embed-fields");

        for (MessageEmbed.Field field : embed.getFields()) {
            Element embedField = document.createElement("div");
            embedField.addClass("chatlog__embed-field");

            // Field nmae
            Element embedFieldName = document.createElement("div");
            embedFieldName.addClass("chatlog__embed-field-name");

            Element embedFieldNameMarkdown = document.createElement("div");
            embedFieldNameMarkdown.addClass("markdown preserve-whitespace");
            embedFieldNameMarkdown.html(field.getName());

            embedFieldName.appendChild(embedFieldNameMarkdown);
            embedField.appendChild(embedFieldName);


            // Field value
            Element embedFieldValue = document.createElement("div");
            embedFieldValue.addClass("chatlog__embed-field-value");

            Element embedFieldValueMarkdown = document.createElement("div");
            embedFieldValueMarkdown.addClass("markdown preserve-whitespace");
            embedFieldValueMarkdown
                    .html(Formatter.format(field.getValue()));

            embedFieldValue.appendChild(embedFieldValueMarkdown);
            embedField.appendChild(embedFieldValue);

            embedFields.appendChild(embedField);
        }

        embedText.appendChild(embedFields);
    }

    private void handleEmbedDescription(Document document, MessageEmbed embed, Element embedText) {
        Element embedDescription = document.createElement("div");
        embedDescription.addClass("chatlog__embed-description");

        Element embedDescriptionMarkdown = document.createElement("div");
        embedDescriptionMarkdown.addClass("markdown preserve-whitespace");
        embedDescriptionMarkdown
                .html(Formatter.format(embed.getDescription()));

        embedDescription.appendChild(embedDescriptionMarkdown);
        embedText.appendChild(embedDescription);
    }

    private void handleEmbedTitle(Document document, MessageEmbed embed, Element embedText) {
        Element embedTitle = document.createElement("div");
        embedTitle.addClass("chatlog__embed-title");

        if (embed.getUrl() != null) {
            Element embedTitleLink = document.createElement("a");
            embedTitleLink.addClass("chatlog__embed-title-link");
            embedTitleLink.attr("href", embed.getUrl());

            Element embedTitleMarkdown = document.createElement("div");
            embedTitleMarkdown.addClass("markdown preserve-whitespace")
                    .html(Formatter.format(embed.getTitle()));

            embedTitleLink.appendChild(embedTitleMarkdown);
            embedTitle.appendChild(embedTitleLink);
        } else {
            Element embedTitleMarkdown = document.createElement("div");
            embedTitleMarkdown.addClass("markdown preserve-whitespace")
                    .html(Formatter.format(embed.getTitle()));

            embedTitle.appendChild(embedTitleMarkdown);
        }
        embedText.appendChild(embedTitle);
    }

    private void handleEmbedAuthor(Document document, MessageEmbed embed, Element embedText) {
        Element embedAuthor = document.createElement("div");
        embedAuthor.addClass("chatlog__embed-author");

        if (embed.getAuthor().getIconUrl() != null) {
            Element embedAuthorIcon = document.createElement("img");
            embedAuthorIcon.addClass("chatlog__embed-author-icon");
            embedAuthorIcon.attr("src", embed.getAuthor().getIconUrl());
            embedAuthorIcon.attr("alt", "Author icon");
            embedAuthorIcon.attr("loading", "lazy");

            embedAuthor.appendChild(embedAuthorIcon);
        }

        Element embedAuthorName = document.createElement("span");
        embedAuthorName.addClass("chatlog__embed-author-name");

        if (embed.getAuthor().getUrl() != null) {
            Element embedAuthorNameLink = document.createElement("a");
            embedAuthorNameLink.addClass("chatlog__embed-author-name-link");
            embedAuthorNameLink.attr("href", embed.getAuthor().getUrl());
            embedAuthorNameLink.text(embed.getAuthor().getName());

            embedAuthorName.appendChild(embedAuthorNameLink);
        } else {
            embedAuthorName.text(embed.getAuthor().getName());
        }

        embedAuthor.appendChild(embedAuthorName);
        embedText.appendChild(embedAuthor);
    }

    private void handleUnknownAttachmentTypes(Document document, Message.Attachment attach, Element attachmentsDiv) {
        Element attachmentGeneric = document.createElement("div");
        attachmentGeneric.addClass("chatlog__attachment-generic");

        Element attachmentGenericIcon = document.createElement("svg");
        attachmentGenericIcon.addClass("chatlog__attachment-generic-icon");

        Element attachmentGenericIconUse = document.createElement("use");
        attachmentGenericIconUse.attr("xlink:href", "#icon-attachment");

        attachmentGenericIcon.appendChild(attachmentGenericIconUse);
        attachmentGeneric.appendChild(attachmentGenericIcon);

        Element attachmentGenericName = document.createElement("div");
        attachmentGenericName.addClass("chatlog__attachment-generic-name");

        Element attachmentGenericNameLink = document.createElement("a");
        attachmentGenericNameLink.attr("href", attach.getUrl());
        attachmentGenericNameLink.text(attach.getFileName());

        attachmentGenericName.appendChild(attachmentGenericNameLink);
        attachmentGeneric.appendChild(attachmentGenericName);

        Element attachmentGenericSize = document.createElement("div");
        attachmentGenericSize.addClass("chatlog__attachment-generic-size");

        attachmentGenericSize.text(Formatter.formatBytes(attach.getSize()));
        attachmentGeneric.appendChild(attachmentGenericSize);

        attachmentsDiv.appendChild(attachmentGeneric);
    }

    private void handleAudios(Document document, Message.Attachment attach, Element attachmentsDiv) {
        Element attachmentAudio = document.createElement("audio");
        attachmentAudio.addClass("chatlog__attachment-media");
        attachmentAudio.attr("src", attach.getUrl());
        attachmentAudio.attr("alt", "Audio attachment");
        attachmentAudio.attr("controls", true);
        attachmentAudio.attr("title",
                "Audio: " + attach.getFileName() + Formatter.formatBytes(attach.getSize()));

        attachmentsDiv.appendChild(attachmentAudio);
    }

    private void handleVideos(Document document, Message.Attachment attach, Element attachmentsDiv) {
        Element attachmentVideo = document.createElement("video");
        attachmentVideo.addClass("chatlog__attachment-media");
        attachmentVideo.attr("src", attach.getUrl());
        attachmentVideo.attr("alt", "Video attachment");
        attachmentVideo.attr("controls", true);
        attachmentVideo.attr("title",
                "Video: " + attach.getFileName() + Formatter.formatBytes(attach.getSize()));

        attachmentsDiv.appendChild(attachmentVideo);
    }

    private void handleImages(Document document, Message.Attachment attach, Element attachmentsDiv) {
        Element attachmentLink = document.createElement("a");

        Element attachmentImage = document.createElement("img");
        attachmentImage.addClass("chatlog__attachment-media");

        attachmentImage.attr("src", attach.getUrl());

        attachmentImage.attr("alt", "Image attachment");
        attachmentImage.attr("loading", "lazy");
        attachmentImage.attr("title",
                "Image: " + attach.getFileName() + Formatter.formatBytes(attach.getSize()));
        attachmentLink.appendChild(attachmentImage);
        attachmentsDiv.appendChild(attachmentLink);
    }

    private void handleMessageReferences(GuildChannel channel, Document document, Message message, Element messageGroup) {
        // message.reference?.messageId
        // create symbol
        Element referenceSymbol = document.createElement("div");
        referenceSymbol.addClass("chatlog__reference-symbol");

        // create reference
        Element reference = document.createElement("div");
        reference.addClass("chatlog__reference");

        var referenceMessage = message.getReferencedMessage();
        User author = referenceMessage.getAuthor();
        Member member = channel.getGuild().getMember(author);
        var color = Formatter.toHex(Objects.requireNonNull(member.getColor()));

        //        System.out.println("REFERENCE MSG " + referenceMessage.getContentDisplay());
        reference.html("<img class=\"chatlog__reference-avatar\" src=\""
                + author.getAvatarUrl() + "\" alt=\"Avatar\" loading=\"lazy\">" +
                "<span class=\"chatlog__reference-name\" title=\"" + author.getName()
                + "\" style=\"color: " + color + "\">" + author.getName() + "\"</span>" +
                "<div class=\"chatlog__reference-content\">" +
                " <span class=\"chatlog__reference-link\" onclick=\"scrollToMessage(event, '"
                + referenceMessage.getId() + "')\">" +
                "<em>" +
                referenceMessage.getContentDisplay() != null
                ? referenceMessage.getContentDisplay().length() > 42
                ? referenceMessage.getContentDisplay().substring(0, 42)
                + "..."
                : referenceMessage.getContentDisplay()
                : "Click to see attachment" +
                "</em>" +
                "</span>" +
                "</div>");

        messageGroup.appendChild(referenceSymbol);
        messageGroup.appendChild(reference);
    }

    public FileUpload createTranscript(GuildMessageChannel channel) throws IOException {
        return createTranscript(channel, null);
    }

    public FileUpload createTranscript(GuildMessageChannel channel, String fileName) throws IOException {
        return FileUpload.fromData(generateFromMessages(channel.getIterableHistory().stream().collect(Collectors.toList())),
                fileName != null ? fileName : "transcript.html");
    }

    public String fileUploadToFileContent(FileUpload fileUpload) {
        InputStreamReader inputStreamReader = new InputStreamReader(fileUpload.getData());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder lines = new StringBuilder();
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.append(line);
            }
        } catch (IOException e) {
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
            }
        }
        return lines.toString();
    }

    public String saveHtmlFile(TextChannel textChannel, String path) {
        DiscordHtmlTranscripts transcript = DiscordHtmlTranscripts.getInstance();
        try {
            textChannel.sendFiles(transcript.createTranscript(textChannel)).queue();

            File file = new File(path);
            if (file.isDirectory()) {
                if (path.toLowerCase().endsWith(".html")) {
                    FileUtils.writeStringToFile(file, transcript.fileUploadToFileContent(transcript.createTranscript(textChannel)), String.valueOf(StandardCharsets.UTF_8));
                    return "Success (Saved to \"" + path + "\")";
                } else {
                    return "Err: Invaild File! It should end with .html!";
                }
            } else {
                return "Err: Invaild File Path!";
            }

        } catch (IOException e) {
            return "Err: " + e.getMessage();
        }
    }

    public InputStream generateFromMessages(Collection<Message> messages) throws IOException {
        InputStream htmlTemplate = findFile();
        if (messages.isEmpty()) {
            throw new IllegalArgumentException("No messages to generate a transcript from");
        }

        GuildChannel channel = messages.iterator().next().getChannel().asGuildMessageChannel();

        Document document = Jsoup.parse(htmlTemplate, "UTF-8", "template.html");
        Element metaTag = document.select("meta#discordembeddescforembed").first();
        document.outputSettings().indentAmount(0).prettyPrint(true);
        document.getElementsByClass("preamble__guild-icon").first().attr("src", channel.getGuild().getIconUrl()); // set guild icon

        document.getElementById("transcriptTitle").text("#" + channel.getName() + " | " + messages.size() + " Nachrichten"); // set tit
        metaTag.attr("content", "Transcript von dem Channel " + channel.getName());
        document.getElementById("guildname").text(channel.getGuild().getName()); // set guild name
        document.getElementById("ticketname").text("#" + channel.getName()); // set channel name

        Element chatLog = document.getElementById("chatlog"); // chat log
        for (Message message : messages.stream().sorted(Comparator.comparing(ISnowflake::getTimeCreated)).collect(Collectors.toList())) {
/*
            if (message.getAuthor().isBot()) {
                continue;
            }*/
            // create message group
            Element messageGroup = document.createElement("div");
            messageGroup.addClass("chatlog__message-group");

            // message reference
            if (message.getReferencedMessage() != null) { // preguntar si es eso
                handleMessageReferences(channel, document, message, messageGroup);
            }

            var author = message.getAuthor();

            Element authorElement = document.createElement("div");
            authorElement.addClass("chatlog__author-avatar-container");

            Element authorAvatar = document.createElement("img");
            authorAvatar.addClass("chatlog__author-avatar");
            authorAvatar.attr("alt", "Avatar");
            authorAvatar.attr("loading", "lazy");

            Element authorName = document.createElement("span");
            authorName.addClass("chatlog__author-name");

            if (author != null) {
                authorName.attr("title", Objects.requireNonNull(author.getName()));
                authorName.text(author.getName());
                authorName.attr("data-user-id", author.getId());
                authorAvatar.attr("src", Objects.requireNonNull(author.getAvatarUrl()));
            } else {
                // Handle the case when author is null (e.g., when the message is from a bot)
                authorName.attr("title", "Bot");
                authorName.text("Bot");
                authorName.attr("data-user-id", "Bot");
                authorAvatar.attr("src", "default_bot_avatar_url"); // replace with your default bot avatar URL
            }

            authorElement.appendChild(authorAvatar);
            messageGroup.appendChild(authorElement);

            // message content
            Element content = document.createElement("div");
            content.addClass("chatlog__messages");

            content.appendChild(authorName);

            if (author != null && author.isBot()) {
                Element botTag = document.createElement("span");
                botTag.addClass("chatlog__bot-tag").text("BOT");
                content.appendChild(botTag);
            }

            // timestamp
            Element timestamp = document.createElement("span");
            timestamp.addClass("chatlog__timestamp");
            timestamp
                    .text(message.getTimeCreated().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

            content.appendChild(timestamp);

            Element messageContent = document.createElement("div");
            messageContent.addClass("chatlog__message");
            messageContent.attr("data-message-id", message.getId());
            messageContent.attr("id", "message-" + message.getId());
            messageContent.attr("title", "Message sent: " + message.getTimeCreated().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

            if (!message.getContentDisplay().isEmpty()) {
                Element messageContentContent = document.createElement("div");
                messageContentContent.addClass("chatlog__content");

                Element messageContentContentMarkdown = document.createElement("div");
                messageContentContentMarkdown.addClass("markdown");

                Element messageContentContentMarkdownSpan = document.createElement("span");
                messageContentContentMarkdownSpan.addClass("preserve-whitespace");
                messageContentContentMarkdownSpan.html(Formatter.format(message.getContentDisplay()));

                messageContentContentMarkdown.appendChild(messageContentContentMarkdownSpan);
                messageContentContent.appendChild(messageContentContentMarkdown);
                messageContent.appendChild(messageContentContent);
            }

            // messsage attachments
            if (!message.getAttachments().isEmpty()) {
                for (Message.Attachment attach : message.getAttachments()) {
                    Element attachmentsDiv = document.createElement("div");
                    attachmentsDiv.addClass("chatlog__attachment");

                    var attachmentType = attach.getFileExtension();
                    if (imageFormats.isFormat(attachmentType)) {
                        handleImages(document, attach, attachmentsDiv);
                    } else if (videoFormats.isFormat(attachmentType)) {
                        handleVideos(document, attach, attachmentsDiv);
                    } else if (audioFormats.isFormat(attachmentType)) {
                        handleAudios(document, attach, attachmentsDiv);
                    } else {
                        handleUnknownAttachmentTypes(document, attach, attachmentsDiv);
                    }

                    messageContent.appendChild(attachmentsDiv);
                }
            }

            content.appendChild(messageContent);

            if (!message.getEmbeds().isEmpty()) {
                for (MessageEmbed embed : message.getEmbeds()) {
                    if (embed == null) {
                        continue;
                    }
                    Element embedDiv = document.createElement("div");
                    embedDiv.addClass("chatlog__embed");

                    // embed color
                    Element embedColorPill = document.createElement("div");

                    if (embed.getColor() == null) {
                        embedColorPill.addClass("chatlog__embed-color-pill chatlog__embed-color-pill--default");
                    } else {
                        embedColorPill.addClass("chatlog__embed-color-pill");
                        embedColorPill.attr("style",
                                "background-color: #" + Formatter.toHex(embed.getColor()));
                    }
                    embedDiv.appendChild(embedColorPill);

                    Element embedContentContainer = document.createElement("div");
                    embedContentContainer.addClass("chatlog__embed-content-container");

                    Element embedContent = document.createElement("div");
                    embedContent.addClass("chatlog__embed-content");

                    Element embedText = document.createElement("div");
                    embedText.addClass("chatlog__embed-text");

                    // embed author
                    if (embed.getAuthor() != null && embed.getAuthor().getName() != null) {
                        handleEmbedAuthor(document, embed, embedText);
                    }

                    // embed title
                    if (embed.getTitle() != null) {
                        handleEmbedTitle(document, embed, embedText);
                    }

                    // embed description
                    if (embed.getDescription() != null) {
                        handleEmbedDescription(document, embed, embedText);
                    }

                    // embed fields
                    if (!embed.getFields().isEmpty()) {
                        handleEmbedFields(document, embed, embedText);
                    }

                    embedContent.appendChild(embedText);

                    // embed thumbnail
                    if (embed.getThumbnail() != null) {
                        handleEmbedThumbnail(document, embed, embedContent);
                    }

                    embedContentContainer.appendChild(embedContent);

                    // embed image
                    if (embed.getImage() != null) {
                        handleEmbedImage(document, embed, embedContentContainer);
                    }

                    // embed footer
                    if (embed.getFooter() != null) {
                        handleFooter(document, embed, embedContentContainer);
                    }

                    embedDiv.appendChild(embedContentContainer);
                    content.appendChild(embedDiv);
                }
            }

            messageGroup.appendChild(content);
            assert chatLog != null;
            chatLog.appendChild(messageGroup);
        }
        return new ByteArrayInputStream(document.outerHtml().getBytes(Charsets.UTF_8));
    }

    private InputStream findFile() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("template.html");
        if (inputStream == null) {
            throw new IllegalArgumentException("file is not found: " + "template.html");
        }
        return inputStream;
    }
}