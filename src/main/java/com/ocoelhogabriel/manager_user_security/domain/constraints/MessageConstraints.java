package com.ocoelhogabriel.manager_user_security.domain.constraints;

/**
 * Centralized message class for the entire application.
 * Stores all system messages in one place to ensure consistency
 * and facilitate maintenance and internationalization.
 */
public final class MessageConstraints {

    // General messages
    public static final String ERROR_GENERIC = "An internal error occurred. Please try again.";
    public static final String SUCCESS_GENERIC = "Operation completed successfully.";
    
    // Authentication related messages
    public static final String AUTH_INVALID_CREDENTIALS = "Invalid credentials.";
    public static final String AUTH_ACCESS_DENIED = "Access denied. You don't have permission for this operation.";
    public static final String AUTH_TOKEN_EXPIRED = "Your session has expired. Please login again.";
    public static final String AUTH_INVALID_TOKEN = "Invalid or malformed token.";
    public static final String AUTH_SUCCESS = "Authentication successful.";
    public static final String AUTH_FAILED = "Authentication failed.";
    public static final String AUTH_REFRESH_SUCCESS = "Token refreshed successfully.";
    public static final String AUTH_REFRESH_FAILED = "Failed to refresh token: {0}";
    public static final String AUTH_VALIDATION_SUCCESS = "Valid token.";
    public static final String AUTH_VALIDATION_FAILED = "Invalid token.";
    public static final String AUTH_VALIDATION_ERROR = "Error validating token: {0}";
    public static final String AUTH_LOGIN_NULL = "Authentication data cannot be null.";
    public static final String AUTH_USERNAME_NULL = "Username cannot be null or empty.";
    public static final String AUTH_PASSWORD_NULL = "Password cannot be null or empty.";
    public static final String AUTH_TOKEN_NULL = "Token cannot be null or empty.";
    public static final String AUTH_USER_NOT_FOUND = "User not found: {0}";
    
    // User related messages
    public static final String USER_NOT_FOUND = "User not found.";
    public static final String USER_CREATED = "User created successfully.";
    public static final String USER_UPDATED = "User updated successfully.";
    public static final String USER_DELETED = "User removed successfully.";
    public static final String USER_ALREADY_EXISTS = "A user with this email or username already exists.";
    public static final String USER_INVALID_PASSWORD = "Password does not meet security requirements.";
    public static final String USER_SAVING = "Saving new user";
    public static final String USER_UPDATING = "Updating user with ID: {0}";
    public static final String USER_FINDING_BY_ID = "Finding user with ID: {0}";
    public static final String USER_FINDING_ALL = "Listing all users";
    public static final String USER_FINDING_WITH_FILTER = "Finding users with filter: {0}";
    public static final String USER_DELETING = "Removing user with ID: {0}";
    public static final String USER_FINDING_BY_LOGIN = "Finding user by login: {0}";
    
    // Plant related messages
    public static final String PLANT_NOT_FOUND = "Plant not found.";
    public static final String PLANT_CREATED = "Plant registered successfully.";
    public static final String PLANT_UPDATED = "Plant updated successfully.";
    public static final String PLANT_DELETED = "Plant removed successfully.";
    public static final String PLANT_ERROR_CREATE = "Error creating plant: {0}";
    public static final String PLANT_ERROR_UPDATE = "Error updating plant: {0}";
    public static final String PLANT_ERROR_DELETE = "Error removing plant: {0}";
    public static final String PLANT_LIST_ERROR = "Error listing plants: {0}";
    public static final String PLANT_NAME_EXISTS = "Plant name already registered: {0}";
    public static final String PLANT_NAME_EXISTS_OTHER = "Plant name already registered for another plant: {0}";
    public static final String PLANT_CREATING = "Creating new plant: {0}";
    public static final String PLANT_UPDATING = "Updating plant with ID: {0}";
    public static final String PLANT_CREATED_SUCCESS = "Plant created successfully. ID: {0}";
    public static final String PLANT_UPDATED_SUCCESS = "Plant updated successfully. ID: {0}";
    public static final String PLANT_SEARCHING = "Searching for plant with ID: {0}";
    public static final String PLANT_LISTING = "Listing plants{0}";
    public static final String PLANT_LISTING_BY_COMPANY = " from company ID: {0}";
    public static final String PLANT_REMOVING = "Removing plant with ID: {0}";
    public static final String PLANT_REMOVED_SUCCESS = "Plant removed successfully. ID: {0}";
    
