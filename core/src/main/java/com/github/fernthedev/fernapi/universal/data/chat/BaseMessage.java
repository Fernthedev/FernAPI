package com.github.fernthedev.fernapi.universal.data.chat;

import lombok.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Setter
@ToString(exclude = "parent")
@NoArgsConstructor
public abstract class BaseMessage {
    
    @Setter(AccessLevel.NONE)
    BaseMessage parent;


    /**
     * The color of this component and any child components (unless overridden)
     */
    private ChatColor color;
    /**
     * Whether this component and any child components (unless overridden) is
     * bold
     */
    private Boolean bold;
    /**
     * Whether this component and any child components (unless overridden) is
     * italic
     */
    private Boolean italic;
    /**
     * Whether this component and any child components (unless overridden) is
     * underlined
     */
    private Boolean underlined;
    /**
     * Whether this component and any child components (unless overridden) is
     * strikethrough
     */
    private Boolean strikethrough;
    /**
     * Whether this component and any child components (unless overridden) is
     * obfuscated
     */
    private Boolean obfuscated;
    /**
     * The text to insert into the chat when this component (and child
     * components) are clicked while pressing the shift key
     */
    @Getter
    private String insertion;

    /**
     * Appended components that inherit this component's formatting and events
     */
    @Getter
    @Nullable
    private List<BaseMessage> extra;

    public BaseMessage setColor(ChatColor color) {
        this.color = color;
        return this;
    }

    public BaseMessage setBold(Boolean bold) {
        this.bold = bold;
        return this;
    }

    public BaseMessage setItalic(Boolean italic) {
        this.italic = italic;
        return this;
    }

    public BaseMessage setUnderlined(Boolean underlined) {
        this.underlined = underlined;
        return this;
    }

    public BaseMessage setStrikethrough(Boolean strikethrough) {
        this.strikethrough = strikethrough;
        return this;
    }

    public BaseMessage setObfuscated(Boolean obfuscated) {
        this.obfuscated = obfuscated;
        return this;
    }

    public BaseMessage setInsertion(String insertion) {
        this.insertion = insertion;
        return this;
    }

    public BaseMessage setClickData(ClickData clickData) {
        this.clickData = clickData;
        return this;
    }

    public BaseMessage setHoverData(HoverData hoverData) {
        this.hoverData = hoverData;
        return this;
    }

    /**
     * The action to preform when this component (and child components) are
     * clicked
     */
    @Getter
    private ClickData clickData;
    /**
     * The action to preform when this component (and child components) are
     * hovered over
     */
    @Getter
    private HoverData hoverData;

    BaseMessage(BaseMessage old)
    {
        copyFormatting( old, FormatRetention.ALL, true );

        if ( old.getExtra() != null )
        {
            for ( BaseMessage extra : old.getExtra() )
            {
                addExtra( extra.duplicate() );
            }
        }
    }

    /**
     * Retains only the specified formatting.
     *
     * @param retention the formatting to retain
     */
    public void retain(FormatRetention retention)
    {
        if ( retention == FormatRetention.FORMATTING || retention == FormatRetention.NONE )
        {
            setClickData( null );
            setHoverData( null );
        }
        if ( retention == FormatRetention.EVENTS || retention == FormatRetention.NONE )
        {
            setColor( null );
            setBold( null );
            setItalic( null );
            setUnderlined( null );
            setStrikethrough( null );
            setObfuscated( null );
            setInsertion( null );
        }
    }


    /**
     * Copies the events and formatting of a BaseComponent. Already set
     * formatting will be replaced.
     *
     * @param component the component to copy from
     */
    public void copyFormatting(BaseMessage component)
    {
        copyFormatting( component, FormatRetention.ALL, true );
    }

    /**
     * Copies the events and formatting of a BaseComponent.
     *
     * @param component the component to copy from
     * @param replace if already set formatting should be replaced by the new
     * component
     */
    public void copyFormatting(BaseMessage component, boolean replace)
    {
        copyFormatting( component, FormatRetention.ALL, replace );
    }

