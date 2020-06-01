package com.github.fernthedev.fernapi.universal.api.command

import com.github.fernthedev.fernapi.universal.api.command.arguments.CategoryArgument
import com.github.fernthedev.fernapi.universal.exceptions.command.ArgumentException
import com.github.fernthedev.fernapi.universal.exceptions.command.ArgumentPermissionException
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.ToString


@Getter
@ToString
@AllArgsConstructor
abstract class AbstractArgument<T>(
        var identifier: String? = null,
        val possibleValues: Set<T>?,
        open val suggester: ArgumentSuggester<out AbstractArgument<T>>? = null,
        open val validator: ArgumentValidator<out AbstractArgument<T>>? = null,
        var permission: String? = null
) {

    /**
     * If true, this class is like [CategoryArgument] in which [possibleValues] are of type [AbstractArgument]
     */
    open val isMultiArgument = false

    open val countOfArgs: Int = 1
    protected var value: T? = null

    open fun setValueParsed(sender: CommandSender, rawValue: String): T {
        value = getValueParsed(sender, rawValue)
        return value!!;
    }


    abstract fun getValueParsed(sender: CommandSender, rawValue: String): T

    val stringValue: String
        get() = value.toString()

    protected open fun argumentError(arg: String, data: ValidatorData) {
        throw ArgumentException(ArgumentException.ArgumentInfo(
                identifier,
                data.message,
                getPossibleValuesAsString(),
                arg
        ));
    }

    protected open fun noPermission(arg: String) {
        throw ArgumentPermissionException(ArgumentPermissionException.ArgumentInfo(
                identifier,
                arg
        ));
    }

    protected open fun getPossibleValuesAsString(): List<String>? {
        return possibleValues?.map { v -> v.toString() }
    }


    /**
     * Used only if ArgumentRunnable is null
     * Is used to allow usage of argument as categories into other argument runnables
     *
     * Example:
     * - main
     * - foo
     * - bar
     * - thing
     * - another
     * - another2
     * - other
     * - misc
     *
     *
     * It can also be used as an example to have a hybrid category/argument style
     *
     * Such as:
     * main -> "foo"
     * main bar -> "bar"
     */
    //    private UniversalCommand.ArgumentRunnable defaultArgument(List<AbstractArgument<?,?>> argumentRunnables) {
    //        return (sender, args) -> {
    //            String arg = args.length == 0 ? "" : args[0];
    //            try {
    //                String[] argCopy = args.length == 0 ? new String[0] : Arrays.copyOfRange(args, 1, args.length);
    //                UniversalCommand.handleArguments(sender, argumentRunnables, arg).getArgumentRunnable().run(sender, argCopy);
    //            } catch (ArgumentNotFoundException e) {
    //                e.setArgumentInfo(new ArgumentNotFoundException.ArgumentInfo(this, innerArguments, arg));
    //                throw e;
    //            }
    //        };
    //    }
    //    /**
    //     *
    //     * @param name The name of the argument
    //     * @param argumentRunnable The code ran when the argument is used. Null if you want the default behaviour of using innerArguments. {@link #defaultArgument(List)}
    //     * @param innerArguments Any inner arguments if you wish to use the argument as a category
    //     */
    //    public AbstractArgument(@NonNull T name, @Nullable  argumentRunnable, @Nullable AbstractArgument<?,?>... innerArguments) {
    //        this.innerArguments = Arrays.asList(innerArguments != null ? innerArguments : new AbstractArgument<?,?>[0]);
    //
    //        if (argumentRunnable == null && innerArguments == null) throw new NullPointerException("Both argumentRunnable and innerArguments cannot be null." +
    //                " Provide inner arguments to make a category or provide an argument runnable to run code when argument is called");
    //
    //        if(argumentRunnable == null) this.argumentRunnable = defaultArgument(this.innerArguments);
    //        else this.argumentRunnable = argumentRunnable;
    //
    //    }
    @FunctionalInterface
    interface ArgumentValidator<T : AbstractArgument<*>> {
        fun isValid(sender: CommandSender, arg: String, permission: String?, argumentHandler: T): ValidatorData
    }

    data class ValidatorData(
            val message: String?
    ) {
        fun isValid() : Boolean {
            return message == null
        }
    }

    @FunctionalInterface
    interface ArgumentSuggester<T : AbstractArgument<*>> {
        fun getSuggestions(sender: CommandSender, arg: String, permission: String?, argumentHandler: T): List<String?>?
    }
}