    // Enterprise related messages
    public static final String ENTERPRISE_NOT_FOUND = "Enterprise not found.";
    public static final String ENTERPRISE_CREATED = "Enterprise registered successfully.";
    public static final String ENTERPRISE_UPDATED = "Enterprise updated successfully.";
    public static final String ENTERPRISE_DELETED = "Enterprise removed successfully.";
    public static final String ENTERPRISE_NOT_FOUND_ID = "Enterprise not found with ID: {0}";
    public static final String ENTERPRISE_FINDING_ALL = "Listing all enterprises";
    public static final String ENTERPRISE_FINDING_BY_ID = "Finding enterprise with ID: {0}";
    public static final String ENTERPRISE_SAVING = "Saving enterprise: {0}";
    public static final String ENTERPRISE_DELETING = "Removing enterprise with ID: {0}";
    public static final String ENTERPRISE_FINDING_BY_CNPJ = "Finding enterprise by CNPJ: {0}";
    public static final String ENTERPRISE_FINDING_BY_NAME = "Finding enterprises by name: {0}";
    public static final String ENTERPRISE_CHECK_EXISTS_BY_CNPJ = "Checking if enterprise exists with CNPJ: {0}";
    public static final String ENTERPRISE_INVALID_CNPJ = "Invalid CNPJ: {0}";
    
    // Scope related messages
    public static final String SCOPE_NOT_FOUND = "Scope not found.";
    public static final String SCOPE_CREATED = "Scope registered successfully.";
    public static final String SCOPE_UPDATED = "Scope updated successfully.";
    public static final String SCOPE_DELETED = "Scope removed successfully.";
    public static final String SCOPE_NOT_FOUND_ID = "Scope not found with ID: {0}";
    public static final String SCOPE_FIND_ALL = "Retrieving all scopes";
    public static final String SCOPE_FIND_BY_ID = "Finding scope by ID: {0}";
    public static final String SCOPE_SAVING = "Saving scope: {0}";
    public static final String SCOPE_DELETING = "Removing scope with ID: {0}";
    public static final String SCOPE_FIND_BY_USER = "Finding scopes by user ID: {0}";
    public static final String SCOPE_FIND_BY_ENTERPRISE = "Finding scopes by enterprise ID: {0}";
    public static final String SCOPE_CHECK_EXISTS = "Checking if scope exists for user {0} and enterprise {1}";
    public static final String SCOPE_FIND_BY_NAME = "Finding scope entity by name: {0}";
    public static final String SCOPE_FIND_BY_RESOURCE = "Finding scope details by scope and resource";
    public static final String SCOPE_CREATE_UPDATE = "Creating/updating scope entity";
    public static final String SCOPE_SAVE_UPDATE_DETAILS = "Saving/updating scope details";
    