    /**
     * Copies the specified formatting of a BaseComponent.
     *
     * @param component the component to copy from
     * @param retention the formatting to copy
     * @param replace if already set formatting should be replaced by the new
     * component
     */
    public void copyFormatting(BaseMessage component, FormatRetention retention, boolean replace)
    {
        if ( retention == FormatRetention.EVENTS || retention == FormatRetention.ALL )
        {
            if ( replace || clickData == null )
            {
                setClickData( component.getClickData() );
            }
            if ( replace || hoverData == null )
            {
                setHoverData( component.getHoverData() );
            }
        }
        if ( retention == FormatRetention.FORMATTING || retention == FormatRetention.ALL )
        {
            if ( replace || color == null )
            {
                setColor( component.getColorRaw() );
            }
            if ( replace || bold == null )
            {
                setBold( component.isBoldRaw() );
            }
            if ( replace || italic == null )
            {
                setItalic( component.isItalicRaw() );
            }
            if ( replace || underlined == null )
            {
                setUnderlined( component.isUnderlinedRaw() );
            }
            if ( replace || strikethrough == null )
            {
                setStrikethrough( component.isStrikethroughRaw() );
            }
            if ( replace || obfuscated == null )
            {
                setObfuscated( component.isObfuscatedRaw() );
            }
            if ( replace || insertion == null )
            {
                setInsertion( component.getInsertion() );
            }
        }
    }


//    BaseMessage(BaseMessage old)
//    {
//        new RuntimeException("Basemessage copy constructor ");
//        parent = old.parent;
//        setColor( old.getColorRaw() );
//        setBold( old.isBoldRaw() );
//        setItalic( old.isItalicRaw() );
//        setUnderlined( old.isUnderlinedRaw() );
//        setStrikethrough( old.isStrikethroughRaw() );
//        setObfuscated( old.isObfuscatedRaw() );
//        setInsertion( old.getInsertion() );
//        setClickData( old.getClickData() );
//        setHoverData( old.getHoverData() );
//        if ( old.getExtra() != null )
//        {
//            for ( BaseMessage component : old.getExtra() )
//            {
//                addExtra( component.duplicate() );
//            }
//        }
//    }

    /**
     * Clones the BaseMessage and returns the clone.
     *
     * @return The duplicate of this BaseMessage
     */
    public abstract BaseMessage duplicate();


    /**
     * Converts the components to a string that uses the old formatting codes
     * ({@link ChatColor#COLOR_CHAR}
     *
     * @param components the components to convert
     * @return the string in the old format
     */
    public static String toLegacyText(BaseMessage... components)
    {
        StringBuilder builder = new StringBuilder();
        for ( BaseMessage msg : components )
        {
            builder.append( msg.toLegacyText() );
        }
        return builder.toString();
    }


    /**
     * Converts the components into a string without any formatting
     *
     * @param components the components to convert
     * @return the string as plain text
     */
    public static String toPlainText(BaseMessage... components)
    {
        StringBuilder builder = new StringBuilder();
        for ( BaseMessage msg : components )
        {
            builder.append( msg.toPlainText() );
        }
        return builder.toString();
    }

    /**
     * Returns the color of this component. This uses the parent's color if this
     * component doesn't have one. {@link ChatColor#WHITE}
     * is returned if no color is found.
     *
     * @return the color of this component
     */
    public ChatColor getColor()
    {
        if ( color == null )
        {
            if ( parent == null )
            {
                return ChatColor.WHITE;
            }
            return parent.getColor();
        }
        return color;
    }

    /**
     * Returns the color of this component without checking the parents color.
     * May return null
     *
     * @return the color of this component
     */
    public ChatColor getColorRaw()
    {
        return color;
    }

    /**
     * Returns whether this component is bold. This uses the parent's setting if
     * this component hasn't been set. false is returned if none of the parent
     * chain has been set.
     *
     * @return whether the component is bold
     */
    public boolean isBold()
    {
        if ( bold == null )
        {
            return parent != null && parent.isBold();
        }
        return bold;
    }

    /**
     * Returns whether this component is bold without checking the parents
     * setting. May return null
     *
     * @return whether the component is bold
     */
    public Boolean isBoldRaw()
    {
        return bold;
    }

    /**
     * Returns whether this component is italic. This uses the parent's setting
     * if this component hasn't been set. false is returned if none of the
     * parent chain has been set.
     *
     * @return whether the component is italic
     */
    public boolean isItalic()
    {
        if ( italic == null )
        {
            return parent != null && parent.isItalic();
        }
        return italic;
    }

    /**
     * Returns whether this component is italic without checking the parents
     * setting. May return null
     *
     * @return whether the component is italic
     */
    public Boolean isItalicRaw()
    {
        return italic;
    }

