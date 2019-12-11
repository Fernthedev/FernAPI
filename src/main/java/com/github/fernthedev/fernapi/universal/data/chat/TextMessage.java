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
        this.parentText = text;
    }

    /**
     * Creates a TextMessage with formatting and text from the passed
     * component
     *
     * @param TextMessage the component to copy from
     */
    public TextMessage(TextMessage TextMessage)
    {
        super( TextMessage );
        setText( TextMessage.getText() );
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
     * Creates a duplicate of this TextMessage.
     *
     * @return the duplicate of this TextMessage.
     */
    @Override
    public BaseMessage duplicate()
    {
        return new TextMessage( this );
    }

    @Override
    protected void toPlainText(StringBuilder builder)
    {
        builder.append( text );
        super.toPlainText( builder );
    }

    @Override
    public String getParentText() {
        if(parent != null) {
            return parent.parentText;
        }

        return text;
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