    // Resource related messages
    public static final String RESOURCE_NOT_FOUND = "Resource not found.";
    public static final String RESOURCE_CREATED = "Resource registered successfully.";
    public static final String RESOURCE_UPDATED = "Resource updated successfully.";
    public static final String RESOURCE_DELETED = "Resource removed successfully.";
    public static final String RESOURCE_NOT_FOUND_ID = "Resource not found with ID: {0}";
    public static final String RESOURCE_FIND_ALL = "Retrieving all resources";
    public static final String RESOURCE_FIND_BY_ID = "Finding resource by ID: {0}";
    public static final String RESOURCE_FIND_BY_NAME = "Finding resource by name: {0}";
    public static final String RESOURCE_SAVING = "Saving resource: {0}";
    public static final String RESOURCE_DELETING = "Removing resource with ID: {0}";
    public static final String RESOURCE_CHECK_EXISTS = "Checking if resource exists with name: {0}";
    public static final String RESOURCE_FIND_BY_TYPE = "Finding resources by type: {0}";
    public static final String RESOURCE_RETRIEVING_ALL = "Retrieving all resources";
    public static final String RESOURCE_FINDING_BY_ID = "Finding resource with id: {0}";
    public static final String RESOURCE_SAVING_NEW = "Saving new resource";
    public static final String RESOURCE_SAVING_ENTITY = "Saving resource via compatibility method";
    public static final String RESOURCE_FINDING_ENTITY_BY_NAME = "Finding resource entity by name: {0}";
    public static final String RESOURCE_CHECKING_EXISTS_BY_NAME = "Checking if resource exists with name: {0}";
    
    // Profile related messages
    public static final String PROFILE_NOT_FOUND = "Profile not found.";
    public static final String PROFILE_CREATED = "Profile registered successfully.";
    public static final String PROFILE_UPDATED = "Profile updated successfully.";
    public static final String PROFILE_DELETED = "Profile removed successfully.";
    public static final String PROFILE_NOT_FOUND_ID = "Profile not found with ID: {0}";
    public static final String PROFILE_NOT_FOUND_NAME = "Profile not found with name: {0}";
    public static final String PROFILE_FIND_BY_NAME = "Finding profile by name: {0}";
    public static final String PROFILE_FIND_BY_ID = "Finding profile by ID: {0}";
    public static final String PROFILE_SAVING = "Saving profile: {0}";
    public static final String PROFILE_CREATE_UPDATE = "Creating or updating profile: {0}";
    
    // Permission related messages
    public static final String PERMISSION_CHECKING = "Checking if profile {0} has permission for resource {1}";
    public static final String PERMISSION_GRANTED = "Permission granted for profile {0} on resource {1}";
    public static final String PERMISSION_DENIED = "Permission denied for profile {0} on resource {1}";
    public static final String PERMISSION_GRANTING = "Granting permission for profile {0} on resource {1}";
    public static final String PERMISSION_REVOKING = "Revoking permission for profile {0} on resource {1}";
    public static final String PERMISSION_REVOKING_ALL = "Revoking all permissions for profile {0}";
    public static final String PERMISSION_FIND_BY_PROFILE_RESOURCE = "Finding permission for profile {0} and resource {1}";
    public static final String PERMISSION_ERROR_CHECK = "Error checking permission: {0}";
    public static final String PERMISSION_ERROR_GRANT = "Error granting permission: {0}";
    public static final String PERMISSION_ERROR_REVOKE = "Error revoking permission: {0}";
    public static final String PERMISSION_SAVING = "Saving permission for profile {0}";
    public static final String PERMISSION_CHECK_FOR_URL = "Checking permission: profile={0}, resource={1}, action={2}";
    public static final String PERMISSION_RESOURCE_NOT_RECOGNIZED = "Resource not recognized for URL: {0}";
    public static final String PERMISSION_PROFILE_NOT_FOUND = "Profile not found: {0}";
    public static final String PERMISSION_ACCESS_GRANTED = "Access authorized: profile={0}, resource={1}, method={2}";
    public static final String PERMISSION_NULL_PROFILE = "Profile is null";
    public static final String PERMISSION_NULL_URL_VALIDATOR = "UrlValidator is null";
    public static final String PERMISSION_NULL_HTTP_METHOD = "HTTP Method is null";
    
    // Log messages
    public static final String LOG_PLANT_SEARCH = "Searching plant by code: {0}";
    public static final String LOG_PLANT_LIST = "Listing all plants";
    public static final String LOG_PLANT_CREATE = "Creating new plant: {0}";
    public static final String LOG_PLANT_UPDATE = "Updating plant with code {0}: {1}";
    public static final String LOG_PLANT_DELETE = "Deleting plant with code: {0}";
    public static final String LOG_PLANT_PAGED = "Finding paginated plants: page {0}, size {1}, name: {2}, enterprise: {3}";
    
