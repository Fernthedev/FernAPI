package com.github.fernthedev.fernapi.universal.data.chat;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

@Getter
@Setter
public class TextMessage extends BaseMessage {

    private static final Pattern url = Pattern.compile( "^(?:(https?)://)?([-\\w_\\.]{2,}\\.[a-z]{2,4})(/\\S*)?$" );

    /**
     * The text of the component that will be displayed to the client
     */
    private String text = "";

    public TextMessage() {
        this("");
    }

    public TextMessage(String text) {
        this.text = text;
    }

    public static TextMessage fromColor(String message) {
        return new TextMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    /**
     * Creates a textMessage with formatting and text from the passed
     * component
     *
     * @param textMessage the component to copy from
     */
    public TextMessage(TextMessage textMessage)
    {
        super( textMessage );
        parent = null;
        setText( textMessage.getText() );
    }

    /**
     * Creates a TextMessage with blank text and the extras set to the passed
     * array
     *
     * @param extras the extras to set
     */
    public TextMessage(BaseMessage... extras)
    {
        setText( "" );
        setExtra(new ArrayList<>(Arrays.asList(extras)) );
    }


    /**
     * Creates a duplicate of this TextComponent.
     *
     * @return the duplicate of this TextComponent.
     */
    @Override
    public BaseMessage duplicate()
    {
        return new TextMessage( this );
    }

    /**
     * Only plain text itself
     *
     * @return
     */
    @Override
    public String selfPlainText() {
        return text;
    }

    @Override
    protected void toPlainText(StringBuilder builder)
    {
        builder.append( text );
        super.toPlainText( builder );
    }

    @Override
    protected void toLegacyText(StringBuilder builder)
    {
        builder.append( getColor() );
        if ( isBold() )
        {
            builder.append( ChatColor.BOLD );
        }
        if ( isItalic() )
        {
            builder.append( ChatColor.ITALIC );
        }
        if ( isUnderlined() )
        {
            builder.append( ChatColor.UNDERLINE );
        }
        if ( isStrikethrough() )
        {
            builder.append( ChatColor.STRIKETHROUGH );
        }
        if ( isObfuscated() )
        {
            builder.append( ChatColor.MAGIC );
        }
        builder.append( text );
        super.toLegacyText( builder );
    }

    @Override
    public String toString()
    {
        return String.format( "TextMessage{text=%s, %s}", text, super.toString() );
    }
}
