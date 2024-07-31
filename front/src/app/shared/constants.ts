/**
 * @typedef {Object} APP_CONSTANTS
 * @property {Object} VALDATION_MESSAGES - Contains validation messages for different fields
 * @property {string} VALDATION_MESSAGES.PASSWORD_VALIDATION_RULE - Password validation message
 * @property {string} VALDATION_MESSAGES.USERNAME_VALIDATION_RULE - Username validation message
 * @property {string} VALDATION_MESSAGES.EMAIL_VALIDATION_RULE - Email validation message
 *
 * @author Pierre-Olivier Pignon
 * @version 1.0
 */
export const APP_CONSTANTS = {
  VALIDATION_MESSAGES: {
    PASSWORD_VALIDATION_RULE: 'a password with at least 8 characters, including 1 uppercase letter, ' +
      '1 lowercase letter, 1 number and 1 special character',
    USERNAME_VALIDATION_RULE: 'a username with at least 4 characters',
    EMAIL_VALIDATION_RULE: "a valid email address"

  },
  SORT_DIRECTIONS: {
    ASCENDING: 'asc',
    DESCENDING: 'desc',
  }
}