    // General operation log messages
    public static final String LOG_OPERATION_START = "Starting operation: {0}";
    public static final String LOG_OPERATION_END = "Operation finished: {0}";
    public static final String LOG_OPERATION_ERROR = "Error in operation {0}: {1}";
    
    // Authentication log messages
    public static final String LOG_AUTH_LOGIN = "Login attempt for user: {0}";
    public static final String LOG_AUTH_TOKEN_VALIDATE = "Validating access token";
    public static final String LOG_AUTH_TOKEN_REFRESH = "Refreshing access token";
    public static final String LOG_AUTH_ERROR = "Authentication error: {0}";
    
    // Validation messages
    public static final String VALIDATION_REQUIRED_FIELD = "The field {0} is required.";
    public static final String VALIDATION_INVALID_FORMAT = "The field {0} has an invalid format.";
    public static final String VALIDATION_MIN_LENGTH = "The field {0} must have at least {1} characters.";
    public static final String VALIDATION_MAX_LENGTH = "The field {0} must have at most {1} characters.";
    public static final String DATE_PARSING_ERROR = "Error parsing date: ";
    
    // Security and exception messages
    public static final String SECURITY_ACCESS_DENIED = "Access denied. You don't have permission to access this resource.";
    public static final String SECURITY_UNAUTHORIZED = "Unauthorized. Authentication is required to access this resource.";
    public static final String SECURITY_TOKEN_EXPIRED = "Token expired. Please login again.";
    public static final String SECURITY_TOKEN_INVALID = "Invalid or malformed token.";
    public static final String SECURITY_AUTHENTICATION_REQUIRED = "Authentication required to access this resource.";
    public static final String SECURITY_INVALID_URL = "Invalid URL: {0}";
    public static final String SECURITY_NOT_AUTHORIZED_ACTION = "Not authorized to perform this action.";
    public static final String SECURITY_AUTH_FAILURE = "Authentication failure: {0}";
    
    // JWT related messages
    public static final String ERROR_JWT_TOKEN_GENERATION = "Error generating JWT token: {0}";
    public static final String ERROR_JWT_TOKEN_VALIDATION = "Error validating JWT token: {0}";
    public static final String ERROR_JWT_TOKEN_REFRESH = "Error refreshing JWT token: {0}";
    public static final String LOG_JWT_TOKEN_GENERATION = "Generating JWT token for user: {0}";
    public static final String LOG_JWT_TOKEN_GENERATED_SUCCESSFULLY = "JWT token generated successfully";
    public static final String LOG_JWT_TOKEN_VALIDATION = "Validating JWT token";
    public static final String LOG_JWT_TOKEN_VALIDATION_SUCCESS = "JWT token validated successfully";
    public static final String LOG_JWT_TOKEN_VALIDATION_FAILED = "JWT token validation failed: {0}";
    public static final String LOG_JWT_TOKEN_REFRESH = "Refreshing JWT token";
    public static final String LOG_JWT_TOKEN_REFRESH_SUCCESS = "JWT token refreshed successfully";
    public static final String LOG_JWT_TOKEN_REFRESH_FAILED = "Failed to refresh JWT token: {0}";
    
    
    /**
     * Utility class for message formatting
     */
    public static class MessageFormatter {
        /**
         * Private constructor to prevent instantiation
         */
        private MessageFormatter() {}
        
        /**
         * Formats a message with parameters
         * @param template Message template with placeholders {0}, {1}, etc.
         * @param params Parameters to replace in the placeholders
         * @return Formatted message
         */
        public static String format(String template, Object... params) {
            if (template == null) {
                return null;
            }
            
            String result = template;
            for (int i = 0; i < params.length; i++) {
                String placeholder = "{" + i + "}";
                result = result.replace(placeholder, String.valueOf(params[i]));
            }
            
            return result;
        }
    }
    
    // Private constructor to prevent instantiation
    private MessageConstraints() {}
}