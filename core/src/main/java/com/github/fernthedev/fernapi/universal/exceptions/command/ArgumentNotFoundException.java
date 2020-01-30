package com.github.fernthedev.fernapi.universal.exceptions.command;

import com.github.fernthedev.fernapi.universal.api.UniversalCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.annotation.Nullable;
import java.security.PrivilegedActionException;
import java.util.List;

public class ArgumentNotFoundException extends IllegalArgumentException {

    @Getter
    @Setter
    private ArgumentInfo argumentInfo;

    /**
     * Constructs an <code>IllegalArgumentException</code> with no
     * detail message.
     */
    public ArgumentNotFoundException(ArgumentInfo argumentInfo) {
        super();
        this.argumentInfo = argumentInfo;
    }

    /**
     * Constructs an <code>IllegalArgumentException</code> with the
     * specified detail message.
     *
     * @param s the detail message.
     */
    public ArgumentNotFoundException(ArgumentInfo argumentInfo, String s) {
        super(s);
        this.argumentInfo = argumentInfo;
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.
     *
     * <p>Note that the detail message associated with <code>cause</code> is
     * <i>not</i> automatically incorporated in this exception's detail
     * message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link Throwable#getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link Throwable#getCause()} method).  (A {@code null} value
     *                is permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     * @since 1.5
     */
    public ArgumentNotFoundException(ArgumentInfo argumentInfo, String message, Throwable cause) {
        super(message, cause);
        this.argumentInfo = argumentInfo;
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of {@code (cause==null ? null : cause.toString())} (which
     * typically contains the class and detail message of {@code cause}).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * PrivilegedActionException}).
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link Throwable#getCause()} method).  (A {@code null} value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     * @since 1.5
     */
    public ArgumentNotFoundException(ArgumentInfo argumentInfo, Throwable cause) {
        super(cause);
        this.argumentInfo = argumentInfo;
    }

    @AllArgsConstructor
    @Getter
    public static class ArgumentInfo {
        @Nullable
        private UniversalCommand.Argument parentArgument;

        @NonNull
        private List<UniversalCommand.Argument> possibleArguments;

        @NonNull
        private String providedArg;
    }
}