    /**
     * Returns whether this component is underlined. This uses the parent's
     * setting if this component hasn't been set. false is returned if none of
     * the parent chain has been set.
     *
     * @return whether the component is underlined
     */
    public boolean isUnderlined()
    {
        if ( underlined == null )
        {
            return parent != null && parent.isUnderlined();
        }
        return underlined;
    }

    /**
     * Returns whether this component is underlined without checking the parents
     * setting. May return null
     *
     * @return whether the component is underlined
     */
    public Boolean isUnderlinedRaw()
    {
        return underlined;
    }

    /**
     * Returns whether this component is strikethrough. This uses the parent's
     * setting if this component hasn't been set. false is returned if none of
     * the parent chain has been set.
     *
     * @return whether the component is strikethrough
     */
    public boolean isStrikethrough()
    {
        if ( strikethrough == null )
        {
            return parent != null && parent.isStrikethrough();
        }
        return strikethrough;
    }

    /**
     * Returns whether this component is strikethrough without checking the
     * parents setting. May return null
     *
     * @return whether the component is strikethrough
     */
    public Boolean isStrikethroughRaw()
    {
        return strikethrough;
    }

    /**
     * Returns whether this component is obfuscated. This uses the parent's
     * setting if this component hasn't been set. false is returned if none of
     * the parent chain has been set.
     *
     * @return whether the component is obfuscated
     */
    public boolean isObfuscated()
    {
        if ( obfuscated == null )
        {
            return parent != null && parent.isObfuscated();
        }
        return obfuscated;
    }

    /**
     * Returns whether this component is obfuscated without checking the parents
     * setting. May return null
     *
     * @return whether the component is obfuscated
     */
    public Boolean isObfuscatedRaw()
    {
        return obfuscated;
    }

    public void setExtra(List<BaseMessage> components)
    {
        for ( BaseMessage component : components )
        {
            component.parent = this;
        }
        extra = components;
    }

    /**
     * Appends a text element to the component. The text will inherit this
     * component's formatting
     *
     * @param text the text to append
     */
    public void addExtra(String text)
    {
        addExtra( new TextMessage( text ) );
    }

    /**
     * Appends a component to the component. The text will inherit this
     * component's formatting
     *
     * @param component the component to append
     */
    public BaseMessage addExtra(BaseMessage component)
    {
        if ( extra == null )
        {
            extra = new ArrayList<>();
        }
        new RuntimeException("Add extra called " +
                "\nthis" + this.toString() +
                "\ncomponent" + component.toString()).printStackTrace();
        component.parent = this;
        extra.add( component );
        return this;
    }

    /**
     * Returns whether the component has any formatting or events applied to it
     *
     * @return Whether any formatting or events are applied
     */
    public boolean hasFormatting()
    {
        return color != null || bold != null
                || italic != null || underlined != null
                || strikethrough != null || obfuscated != null
                || hoverData != null || clickData != null;
    }

    /**
     * Converts the component into a string without any formatting
     *
     * @return the string as plain text
     */
    public String toPlainText()
    {
        StringBuilder builder = new StringBuilder();
        toPlainText( builder );
        return builder.toString();
    }

    void toPlainText(StringBuilder builder)
    {
        if ( extra != null )
        {
            for ( BaseMessage e : extra )
            {
                e.toPlainText( builder );
            }
        }
    }

    /**
     * Converts the component to a string that uses the old formatting codes
     * ({@link ChatColor#COLOR_CHAR}
     *
     * @return the string in the old format
     */
    public String toLegacyText()
    {
        StringBuilder builder = new StringBuilder();
        toLegacyText( builder );
        return builder.toString();
    }

    void toLegacyText(StringBuilder builder)
    {
        if ( extra != null )
        {
            for ( BaseMessage e : extra )
            {
                e.toLegacyText( builder );
            }
        }
    }


    public static enum FormatRetention
    {

        /**
         * Specify that we do not want to retain anything from the previous
         * component.
         */
        NONE,
        /**
         * Specify that we want the formatting retained from the previous
         * component.
         */
        FORMATTING,
        /**
         * Specify that we want the events retained from the previous component.
         */
        EVENTS,
        /**
         * Specify that we want to retain everything from the previous
         * component.
         */
        ALL
    }

}
