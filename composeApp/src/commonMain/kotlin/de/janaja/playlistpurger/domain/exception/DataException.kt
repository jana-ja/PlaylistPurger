package de.janaja.playlistpurger.domain.exception

sealed class DataException : Exception() {
    sealed class Remote : DataException() {
        data object InvalidAccessToken: Remote()
        data object RequestTimeout : Remote()
        data object TooManyRequests : Remote()
        data object NoInternet : Remote()
        data object Server : Remote()
        data object Serialization : Remote()
        data object Unknown : Remote()
    }
    sealed class Auth: DataException() {
        data object MissingAccessToken: Auth()
        data object MissingCurrentUser: Auth()
        data object MissingOrInvalidRefreshToken: Auth()

    }
//    sealed class Local : DataException() {
//        data object DiskFull : Local()
//        data object Unknown : Local()
//    }
}

/*



Comparison: Pros and Cons | Feature |
Approach 1: Custom Error | Approach 2: Custom Exception |
|---|---|---|
| Error Type | Custom Error interface | Subclasses of Exception |
| Result Type | Custom Result | Kotlin's built-in Result |
| Integration with Kotlin | Less integrated | More integrated |
| Interoperability | May require adaptation for use with other Kotlin/Java code | Works seamlessly with existing Kotlin/Java exception handling |
| Exception Handling | Forces handling of all custom errors | Requires an else case for unhandled exceptions |
| Clarity | Clear separation of error types from exceptions | May blur the line between expected errors and unexpected exceptions |
| Extensibility | Can be extended with custom error hierarchies | Can be extended, but requires care to maintain consistency with Kotlin's exception handling |
| Library Compatibility | May require adapting libraries that throw standard exceptions | Works directly with libraries that throw standard exceptions |
| Error Mapping | Requires explicit mapping of Kotlin exceptions to custom errors in the repository | Can directly use custom exceptions in catch blocks |
| ViewModel Handling | Exhaustive when for all custom errors | when for custom exceptions, with an else for unexpected exceptions |
Recommendation Approach 2 (Custom Exceptions Inheriting from Exception) is generally the recommended way in Kotlin, especially for Android development. Reasons:
 */