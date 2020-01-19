package com.github.fernthedev.fernapi.universal.exceptions.database;

import java.sql.SQLException;

public class DatabaseException extends SQLException implements IDatabaseException {
    public DatabaseException(String s) {
        super(s);
    }

    public DatabaseException(String s, NullPointerException e) {
        super(s,e);
    }

    /**
     * Constructs a <code>SQLException</code> object with a given
     * <code>reason</code>, <code>SQLState</code>  and
     * <code>vendorCode</code>.
     * <p>
     * The <code>cause</code> is not initialized, and may subsequently be
     * initialized by a call to the
     * {@link Throwable#initCause(Throwable)} method.
     *
     * @param reason     a description of the exception
     * @param SQLState   an XOPEN or SQL:2003 code identifying the exception
     * @param vendorCode a database vendor-specific exception code
     */
    public DatabaseException(String reason, String SQLState, int vendorCode) {
        super(reason, SQLState, vendorCode);
    }

    /**
     * Constructs a <code>SQLException</code> object with a given
     * <code>reason</code> and <code>SQLState</code>.
     * <p>
     * The <code>cause</code> is not initialized, and may subsequently be
     * initialized by a call to the
     * {@link Throwable#initCause(Throwable)} method. The vendor code
     * is initialized to 0.
     *
     * @param reason   a description of the exception
     * @param SQLState an XOPEN or SQL:2003 code identifying the exception
     */
    public DatabaseException(String reason, String SQLState) {
        super(reason, SQLState);
    }

    /**
     * Constructs a <code>SQLException</code> object.
     * The <code>reason</code>, <code>SQLState</code> are initialized
     * to <code>null</code> and the vendor code is initialized to 0.
     * <p>
     * The <code>cause</code> is not initialized, and may subsequently be
     * initialized by a call to the
     * {@link Throwable#initCause(Throwable)} method.
     */
    public DatabaseException() {
        super();
    }

    /**
     * Constructs a <code>SQLException</code> object with a given
     * <code>cause</code>.
     * The <code>SQLState</code> is initialized
     * to <code>null</code> and the vendor code is initialized to 0.
     * The <code>reason</code>  is initialized to <code>null</code> if
     * <code>cause==null</code> or to <code>cause.toString()</code> if
     * <code>cause!=null</code>.
     *
     * @param cause the underlying reason for this <code>SQLException</code>
     *              (which is saved for later retrieval by the <code>getCause()</code> method);
     *              may be null indicating the cause is non-existent or unknown.
     * @since 1.6
     */
    public DatabaseException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a <code>SQLException</code> object with a given
     * <code>reason</code> and  <code>cause</code>.
     * The <code>SQLState</code> is  initialized to <code>null</code>
     * and the vendor code is initialized to 0.
     *
     * @param reason a description of the exception.
     * @param cause  the underlying reason for this <code>SQLException</code>
     *               (which is saved for later retrieval by the <code>getCause()</code> method);
     *               may be null indicating the cause is non-existent or unknown.
     * @since 1.6
     */
    public DatabaseException(String reason, Throwable cause) {
        super(reason, cause);
    }

    /**
     * Constructs a <code>SQLException</code> object with a given
     * <code>reason</code>, <code>SQLState</code> and  <code>cause</code>.
     * The vendor code is initialized to 0.
     *
     * @param reason   a description of the exception.
     * @param sqlState an XOPEN or SQL:2003 code identifying the exception
     * @param cause    the underlying reason for this <code>SQLException</code>
     *                 (which is saved for later retrieval by the
     *                 <code>getCause()</code> method); may be null indicating
     *                 the cause is non-existent or unknown.
     * @since 1.6
     */
    public DatabaseException(String reason, String sqlState, Throwable cause) {
        super(reason, sqlState, cause);
    }

    /**
     * Constructs a <code>SQLException</code> object with a given
     * <code>reason</code>, <code>SQLState</code>, <code>vendorCode</code>
     * and  <code>cause</code>.
     *
     * @param reason     a description of the exception
     * @param sqlState   an XOPEN or SQL:2003 code identifying the exception
     * @param vendorCode a database vendor-specific exception code
     * @param cause      the underlying reason for this <code>SQLException</code>
     *                   (which is saved for later retrieval by the <code>getCause()</code> method);
     *                   may be null indicating the cause is non-existent or unknown.
     * @since 1.6
     */
    public DatabaseException(String reason, String sqlState, int vendorCode, Throwable cause) {
        super(reason, sqlState, vendorCode, cause);
    }
}
